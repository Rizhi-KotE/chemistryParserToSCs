package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.jsoup.nodes.Element;

import utils.Util;
import utils.UtilsToSCs;
import utils.UtilsToSCs.enumConnectors;
import utils.UtilsToSCs.enumLineEnd;

public class ChemicalElement {
	private static List<String> fieldsNames = new ArrayList<>();
	private static Properties fieldsType = new Properties();
	private static Properties fieldsSCsType = new Properties();
	static {
		try {
			Scanner scanner = new Scanner(new FileInputStream("fieldsName.properties"));
			while (scanner.hasNextLine()) {
				fieldsNames.add(scanner.nextLine());
			}
			fieldsType.load(new FileInputStream("fieldsType.properties"));
			fieldsSCsType.load(new FileInputStream("fieldsSCsType.properties"));
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
		out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, "nrel_main_idtf", "["+fields.get("name")+"]", enumLineEnd.to_be_continued));
		out.append("\n	");
		out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, "nrel_idtf", "["+fields.get("symbol")+"]", enumLineEnd.end));
		return out.toString();
	}

	public String chemToSCsMain() {
		StringBuilder out = new StringBuilder();
		out.append(sys_idtf);
		out.append("\n	");
		for (String field : fieldsNames) {
			String SCsType = fieldsSCsType.getProperty(field);
			switch (SCsType) {
			case "nrel":
				out.append(nrelToSCs(field));
				break;
			case "rrel":
				out.append(rrelToSCs(field));
				break;
			}
		}
		out.append(";");
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

	private String nrelToSCs(String field) {
		StringBuilder out = new StringBuilder();
		String fieldType = fieldsType.getProperty(field);
		switch (fieldType) {
		case "Double":
			out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, fieldType + "_" + field, "...",
					enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			break;
		case "Integer":
			out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, fieldType + "_" + field, "...",
					enumLineEnd.open));
			out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			break;
		case "ArrayOfInteger":
			List<Integer> values = (List<Integer>) fields.get(field);
			for (Integer value : values) {
				out.append(UtilsToSCs.SCsFive("", enumConnectors.binarRight, fieldType + "_" + field, "...",
						enumLineEnd.open));
				out.append(UtilsToSCs.value(fields.get(field).toString(), enumLineEnd.to_be_continued));
			}
			break;
		}
		return out.toString();
	}

	public String chemToSCsRu() {
		StringBuilder builder = new StringBuilder();
		return builder.toString();
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