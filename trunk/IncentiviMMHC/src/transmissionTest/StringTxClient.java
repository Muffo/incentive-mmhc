package transmissionTest;

import java.net.*;
import java.io.*;

public class StringTxClient {

	public static void main(String[] args) throws IOException {
		InetAddress remoteAddr = null;
		int remotePort = -1;

		/* controllo argomenti */
		try {
			if (args.length == 2) {
				remoteAddr = InetAddress.getByName(args[0]);
				remotePort = Integer.parseInt(args[1]);
			} else {
				System.out.println("Usage: java StringTxClient serverAddr serverPort");
				System.exit(1);
			}
		} // try
		catch (Exception e) {
			System.out.println("Problemi, i seguenti: ");
			e.printStackTrace();
			System.out
					.println("Usage: java StringTxClient serverAddr serverPort");
			System.exit(2);
		}

		// oggetti utilizzati dal client per la comunicazione
		Socket localSocket = null;
		InetSocketAddress remoteSocketAddress = null;
		DataOutputStream outSock = null;

		
		
		try {
			// creazione socket
			try {
				remoteSocketAddress = new InetSocketAddress(remoteAddr,	remotePort);
				localSocket = new Socket();

				// setto il timeout per non bloccare indefinitivamente
				// il client
				localSocket.connect(remoteSocketAddress, 2000);
				localSocket.setSoTimeout(5000);
				System.out.println("Creata la socket: " + localSocket);
			} catch (SocketTimeoutException ste) {
				System.out.println("Timeout scattato durante la creazione della socket.");
				ste.printStackTrace();
				System.exit(3);
				
			} catch (Exception e) {
				System.out.println("Problemi nella creazione della socket: ");
				e.printStackTrace();
				System.exit(3);
			}
			
			// creazione stream di input/output su socket
			try {
				outSock = new DataOutputStream(localSocket.getOutputStream());
			} catch (IOException e) {
				System.out.println("Problemi nella creazione degli stream su socket: ");
				e.printStackTrace();
				System.exit(3);
				// il client continua l'esecuzione riprendendo
				// dall'inizio del ciclo
			}
			
			// gestione input da console dell'utente
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String txString = null;
			System.out.print("\"exit\" per uscire, oppure immetti stringa da trasmettere: ");
			
			while ((txString = stdIn.readLine()) != null) {
				
				if (txString.equalsIgnoreCase("exit"))
				{
					System.out.print("Ricevuto \"exit\": chiudo la socket ed esco");
					localSocket.setSoLinger(true, 3000);
					localSocket.shutdownOutput();
					localSocket.close();
					System.exit(0);
				}
				
				// trasmissione della stringa
				try {
					outSock.writeUTF(txString);
				} catch (Exception e) {
					System.out.println("Problemi nell'invio di " + txString + ": ");
					e.printStackTrace();
					System.out.print("\"exit\" per uscire, oppure immetti stringa da trasmettere: ");
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
					continue;
				}

				// tutto ok, pronto per nuova richiesta
				System.out.print("\"exit\" per uscire, oppure immetti stringa da trasmettere: ");


			}// while
			System.out.println("Termino...");
		}
		
		// qui catturo le eccezioni non catturate all'interno del while
		// quali per esempio la caduta della connessione con il server
		// in seguito alle quali il client termina l'esecuzione
		catch (Exception e) {
			System.err.println("Errore irreversibile, il seguente: ");
			e.printStackTrace();
			System.err.println("Chiudo!");
			System.exit(3);
		}

	} // main

}
