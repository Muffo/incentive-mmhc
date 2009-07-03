package reputationExchangeManager;

import java.io.IOException;

import informationProvider.ConnectionProvider;

/**
 * Avvia periodicamente la trasmissione dei dati sulla reputazione ai nodi
 * vicini.
 * 
 * @author Andrea Grandi
 * @see RepSender
 * 
 */
public class ExClient implements Runnable {

	/**
	 * Tempo il millisec tra due trasmissioni della reputazione
	 */
	private final static int txDelay = 20000;

	public void run() {

		RepSender repSender = new RepSender();

		while (true) {
			try {
				Thread.sleep(txDelay);
			} catch (InterruptedException e) {
				continue;
			}

			String connections[] = ConnectionProvider.getSingleHopConnections();

			for (int i = 0; i < connections.length; i++) {
				repSender.setDestAddr(connections[i], ExServer.serverPort);
				try {
					repSender.SendRepData();
				} catch (IOException e) {
					continue;
					// e.printStackTrace();
				}
			}

		}
	}
}
