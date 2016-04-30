package utils;

import java.util.EnumMap;

public class UtilsToSCs {
	public static enum enumConnectors {
		binarLeft, binarRight, unarLeft, unarRight, none
	}

	public static enum enumLineEnd {
		end, open, to_be_continued
	}
	private static EnumMap<enumConnectors, String> mapConnectors = new EnumMap<>(enumConnectors.class);
	private static EnumMap<enumLineEnd, String> mapLineEnd =  new EnumMap<>(enumLineEnd.class);;
	static {
		mapConnectors.put(enumConnectors.binarLeft, "=>");
		mapConnectors.put(enumConnectors.binarRight, "<=");
		mapConnectors.put(enumConnectors.unarRight, "->");
		mapConnectors.put(enumConnectors.unarLeft, "<-");
		mapConnectors.put(enumConnectors.none, "");
	}
	static {
		mapLineEnd.put(enumLineEnd.end, ";;");
		mapLineEnd.put(enumLineEnd.to_be_continued, ";");
		mapLineEnd.put(enumLineEnd.open, "");
	}

	public static String SCsFive(String first, enumConnectors type, String second, String third, enumLineEnd end) {
		StringBuilder out = new StringBuilder();
		out.append(first);
		out.append(mapConnectors.get(type));
		out.append(" ");
		out.append(second);
		out.append(": ");
		out.append(third);
		out.append(mapLineEnd.get(end));
		return out.toString();
	}

	public static String SCsTriple(String first, enumConnectors type, String third, enumLineEnd end) {
		StringBuilder out = new StringBuilder();
		out.append(first);
		out.append(mapConnectors.get(type));
		out.append(" ");
		out.append(third);
		out.append(mapLineEnd.get(end));
		return out.toString();
	}

	public static String value(String value, enumLineEnd end) {
		StringBuilder out = new StringBuilder();
		out.append("\n	(*\n		");
		out.append(SCsTriple("", enumConnectors.unarLeft, "value", enumLineEnd.end));
		out.append("\n		");
		out.append(SCsFive("", enumConnectors.binarLeft, "nrel_maybe_value", "..." ,
				enumLineEnd.open));
		out.append("\n		");
		out.append("(*\n			");
		out.append(SCsTriple("", enumConnectors.unarRight,value, enumLineEnd.open));
		out.append("(*");
		out.append(SCsTriple("", enumConnectors.unarLeft, "number" , enumLineEnd.end));
		out.append("*)");
		out.append(mapLineEnd.get(enumLineEnd.end));
		out.append("\n		*)");
		out.append(mapLineEnd.get(enumLineEnd.end));
		out.append("\n	*)");
		out.append(mapLineEnd.get(end));
		return out.toString();
	}

	public static String DoubleToSCs(Object in, enumLineEnd end) {
		return value(in.toString(), end);
	}

	public static String IntegerToSCs(Object in, enumLineEnd end) {
		return value(in.toString(), end);
	}

	public static String ArrayOfIntegerToSCs(Object in, enumConnectors type, String second, String third,
			enumLineEnd end) {
		StringBuilder out = new StringBuilder();
		out.append("(*/n	");
		for (Integer number : (int[]) in) {
			out.append(SCsFive("", type, second, third, end));
		}
		out.append("*)");
		out.append(mapLineEnd.get(end));
		return value(in.toString(), end);
	}
}
