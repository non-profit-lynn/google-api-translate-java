import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.google.api.GoogleAPI;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Main {
	private static Properties properties = new Properties();

	public static void main(String[] args) throws Exception {

		InputStream input = null;
		String translatedText = null;
		
		try {

			input = new FileInputStream("project.properties");
			properties.load(input);
			
			// Set the Google Translate API key
			GoogleAPI.setKey(properties.getProperty("apiKey"));

			
			String text = "Bonjour le monde";
			Language from = Language.FRENCH;
			Language to = Language.ENGLISH;

			System.out.printf("Translating: %s \n\nFrom %s to %s\n\n", text, from, to);
		    System.out.println(Translate.DEFAULT.execute(text, from, to)+"\n");
			System.out.println("Completed Translate API call successfully");
			

		} catch (Exception ex) {
			ex.printStackTrace();	
		}
	}
}