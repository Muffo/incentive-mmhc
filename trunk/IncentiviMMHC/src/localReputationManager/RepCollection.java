package localReputationManager;

import java.util.Enumeration;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class RepCollection {

	// private Dictionary<String, RepLevel> _cReputations;
	// private Dictionary<String, RepLevel> _hReputations;

	private static RepCollection _instance = new RepCollection();

	private Hashtable _cReputations;
	private Hashtable _hReputations;

	private RepCollection() {
		_cReputations = new Hashtable();
		_hReputations = new Hashtable();
	}

	public static RepCollection getInstance() {
		return _instance;
	}

	public boolean containsKey(String nodeId) {
		return (_hReputations.containsKey(nodeId));
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

		if (containsKey(nodeId))
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

		return result.toString();
	}

	// public void setHRep(String nodeId, RepLevel reputation) {
	// if (reputation == null)
	// throw new IllegalArgumentException("reputation == null");
	//
	// _hReputations.put(nodeId, reputation);
	//
	// if (!_cReputations.containsKey(nodeId))
	// _cReputations.put(nodeId, reputation);
	// }
	//	
	// public void setCRep(String nodeId, RepLevel reputation) {
	// if (reputation == null)
	// throw new IllegalArgumentException("reputation == null");
	//
	// // da valutare...
	// if (!_hReputations.containsKey(nodeId))
	// throw new IllegalArgumentException(
	// "!_hReputations.containsKey(nodeId)");
	//
	// _cReputations.put(nodeId, reputation);
	// }

}
