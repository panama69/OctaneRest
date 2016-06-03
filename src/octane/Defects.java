package octane;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.*;

import com.google.gson.*;

// Read https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientFormLogin.java

// This can be used as a way to unit test Json response never used but looks interesting
// https://github.com/rest-assured/rest-assured
public class Defects {

	public static void main(String[] args) throws ParseException,IOException {
		String hpsso_cookie_key = null;
		String lwsso_cookie_key = null;
		String octane_cookie_key = null;

		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpHost target = new HttpHost("10.0.0.13", 8080, "http");

			// specify the get request
			/* These values are what Octane gave you when you created the API Access in Octane admin area
			 * Client ID: Pogo_5g21wdy5dqnnjuno339qnwv04 
			 * Client secret: %97db984e797540c2N
			 */
			HttpPost postRequest = new HttpPost("/authentication/sign_in");
			postRequest.addHeader("Content-Type", "application/json");
			JSONObject apiKey = new JSONObject();
			apiKey.put("client_id", "Pogo_5g21wdy5dqnnjuno339qnwv04");
			apiKey.put("client_secret", "%97db984e797540c2N");

			StringEntity param = new StringEntity(apiKey.toString());
			postRequest.setEntity(param);

			System.out.println("Executing post request to: " + target);

			HttpResponse httpResponse = httpClient.execute(target, postRequest);

			// System.out.println("RESPONSE:" + httpResponse.toString());
			HttpEntity entity = httpResponse.getEntity();

			System.out
					.println("---------------- Post Response ----------------");
			System.out.println(httpResponse.getStatusLine());
			Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
				if (headers[i].getName().equals("Set-Cookie")) {
					if (headers[i].getValue()
							.substring(0, headers[i].getValue().indexOf("="))
							.equals("LWSSO_COOKIE_KEY") == true) {
						lwsso_cookie_key = headers[i].getValue().substring(0,
								headers[i].getValue().indexOf(";"));
						System.out.println("my lwsso:" + lwsso_cookie_key);
					}
					if (headers[i].getValue()
							.substring(0, headers[i].getValue().indexOf("="))
							.equals("HPSSO_COOKIE_CSRF")) {
						// parse
						hpsso_cookie_key = headers[i].getValue().substring(0,
								headers[i].getValue().indexOf(";"));
						System.out.println("my hpsso:" + hpsso_cookie_key);
					}
					if (headers[i].getValue()
							.substring(0, headers[i].getValue().indexOf("="))
							.equals("OCTANE_USER")) {
						// parse
						octane_cookie_key = headers[i].getValue().substring(0,
								headers[i].getValue().indexOf(";"));
						System.out.println("my octane user:"
								+ octane_cookie_key);
					}
				}

			}
			System.out
					.println("---------------- End Post Response ----------------");

			if (entity != null) {
				System.out.println(EntityUtils.toString(entity));
			}

			System.out.println("---------------- Get Request ----------------");
			HttpGet getRequest = new HttpGet(
					"/api/shared_spaces/1001/workspaces/1002/defects?fields=logical_name,name");//&order_by=id&limit=2");

			getRequest.addHeader("Cookie", hpsso_cookie_key);
			getRequest.addHeader("Cookie", lwsso_cookie_key);
			// getRequest.addHeader("OCTANE_USER", octane_cookie_key);
			getRequest.addHeader("Accept", "application/json");
			getRequest.addHeader("Content-Type", "application/json");
			System.out.println("executing request to " + target
					+ getRequest.getURI());

			httpResponse = httpClient.execute(target, getRequest);

			// System.out.println("RESPONSE:" + httpResponse.toString());
			entity = httpResponse.getEntity();
			String s = EntityUtils.toString(entity);
			if (entity != null) {
				System.out.println("Json Response: " + s);// EntityUtils.toString(entity));
			}
			System.out
					.println("---------------- End Get Request ----------------");

			checkIfObjectOrArray (s);
			
			JSONObject jo = new JSONObject(s);
			printElementNames (jo);

			for (String elementName: JSONObject.getNames(jo)){
				if (isJSONArray(jo.get(elementName))){
					System.out.println("Processing array...");
					processJSONArray (jo.getJSONArray(elementName));
				} else
					System.out.println("Element: "+elementName+" not an array");
			}
		} finally {
		}
	}
	public static void printElementNames (JSONObject jo){
		// print out the element names that occur in the JSONObject
		String[] elementNames = JSONObject.getNames(jo);
		for (String elementName:elementNames){
			System.out.println("Element Name: "+ elementName);
		}
	}
	
	public static boolean isJSONArray (Object json){
		try{
			if (json instanceof JSONArray)
				return true;					
		} catch (JSONException e){
			return false;
		}
		return false;
	}
	
	public static void checkIfObjectOrArray (String jsonStrn){
		// Method to determine if you have a JSONObject or JSONArray
		//https://developer.android.com/reference/org/json/JSONTokener.html
		Object json = new JSONTokener(jsonStrn).nextValue();
		if (json instanceof JSONObject){
			//you have an object
			System.out.println("Its a object:"+json.getClass().getName().toString());
			System.out.println("\twith the following element names:"+((JSONObject) json).names());
			JSONArray xs = ((JSONObject) json).names();
			for (Object x:((JSONObject) json).names()){				
			}
		}else if (json instanceof JSONArray){
			//you have an array
			System.out.println("Its a array:"+json.getClass().getName().toString());
		}
		
		
		/* Other solution to test if it is a object or array
		 * http://stackoverflow.com/questions/16410421/how-to-tell-if-return-is-jsonobject-or-jsonarray-with-json-simple-java
			Object obj = new JSONParser().parse(result);
			if (obj instanceof JSONObject) {
				JSONObject jo = (JSONObject) obj;
			} else {
				JSONArray ja = (JSONArray) obj;
			}
		 */
	}
	
	public static void processJSONArray (JSONArray ja){
		System.out.println("Array Size: " + ja.length());
		System.out.println("Available fields: "+ ja.getJSONObject(0).names());
		System.out.println("\tOnly printing 'id', 'name'");
		System.out.println("Array Contents:");
		
		//BagOfPrimitives obj2 = gson.fromJson(json, BagOfPrimitives.class);
		for (int x = 0; x < ja.length(); x++) {
			placeInObject (ja.getJSONObject(x).toString());
			System.out.println("\tDefect Name: ("
					+ ja.getJSONObject(x).get("id") + ")"
					+ ja.getJSONObject(x).getString("name"));

		}
	}
	
	public static void placeInObject (String js){
		//https://github.com/google/gson/blob/master/UserGuide.md
		//https://sites.google.com/site/gson/gson-user-guide#TOC-Using-Gson
		Gson gson = new Gson();
		Defect myDefect = gson.fromJson(js, Defect.class);
		System.out.println("\t\tDefect ID from java class: "+myDefect.getId()+ "  -->  "+js);
	}
}
