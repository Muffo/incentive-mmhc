package test.localReputationManager;

import java.net.UnknownHostException;
import localReputationManager.RepCollection;
import localReputationManager.RepUpdater;

public class ClientTest {

	public static final String idTest = "1";

	public static void main(String[] args) throws UnknownHostException {

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater);
		repUpdaterThread.start();

		RepCollection repCollection = RepCollection.getInstance();

		StringTxClient strTx = new StringTxClient("localhost", 54321,
				"192.168.10.10", 2000);
		Thread strTxThread = new Thread(strTx);
		strTxThread.start();

		StatusMonitor statusMonitor = new StatusMonitor(repCollection, idTest);
		Thread statusMonitorThread = new Thread(statusMonitor);
		statusMonitorThread.start();

	}

}
