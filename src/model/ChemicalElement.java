package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Consumer;

import org.jsoup.nodes.Element;

import utils.Util;
import utils.UtilsToSCs;
import utils.UtilsToSCs.enumConnectors;
import utils.UtilsToSCs.enumLineEnd;

public class ChemicalElement {
	private static List<String> fieldsNames = new ArrayList<>();
	private static Properties fieldsType = new Properties();
	private static Properties fieldsSCsType = new Properties();
	private static Properties fieldsLanguage = new Properties();
	static {
		try {
			Scanner scanner = new Scanner(new FileInputStream("fieldsName.properties"));
			while (scanner.hasNextLine()) {
				fieldsNames.add(scanner.nextLine());
			}
			fieldsType.load(new FileInputStream("fieldsType.properties"));
			fieldsSCsType.load(new FileInputStream("fieldsSCsType.properties"));
			fieldsLanguage.load(new FileInputStream("fieldsLanguage.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ChemicalElement parseFromHtmlElement(Element element) {
		ChemicalElement chemicalElement = new ChemicalElement();
		for (String name : fieldsNames) {
			Element value = element.getElementsByAttributeValue("itemprop", name).get(0);
			chemicalElement.setValue(name, value.text());
		}
		chemicalElement.sys_idtf = "elem_" + chemicalElement.fields.get("symbol");
		return chemicalElement;

	}

	Map<String, Object> fields;
	private String sys_idtf;

	/**
	 * @return the sys_idtf
	 */
	public String getSys_idtf() {
		return sys_idtf;
	}

	public ChemicalElement() {
		fields = new HashMap<>();
	}

	public String chemToSCsEng() {
		StringBuilder out = new StringBuilder();
		out.append(sys_idtf);
		out.append("\n	");
		out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, "nrel_main_idtf", "[" + fields.get("name") + "]",
				enumLineEnd.open));
		out.append("(*<- lang_en;;*);");
		out.append("\n	");
		out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, "nrel_idtf", "[" + fields.get("symbol") + "]",
				enumLineEnd.open));
		out.append("(*<- lang_en;;*)");
		out.append(";;");
		return out.toString();
	}

	public String chemToSCsMain() {
		StringBuilder out = new StringBuilder();
		out.append(sys_idtf);
		out.append("\n	");
		List<String> nrelFields = new LinkedList<>();
		List<String> rrelFields = new LinkedList<>();

		fields.keySet().stream().filter(field -> fieldsLanguage.getProperty(field).equals("main")).forEach(field -> {
			switch (fieldsSCsType.getProperty(field)) {
			case "nrel":
				nrelFields.add(field);
				break;
			case "rrel":
				rrelFields.add(field);
				break;
			}
		});
		for (String nrel : nrelFields) {
			out.append(nrelToSCs(nrel));
		}
		Iterator<String> rrelIt = rrelFields.iterator();
		if (rrelIt.hasNext()) {
			out.append("\n");
			String field = rrelIt.next();
			out.append(UtilsToSCs.SCsFive("", enumConnectors.unarLeft, "rrel_periodic_" + field +"_" + fields.get(field), "",
					enumLineEnd.open));
			while (rrelIt.hasNext()) {
				field = rrelIt.next();
				out.append(UtilsToSCs.SCsFive("", enumConnectors.none, "rrel_periodic_" + field +"_" + fields.get(field), "",
						enumLineEnd.open));
			}
			out.append("periodic_table;");
			out.append(";");
		}
		return out.toString();
	}

	public Object getFieldValue(String s) {
		return fields.get(s);
	}

	private String periodicTable() {
		StringBuilder out = new StringBuilder();
		out.append("\n	");
		String row = "rrel_periodic_row" + fields.get("row").toString();
		String column = "rrel_periodic_column" + fields.get("column").toString();
		out.append(UtilsToSCs.SCsFive("", enumConnectors.unarLeft, row, "", enumLineEnd.open));
		out.append(UtilsToSCs.SCsFive("", enumConnectors.none, column, "periodic_table", enumLineEnd.to_be_continued));
		return out.toString();
	}

	private String rrelToSCs(String field) {
		StringBuilder out = new StringBuilder();
		String fieldType = fieldsType.getProperty(field);
		switch (fieldType) {
		case "Double":
			out.append(
					UtilsToSCs.SCsFive("", enumConnectors.unarRight, fieldType + "_" + field, "...", enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			break;
		case "Integer":
			out.append(
					UtilsToSCs.SCsFive("", enumConnectors.unarRight, fieldType + "_" + field, "...", enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			break;
		case "ArrayOfInteger":
			int[] values = (int[]) fields.get(field);
			for (int value : values) {
				out.append(UtilsToSCs.SCsFive("", enumConnectors.unarRight, fieldType + "_" + field, "...",
						enumLineEnd.open));
				out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			}
			break;
		}
		return out.toString();
	}

	private String parseFieldValue(String field) {
		StringBuilder out = new StringBuilder();
		String fieldType = fieldsType.getProperty(field);
		switch (fieldType) {
		case "Double":
			out.append(
					UtilsToSCs.SCsFive("", enumConnectors.unarRight, fieldType + "_" + field, "...", enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			break;
		case "Integer":
			out.append(
					UtilsToSCs.SCsFive("", enumConnectors.unarRight, fieldType + "_" + field, "...", enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			break;
		case "ArrayOfInteger":
			int[] values = (int[]) fields.get(field);
			for (int value : values) {
				out.append(UtilsToSCs.SCsFive("", enumConnectors.unarRight, fieldType + "_" + field, "...",
						enumLineEnd.open));
				out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			}
			break;
		}
		return out.toString();
	}
	
	private String nrelToSCs(String field) {
		StringBuilder out = new StringBuilder();
		String fieldType = fieldsType.getProperty(field);
		switch (fieldType) {
		case "Double":
			out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, fieldsSCsType.getProperty(field) + "_" + field, "...",
					enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			out.append("\n");
			break;
		case "Integer":
			out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, fieldsSCsType.getProperty(field) + "_" + field, "...",
					enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			out.append("\n");
			break;
		case "ArrayOfInteger":
			out.append(UtilsToSCs.ArrayOfIntegerToSCs(fields.get(field), enumConnectors.binarRight,
					fieldsSCsType.getProperty(field) + "_" + field, "...", enumLineEnd.to_be_continued));
			break;
		}
		return out.toString();
	}

	public String chemToSCsRu() {
		StringBuilder out = new StringBuilder();
		out.append(sys_idtf);
		out.append("\n	");
		out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, "nrel_main_idtf", "[элемент " + fields.get("rus_name") + "]",
				enumLineEnd.open));
		out.append("(*<- lang_ru;;*)");
		out.append(";;");
		return out.toString();
	}

	public void setValue(String name, String value) {
		try {
			Method parse = Util.class.getMethod("parse" + fieldsType.getProperty(name),
					new Class<?>[] { String.class });
			Object valueObject = parse.invoke(null, value);
			fields.put(name, valueObject);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}