package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
  
/**
* Java Program to parse/read HTML documents from File using Jsoup library.
* Jsoup is an open source library which allows Java developer to parse HTML
* files and extract elements, manipulate data, change style using DOM, CSS and
* JQuery like method.
*
* @author Javin Paul
*/
public class GetElementsParametres{
  
    public static void main(String args[]) {
  
    	Scanner fileScanner;
    	try {
			fileScanner = new Scanner(new FileInputStream("elementscas.html"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
        // Parse HTML String using JSoup library
        StringBuilder htmlPage = new StringBuilder();
        while(fileScanner.hasNextLine()){
        	htmlPage.append(fileScanner.nextLine());
        	htmlPage.append('\n');
        }
        Document html = Jsoup.parse(htmlPage.toString());
        List<String> parameters = new LinkedList<>();
        for(Element element: html.getElementsByTag("dd")){
        	parameters.add(element.attr("itemprop"));
        	System.out.println(element.attr("itemprop"));
        }
        File newFile = new File(".");
        FileWriter writer = null;
        try {
			 writer = new FileWriter("txt.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(String string: parameters){
        	try {
				writer.write(string);
				writer.write('\n');
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        try {
			writer.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
}