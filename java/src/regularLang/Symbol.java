package regularLang;

/**
 * A class for symbols(alphabets).
 * 
 * @author Orb_H
 * @since 2019-10-16
 */
public class Symbol {

	/**
	 * A symbol of a language.
	 */
	private String symbol;

	/**
	 * A special symbol indicating ех, which indicates a blank symbol whose length is
	 * 0.
	 */
	public static final Symbol EPSILON = new Symbol("");

	/**
	 * Creates a new object with given symbol. This object is considered as a same
	 * object when {@code symbol} of the other object is same. In other words,
	 * {@code symbol.equals(other.symbol)} matters.
	 * 
	 * @param symbol - A symbol of a language.
	 */
	public Symbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns a symbol of this object.
	 * 
	 * @return A symbol.
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other.getClass().equals(this.getClass()))
			if (symbol.equals(((Symbol) other).symbol))
				return true;
		return false;
	}

	@Override
	public String toString() {
		return symbol;
	}

}
