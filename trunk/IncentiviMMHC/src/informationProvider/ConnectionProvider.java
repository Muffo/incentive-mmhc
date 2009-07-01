package informationProvider;

/**
 * Fornisce a LRM informazioni sulle possibili connessioni single-hop con i nodi
 * vicini
 * 
 * @author Andrea Grandi
 * 
 */
public class ConnectionProvider {

	/**
	 * Fornisce l'elenco degli indirizzi IP dei nodi vicini con i quali sono
	 * state instaurate connessioni single-hop
	 * 
	 * @return array di String contenete gli indirizzi IP
	 */
	public static String[] getSingleHopConnections() {
		String result[] = { "192.168.0.1" };
		return result;
	}

}
