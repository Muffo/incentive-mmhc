package connectionStatusManager;

import java.util.Enumeration;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * Gestisce i risultati delle connessioni forniti dal middleware.
 * @author Andrea Grandi
 *
 */
public class ConnectionNotifier {

	private static Hashtable _status = new Hashtable();

	/**
	 * Notifica al CSM il successo o il fallimento di una connessione
	 * 
	 * @param nodeId
	 *            identificativo del nodo al quale ci si è connessi
	 * @param success
	 *            risultato della connessione: true per successo, false per
	 *            fallimento
	 */
	public synchronized static void notifyResult(String nodeId, boolean success) {

		Integer value;

		if (_status.containsKey(nodeId))
			value = (Integer) _status.get(nodeId);
		else
			value = new Integer(0);

		int intValue;

		if (success)
			intValue = value.intValue() + 1;
		else
			intValue = value.intValue() - 1;

		value = new Integer(intValue);

		_status.put(nodeId, value);
	}
	
	/**
	 * Restituisce il risultato delle connessioni per un determinato nodo.
	 * 
	 * @param nodeId	identificativo del nodo
	 * @return	differenza tra il numero di successi e fallimenti
	 */
	public synchronized static int getStatusOf(String nodeId) {

		if (!_status.containsKey(nodeId))
			return 0;

		return (Integer) _status.get(nodeId);
	}

	/**
	 * Restituisce il risultato delle connessioni per un determinato nodo.
	 * Inoltre consente di resettare il contatore associato a tale nodo
	 * 
	 * @param nodeId	identificativo del nodo
	 * @param reset		se true resetta il contatore associato al nodo.
	 * @return			differenza tra il numero di successi e fallimenti
	 */
	public synchronized static int getStatusOf(String nodeId, boolean reset) {
		int result = getStatusOf(nodeId);

		if (reset)
			_status.put(nodeId, new Integer(0));

		return result;
	}
	
	/**
	 * Fornisce la lista dei nodi notificati presso CSM
	 * 
	 * @return Enumerativo composto da stringhe contenenti i nodeId
	 */
	@SuppressWarnings("unchecked")
	public synchronized static Enumeration getIdentifiers() {
		return _status.keys();
	}

}
