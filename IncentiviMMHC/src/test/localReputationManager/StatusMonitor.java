package test.localReputationManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import localReputationManager.RepCollection;
import localReputationManager.RepProvider;
import connectionStatusManager.ConnectionNotifier;

public class StatusMonitor implements Runnable {

	private RepCollection _repCollection;
	private String _nodeId;

	public StatusMonitor(RepCollection repCollection, String nodeId) {
		_repCollection = repCollection;
		_nodeId = nodeId;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			printStatus();
		}

	}

	private void printStatus() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("status.txt", true));

			// writer.write("**********");
			writer.write(_repCollection.toString() + "\n");
			writer.write("Rep " + RepProvider.getAvgReputation(_nodeId)
					+ " / selfish: " + RepProvider.isSelfishNode(_nodeId)
					+ " / trusted: " + RepProvider.isTrustedNode(_nodeId)
					+ " / ConnNotifierStatus: "
					+ ConnectionNotifier.getStatusOf(_nodeId) + "\n");

			writer.close();
		} catch (IOException e) {
			System.out.println("Errore scrittura sul file result.txt");
			e.printStackTrace();
		}
	}

}
