package test.reputationExchangeManager;

import java.net.UnknownHostException;

import localReputationManager.RepCollection;
import localReputationManager.RepUpdater;
import reputationExchangeManager.ExClient;

public class ClientTest {

	public static void main(String[] args) throws UnknownHostException {

		RepCollection repCollection = RepCollection.getInstance();
		repCollection.setSaveFileName("reputationClient.txt");
		repCollection.LoadFromFile();

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater);
		repUpdaterThread.start();

		ExClient exClient = new ExClient();
		Thread exClientThread = new Thread(exClient);
		exClientThread.start();
	}
}
