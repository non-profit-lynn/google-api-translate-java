/**
 * Translate.java
 *
 * Copyright (C) 2007,  Richard Midwinter
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
package com.google.api.translate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.GoogleAPIException;
import com.google.api.GoogleCloudAPI;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;

/**
 * Makes version 2 of the Google Translate API available to Java applications.
 * 
 * @author William Ferguson
 * @author Richard Midwinter
 */
public final class TranslateV2 extends GoogleCloudAPI implements Translate {

	/**
	 * Constants.
	 */
	private static final String URL = "https://translation.googleapis.com/language/translate/v2";

	private String getLocalAccessToken() {
		try { return new Scanner(new File("localAccessToken")).useDelimiter("\\A").next().trim(); }
		catch (FileNotFoundException e) { e.printStackTrace(); return null; }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String execute(final String text, final Language from, final Language to) throws GoogleAPIException {
		try {

			// SET UP TOKEN
			validateToken(getLocalAccessToken());

			
			// SET UP REQUEST
			JSONObject request = new JSONObject();
			request.put("q", text);
			request.put("source", from.toString());
			request.put("target", to.toString());
			request.put("format", "text");
			validateRequest(request.toString());
			

			// MAKE REQUEST
			final URL url = new URL(URL);
			final JSONObject json = postJSON(url);

			
			// GET RESPONSE
			return getJSONResponse(json);
			
		} catch (final Exception e) {
			System.out.println("Error: " +e.getMessage());

			throw new GoogleAPIException(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] execute(final String[] text, final Language from, final Language to) throws GoogleAPIException {
		try {
//			validateToken();
//			validateRequest();
			
			final Language[] fromArgs = new Language[text.length];
			final Language[] toArgs = new Language[text.length];
			
			for (int i = 0; i<text.length; i++) {
				fromArgs[i] = from;
				toArgs[i] = to;
			}
			
			return execute(text, fromArgs, toArgs);
		} catch (final Exception e) {
			throw new GoogleAPIException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] execute(final String text, final Language from, final Language[] to) throws GoogleAPIException {
		try {
//			validateToken();
//			validateRequest();
			
			final String[] textArgs = new String[to.length];
			final Language[] fromArgs = new Language[to.length];
			
			for (int i = 0; i<to.length; i++) {
				textArgs[i] = text;
				fromArgs[i] = from;
			}
			
			return execute(textArgs, fromArgs, to);
		} catch (final Exception e) {
			throw new GoogleAPIException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] execute(final String[] text, final Language from[], final Language[] to) throws GoogleAPIException {
		try {
//			validateToken();
//			validateRequest();
			
			if (text.length != from.length || from.length != to.length) {
				throw new Exception(
						"[google-api-translate-java] The same number of texts, from and to languages must be supplied.");
			}
			
			if (text.length == 1) {
				return new String[] { execute(text[0], from[0], to[0]) };
			}
			
			final String[] responses = new String[text.length];
			for (int i = 0; i < responses.length; i++) {
				responses[i] = execute(text[i], from[i], to[i]);
			}
			
			return responses;
		} catch (final Exception e) {
			throw new GoogleAPIException(e);
		}
	}
	
	/**
	 * Returns the JSON response data as a String. Throws an exception if the status is not a 200 OK.
	 * 
	 * @param json The JSON object to retrieve the response data from.
	 * @return The responseData from the JSONObject.
	 * @throws Exception If the responseStatus is not 200 OK.
	 */
	private static String getJSONResponse(final JSONObject json) throws Exception {
		final JSONObject data = json.getJSONObject("data");
		final JSONArray translations = data.getJSONArray("translations");
		final JSONObject translation = translations.getJSONObject(0);
		final String translatedText = translation.getString("translatedText");
		
		return HTMLEntities.unhtmlentities(translatedText);
	}
}