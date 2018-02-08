
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Main {

	public static void main(String[] args) {
				
		try {
			
			String text = "Bonjour le monde";
			Language from = Language.FRENCH;
			Language to = Language.ENGLISH;

			System.out.printf("Translating: %s \n\nFrom %s to %s\n\n", text, from, to);
		    System.out.println(Translate.DEFAULT.execute(text, from, to));
			
			System.out.println("Completed translation successfully");
		    
		} catch (Exception ex) { ex.printStackTrace(); }
		
	}
}
