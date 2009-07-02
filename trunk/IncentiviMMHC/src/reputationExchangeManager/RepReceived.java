package reputationExchangeManager;

import java.util.Enumeration;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class RepReceived {

	private static Hashtable _received = new Hashtable();

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

	public static int getRxReputationOf(String nodeId) {

		if (!_received.containsKey(nodeId))
			return 0;

		return (Integer) _received.get(nodeId);
	}

	
	public static int getRxReputationOf(String nodeId, boolean reset) {
		int result = getRxReputationOf(nodeId);

		if (reset)
			_received.remove(nodeId);

		return result;
	}

	
	@SuppressWarnings("unchecked")
	public static Enumeration getIdentifiers() {
		return _received.keys();
	}

}
