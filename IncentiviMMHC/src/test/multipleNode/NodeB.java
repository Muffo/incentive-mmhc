package test.multipleNode;

import java.net.UnknownHostException;

import reputationExchangeManager.ExClient;
import reputationManager.RepCollection;
import reputationManager.RepUpdater;
import test.support.AutoStringTxClient;
import test.support.StatusMonitor;

public class NodeB {

	public static void main(String[] args) throws UnknownHostException {

		RepCollection repCollection = RepCollection.getInstance();
		repCollection.setSaveFileName("reputationB.txt");
		repCollection.LoadFromFile();

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater);
		repUpdaterThread.start();

		ExClient exClient = new ExClient();
		Thread exClientThread = new Thread(exClient);
		exClientThread.start();

		StatusMonitor statusMonitor = new StatusMonitor(repCollection,
				"statusB.txt");
		Thread statusMonitorThread = new Thread(statusMonitor, "statusMonitor");
		statusMonitorThread.start();

		AutoStringTxClient strTx = new AutoStringTxClient("localhost", 54321,
				"192.168.10.10", 2000);
		Thread strTxThread = new Thread(strTx, "strTx");
		strTxThread.start();

	}
}
