Provides a Simple Java API Client for using Google Translate.

# Features: #

Generates strings.xml file in correct folder (SPANISH => values-es)

Uses Google Cloud Translation API
- Access 2,000,000 free characters / day
![Translate API](screenshots/google-translate-api.png)

Small library size - less than 50Kb.


# Quickstart: #

## Clone the repo

```bash
git clone https://github.com/the-mac/google-api-translate-java.git
```

## Set up your API Key

### Add Translation Service
1. Visit Google [APIs & Services](https://console.developers.google.com/apis) to create key
1. Click on **Create** if there is no existing project
1. Complete project Creation
1. Click on **Dashboard**
1. Click on **ENABLE APIS AND SERVICES**
1. Type **translate**
1. Click on **Google Cloud Translation API**
1. Click on **Enable** (Note: You will need to set up Billing / For Free Trial)

### Create & Insert Credentials into Project 
1. Click on **Credentials**
1. Click on **Create Credentials**
1. Click on **API Key**
1. Copy/Paste **sample-project.properties** to a **project.properties** file
```bash
apiKey=API_KEY_HERE
fromLanguage=SPANISH
toLanguage=ENGLISH

```
1. Copy **Your API Key** value from Google Console
1. Paste your New API Key replacing **API_KEY_HERE**
```bash
apiKey=AIzaSyAkYbwMzqgUOo_nXHe7n_XIoLEZG8Su6N4
fromLanguage=SPANISH
toLanguage=ENGLISH

```
1. Click Close on Google Console **API Key** Prompt
1. Run Main java application

```java

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
```
The result should look like the following:
```bash
Translating: Bonjour le monde 

From fr to en

Hi world

Completed Translate API call successfully

```