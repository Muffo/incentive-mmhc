package test.localReputationManager;

import java.net.*;
import java.io.*;

import connectionStatusManager.ConnectionNotifier;

public class StringTxClient implements Runnable {

	private String _remoteAddr = null;
	private int _remotePort = -1;

	private InetAddress _relayAddr = null;
	private int _relayPort = -1;

	public StringTxClient(String relayAddr, int relayPort, String remoteAddr,
			int remotePort) throws UnknownHostException {
		_remoteAddr = remoteAddr;
		_remotePort = remotePort;

		_relayAddr = InetAddress.getByName(relayAddr);
		_relayPort = relayPort;
	}

	public void run() {

		try {

			// gestione input da console dell'utente
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			String txString = null;
			System.out.println("Inserisci uno dei seguenti comandi: ");
			System.out.println("[Stringa] - testo da trasmettere");
			System.out.println("[int] - numero ripetizioni");
			System.out.println("exit - esci");
			

			System.out.print("Comando: ");

			while ((txString = stdIn.readLine()) != null) {

				if (txString.equalsIgnoreCase("exit")) {
					System.out.print("Ricevuto \"exit\":  esco");
					return;
				}

				int repeat = 0;

				try {
					repeat = Integer.parseInt(txString);

					for (int i = 0; i < repeat; i++) {
						String result = sendString("pippo");

						System.out.println("Risultato: " + result);
						if (result.equalsIgnoreCase("ok"))
							ConnectionNotifier.notifyResult("1", true);
						else
							ConnectionNotifier.notifyResult("1", false);
					}
				} catch (NumberFormatException e) {
					String result = sendString(txString);

					System.out.println("Risultato: " + result);
					if (result.equalsIgnoreCase("ok"))
						ConnectionNotifier.notifyResult("1", true);
					else
						ConnectionNotifier.notifyResult("1", false);
				}

				System.out.print("Comando: ");

			}// while
			System.out.println("Termino...");
		} catch (Exception e) {
			System.err.println("Errore irreversibile, il seguente: ");
			e.printStackTrace();
			System.err.println("Chiudo!");
			System.exit(3);
		}

	} // main

	public String sendString(String txString) throws IOException {

		Socket localSocket = null;
		InetSocketAddress relaySocketAddress = null;
		DataOutputStream outSock = null;
		DataInputStream inSock = null;

		String result = null;

		// creazione socket
		try {
			relaySocketAddress = new InetSocketAddress(_relayAddr,
			 _relayPort);

			localSocket = new Socket();

			localSocket.connect(relaySocketAddress, 2000);
			localSocket.setSoTimeout(5000);
			System.out.println("Creata la socket: " + localSocket);
		} catch (SocketTimeoutException ste) {
			// ste.printStackTrace();
			return "Timeout scattato durante la creazione della socket.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Problemi nella creazione della socket";
		}

		// creazione stream di input/output su socket
		try {
			outSock = new DataOutputStream(localSocket.getOutputStream());
			inSock = new DataInputStream(localSocket.getInputStream());
		} catch (IOException e) {
			localSocket.close();
			// e.printStackTrace();
			return "Problemi nella creazione degli stream su socket";
		}

		// trasmissione della stringa
		try {
			outSock.writeUTF(_remoteAddr);
			outSock.writeInt(_remotePort);
			outSock.writeUTF(txString);

		} catch (Exception e) {
			localSocket.close();
			// e.printStackTrace();
			return "Problemi nell'invio di " + txString;
		}

		// ricezione risultato
		try {
			result = inSock.readUTF();
		} catch (SocketTimeoutException ste) {
			localSocket.close();
			// ste.printStackTrace();
			return "Timeout scattato durante l'attesa del risultato.";
		} catch (Exception e) {
			localSocket.close();
			// e.printStackTrace();
			return "Problemi nella ricezione del risultato";
		}

		localSocket.close();
		return result;
	}

}
