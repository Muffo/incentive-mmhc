package localReputationManager;

public class RepLevel implements Comparable<RepLevel>{

	private static final int MaxRep = 6000;
	private static final int MinRep = 0;

	private static final int LowRepThreshold = 2000;
	private static final int HighRepThreshold = 4000;

	private static final int DefaultRep = 3000;

	private int _level;

	public RepLevel() {
		this(DefaultRep);
	}

	public RepLevel(int level) {
		setLevel(level);
	}

	public void setLevel(int level) {
		if (level > MaxRep)
			_level = MaxRep;

		else if (level < MinRep)
			_level = MinRep;

		else
			_level = level;
	}

	public int getLevel() {
		return _level;
	}

	public boolean isLowRep() {
		return (_level < LowRepThreshold);
	}

	public boolean isHighRep() {
		return (_level > HighRepThreshold);
	}

	public String toString()
	{
		return "" + _level;
	}

	public int compareTo(RepLevel reputation) {
		return this.getLevel() - reputation.getLevel();
		
	}
}
