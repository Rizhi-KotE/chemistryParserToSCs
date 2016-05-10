package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.ChemicalElement;

public class ParseHtml {
	public static Map<Integer, ChemicalElement> elementsNumber;
	
	public static void main(String[] args) throws IOException {
		StringBuilder htmlText = new StringBuilder();
		Scanner scanner = new Scanner(new FileInputStream("PeriodicTableHTML.txt"));
		while (scanner.hasNextLine()) {
			htmlText.append(scanner.nextLine());
		}
		Document doc = Jsoup.parse(htmlText.toString());
		Elements rows = doc.getElementById("periodic-table").getElementsByTag("tbody").get(0).getElementsByTag("tr");
		ArrayList<ChemicalElement> chems = new ArrayList<>(110);
		for (Element element : rows) {
			if (element.className().indexOf("row") == -1) {
				continue;
			}
			for (Element chem : element.getElementsByTag("td")) {
				if (chem.className().indexOf("cell element") == -1) {
					continue;
				}
				chems.add(ChemicalElement.parseFromHtmlElement(chem));
			}
			
			elementsNumber = chems.stream().collect(Collectors.toMap(t->(Integer)((ChemicalElement)t).getFieldValue("atomic_number"),t->t));
		}
		ParseExtendedTable.main(args);
		
		for(ChemicalElement chem: chems){
			String mainChem = chem.chemToSCsMain();
			String engChem = chem.chemToSCsEng();
			String ruChem = chem.chemToSCsRu();
			String sys_idtf = chem.getSys_idtf();
			new File("kb/kb_main/"+sys_idtf).mkdirs();
			new File("kb/kb_eng/"+sys_idtf).mkdirs();
			new File("kb/kb_ru/"+sys_idtf).mkdirs();
			File main = new File("kb/kb_main/"+sys_idtf+"/"+sys_idtf+".scs");
			File eng = new File("kb/kb_eng/"+sys_idtf+"/"+sys_idtf+".scs");
			File ru = new File("kb/kb_ru/"+sys_idtf+"/"+sys_idtf+".scs");
			Writer mainWriter = new FileWriter(main);
					mainWriter.write(mainChem);
			mainWriter.close();
			Writer engWriter = new FileWriter(eng);
					engWriter.write(engChem);
			engWriter.close();
			Writer ruWriter = new FileWriter(ru);
					ruWriter.write(ruChem);
			ruWriter.close();
		}
	}
}