package localReputationManager;

/**
 * Fornisce al middleware le informazioni sulla reputazione dei nodi
 * @author Andrea Grandi
 *
 */
public class RepProvider {

	/**
	 * Ottiene il livello di reputazione massimo tra HRep e CRep per un particolare nodo
	 * @param nodeId	identificativo del nodo
	 * @return	RepLevel massimo tra HRep e CRep
	 * @see RepLevel
	 */
	public static RepLevel getMaxReputation(String nodeId) {
		RepCollection repCollection = RepCollection.getInstance();

		RepLevel hRep = repCollection.getHRep(nodeId);
		RepLevel cRep = repCollection.getCRep(nodeId);

		if (cRep.getLevel() > hRep.getLevel())
			return cRep;
		else
			return hRep;
	}

	/**
	 * Ottiene il livello di reputazione medio tra HRep e CRep per un particolare nodo
	 * @param nodeId	identificativo del nodo
	 * @return	media dei RepLevel HRep e CRep
	 * @see RepLevel
	 */
	public static RepLevel getAvgReputation(String nodeId) {
		RepCollection repCollection = RepCollection.getInstance();

		RepLevel hRep;
		RepLevel cRep;
		try {
			hRep = repCollection.getHRep(nodeId);
			cRep = repCollection.getCRep(nodeId);
		} catch (IllegalArgumentException e) {
			return new RepLevel();
		}

		return new RepLevel(Math.round((hRep.getLevel() + cRep.getLevel()) / 2));
	}

	/**
	 * Indica se il comportamento di un particolare nodo è considerato selfish
	 * @param nodeId identificativo del nodo
	 * @return	true se il nodo è selfish, false altrimenti
	 * @see RepLevel
	 */
	public static boolean isSelfishNode(String nodeId) {
		return getAvgReputation(nodeId).isLowRep();
	}
	
	/**
	 * Indica se il comportamento di un particolare nodo è considerato corretto
	 * @param nodeId identificativo del nodo
	 * @return	true se il nodo è trusted, false altrimenti
	 * @see RepLevel
	 */
	public static boolean isTrustedNode(String nodeId) {
		return getAvgReputation(nodeId).isHighRep();
	}


}
