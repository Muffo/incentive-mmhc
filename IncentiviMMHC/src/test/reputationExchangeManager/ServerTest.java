package test.reputationExchangeManager;

import java.net.UnknownHostException;

import localReputationManager.RepCollection;
import localReputationManager.RepUpdater;
import reputationExchangeManager.ExServer;
import test.localReputationManager.StatusMonitor;

public class ServerTest {
	
public static final String idTest = "1";
	
	public static void main(String[] args) throws UnknownHostException {
		
		RepCollection repCollection = RepCollection.getInstance();

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater, "repUpdater");
		repUpdaterThread.start();
		
		ExServer exServer = new ExServer();
		Thread exServerThread = new Thread(exServer, "exServer");
		exServerThread.start();
		
		StatusMonitor statusMonitor = new StatusMonitor(repCollection, idTest);
		Thread statusMonitorThread = new Thread(statusMonitor, "statusMonitor");
		statusMonitorThread.start();

	}

}
