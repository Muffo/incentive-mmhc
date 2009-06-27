package connectionStatusManager;

import java.util.Enumeration;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class ConnectionNotifier {

	private static Hashtable _status = new Hashtable();

	// public static void notifyError(String nodeId)
	// {
	//		
	// }
	//
	// public static void notifySuccess(String nodeId) {
	//
	// }

	public static void notifyResult(String nodeId, boolean success) {

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

	public static int getStatusOf(String nodeId) {

		if (!_status.containsKey(nodeId))
			// throw new
			// IllegalArgumentException("!_status.containsKey(nodeId)");
			return 0;

		return (Integer) _status.get(nodeId);

	}

	public static int getStatusOf(String nodeId, boolean reset) {
		int result = getStatusOf(nodeId);

		if (reset)
			_status.put(nodeId, new Integer(0));

		return result;
	}

	@SuppressWarnings("unchecked")
	public static Enumeration getIdentifiers() {
		return _status.keys();
	}

}
