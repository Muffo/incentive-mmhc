package informationProvider;

/**
 * Rappresenta un valore del parametro ET per un certo nodo.
 * 
 * @author Andrea Grandi
 */
public class EstimatedThroughput {

	/**
	 * Soglia sotto la quale un nodo è considerato sovraccarico
	 */
	private static final int Threshold = 20;

	private int _et;

	public EstimatedThroughput(int et) {
		_et = et;
	}

	/**
	 * Indica se il nodo è sovraccarico dal punto di vista del traffico.
	 * 
	 * @return true se il nodo è sovraccarico, false altrimenti
	 */
	public boolean isOverloaded() {
		return (_et < Threshold);
	}

}
