package test.support;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import localReputationManager.RepCollection;
import localReputationManager.RepLevel;
import localReputationManager.RepProvider;
import connectionStatusManager.ConnectionNotifier;

/**
 * Stampa su un file le reputazioni memorizzate localmente su un nodo, insieme
 * ad altre informazioni utili per monitorarne lo stato.
 * 
 * @author Andrea Grandi
 * 
 */
public class StatusMonitor implements Runnable {

	/**
	 * Tempo in millisec tra le operazioni di monitoraggio
	 */
	private static final int monitorDelay = 1000;

	/**
	 * Nome di default del file di output
	 */
	private final static String defaultFileName = "status.txt";

	private RepCollection _repCollection;
	private String _fileName;

	/**
	 * Effettua il monitoraggio del {@link RepCollection} passato, scrivendo
	 * l'output sul file di defu
	 * 
	 * @param repCollection
	 *            reputazioni da monitorare
	 */
	public StatusMonitor(RepCollection repCollection) {
		this(repCollection, defaultFileName);
	}

	/**
	 * Effettua il monitoraggio del {@link RepCollection} passato, scrivendo i
	 * 
	 * @param repCollection
	 * @param fileName
	 */
	public StatusMonitor(RepCollection repCollection, String fileName) {
		_repCollection = repCollection;
		_fileName = fileName;
	}

	public void run() {
		System.out.println("Monitor: avviato ");
		while (true) {
			try {
				Thread.sleep(monitorDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			printStatus();
		}

	}

	/**
	 * Stampa lo stato corrente della reputazione sul file di output
	 */
	private void printStatus() {
		BufferedWriter writer;
		try {
			Date todaysDate = new java.util.Date();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"EEE, dd-MMM-yyyy HH:mm:ss");
			String formattedDate = formatter.format(todaysDate);

			writer = new BufferedWriter(new FileWriter(_fileName, true));

			writer.write("**** " + formattedDate + " *****\n");

			Enumeration keys = _repCollection.getIdentifiers();
			while (keys.hasMoreElements()) {
				String nodeId = (String) keys.nextElement();

				RepLevel hRep = _repCollection.getHRep(nodeId);
				RepLevel cRep = _repCollection.getCRep(nodeId);

				writer.write("NodeId: " + nodeId + "\thRep: " + hRep
						+ "\tcRep: " + cRep + "\tSelfish: "
						+ RepProvider.isSelfishNode(nodeId) + "\tTrusted: "
						+ RepProvider.isTrustedNode(nodeId)
						+ "\tConnNotifierStatus: "
						+ ConnectionNotifier.getStatusOf(nodeId) + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Errore scrittura sul file status.txt");
			e.printStackTrace();
		}
	}
}
