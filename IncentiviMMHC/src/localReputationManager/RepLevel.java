/**
 * local
 */
package localReputationManager;

/**
 * Mantiene le informazioni su un livello di reputazione
 * 
 * @author Andrea Grandi
 */
public class RepLevel implements Comparable<RepLevel> {

	/**
	 * Livello di reputazione massimo
	 */
	private static final int MaxRep = 6000;

	/**
	 * Livello di reputazione minimo
	 */
	private static final int MinRep = 0;

	/**
	 * Soglia dell'intervallo LowRep
	 */
	private static final int LowRepThreshold = 2000;

	/**
	 * Soglia dell'intervallo HighRep
	 */
	private static final int HighRepThreshold = 4000;

	/**
	 * Valore di default iniziale per nodi non conosciuti
	 */
	private static final int DefaultRep = 3000;

	private int _level;

	public RepLevel() {
		this(DefaultRep);
	}

	public RepLevel(int level) {
		setLevel(level);
	}

	/**
	 * Setta il livello di reputazione ad un determinato valore
	 * 
	 * @param level
	 *            nuovo livello di reputazione
	 */
	public void setLevel(int level) {
		if (level > MaxRep)
			_level = MaxRep;

		else if (level < MinRep)
			_level = MinRep;

		else
			_level = level;
	}

	/**
	 * Ottiene il livello di reputazione corrente
	 * 
	 * @return livello di reputazione
	 */
	public int getLevel() {
		return _level;
	}

	/**
	 * Indica se il livello di reputazione appartiene all'intervallo LowRep
	 * 
	 * @return true se appartiene a LowRep, false altrimenti
	 */
	public boolean isLowRep() {
		return (_level < LowRepThreshold);
	}

	/**
	 * Indica se il livello di reputazione appartiene all'intervallo HighRep
	 * 
	 * @return true se appartiene a HighRep, false altrimenti
	 */
	public boolean isHighRep() {
		return (_level > HighRepThreshold);
	}

	public String toString() {
		return "" + _level;
	}

	public int compareTo(RepLevel reputation) {
		return this.getLevel() - reputation.getLevel();

	}
}
