package localReputationManager;

public class RepProvider {

	public static RepLevel getMaxReputation(String nodeId) {
		RepCollection repCollection = RepCollection.getInstance();

		RepLevel hRep = repCollection.getHRep(nodeId);
		RepLevel cRep = repCollection.getCRep(nodeId);

		if (cRep.getLevel() > hRep.getLevel())
			return cRep;
		else
			return hRep;
	}

	public static RepLevel getAvgReputation(String nodeId) {
		RepCollection repCollection = RepCollection.getInstance();
		
		

		RepLevel hRep;
		RepLevel cRep;
		try {
			hRep = repCollection.getHRep(nodeId);
			cRep = repCollection.getCRep(nodeId);
		} catch (IllegalArgumentException e) {
			return new RepLevel();
		}

		return new RepLevel(Math.round((hRep.getLevel() + cRep.getLevel()) / 2));
	}

	public static boolean isSelfishNode(String nodeId) {
		return getAvgReputation(nodeId).isLowRep();
	}

}
