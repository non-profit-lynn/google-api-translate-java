import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.api.GoogleAPI;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class GenerateAndroidResources {
	private static Properties properties = new Properties();

	public static void main(String[] args) throws Exception {

		InputStream input = null;
		
		try {

			input = new FileInputStream("project.properties");
			properties.load(input);
			
			// Set the Google Translate API key
			GoogleAPI.setKey(properties.getProperty("apiKey"));
			
			Language from = Language.valueOf(properties.getProperty("fromLanguage"));//Language.ENGLISH;
			Language to = Language.valueOf(properties.getProperty("toLanguage"));//Language.FRENCH;
			
			ArrayList<String> xmlLines = new ArrayList<String>();
			ArrayList<String[]> xmlStringResources = new ArrayList<String[]>();
			
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("strings.xml"));

			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("string");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				String key = nNode.getAttributes().getNamedItem("name").getNodeValue();
				String value = nNode.getFirstChild().getNodeValue();
				xmlStringResources.add(new String[] { key, value });
			}
			
		    
		    xmlLines.add(String
					.format("<?xml version=%1$s1.0%1$s encoding=%1$sutf-8%1$s?>\n<resources>\n",
							'"'));

			for (String[] resource : xmlStringResources) {
				 
				String key = resource[0];
				String value = resource[1];
				String translation = Translate.DEFAULT.execute(value, from, to);

				System.out.println("\nKey: " + key);
				System.out.println("Value: " + value);
				System.out.println("Translation: " + translation);
				
				xmlLines.add(String.format("\t<string name=\"" + "%s\">%s</string>\n", key, translation));
				
			}

			File newXmlDir = new File(String.format("res/values-%s", to));
			newXmlDir.mkdir();
			
			
			xmlLines.add("</resources>\n");
			
			File newXml = new File(String.format("res/values-%s/strings.xml", to));
			newXml.createNewFile();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(newXml));

			for (String xmlLine : xmlLines) {
				bw.write(xmlLine);
			}

			bw.close();
			System.out.println("\nCompleted Translate API call successfully");
			

		} catch (Exception ex) {
			ex.printStackTrace();	
		}
	}
}