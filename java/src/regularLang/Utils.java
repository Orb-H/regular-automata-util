package regularLang;

import java.util.function.BinaryOperator;

public class Utils {

	public static BinaryOperator<String> mergeByComma = (s, t) -> (s.length() == 0 ? s : (s + ", ")) + t;

}
