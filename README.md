Provides a simple, unofficial, Java client API for using Google Translate.

Features:
=========

Small library size - less than 50Kb.

Uses English as an intermediate language to attempt translations between language pairings that are unsupported directly by Google Translate.

The there is a runnable jar and it has a very lightweight GUI to demonstrate translation functionality.

# Quickstart: #

## Clone the repo

```bash
git clone https://github.com/the-mac/google-api-translate-java.git
```

## Set up your Access Token

1. Visit [Google API Site](https://cloud.google.com/translate/docs/quickstart?csw=1#make_a_translation_api_request) to create key
1. Click on "Set Up A Project".
1. Enable the Cloud Translation API for that project.
1. Create a service account.
1. Download a private key as JSON.
1. Set Up [Google Cloud SDK](https://cloud.google.com/sdk/) Command Line Tool


## Run the java application
1. Obtain an authorizaton token using your service account:
```bash
gcloud auth application-default print-access-token | tr -d '\n' > localAccessToken
```
2. Execute Java application
```java

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Main {

	public static void main(String[] args) {
				
		try {
			
			String text = "Bonjour le monde";
			Language from = Language.FRENCH;
			Language to = Language.ENGLISH;

			System.out.printf("Translating %s \n\nFrom %s to %s\n\n", text, from, to);
		    System.out.println(Translate.DEFAULT.execute(text, from, to));
			
			System.out.println("Completed translation successfully");
		    
		} catch (Exception ex) { ex.printStackTrace(); }
		
	}
}

```