package informationProvider;

public class NodeBatteryLevel {
	private static final int Threshold = 10;

	private int _nbl;

	public NodeBatteryLevel(int et) {
		_nbl = et;
	}

	public boolean isExhausted() {
		return (_nbl < Threshold);
	}
}
