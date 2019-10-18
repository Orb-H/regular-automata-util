package regularLang;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A class to control information of regular language.<br/>
 * <br/>
 * 
 * A regular language is a 5-tuple as below.<br/>
 * <code>L=(Q,q<sub>0</sub>,¥Ò,F,¥ä)</code><br/>
 * <ul>
 * <li><code>Q</code> is a set of states of a language.<br/>
 * </li>
 * <li><code>q<sub>0</sub></code> is a start state which is an element of
 * <code>Q</code>.<br/>
 * </li>
 * <li><code>¥Ò</code> is a set of symbols of a language.<br/>
 * </li>
 * <li><code>F</code> is a subset of <code>Q</code> whose elements are final
 * states of a language.<br/>
 * </li>
 * <li><code>¥ä</code> is a function which consumes a tuple of a state and a
 * symbol, and provides next state.</li>
 * </ul>
 * 
 * @author Orb_H
 * @since 2019-10-16
 */
public class RegularLanguage implements Function<String, Boolean> {

	/**
	 * A set of states of a language.({@code Q}) This states also contain
	 * information of final states({@code F}).
	 */
	private Set<State> states;
	/**
	 * A start state of a language.(<code>q<sub>0</sub></code>)
	 */
	private State start;
	/**
	 * A set of symbols of a language.({@code ¥Ò})
	 */
	private Set<Symbol> symbol;

	/**
	 * Default Constructor. Creates a language without any information.
	 */
	public RegularLanguage() {
		states = new HashSet<>();
		start = null;
		symbol = new HashSet<>();
	}

	/**
	 * Creates a language with symbols.
	 * 
	 * @param symbols - Possible symbols in language.
	 */
	public RegularLanguage(Iterable<String> symbols) {
		this();
		symbols.forEach((s) -> symbol.add(new Symbol(s)));
	}

	/**
	 * Creates a language with given symbols and states.
	 * 
	 * @param symbols - Possible symbols of language.
	 * @param state   - States of language.
	 */
	public RegularLanguage(Iterable<String> symbols, Iterable<String> state) {
		this(symbols);
		state.forEach(s -> states.add(new State(s)));
	}

	/**
	 * Creates a language with given symbols, states, and a start state.
	 * 
	 * @param symbols - Possible symbols of language.
	 * @param states  - States of language.
	 * @param start   - A start state of language.
	 */
	public RegularLanguage(Iterable<String> symbols, Iterable<String> states, String start) {
		this(symbols, states);
		this.start = findState(start);
	}

	/**
	 * Creates a language with given symbols, states, a start state, and end states.
	 * 
	 * @param symbols - Possible symbols of language.
	 * @param states  - States of language.
	 * @param start   - A start state of language.
	 * @param ends    - A set of end states of language.
	 */
	public RegularLanguage(Iterable<String> symbols, Iterable<String> states, String start, Iterable<String> ends) {
		this(symbols, states, start);
		ends.forEach(s -> findState(s).setFinal());
	}

	/**
	 * Returns a set of states of this language.
	 * 
	 * @return A set of states.
	 */
	public Set<State> getStates() {
		return states;
	}

	/**
	 * Returns a start state of this language.
	 * 
	 * @return A Start state.
	 */
	public State getStartState() {
		return start;
	}

	/**
	 * Returns a set of final states of this language.
	 * 
	 * @return A set of final states.
	 */
	public Set<State> getFinalState() {
		Set<State> state = new HashSet<>();
		states.stream().filter((s) -> s.isFinal()).forEach((s) -> state.add(s));
		return state;
	}

	/**
	 * Returns a set of possible symbols of this language.
	 * 
	 * @return A set of symbols.
	 */
	public Set<Symbol> getSymbols() {
		return symbol;
	}

	/**
	 * Returns a state which has an ID {@code id}.
	 * 
	 * @param id - An ID of state.
	 * @return A state with given ID.
	 */
	public State findState(String id) {
		return states.stream().filter(state -> state.getName().equals(id)).iterator().next();
	}

	/**
	 * Returns a symbol with given string.
	 * 
	 * @param sym - A target string to find.
	 * @return A symbol with given string.
	 */
	public Symbol findSymbol(String sym) {
		return symbol.stream().filter((symbol) -> symbol.getSymbol().equals(sym)).iterator().next();
	}

	/**
	 * Adds a new pair of transition function {@code (src, sym) -> (dst)}.
	 * 
	 * @param src - Source state
	 * @param sym - Symbol
	 * @param dst - Destination state, value of pair
	 * @return {@code true} if the new pair is successfully added to dataset,
	 *         {@code false} if there is already same pair, or one or more
	 *         parameters are {@code null}.
	 */
	public boolean addDelta(String src, String sym, String dst) {
		State source = findState(src);
		Symbol symbol = findSymbol(sym);
		State destination = findState(dst);

		if (source == null || symbol == null || destination == null)
			return false;

		return source.addNext(symbol, destination);
	}

	/**
	 * Checks if given input {@code tar} can be accepted by this automaton, i.e.
	 * {@code tar} is an element of this language.
	 * 
	 * @param tar - An input string.
	 * @return {@code true} if input string is accepted.
	 * @exception IllegalArgumentException Thrown if given string contains symbols
	 *                                     which this language doesn't contain.
	 */
	@Override
	public Boolean apply(String tar) {
		java.util.Queue<Symbol> symbolize = new LinkedList<>();
		while (tar.length() > 0) {
			String tmp = tar;
			Optional<Symbol> match = symbol.stream().filter((s) -> tmp.startsWith(s.getSymbol())).findFirst();
			if (!match.isPresent())
				throw new IllegalArgumentException("input string is not an element of ¥Ò*");
			Symbol sym = match.get();
			symbolize.add(sym);
			tar = tar.substring(sym.getSymbol().length());
		}

		Set<State> s = new HashSet<>();
		s.addAll(start.getEClosure());

		while (!symbolize.isEmpty()) {
			if (s.isEmpty())
				return false;
			final Set<State> t = new HashSet<>();
			Symbol next = symbolize.poll();
			s.forEach((state) -> state.getNext(next).forEach((state2) -> t.addAll(state2.getEClosure())));
			s = t;
		}

		if (s.stream().anyMatch((state) -> state.isFinal()))
			return true;
		return false;
	}

}