package test.reputationExchangeManager;

import java.net.UnknownHostException;
import localReputationManager.RepUpdater;
import reputationExchangeManager.ExClient;

public class ClientTest {

	public static final String idTest = "1";

	public static void main(String[] args) throws UnknownHostException {

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater);
		repUpdaterThread.start();

		ExClient exClient = new ExClient();
		Thread exClientThread = new Thread(exClient);
		exClientThread.start();
	}
}
