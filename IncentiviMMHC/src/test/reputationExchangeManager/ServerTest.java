package test.reputationExchangeManager;

import java.net.UnknownHostException;

import reputationExchangeManager.ExServer;
import reputationManager.RepCollection;
import reputationManager.RepUpdater;
import test.support.StatusMonitor;

public class ServerTest {

	public static void main(String[] args) throws UnknownHostException {

		RepCollection repCollection = RepCollection.getInstance();
		repCollection.setSaveFileName("reputationServer.txt");
		repCollection.LoadFromFile();

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater, "repUpdater");
		repUpdaterThread.start();

		ExServer exServer = new ExServer();
		Thread exServerThread = new Thread(exServer, "exServer");
		exServerThread.start();

		StatusMonitor statusMonitor = new StatusMonitor(repCollection,
				"statusServer.txt");
		Thread statusMonitorThread = new Thread(statusMonitor, "statusMonitor");
		statusMonitorThread.start();

	}

}
