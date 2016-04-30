package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.ChemicalElement;
import utils.Util;

public class GetFieldsType {

	public static Map<String, String> typesName = new HashMap<>();
	static {
		typesName.put("java.lang.String", "String");
		typesName.put("int", "Integer");
		typesName.put("double", "Double");
		typesName.put("ElectronicConfiguration", "ElectronicConfiguration");
		typesName.put("CrystalStructure", "CrystalStructure");
	}

	public static void main(String[] args) {

			try {
				List<String> fieldNames = new LinkedList<>();
				Map<String, String> fieldsType = new HashMap<>();
				Scanner scanner = new Scanner(new FileInputStream(new File("fieldsName.prop")));

				while (scanner.hasNextLine()) {
					fieldNames.add(scanner.nextLine());
				}
				for (String name : fieldNames) {
					Class clazz = ChemicalElement.class;
					Field field = clazz.getDeclaredField(name);
					fieldsType.put(name, typesName.get(field.getType().getName()));
				}
				Util.writeMapToPropertiesFile(fieldsType, new File("fieldsType.properties"));
			} catch (FileNotFoundException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
