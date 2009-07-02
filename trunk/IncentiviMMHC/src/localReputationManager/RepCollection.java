package localReputationManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import connectionStatusManager.ConnectionNotifier;

/**
 * Collezione di RepLevel associati ai nodi remoti.
 * 
 * @author Andrea Grandi
 */
public class RepCollection {

	// private Dictionary<String, RepLevel> _cReputations;
	// private Dictionary<String, RepLevel> _hReputations;

	/**
	 * Nome del file sul quale viene effettuata la persistenza dei valori di HRep
	 */
	private static final String fileName = "reputation.txt";

	private static RepCollection _instance = new RepCollection();

	private Hashtable _cReputations;
	private Hashtable _hReputations;

	private RepCollection() {
		_cReputations = new Hashtable();
		_hReputations = new Hashtable();

		LoadFromFile();

		System.out.println(this);
	}

	/**
	 * Restituisce l'unica istanza di RepCollection (singleton)
	 * 
	 * @return istanza di RepCollection
	 */
	public synchronized static RepCollection getInstance() {
		return _instance;
	}

	/**
	 * Verifica se la collezione contiene un determinato nodo
	 * 
	 * @param nodeId
	 *            identificativo del nodo
	 * @return true se il nodo  presente, false altrimenti
	 */
	public synchronized boolean containsNode(String nodeId) {
		return (_hReputations.containsKey(nodeId));
	}

	/**
	 * Restituisce la lista dei nodi contenuti nella collezione
	 * 
	 * @return Enumerativo contenente tutti gli identificatori dei nodi
	 */
	public synchronized Enumeration getIdentifiers() {
		return _hReputations.keys();
	}

	/**
	 * Ottiene il valore di HRep associato ad un nodo.
	 * 
	 * @param nodeId
	 *            identificativo del nodo
	 * @return RepLevel associato al valore di HRep del nodo
	 * @see RepLevel
	 */
	public synchronized RepLevel getHRep(String nodeId) {
		if (!_hReputations.containsKey(nodeId))
			throw new IllegalArgumentException(
					"!_hReputations.containsKey(nodeId)");

		return (RepLevel) _hReputations.get(nodeId);
	}

	/**
	 * Ottiene il valore di CRep associato ad un nodo.
	 * 
	 * @param nodeId
	 *            identificativo del nodo
	 * @return RepLevel associato al valore di CRep del nodo
	 * @see RepLevel
	 */
	public synchronized RepLevel getCRep(String nodeId) {
		if (!_cReputations.containsKey(nodeId))
			throw new IllegalArgumentException(
					"!_cReputations.containsKey(nodeId)");

		return (RepLevel) _cReputations.get(nodeId);
	}

	/**
	 * Aggiunge un nuovo nodo alla lista. PoichŽ non sono specificati i RepLevel
	 * vengono inseriti quelli di default
	 * 
	 * @param nodeId
	 *            identificativo del nuovo nodo
	 */
	public void AddNode(String nodeId) {
		RepLevel hRep = new RepLevel();
		RepLevel cRep = new RepLevel();
		AddNode(nodeId, hRep, cRep);

	}

	/**
	 * Aggiunge un nuovo nodo alla lista.
	 * 
	 * @param nodeId	identificativo del nuovo nodo
	 * @param hRep	valore di 
	 * @param cRep
	 * @see RepLevel
	 */
	public void AddNode(String nodeId, RepLevel hRep, RepLevel cRep) {
		if (nodeId.equalsIgnoreCase("") || nodeId == null)
			throw new IllegalArgumentException(
					"nodeId.isEmpty() || nodeId == null");

		if (containsNode(nodeId))
			throw new IllegalArgumentException("containsKey(nodeId)");

		if (hRep == null || cRep == null)
			throw new IllegalArgumentException("hRep == null || cRep == null");

		_cReputations.put(nodeId, cRep);
		_hReputations.put(nodeId, hRep);
	}

	@SuppressWarnings("unchecked")
	public String toString() {
		StringBuilder result = new StringBuilder();

		Enumeration keys = _hReputations.keys();
		while (keys.hasMoreElements()) {
			String nodeId = (String) keys.nextElement();

			RepLevel hRep = getHRep(nodeId);
			RepLevel cRep = getCRep(nodeId);

			result.append("[ " + nodeId + ", " + hRep + ", " + cRep + " ]\n");
		}
		if (result.length() > 0)
			result.deleteCharAt(result.length() - 1);

		return result.toString();
	}

	/**
	 * Carica la lista contentente la reputazione dal file di default
	 */
	private void LoadFromFile() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e1) {
			return;
		}

		String nodeId = null;
		try {
			while ((nodeId = reader.readLine()) != null) {
				int repLevel = Integer.parseInt(reader.readLine());
				RepLevel hRep = new RepLevel(repLevel);
				RepLevel cRep = new RepLevel(repLevel);

				AddNode(nodeId, hRep, cRep);
			}
		} catch (IOException e) {
			return;
		}
	}

	/**
	 * Salva la lista contenente la reputazione sul file di default.
	 */
	public void SaveToFile() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(fileName, false));

			Enumeration keys = getIdentifiers();
			while (keys.hasMoreElements()) {
				String nodeId = (String) keys.nextElement();
				RepLevel hRep = getHRep(nodeId);

				writer.write(nodeId + "\n");
				writer.write(hRep.toString() + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Errore scrittura sul file result.txt");
			e.printStackTrace();
		}

	}
}
