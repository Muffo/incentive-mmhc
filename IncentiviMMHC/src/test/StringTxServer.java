package test;

import java.io.*;
import java.net.*;

public class StringTxServer {

	public static final int PORT = 54321;

	public static void main(String[] args) throws IOException {

		// preparazione socket
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

			String rxString;
			while (true) {

				Socket clientSocket = null;
				DataInputStream inSock = null;
				DataOutputStream outSock = null;

				System.out.println("\nIn attesa di richieste...");
				try {

					clientSocket = serverSocket.accept();
					System.out.println("Connessione accettata: " + clientSocket
							+ "\n");

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

				try {
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

				if (rxString == null) {
					System.out
							.println("Problemi nella ricezione della stringa");
					continue;
				} else {
					System.out.println("Stringa ricevuta:" + rxString);
					try {
						outSock.writeUTF("ok");
					} catch (Exception e) {
						System.out.println("Problemi nell'invio risposta");
						e.printStackTrace();
						clientSocket.close();
						continue;
					}
				}
				
				clientSocket.close();
			} // while (true)
		}
		// qui catturo le eccezioni non catturate all'interno del while
		// in seguito alle quali il server termina l'esecuzione
		catch (Exception e) {
			e.printStackTrace();
			// chiusura di stream e socket
			System.out
					.println("Errore irreversibile, PutFileServerSeq: termino...");
			System.exit(3);
		}
	} // main

}
