package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class TicketMasterAPI {
	private static final String API_HOST = "app.ticketmaster.com";
	private static final String SEARCH_PATH = "/discovery/v2/events.json";
	private static final String DEFAULT_TERM = "";  // no restriction
	private static final String API_KEY = "zqQaEm1B4HJcNX7hUf6CqzoFPcTXMREp";

	/**
	 * Creates and sends a request to the TicketMaster API by term and location.
	 */
	public JSONArray search(double lat, double lon, String term) {
		String url = "http://" + API_HOST + SEARCH_PATH;
		String latlong = lat + "," + lon;
		if (term == null) {
			term = DEFAULT_TERM;
		}
		term = urlEncodeHelper(term);
		String query = String.format("apikey=%s&latlong=%s&keyword=%s", API_KEY, latlong, term);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
			connection.setRequestMethod("GET");
 
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url + "?" + query);
			System.out.println("Response Code : " + responseCode);
 
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Extract events array only.
			JSONObject responseJson = new JSONObject(response.toString());
			JSONObject embedded = (JSONObject) responseJson.get("_embedded");
			JSONArray events = (JSONArray) embedded.get("events");
			return events;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
	private String urlEncodeHelper(String term) {
		try {
			term = java.net.URLEncoder.encode(term, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return term;
	}
 
	private void queryAPI(double lat, double lon) {
		JSONArray events = search(lat, lon, null);
		try {
			for (int i = 0; i < events.length(); i++) {
			    JSONObject event = events.getJSONObject(i);
				System.out.println(event);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * Main entry for sample TicketMaster API requests.
	 */
	public static void main(String[] args) {
		TicketMasterAPI tmApi = new TicketMasterAPI();
		tmApi.queryAPI(37.38, -122.08);
	}
}

