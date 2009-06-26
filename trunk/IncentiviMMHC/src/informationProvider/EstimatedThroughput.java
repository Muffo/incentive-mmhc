package informationProvider;

public class EstimatedThroughput {

	private static final int Threshold = 10;

	private int _et;

	public EstimatedThroughput(int et) {
		_et = et;
	}

	public boolean isOverloaded() {
		return (_et < Threshold);
	}
	
}
