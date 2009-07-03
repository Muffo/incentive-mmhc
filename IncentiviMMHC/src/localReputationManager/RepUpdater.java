package localReputationManager;

import java.util.Enumeration;

import reputationExchangeManager.RepReceived;

import connectionStatusManager.ConnectionNotifier;

import informationProvider.ParameterProvider;

/**
 * Componente che aggiorna periodicamente i valori di reputazione sulla base dei
 * risultati delle connessioni.
 * 
 * @author Andrea Grandi
 * 
 */
public class RepUpdater implements Runnable {

	private static final int RepInc = 40;
	private static final int RepDec = 20;

	/**
	 * Valore Alpha per l'aggiornamento di HRep
	 */
	private static final double Alpha = 0.3;

	/**
	 * Intervallo in millisec tra gli aggiornamenti del valore CRep
	 */
	private static final long updateCurrentDelay = 5000;

	/**
	 * Intervallo in millisec tra gli aggiornamenti del valore HRep
	 */
	private static final long updateHistoricalDelay = 30000;

	private int _updateCount = 0;
	private int _updateHistoricalCountDelay;

	private RepCollection _repCollection;

	public RepUpdater() {
		_updateHistoricalCountDelay = Math.round(updateHistoricalDelay
				/ updateCurrentDelay);

		_repCollection = RepCollection.getInstance();

	}

	@SuppressWarnings("unchecked")
	public void run() {

		while (true) {
			try {
				Thread.sleep(updateCurrentDelay);
			} catch (InterruptedException e) {
				continue;
			}

			UpdateCurrents();

			_updateCount++;

			if (_updateCount == _updateHistoricalCountDelay) {
				UpdateReceived();
				UpdateHistoricals();

				_repCollection.SaveToFile();
			}
			if (_updateCount == _updateHistoricalCountDelay)
				_updateCount = 0;
		}
	}

	/**
	 * Aggiorna i valori di CRep per tutti i nodi contenuti nella lista fornita
	 * da CMS, in particolare da {@link ConnectionNotifier}.
	 * 
	 * @see ConnectionNotifier
	 */
	private void UpdateCurrents() {
		Enumeration<String> nodeIds = ConnectionNotifier.getIdentifiers();
		while (nodeIds.hasMoreElements()) {
			String nodeId = (String) nodeIds.nextElement();
			int status = ConnectionNotifier.getStatusOf(nodeId, true);
			UpdateCurrentRep(nodeId, status);
		}
	}

	/**
	 * Aggiorna i valori di HRep contenuti in {@link RepCollection}
	 * 
	 * @see RepCollection
	 */
	private void UpdateHistoricals() {
		Enumeration nodeIds = _repCollection.getIdentifiers();
		while (nodeIds.hasMoreElements()) {
			String nodeId = (String) nodeIds.nextElement();
			UpdateHistoricalRep(nodeId);
		}
	}

	/**
	 * Aggiorna i valori di HRep sulla base dei dati ricevuti dai nodi remoti e
	 * memorizzati in {@link RepReceived}
	 * 
	 */
	private void UpdateReceived() {
		Enumeration<String> nodeIds = RepReceived.getIdentifiers();
		while (nodeIds.hasMoreElements()) {
			String nodeId = (String) nodeIds.nextElement();
			int rxReputation = RepReceived.getRxReputationOf(nodeId, true);
			UpdateReceivedRep(nodeId, rxReputation);
		}
	}

	/**
	 * Aggiorna il valore di CRep per un particolare nodo in base al numero di
	 * connessioni effettuate con successo
	 * 
	 * @param nodeId
	 *            identificativo del nodo
	 * @param status
	 *            differenza tra connessioni effettuate con successo e
	 *            connessioni fallite
	 * @see RepLevel
	 */
	private void UpdateCurrentRep(String nodeId, int status) {

		if (!_repCollection.containsNode(nodeId)) {
			_repCollection.AddNode(nodeId);
		}

		RepLevel cRep = _repCollection.getCRep(nodeId);

		if (status < 0) {
			// punishment è negativo, quindi va sommato alla fine
			int punishment = RepDec * status;

			if (!ParameterProvider.getET(nodeId).isOverloaded())
				punishment += 2 * RepDec * status;

			if (!ParameterProvider.getNBL(nodeId).isExhausted())
				punishment += 2 * RepDec * status;

			cRep.setLevel(cRep.getLevel() + punishment);

		} else if (status > 0) {
			int reward = RepInc * status;
			cRep.setLevel(cRep.getLevel() + reward);
		}

	}

	/**
	 * Aggiorna il valore di HRep per un particolare nodo in base al valore
	 * precedente di HRep e quello corrente di CRep. Il calcolo è basato sul
	 * parametro Alpha.
	 * 
	 * @param nodeId
	 *            identificativo del nodo
	 * 
	 * @see RepLevel
	 * @see RepUpdater#Alpha
	 */
	private void UpdateHistoricalRep(String nodeId) {

		RepLevel cRep = _repCollection.getCRep(nodeId);
		RepLevel hRep = _repCollection.getHRep(nodeId);

		hRep.setLevel((int) Math.round(Alpha * cRep.getLevel() + (1 - Alpha)
				* hRep.getLevel()));

		cRep.setLevel(hRep.getLevel());
	}

	/**
	 * Aggiorna il valore di HRep per un particolare nodo in base al valore
	 * ricevuto da un nodo remoto
	 * 
	 * @param nodeId
	 *            identificativo del nodo
	 * @param rxReputation
	 *            livello di reputazione ricevuto dal nodo remoto
	 */
	private void UpdateReceivedRep(String nodeId, int rxReputation) {

		if (!_repCollection.containsNode(nodeId)) {
			_repCollection.AddNode(nodeId, new RepLevel(rxReputation),
					new RepLevel(rxReputation));
		} else {
			RepLevel hRep = _repCollection.getHRep(nodeId);

			hRep.setLevel((int) Math.round(hRep.getLevel() * 2 / 3
					+ rxReputation / 3));
		}

	}

}
