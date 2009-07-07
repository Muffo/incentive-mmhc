package reputationExchangeManager;

import java.util.Enumeration;

import reputationManager.RepUpdater;


import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * Tiene traccia dei valori di reputazione ricevuti dai nodi remoti, rendendoli
 * successivamente disponibili a {@link RepUpdater}.
 * 
 * @author Andrea Grandi
 * 
 */
public class RepReceived {

	private static Hashtable _received = new Hashtable();

	/**
	 * Aggiunge la reputazione di un nodo alla lista locale in cui vengono
	 * memorizzati i livelli di reputazione ricevuti
	 * 
	 * @param nodeId
	 *            identificatore del nodo
	 * @param reputation
	 *            livello di reputazion
	 */
	public synchronized static void addRxReputation(String nodeId,
			int reputation) {

		Integer newValue;

		if (_received.containsKey(nodeId)) {
			Integer currentValue = (Integer) _received.get(nodeId);
			newValue = Math.round(currentValue.intValue() / 2 + reputation / 2);
		} else
			newValue = new Integer(reputation);

		_received.put(nodeId, newValue);
	}

	/**
	 * Ottiene la reputazione ricevuta per un particolare nodo
	 * 
	 * @param nodeId
	 *            identificatore del nodo
	 * @return livello di reputazione del nodo
	 */
	public static int getRxReputationOf(String nodeId) {

		if (!_received.containsKey(nodeId))
			return 0;

		return (Integer) _received.get(nodeId);
	}

	/**
	 * Ottiene la reputazione ricevuta per un particolare nodo e permette di
	 * resettare il valore memorizzato localmente
	 * 
	 * @param nodeId
	 *            identificatore del nodo
	 * @param reset
	 *            se true resetta il valore memorizzato in locale, altrimenti no
	 * @return livello di reputazione del nodo
	 */
	public static int getRxReputationOf(String nodeId, boolean reset) {
		int result = getRxReputationOf(nodeId);

		if (reset)
			_received.remove(nodeId);

		return result;
	}

	/**
	 * Ottiene la lista degli identifcatori per i quali è memorizzato un valore
	 * di reputazione ricevuto da un nodo remoto
	 * 
	 * @return enumeratore contenente gli identificatori
	 */
	@SuppressWarnings("unchecked")
	public static Enumeration getIdentifiers() {
		return _received.keys();
	}

}
