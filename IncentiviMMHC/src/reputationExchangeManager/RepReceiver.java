package reputationExchangeManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Si occupa della ricezione dei valori di reputazione da un particolare nodo
 * remoto.
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

		String rxString = null;
		String rxRemoteAddr = null;
		int rxRemotePort = -1;

		try {
			// ricezione dei dati sulla reputazione
			rxRemoteAddr = inSock.readUTF();
			rxRemotePort = inSock.readInt();
			rxString = inSock.readUTF();
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

		if (rxString == null || rxRemoteAddr == null || rxRemotePort < 0) {
			System.out.println("Problemi nella ricezione della stringa");

		} else {
			System.out.println("Stringa ricevuta:" + rxString
					+ ". Simulazione di Inoltro i dati a " + rxRemoteAddr + ":"
					+ rxRemotePort);

			// ********

			if (true) {
				try {
					outSock.writeUTF("ok");
				} catch (Exception e) {
					System.out.println("Problemi nell'invio risposta");
					e.printStackTrace();
					closeSocket();
					return;
				}
			}
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
