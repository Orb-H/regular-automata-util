package regularLang;

import java.util.ArrayList;
import java.util.Arrays;
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

		for (int i = 0; i < next.length; i++) {
			for (int j = 0; j < next[0].length; j++) {
				next[i][j] = "{" + states.get(i).getNext(symbols.get(j)).stream().reduce("",
						(s, t) -> (s.length() == 0 ? s : s + ", ") + t.getName(),
						(s, t) -> (s.length() == 0 ? s : s + ", ") + t) + "}";
			}
		}

		List<String> stateNames = Arrays.asList(IntStream.range(0, states.size()).mapToObj((i) -> {
			State s = states.get(i);
			if (rl.getStartState().equals(s)) {
				return "->" + s.getName();
			} else if (s.isFinal()) {
				return "*" + s.getName();
			}
			return s.getName();
		}).toArray(String[]::new));
		System.out.println(stateNames.size());

		int[] max = new int[next[0].length + 1];
		max[0] = stateNames.stream().reduce(0, (s, t) -> Integer.max(s, t.length()), Integer::max);

		for (int i = 1; i <= next[0].length; i++) {
			final int k = i - 1;
			max[i] = IntStream.range(0, next.length).reduce(0, (j, l) -> Integer.max(j, next[l][k].length()));
		}

		System.out.println("|" + center("", max[0]) + "|" + center("(ех)", max[0]) + IntStream.range(1, next[0].length)
				.mapToObj((i) -> "|" + center(symbols.get(i).getSymbol(), max[i + 1])).reduce("", (s, t) -> s + t)
				+ "|");
		System.out.println("|" + IntStream.range(0, next[0].length + 1).mapToObj((i) -> line(max[i]) + "|").reduce("",
				(s, t) -> s + t));

		for (int i = 0; i < next.length; i++) {
			final int k = i;
			System.out
					.println("|"
							+ center(stateNames.get(i), max[0]) + IntStream.range(0, next[0].length)
									.mapToObj((j) -> "|" + center(next[k][j], max[j + 1])).reduce("", (s, t) -> s + t)
							+ "|");
		}
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

}
