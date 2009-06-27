package test;

import java.io.*;
import java.net.UnknownHostException;

import connectionStatusManager.ConnectionNotifier;
import localReputationManager.RepCollection;
import localReputationManager.RepProvider;
import localReputationManager.RepUpdater;

public class LrmTest {

	public static final String idTest = "1";

	public static void main(String[] args) throws InterruptedException,
			UnknownHostException {

		RepUpdater repUpdater = new RepUpdater();
		Thread repUpdaterThread = new Thread(repUpdater);
		repUpdaterThread.start();

		RepCollection repCollection = RepCollection.getInstance();

		StringTxClient strTx = new StringTxClient("localhost", 54321, "", 2000);
		Thread strTxThread = new Thread(strTx);
		strTxThread.start();

		while (true) {
			Thread.sleep(1000);
			printStatus(repCollection);
		}

		// for (int i = 0; i < 20; i++) {
		// ConnectionNotifier.notifyResult(idTest, false);
		// Thread.sleep(1000);
		//
		// printStatus(repCollection);
		// }
		//
		// for (int i = 0; i < 10; i++) {
		// ConnectionNotifier.notifyResult(idTest, true);
		// Thread.sleep(1000);
		// printStatus(repCollection);
		// }

	}

	private static void printStatus(RepCollection repCollection) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("result.txt", true));

			// writer.write("**********");
			writer.write(repCollection.toString() + "\n");
			writer.write("Rep " + RepProvider.getAvgReputation(idTest)
					+ " / selfish: " + RepProvider.isSelfishNode(idTest)
					+ " / trusted: " + RepProvider.isTrustedNode(idTest)
					+ " / ConnNotifierStatus: "
					+ ConnectionNotifier.getStatusOf(idTest) + "\n");

			writer.close();
		} catch (IOException e) {
			System.out.println("Errore scrittura sul file result.txt");
			e.printStackTrace();
		}
	}
}
