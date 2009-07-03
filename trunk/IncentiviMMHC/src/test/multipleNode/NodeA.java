package test.multipleNode;

import java.net.UnknownHostException;

import localReputationManager.RepCollection;
import localReputationManager.RepUpdater;
import reputationExchangeManager.ExServer;
import test.support.StatusMonitor;

public class NodeA {

	public static void main(String[] args) throws UnknownHostException {

		RepCollection repCollection = RepCollection.getInstance();
		repCollection.setSaveFileName("reputationA.txt");
		repCollection.LoadFromFile();

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater);
		repUpdaterThread.start();

		ExServer exServer = new ExServer();
		Thread exServerThread = new Thread(exServer, "exServer");
		exServerThread.start();

		StatusMonitor statusMonitor = new StatusMonitor(repCollection,
				"statusA.txt");
		Thread statusMonitorThread = new Thread(statusMonitor, "statusMonitor");
		statusMonitorThread.start();
	}
}
