package reputationExchangeManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Si occupa della ricezione dei valori di reputazione da un particolare nodo
 * remoto.
 * La ricezione viene avviata da {@link ExServer}.
 * I valori ricevuti vengono memorizzati in {@link RepReceived}
 * 
 * @author Andrea Grandi
 * @see ExServer
 */
public class RepReceiver implements Runnable {

	private Socket _clientSocket;

	public RepReceiver(Socket clientSocket) {
		_clientSocket = clientSocket;
	}

	public void run() {
		DataInputStream inSock = null;
		DataOutputStream outSock = null;

		try {
			inSock = new DataInputStream(_clientSocket.getInputStream());
			outSock = new DataOutputStream(_clientSocket.getOutputStream());
		} catch (IOException e) {
			// System.out
			// .println("Problemi nella creazione degli stream di input/output "
			// + "su socket: ");
			// e.printStackTrace();
			closeSocket();
			return;
		}

		String rxNodeId = null;
		int rxReputation = 0;
		int rxConuter = 0;

		try {
			// ricezione dei dati sulla reputazione
			while (true) {
				rxNodeId = inSock.readUTF();
				if (rxNodeId.equalsIgnoreCase("end"))
					break;
				
				rxReputation = inSock.readInt();
				System.out.println("NodeId: " + rxNodeId + " / rep: " + rxReputation);
				RepReceived.addRxReputation(rxNodeId, rxReputation);
				rxConuter++;		
			}
		} catch (SocketTimeoutException ste) {
			// System.out.println("Timeout scattato: ");
			// ste.printStackTrace();
			closeSocket();
			return;
		} catch (Exception e) {
			// System.out
			// .println("Problemi nella ricezione del nome del file: ");
			// e.printStackTrace();
			closeSocket();
			return;

		}

		try {
			outSock.writeInt(rxConuter);
		} catch (Exception e) {
			// System.out.println("Problemi nell'invio risposta");
			// e.printStackTrace();
			closeSocket();
			return;
		}

		closeSocket();
	}

	/**
	 * Chiude la socket in modo corretto, catturando le eccezioni
	 */
	private void closeSocket() {
		try {
			_clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
