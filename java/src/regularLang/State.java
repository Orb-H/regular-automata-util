package regularLang;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class for states.
 * 
 * @author Orb_H
 * @since 2019-10-16
 */
public class State {

	/**
	 * A name for state.
	 */
	private String name;
	/**
	 * Next states determined by input.
	 */
	private Map<Symbol, Set<State>> next;

	/**
	 * Creates a new state with given name. Since name is considered as ID, there
	 * should be no more than one state containing same name in one automata. Also,
	 * if a state is compared with other state where name of two states are same, it
	 * will considered as a same state.
	 * 
	 * @param name - A name of state.
	 */
	public State(String name) {
		this.name = name;
		next = new HashMap<>();
		Set<State> cur = new HashSet<>();
		cur.add(this);
		next.put(Symbol.EPSILON, cur);
	}

	/**
	 * Adds new destination state for input symbol {@code sym}.
	 * 
	 * @param sym - An input symbol.
	 * @param dst - A new destination state for {@code sym}.
	 * @return {@code true} if {@code dst} is a new destination state for
	 *         {@code sym}. {@code false} if {@code dst} is already registered as a
	 *         destination state for {@code sym}.
	 */
	public boolean addNext(Symbol sym, State dst) {
		Set<State> val = next.getOrDefault(sym, new HashSet<>());
		if (val.add(dst)) {
			next.put(sym, val);
			return true;
		}
		return false;
	}

	/**
	 * Returns all the next states for input symbol {@code sym}.
	 * 
	 * @param sym - An input symbol.
	 * @return A set of next states for {@code sym}.
	 */
	public Set<State> getNext(Symbol sym) {
		return next.getOrDefault(sym, new HashSet<>());
	}

	/**
	 * Returns name of this state.
	 * 
	 * @return A name of state.
	 */
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other.getClass().equals(this.getClass()))
			return name.equals(((State) other).name);
		return false;
	}

}
