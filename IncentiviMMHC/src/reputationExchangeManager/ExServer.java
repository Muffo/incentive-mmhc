package reputationExchangeManager;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Rimane in attesa dei dati sulla reputazione proveniente dai nodi remoti.
 * Al momento della ricezione crea e avvia correttamente un istanza di RepReceiver
 * 
 * @author Andrea Grandi
 * @see RepReceiver
 */
public class ExServer implements Runnable{

	public static final int serverPort = 54001;

	public void run() {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(serverPort, 2);
			serverSocket.setReuseAddress(true);
			System.out.println("ExServer: avviato ");
			System.out.println("Creata la server socket: " + serverSocket);
		} catch (Exception e) {
			System.err.println("Problemi nella creazione della server socket: "
					+ e.getMessage());
			e.printStackTrace();
			System.exit(2);
		}
		try {

			while (true) {

				Socket clientSocket = null;

				try {
					clientSocket = serverSocket.accept();
					RepReceiver repReceiver = new RepReceiver(clientSocket);
					Thread repRecThread = new Thread(repReceiver);
					repRecThread.start();
				} catch (Exception e) {
					System.err
							.println("Problemi nella accettazione della connessione: "
									+ e.getMessage());
					e.printStackTrace();
					continue;
				}
			} // while (true)
		} catch (Exception e) {
			e.printStackTrace();
			// chiusura di stream e socket
			System.out
					.println("Errore irreversibile, PutFileServerSeq: termino...");
			System.exit(3);
		}
	} // run
}
