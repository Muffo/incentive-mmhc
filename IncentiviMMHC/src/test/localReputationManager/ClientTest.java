package test.localReputationManager;

import java.net.UnknownHostException;
import localReputationManager.RepCollection;
import localReputationManager.RepUpdater;

public class ClientTest {

	public static final String idTest = "1";

	public static void main(String[] args) throws UnknownHostException {

		RepCollection repCollection = RepCollection.getInstance();

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater, "repUpdater");
		repUpdaterThread.start();

		StringTxClient strTx = new StringTxClient("localhost", 54321,
				"192.168.10.10", 2000);
		Thread strTxThread = new Thread(strTx, "strTx");
		strTxThread.start();

		StatusMonitor statusMonitor = new StatusMonitor(repCollection, idTest);
		Thread statusMonitorThread = new Thread(statusMonitor, "statusMonitor");
		statusMonitorThread.start();

	}

}
