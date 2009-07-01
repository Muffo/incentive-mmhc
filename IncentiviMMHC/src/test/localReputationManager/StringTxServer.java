package test.localReputationManager;

import java.io.*;
import java.net.*;

public class StringTxServer implements Runnable {

	private static final int PORT = 54321;

	private boolean _isSelfish;
	
	public StringTxServer(boolean isSelfish)
	{
		_isSelfish = isSelfish;
	}
	

	public void run() {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT, 2);
			serverSocket.setReuseAddress(true);
			System.out.println("StringTxServer: avviato ");
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
				DataInputStream inSock = null;
				DataOutputStream outSock = null;

				System.out.println("\nIn attesa di richieste...");
				try {

					clientSocket = serverSocket.accept();
					System.out
							.println("Connessione accettata: " + clientSocket);

				} catch (Exception e) {
					System.err
							.println("Problemi nella accettazione della connessione: "
									+ e.getMessage());
					e.printStackTrace();
					continue;
				}

				try {
					inSock = new DataInputStream(clientSocket.getInputStream());
					outSock = new DataOutputStream(clientSocket
							.getOutputStream());
				} catch (IOException e) {
					System.out
							.println("Problemi nella creazione degli stream di input/output "
									+ "su socket: ");
					e.printStackTrace();
					clientSocket.close();
					continue;

				}

				String rxString = null;
				String rxRemoteAddr = null;
				int rxRemotePort = -1;

				try {
					rxRemoteAddr = inSock.readUTF();
					rxRemotePort = inSock.readInt();
					rxString = inSock.readUTF();
				} catch (SocketTimeoutException ste) {
					System.out.println("Timeout scattato: ");
					ste.printStackTrace();
					clientSocket.close();
					continue;
				} catch (Exception e) {
					System.out
							.println("Problemi nella ricezione del nome del file: ");
					e.printStackTrace();
					clientSocket.close();
					continue;
				}

				if (rxString == null || rxRemoteAddr == null
						|| rxRemotePort < 0) {
					System.out
							.println("Problemi nella ricezione della stringa");
					continue;
				} else {
					System.out.println("Stringa ricevuta:" + rxString
							+ ". Simulazione di Inoltro i dati a "
							+ rxRemoteAddr + ":" + rxRemotePort);

					if (!_isSelfish) {
						try {
							outSock.writeUTF("ok");
						} catch (Exception e) {
							System.out.println("Problemi nell'invio risposta");
							e.printStackTrace();
							clientSocket.close();
							continue;
						}
					}
				}

				clientSocket.close();
			} // while (true)
		}
		catch (Exception e) {
			e.printStackTrace();
			// chiusura di stream e socket
			System.out
					.println("Errore irreversibile, PutFileServerSeq: termino...");
			System.exit(3);
		}
	} // run

}
