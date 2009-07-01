package reputationExchangeManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import localReputationManager.RepCollection;
import localReputationManager.RepLevel;

/**
 * Invia la reputazione ad un nodo gestendo la comunicazione
 * 
 * @author Andrea Grandi
 * 
 */
public class RepSender {

	private InetAddress _destAddr = null;
	private int _destPort = -1;

	/**
	 * Setta l'indirizzo e la porta del nodo a cui inviare i dati sulla
	 * reputazione
	 * 
	 * @param destAddr
	 *            indirizzo di destinazione dei dati
	 * @param destPort
	 *            porta di destinazione dei dati
	 */
	public void setDestAddr(String destAddr, int destPort) {

		try {
			_destAddr = InetAddress.getByName(destAddr);
			_destPort = destPort;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Invia tutti i livelli HRep presenti in RepCollection al
	 * 
	 * @return
	 * @throws IOException
	 */
	public int SendRepData() throws IOException {
		Socket localSocket = null;
		InetSocketAddress destSocketAddress = null;
		DataOutputStream outSock = null;
		DataInputStream inSock = null;

		int result = 0;

		// creazione socket
		try {
			destSocketAddress = new InetSocketAddress(_destAddr, _destPort);

			localSocket = new Socket();

			localSocket.connect(destSocketAddress, 2000);
			localSocket.setSoTimeout(5000);
			System.out.println("Creata la socket: " + localSocket);
		} catch (SocketTimeoutException ste) {
			// ste.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		// creazione stream di input/output su socket
		try {
			outSock = new DataOutputStream(localSocket.getOutputStream());
			inSock = new DataInputStream(localSocket.getInputStream());
		} catch (IOException e) {
			localSocket.close();
			// e.printStackTrace();
			return 0;
		}

		// trasmissione della stringa
		try {
			RepCollection repCollection = RepCollection.getInstance();

			Enumeration keys = repCollection.getIdentifiers();

			while (keys.hasMoreElements()) {
				String nodeId = (String) keys.nextElement();
				RepLevel hRep = repCollection.getHRep(nodeId);

				outSock.writeUTF(nodeId);
				outSock.writeInt(hRep.getLevel());
			}

			outSock.writeUTF("end");

		} catch (Exception e) {
			localSocket.close();
			// e.printStackTrace();ù
			return 0;
		}

		// ricezione risultato
		try {
			result = inSock.readInt();
		} catch (SocketTimeoutException ste) {
			localSocket.close();
			// ste.printStackTrace();
			return 0;
		} catch (Exception e) {
			localSocket.close();
			// e.printStackTrace();
			return 0;
		}

		localSocket.close();
		return result;
	}

}
