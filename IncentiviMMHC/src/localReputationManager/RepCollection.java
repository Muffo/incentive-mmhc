package localReputationManager;


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

	public RepLevel getHRep(String nodeId) {
		if (!_hReputations.contains(nodeId))
			throw new IllegalArgumentException(
					"!_hReputations.contains(nodeId)");

		return (RepLevel) _hReputations.get(nodeId);
	}

	public synchronized void setHRep(String nodeId, RepLevel reputation) {
		if (reputation == null)
			throw new IllegalArgumentException("reputation == null");

		_hReputations.put(nodeId, reputation);

		if (!_cReputations.contains(nodeId))
			_cReputations.put(nodeId, reputation);
	}

	public RepLevel getCRep(String nodeId) {
		if (!_cReputations.contains(nodeId))
			throw new IllegalArgumentException(
					"!_cReputations.contains(nodeId)");

		return (RepLevel) _cReputations.get(nodeId);
	}

	public synchronized void setCRep(String nodeId, RepLevel reputation) {
		if (reputation == null)
			throw new IllegalArgumentException("reputation == null");

		// da valutare...
		if (!_hReputations.contains(nodeId))
			throw new IllegalArgumentException(
					"!_hReputations.contains(nodeId)");

		_cReputations.put(nodeId, reputation);
	}

}
