package test;

import connectionStatusManager.ConnectionNotifier;
import localReputationManager.RepCollection;
import localReputationManager.RepProvider;
import localReputationManager.RepUpdater;

public class LrmTest {

	public static final String idTest = "10";

	public static void main(String[] args) throws InterruptedException {

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater);
		
		repUpdaterThread.start();
		
		RepCollection repCollection = RepCollection.getInstance();

		

		for (int i = 0; i < 20; i++) {
		
			ConnectionNotifier.notifyResult(idTest, false);
			Thread.sleep(1000);
			
			printStatus(repCollection);
		}

		for (int i = 0; i < 10; i++) {
				ConnectionNotifier.notifyResult(idTest, true);
			
			Thread.sleep(1000);
			printStatus(repCollection);
		}

	}

	private static void printStatus(RepCollection repCollection) {
		//System.out.println("**********");
		System.out.println(repCollection);
		System.out.println("Rep " + RepProvider.getAvgReputation(idTest)
				+ " / selfish: " + RepProvider.isSelfishNode(idTest) + " / ConnNotifierStatus: " + ConnectionNotifier.getStatusOf(idTest));
	}
}
