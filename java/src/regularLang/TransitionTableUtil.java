package regularLang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A class to control transition table.
 * 
 * @author Orb_H
 * @since 2019-10-17
 */
public class TransitionTableUtil {

	/**
	 * Prints a regular language into a format of transition table.
	 * 
	 * @param rl A regular language to print.
	 */
	public static void printTT(RegularLanguage rl) {
		List<State> states = new ArrayList<>(rl.getStates());
		List<Symbol> symbols = new ArrayList<>(rl.getSymbols());
		symbols.add(0, Symbol.EPSILON);
		String[][] next = new String[states.size()][symbols.size()];

		IntStream.range(0, next.length)
				.forEach((i) -> IntStream.range(0, next[0].length)
						.forEach((j) -> next[i][j] = states.get(i).getNext(symbols.get(j)).stream().reduce("",
								(s, t) -> Utils.mergeByComma.apply(s, t.getName()),
								(s, t) -> Utils.mergeByComma.apply(s, t))));

		List<String> stateNames = Arrays.asList(IntStream.range(0, states.size()).mapToObj((i) -> {
			State s = states.get(i);
			if (rl.getStartState().equals(s))
				return "->" + s.getName() + "  ";
			else if (s.isFinal())
				return "*" + s.getName() + " ";
			return s.getName();
		}).toArray(String[]::new));

		int[] max = new int[next[0].length + 1];
		max[0] = stateNames.stream().reduce(Integer.MIN_VALUE, (s, t) -> Integer.max(s, t.length()), Integer::max);

		IntStream.range(0, next[0].length).forEach((i) -> max[i + 1] = Integer
				.max(IntStream.range(0, next.length).reduce(0, (j, l) -> Integer.max(j, next[l][i].length())), 3));

		System.out.println(line(IntStream.range(0, next[0].length + 1).map((i) -> max[i]).reduce(0, (s, t) -> s + t)
				+ next[0].length + 2));
		System.out.println("|" + center("", max[0]) + "|" + center("(ех)", max[1]) + IntStream.range(1, next[0].length)
				.mapToObj((i) -> "|" + center(symbols.get(i).getSymbol(), max[i + 1])).reduce("", (s, t) -> s + t)
				+ "|");
		System.out.println("|" + IntStream.range(0, next[0].length + 1).mapToObj((i) -> line(max[i]) + "|").reduce("",
				(s, t) -> s + t));

		IntStream.range(0, next.length)
				.forEach((i) -> System.out.println("|"
						+ center(stateNames.get(i), max[0]) + IntStream.range(0, next[0].length)
								.mapToObj((j) -> "|" + center(next[i][j], max[j + 1])).reduce("", (s, t) -> s + t)
						+ "|"));
		System.out.println(line(IntStream.range(0, next[0].length + 1).map((i) -> max[i]).reduce(0, (s, t) -> s + t)
				+ next[0].length + 2));
	}

	/**
	 * Aligns a string by given width to center.
	 * 
	 * @param s A string to be aligned.
	 * @param w Desired width to align.
	 * @return A center-aligned string.
	 */
	private static String center(String s, int w) {
		int l = s.length();
		int k = (w + l) / 2;
		s = String.format("%" + k + "s", s);
		s = String.format("%-" + w + "s", s);
		return s;
	}

	/**
	 * Returns dashes with given length.(e.g. a string of dashes with length 4 is
	 * {@code "----"}.)
	 * 
	 * @param w - Length of dashes.
	 * @return A string of dashes.
	 */
	private static String line(int w) {
		char dash[] = new char[w];
		Arrays.fill(dash, '-');
		return new String(dash);
	}

	/**
	 * Converts a transition table to corresponding regular language.
	 * 
	 * @param states  - A list of states.
	 * @param symbols - A list of symbols.
	 * @param nexts   - A list of next states. Each String should match
	 *                <code>{?((\w)(,\s*\w)*)?}?</code>
	 * @param start   - A start state.
	 * @param end     - A series of final states.
	 * @return A regular language constructed using given transition table.
	 */
	public static RegularLanguage convert(List<String> states, List<String> symbols, List<String> nexts, String start,
			String... end) {
		RegularLanguage rl = new RegularLanguage(symbols, states);
		Iterator<String> it = nexts.iterator();
		IntStream.range(0, states.size()).forEachOrdered((i) -> IntStream.range(0, symbols.size()).forEachOrdered(
				(j) -> Arrays.asList(it.next().split("[\\s{},]")).stream().filter((s) -> s.length() > 0).forEach(
						(s) -> rl.findState(states.get(i)).addNext(rl.findSymbol(symbols.get(j)), rl.findState(s)))));
		rl.setStartState(start);
		Arrays.asList(end).stream().forEach((e) -> rl.findState(e).setFinal());
		return rl;
	}

}
