package informationProvider;

/**
 * Fornisce a LRM le informazioni riguardanti i parametri del middleware
 * @author Andrea Grandi
 *
 */
public class ParameterProvider {
	
	/**
	 * Recupera dal middleware NBL di un determinato nodo
	 * @param nodeId 	identificativo del nodo
	 * @return	NodeBatteryLevel del nodo specificato
	 */
	public static NodeBatteryLevel getNBL(String nodeId)
	{
		return new NodeBatteryLevel(100);
	}
	

	/**
	 * Recupera dal middleware ET di un determinato nodo
	 * @param nodeId 	identificativo del nodo
	 * @return	EstimatedThroughput del nodo specificato
	 */
	public static EstimatedThroughput getET(String nodeId)
	{
		return new EstimatedThroughput(100);
	}
}
