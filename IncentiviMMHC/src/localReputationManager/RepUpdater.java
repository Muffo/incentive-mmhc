package localReputationManager;

import java.util.Enumeration;

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

	private static final int RepInc = 4;
	private static final int RepDec = 2;

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
	private static final long updateHistoricalDelay = 10000;

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
				UpdateHistoricals();

			}
			if (_updateCount == _updateHistoricalCountDelay)
				_updateCount = 0;
		}
	}

	/**
	 * Aggiorna i valori di CRep per tutti i nodi contenuti nella lista fornita
	 * da CSM.
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
	 * Aggiorna i valori di HRep contenuti nella RepCollection
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
			int punishment = RepDec * status;

			if (!ParameterProvider.getET(nodeId).isOverloaded())
				punishment += 2 * RepDec * status;

			if (!ParameterProvider.getNBL(nodeId).isExhausted())
				punishment += 2 * RepDec * status;

			cRep.setLevel(cRep.getLevel() - punishment);

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

		_repCollection.SaveToFile();
	}

}
