package utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.CrystalStructure;
import model.ElectronicConfiguration;

public class Util {
	public static Double parseDouble(String s) {
		if("-".equals(s))
			return 0d;
		return Double.parseDouble(s);
	}

	public static List<Integer> parseArrayOfInteger(String s) {
		List<Integer> value = new ArrayList<>(6);
		Pattern pattern = Pattern.compile("(,)");
		String[] numbers = pattern.split(s,10);
		for(String number: numbers){
			value.add(parseInteger(number));
		}
		return value;
	}
	
	public static Integer parseInteger(String s) {
		if("-".equals(s))
			return 0;
		return Integer.parseInt(s);
	}

	public static String parseString(String s) {
		return s;
	}

	public static ElectronicConfiguration parseElectronicConfiguration(String s) {
		return ElectronicConfiguration.parse(s);
	}

	public static CrystalStructure parseCrystalStructure(String s) {
		return CrystalStructure.parse(s);
	}

	public static void writeMapToPropertiesFile(Map<?, ?> map, File file) {
		try {
			StringBuilder out = new StringBuilder();
			for (Object i : map.keySet()) {
				out.append(i.toString());
				out.append(" = ");
				out.append(map.get(i).toString());
				out.append("\n");
			}
			Writer writer = new FileWriter(file);
			writer.write(out.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
