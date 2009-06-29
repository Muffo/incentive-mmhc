package informationProvider;

/**
 * Rappresenta un valore del parametro NBL per un certo nodo.
 * 
 * @author Andrea Grandi
 */
public class NodeBatteryLevel {

	/**
	 * Soglia energetica sotto la quale un nodo è considerato scarico
	 */
	private static final int Threshold = 20;

	private int _nbl;

	public NodeBatteryLevel(int et) {
		_nbl = et;
	}

	/**
	 * Indica se il nodo ha la batteria esaurita oltre la soglia prestabilita.
	 * 
	 * @return true se la batteria è scarica, false altrimenti
	 */
	public boolean isExhausted() {
		return (_nbl < Threshold);
	}
}
