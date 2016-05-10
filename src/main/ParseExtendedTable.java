package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import model.ChemicalElement;

public class ParseExtendedTable {

	private static Set<String> nonmetal = new HashSet();
	static {
		nonmetal.addAll(Arrays.asList("H", "He", "B", "C", "N", "O", "F", "Ne", "Si", "P", "S", "Cl", "Ar", "As", "Se",
				"Br", "Kr", "Te", "I", "Xe", "At", "Rn"));
	};

	public static void main(String[] args) throws IOException {
		StringBuilder htmlText = new StringBuilder();
		Scanner scanner = new Scanner(new FileInputStream("extendedPeriodicTable"));
		while (scanner.hasNextLine()) {
			htmlText.append(scanner.nextLine());
		}
		Document doc = Jsoup.parse(htmlText.toString());

		Node table = doc.getElementById("Main").childNode(3);

		List<Node> rows = table.childNodes();

		List<Node> elements = new ArrayList<>(120);
		for (Node row : rows) {
			elements.addAll(row.childNodes().stream().filter(t -> t.attr("class").indexOf("Element") != -1)
					.collect(Collectors.toList()));
		}
		for (Node elementNode : elements) {
			List<Node> parameters = elementNode.childNode(0).childNodes();
			Node names = parameters.get(0);
			Node group = parameters.get(1);
			Integer number = Integer.parseInt(names.childNodes().get(0).attr("an"));
			String rusName = ((TextNode) names.childNodes().get(4).childNode(0)).text();
			String groupNumber = ((TextNode) new LinkedList<>(group.childNodes()).getLast()).text();
			ChemicalElement chem = ParseHtml.elementsNumber.get(number);
			chem.setValue("metal_nonmetal", nonmetal.contains(chem.getFieldValue("symbol")) ? "nonmetal" : "metal");
			chem.setValue("rus_name", rusName);
			chem.setValue("group", groupNumber);
		}

		System.out.println(elements.size());

	}
}