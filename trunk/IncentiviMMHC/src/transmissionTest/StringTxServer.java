package transmissionTest;

import java.io.*;
import java.net.*;

public class StringTxServer {

	public static final int PORT = 54321;

	public static void main(String[] args) throws IOException {

		int port = -1;

		/* controllo argomenti */
		try {
			if (args.length == 1) {
				port = Integer.parseInt(args[0]);
				// controllo che la porta sia nel range consentito 1024-65535
				if (port < 1024 || port > 65535) {
					System.out
							.println("Usage: java StringTxServer [serverPort>1024]");
					System.exit(1);
				}
			} else if (args.length == 0) {
				port = PORT;
			} else {
				System.out
						.println("Usage: java StringTxServer [serverPort>1024]");
				System.exit(1);
			}
		} // try
		catch (Exception e) {
			System.out.println("Problemi, i seguenti: ");
			e.printStackTrace();
			System.out.println("Usage: java StringTxServer [serverPort>1024]");
			System.exit(1);
		}

		// preparazione socket
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port, 2);
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

			Socket clientSocket = null;
			DataInputStream inSock = null;
			//DataOutputStream outSock = null;

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
				System.exit(5);

			}
			try {
				inSock = new DataInputStream(clientSocket.getInputStream());
				//outSock = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				System.out
						.println("Problemi nella creazione degli stream di input/output "
								+ "su socket: ");
				e.printStackTrace();
				System.exit(6);
			}
			// stringa ricezione file
			String rxString;
			while (true) {

				try {
					rxString = inSock.readUTF();
				} catch (SocketTimeoutException ste) {
					System.out.println("Timeout scattato: ");
					ste.printStackTrace();
					clientSocket.close();
					System.out
							.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
					// il client continua l'esecuzione riprendendo dall'inizio
					// del ciclo
					continue;
				} catch (Exception e) {
					System.out
							.println("Problemi nella ricezione del nome del file: ");
					e.printStackTrace();
					// servo nuove richieste
					continue;
				}

				if (rxString == null) {
					System.out
							.println("Problemi nella ricezione della stringa");
					continue;
				} else if (rxString.equalsIgnoreCase("exit"))
				{
					System.out.print("Ricevuto \"exit\": chiudo la socket ed esco");
					clientSocket.close();
					System.exit(0);
				}
				else
				{	
					System.out.println("Stringa ricevuta:" + rxString);
				}
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
