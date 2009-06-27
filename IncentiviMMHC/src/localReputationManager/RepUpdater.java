package localReputationManager;

import java.util.Enumeration;

import connectionStatusManager.ConnectionNotifier;

import informationProvider.ParameterProvider;

public class RepUpdater implements Runnable {

	private static final int RepInc = 4;
	private static final int RepDec = 2;

	private static final double Alpha = 0.3;

	private static final long updateCurrentDelay = 2000;
	private static final long updateHistoricalDelay = 10000;

	private int _updateCount = 0;
	private int _updateHistoricalCountDelay;

	private RepCollection _repCollection;

	public RepUpdater() {
		_updateHistoricalCountDelay = Math.round(updateHistoricalDelay
				/ updateCurrentDelay);

		_repCollection = RepCollection.getInstance();

	}

	@SuppressWarnings("unchecked")
	public void run() {

		while (true) {
			try {
				Thread.sleep(updateCurrentDelay);
			} catch (InterruptedException e) {
				continue;
			}

			_updateCount++;

			Enumeration<String> nodeIds = ConnectionNotifier.getIdentifiers();
			while (nodeIds.hasMoreElements()) {
				String nodeId = (String) nodeIds.nextElement();
				int status = ConnectionNotifier.getStatusOf(nodeId);
				UpdateCurrentRep(nodeId, status);
				if (_updateCount == _updateHistoricalCountDelay)
					UpdateHistoricalRep(nodeId);
			}

			if (_updateCount == _updateHistoricalCountDelay)
				_updateCount = 0;
		}
	}

	public void UpdateCurrentRep(String nodeId, int status) {
	

		if (!_repCollection.containsNode(nodeId)) {
			_repCollection.AddNode(nodeId);
		}

		RepLevel cRep = _repCollection.getCRep(nodeId);

		if (status < 0) {
			int punishment = RepDec * status;

			if (!ParameterProvider.getET(nodeId).isOverloaded())
				punishment += 2 * RepDec * status;

			if (!ParameterProvider.getNBL(nodeId).isExhausted())
				punishment += 2 * RepDec * status;

			cRep.setLevel(cRep.getLevel() - punishment);

		} else if (status > 0) {
			int reward = RepInc * status;
			cRep.setLevel(cRep.getLevel() + reward);
		}

	}

	public void UpdateHistoricalRep(String nodeId) {

		RepLevel cRep = _repCollection.getCRep(nodeId);
		RepLevel hRep = _repCollection.getHRep(nodeId);

		hRep.setLevel((int) Math.round(Alpha * cRep.getLevel() + (1 - Alpha)
				* hRep.getLevel()));
	}

}
