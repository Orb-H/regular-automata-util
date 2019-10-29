package regularLang;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
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
	private RegularLanguage() {
		states = new HashSet<>();
		start = null;
		symbol = new HashSet<>();
	}

	/**
	 * A builder for {@link RegularLanguage}. This builder now replaces all the
	 * constructors of {@link RegularLanguage} and original constructors are
	 * removed.
	 * 
	 * @author Orb_H
	 * @since 2019-10-24
	 */
	static class Builder {
		/**
		 * A set of states of regular language.
		 * 
		 * @see {@link RegularLanguage#states}
		 */
		private Set<State> states = new HashSet<>();
		/**
		 * A set of symbols of regular language.
		 * 
		 * @see {@link RegularLanguage#symbols}
		 */
		private Set<Symbol> symbols = new HashSet<>();
		/**
		 * A start state of regular language.
		 * 
		 * @see {@link RegularLanguage#start}
		 */
		private State start = null;

		// States
		/**
		 * Adds a new state with name {@code name}. Equivalent with
		 * {@code addState(new State(name))}.
		 * 
		 * @param name - A name of state to add.
		 * @return A builder object.
		 * @see {@link Builder#addState(State)}
		 */
		public Builder addStateS(String name) {
			return this.addState(new State(name));
		}

		/**
		 * Adds a new state {@code s}.
		 * 
		 * @param state - A state to add.
		 * @return A builder object.
		 */
		public Builder addState(State state) {
			states.add(state);
			return this;
		}

		/**
		 * Adds states with given names {@code names}.
		 * 
		 * @param names - Names of states to add.
		 * @return A builder object.
		 */
		public Builder addStatesS(Iterable<String> names) {
			names.forEach(name -> addStateS(name));
			return this;
		}

		/**
		 * Adds given states.
		 * 
		 * @param states - States to add.
		 * @return A builder object.
		 */
		public Builder addStates(Iterable<State> states) {
			states.forEach(state -> addState(state));
			return this;
		}

		// Symbols
		/**
		 * Adds a new symbol with given string {@code symbol}. Equivalent with
		 * {@code addSymbol(new Symbol(symbol))}.
		 * 
		 * @param symbol - A string which indicates a symbol to add.
		 * @return A builder object.
		 * @see Builder#addSymbol(Symbol)
		 */
		public Builder addSymbolS(String symbol) {
			return addSymbol(new Symbol(symbol));
		}

		/**
		 * Adds a new symbol {@code symbol}.
		 * 
		 * @param symbol - A symbol to add.
		 * @return A builder object.
		 */
		public Builder addSymbol(Symbol symbol) {
			symbols.add(symbol);
			return this;
		}

		/**
		 * Adds symbols indicated by given strings {@code symbols}.
		 * 
		 * @param symbols - Strings of symbols to add.
		 * @return A builder object.
		 */
		public Builder addSymbolsS(Iterable<String> symbols) {
			symbols.forEach(symbol -> addSymbolS(symbol));
			return this;
		}

		/**
		 * Adds given symbols {@code symbols}.
		 * 
		 * @param symbols - Symbols to add.
		 * @return A builder object.
		 */
		public Builder addSymbols(Iterable<Symbol> symbols) {
			symbols.forEach(symbol -> addSymbol(symbol));
			return this;
		}

		// Start state
		/**
		 * Set a start state defined by given name {@code start}.
		 * 
		 * @param start - A name of desired start state.
		 * @return A builder object.
		 * @throws IllegalArgumentException thrown if state is not found.
		 */
		public Builder setStartS(String start) {
			try {
				this.start = states.stream().filter(state -> state.getName().equals(start)).iterator().next();
			} catch (NoSuchElementException e) {
				throw new IllegalArgumentException("Can't find a state with given name: " + start);
			}
			return this;
		}

		/**
		 * Set a start state {@code start}.
		 * 
		 * @param start - A state to mark as start.
		 * @return A builder object.
		 * @throws IllegalArgumentException thrown if state is not found.
		 */
		public Builder setStart(State start) {
			if (!this.states.contains(start))
				throw new IllegalArgumentException(
						"Given state doesn't exist in the state set. Name: " + start.getName());
			this.start = start;
			return this;
		}

		// Final state
		/**
		 * Adds a final state defined by given name {@code end}.
		 * 
		 * @param end - A name of desired final state.
		 * @return A builder object.
		 * @throws IllegalArgumentException thrown if state is not found.
		 */
		public Builder addFinalS(String end) {
			try {
				State s = states.stream().filter(state -> state.getName().equals(end)).iterator().next();
				s.setFinal();
			} catch (NoSuchElementException e) {
				throw new IllegalArgumentException("Can't find a state with given name: " + end);
			}
			return this;
		}

		/**
		 * Adds a final state {@code end}.
		 * 
		 * @param end - A state to mark as final.
		 * @return A builder object.
		 * @throws IllegalArgumentException thrown if state is not found.
		 */
		public Builder addFinal(State end) {
			if (!this.states.contains(end))
				throw new IllegalArgumentException(
						"Given state doesn't exist in the state set. Name: " + end.getName());
			end.setFinal();
			return this;
		}

		/**
		 * Adds final states defined by given names {@code ends}.
		 * 
		 * @param ends - Names of states to mark as final.
		 * @return A builder object.
		 * @throws IllegalArgumentException thrown if state is not found by any of given
		 *                                  strings.
		 */
		public Builder addFinalsS(Iterable<String> ends) {
			ends.forEach(end -> addFinalS(end));
			return this;
		}

		/**
		 * Adds final states {@code ends}.
		 * 
		 * @param ends - States to mark as final.
		 * @return A builder object.
		 * @throws IllegalArgumentException thrown if any of state is not found.
		 */
		public Builder addFinals(Iterable<State> ends) {
			ends.forEach(end -> addFinal(end));
			return this;
		}

		// Build
		/**
		 * Returns a regular language constructed by this builder.
		 * 
		 * @return A resulting regular language.
		 */
		public RegularLanguage build() {
			RegularLanguage rl = new RegularLanguage();
			rl.states = states;
			rl.symbol = symbols;
			rl.start = start;
			return rl;
		}
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
	 * Set a start state with given id.
	 * 
	 * @param s - ID of state to set as a start state.
	 * @return {@code true} if start state is successfully set.
	 */
	public boolean setStartState(String s) {
		State st;
		if ((st = findState(s)) != null) {
			this.start = findState(s);
			return true;
		}
		return false;
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

		return s.stream().anyMatch((state) -> state.isFinal());
	}

}