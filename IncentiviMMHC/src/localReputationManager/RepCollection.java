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

public class RepCollection {

	// private Dictionary<String, RepLevel> _cReputations;
	// private Dictionary<String, RepLevel> _hReputations;

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

	public static RepCollection getInstance() {
		return _instance;
	}

	public boolean containsNode(String nodeId) {
		return (_hReputations.containsKey(nodeId));
	}
	
	public Enumeration getIdentifiers()
	{
		return _hReputations.keys();
	}

	public RepLevel getHRep(String nodeId) {
		if (!_hReputations.containsKey(nodeId))
			throw new IllegalArgumentException(
					"!_hReputations.containsKey(nodeId)");

		return (RepLevel) _hReputations.get(nodeId);
	}

	public RepLevel getCRep(String nodeId) {
		if (!_cReputations.containsKey(nodeId))
			throw new IllegalArgumentException(
					"!_cReputations.containsKey(nodeId)");

		return (RepLevel) _cReputations.get(nodeId);
	}

	public void AddNode(String nodeId) {
		RepLevel hRep = new RepLevel();
		RepLevel cRep = new RepLevel();
		AddNode(nodeId, hRep, cRep);

	}

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

	private void LoadFromFile()  {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e1) {
			return;
		}
		
		String nodeId = null;
		try {
			while ((nodeId = reader.readLine()) != null)
			{
				int repLevel = Integer.parseInt(reader.readLine());
				RepLevel hRep = new RepLevel(repLevel);
				RepLevel cRep = new RepLevel(repLevel);
				
				AddNode(nodeId, hRep, cRep);
			}
		} catch (IOException e) {
			return;
		}	
	}

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
