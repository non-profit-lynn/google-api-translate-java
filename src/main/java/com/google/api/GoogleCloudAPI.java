/**
 * GoogleAPI.java
 *
 * Copyright (C) 2009,  Richard Midwinter
 *
 * This file is part of google-api-translate-java.
 *
 * google-api-translate-java is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * google-api-translate-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with google-api-translate-java. If not, see <http://www.gnu.org/licenses/>.
 */
package com.google.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

/**
 * Makes generic Google API functionality available to specific API classes.
 * 
 * @author Richard Midwinter
 * @author Kramar Tomas
 */
public abstract class GoogleCloudAPI {
	
	/**
	 * Default encoding to use.
	 */
	protected static final String ENCODING = "UTF-8";
	
    /**
     * The HTTP token attribute.
     * 
     * This allows Google to distinguish between programs.
     */
    protected static String token;//, translate_request;
   
    
    /**
     * Sets the API token.
     * @param pToken The API token.
     */
    public static void setToken(final String pToken) {
    	token = pToken;
    }
    public static void validateToken(String t) throws Exception {
		setToken(t);

    	if (token == null || token.length() == 0) {
    		throw new Exception("[google-api-translate-java] Token is not set. Call setToken(...).");
    	}
    }
    
    /**
     * Sets the API translate request.
     * @param pToken The API token.
     */
    public static void setTranslateRequest(final String pRequest) {
    	try{    
    		FileWriter fw=new FileWriter("translate-request.json");
    		fw.write(pRequest);    
    		fw.close();    
    	} catch(Exception e) { System.out.println(e); }  
    }
    public static void validateRequest(String request) throws Exception {
    	setTranslateRequest(request);
    	
    	File requestFile = new File("translate-request.json");
    	if (!requestFile.exists()) {
    		throw new Exception("[google-api-translate-java] Request is not set. Call setTranslateRequest(...).");
    	}
    }

    /**
     * Forms an HTTP request, sends it using POST method and returns the result of the request as a JSONObject.
     * 
     * @param url The URL to query for a JSONObject.
     * @param parameters Additional POST parameters
     * @return The translated String.
     * @throws Exception on error.
     */
    protected static JSONObject postJSON(final URL url) throws Exception {
    	Runtime rt = Runtime.getRuntime();
    	String request = 
    	String.format(
//			"pwd");
//			"cat translate-request.json");
//			"curl -s -X POST -H 'Content-Type: application/json' -d '{\"message\":\"Hello fellow\"}' http://the-mac.us/json.php");
//			"curl -s -X GET http://the-mac.us/json.php?message=Hello%%20fellow");
//			"curl -s -X POST -H 'Content-Type: application/json'     -H 'Authorization: Bearer %s'     'https://translation.googleapis.com/language/translate/v2'    -d @translate-request.json", token);
			"curl -s -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Authorization: Bearer %s' %s -d @translate-request.json", token, url);

    	System.out.println(request+ "\n");
		Process pr = rt.exec(request);
    	
    	String response = new Scanner(pr.getInputStream()).useDelimiter("\\A").next();
    	System.out.println(response+ "\n");
    	
    	return new JSONObject(response);
    }
    
    /**
     * Forms an HTTP request, sends it using POST method and returns the result of the request as a JSONObject.
     * 
     * @param url The URL to query for a JSONObject.
     * @param parameters Additional POST parameters
     * @return The translated String.
     * @throws Exception on error.
     */
    protected static JSONObject postJSON(final URL url, Object other) throws Exception {
    	try {
    		final HttpURLConnection uc = (HttpURLConnection) url.openConnection();
    		uc.setRequestProperty("Content-Type","application/json");
    		uc.setRequestProperty("Authorization: Bearer ", token);
    		uc.setRequestMethod("POST");
    		uc.setDoOutput(true);
            
			final PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.write("{}");
			pw.close();
			uc.getOutputStream().close();
    		
    		try {
    			final String result = inputStreamToString(uc.getInputStream());
    			
    			return new JSONObject(result);
    		} finally { // http://java.sun.com/j2se/1.5.0/docs/guide/net/http-keepalive.html
    			if (uc.getInputStream() != null) {
    				uc.getInputStream().close();
    			}
    			if (uc.getErrorStream() != null) {
    				uc.getErrorStream().close();
    			}
    			if (pw != null) {
    				pw.close();
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[google-api-translate-java] Error retrieving translation.", ex);
    	}
    }
    
    /**
     * Reads an InputStream and returns its contents as a String.
     * Also effects rate control.
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     * @throws Exception on error.
     */
    private static String inputStreamToString(final InputStream inputStream) throws Exception {
    	final StringBuilder outputBuilder = new StringBuilder();
    	
    	try {
    		String string;
    		if (inputStream != null) {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
    			while (null != (string = reader.readLine())) {
    				outputBuilder.append(string).append('\n');
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[google-api-translate-java] Error reading translation stream.", ex);
    	}
    	
    	return outputBuilder.toString();
    }
}