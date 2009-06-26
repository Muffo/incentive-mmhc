package localReputationManager;

import informationProvider.ParameterProvider;

public class RepUpdater {

	private static final int RepInc = 4;
	private static final int RepDec =2;

	private static final double Alpha = 0.01;

	public static void UpdateRep(String nodeId, boolean txFail) {
		RepCollection repCollection = RepCollection.getInstance();

		RepLevel cRep = repCollection.getCRep(nodeId);

		RepLevel hRep = repCollection.getHRep(nodeId);

		if (txFail) {
			cRep.setLevel(cRep.getLevel() - RepDec);

			if (!ParameterProvider.getET(nodeId).isOverloaded())
				cRep.setLevel(cRep.getLevel() - 2 * RepDec);

			if (!ParameterProvider.getNBL(nodeId).isExhausted())
				cRep.setLevel(cRep.getLevel() - 2 * RepDec);

		} else {
			cRep.setLevel(cRep.getLevel() + RepInc);
		}

		hRep.setLevel((int) Math.round(Alpha * cRep.getLevel() + (1 - Alpha)
				* hRep.getLevel()));

	}

}
