package regularLang;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A class to control transition table.
 * 
 * @author Orb_H
 * @since 2019-10-17
 */
public class TransitionTableUtil {

	public static void printTT(RegularLanguage rl) {
		List<State> states = new ArrayList<>(rl.getStates());
		List<Symbol> symbols = new ArrayList<>(rl.getSymbols());
		String[][] next = new String[states.size()][symbols.size()];

		for (int i = 0; i < next.length; i++) {
			for (int j = 0; j < next[0].length; j++) {
				next[i][j] = "{" + states.get(i).getNext(symbols.get(j)).stream().reduce("",
						(s, t) -> (s.length() == 0 ? s : s + ", ") + t.getName(),
						(s, t) -> (s.length() == 0 ? s : s + ", ") + t) + "}";
			}
		}

		int[] max = new int[next[0].length + 1];
		max[0] = states.stream().reduce(0, (s, t) -> Integer.max(s, t.getName().length()), Integer::max);

		for (int i = 1; i <= next[0].length; i++) {
			final int k = i - 1;
			max[i] = IntStream.range(0, next.length).reduce(0, (j, l) -> Integer.max(j, next[l][k].length()));
		}

		System.out.println("|" + center("", max[0]) + IntStream.range(0, symbols.size())
				.mapToObj((i) -> "|" + center(symbols.get(i).getSymbol(), max[i + 1])).reduce("", (s, t) -> s + t)
				+ "|");

		for (int i = 0; i < next[0].length; i++) {
			final int k = i;
			System.out
					.println("|"
							+ center(states.get(i).getName(), max[0]) + IntStream.range(0, symbols.size())
									.mapToObj((j) -> "|" + center(next[k][j], max[j + 1])).reduce("", (s, t) -> s + t)
							+ "|");
		}
	}

	private static String center(String s, int w) {
		int l = s.length();
		int k = (w + l) / 2;
		s = String.format("%" + k + "s", s);
		s = String.format("%-" + w + "s", s);
		return s;
	}

}
