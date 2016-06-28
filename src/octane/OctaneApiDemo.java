package octane;

import octane.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.*;

import com.google.gson.*;

import apppulse.api.OctaneApppulseApiImp;

// jar files used
//		httpcomponents-client-4.5.2
//		json-20160212.jar
//		gson-2.2.2.jar

// Read https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientFormLogin.java

// This can be used as a way to unit test Json response never used but looks interesting
// https://github.com/rest-assured/rest-assured
public class OctaneApiDemo {
	
	public static void main(String[] args) throws ParseException,IOException, URISyntaxException {
        // Input validation
		// for later implementation
        /*
        if (args.length == 1) {
            String command = args[0];
            if (!command.equalsIgnoreCase("help")) {
                System.err.println("Unknown command: " + command);
            }
            System.out.println(USAGE);
            return;
        } else if (args.length == 2) {
            client_id = args[0];
            client_secret = args[1];
        } else {
            System.err.println(USAGE);
            return;
        }*/
		//OctaneApiImp.test();
        // 1. Authorize step
		OctaneApppulseApiImp.authenticate("672149734#C1", "99696968-001a-4144-bff9-cc34698e8265");
		OctaneApppulseApiImp.getApplications("99696968-001a-4144-bff9-cc34698e8265");
		
		List<org.apache.http.cookie.Cookie> octaneCookies = null;
        try {
            octaneCookies = OctaneApiImp.authenticate(constants.CLIENT_ID, constants.CLIENT_SECRET);
        } catch (IOException e) {
            System.out.println("failed to authorise (get cookies)" + e.getMessage());;
            return;
        }
        //printLine();
        
        // 2. get list of Defects
        //OctaneApiImp.getDefects(octaneCookies);
        
        // 3. put Defects
        //OctaneApiImp.putDefects(octaneCookies);
        
        // Misc
        //OctaneApiImp.getWorkspaceApiEntities();
        //OctaneApiImp.getSharedspaceApiEntities();
        //OctaneApiImp.getGherkinTests();

	}
	public static void printElementNames (JSONObject jo){
		// print out the element names that occur in the JSONObject
		String[] elementNames = JSONObject.getNames(jo);
		for (String elementName:elementNames){
			System.out.print("Element Name: "+ elementName);
			//JSONObject jObj = new JSONObject(jString);
			Object aObj = jo.get(elementName);
			if(aObj instanceof Integer){
			    System.out.println(" "+jo.getInt(elementName));
			} else if (aObj instanceof String){
				System.out.println(" "+jo.getString(elementName));
			} else if (aObj instanceof JSONArray){
				System.out.println(" JSON Array");
			} else {
				System.out.println(" "+jo.getBoolean(elementName));
			}
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
		//	placeInObject (ja.getJSONObject(x).toString());
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
	
	public static HttpGet setRequestHeaders(String hpsso_cookie_key, String lwsso_cookie_key){
		HttpGet getRequest = new HttpGet();

		getRequest.addHeader("Cookie", hpsso_cookie_key);
		getRequest.addHeader("Cookie", lwsso_cookie_key);
		// getRequest.addHeader("OCTANE_USER", octane_cookie_key);
		getRequest.addHeader("Accept", "application/json");
		getRequest.addHeader("Content-Type", "application/json");
		//HPECLIENTTPE: HPE_REST_API_BETA needs to be added to use the beta rest api
		getRequest.addHeader("HPECLIENTTYPE", "HPE_REST_API_BETA");
		
		return getRequest;
	}
	
	public static String getMetaData (HttpClient httpClient, HttpHost target, HttpGet getRequest, String hpsso_cookie_key, String lwsso_cookie_key) throws ParseException, IOException, URISyntaxException{
		System.out.println("---------------- Get Request ----------------");

		//String q = "\"name EQ ^defect^\"";
		//getRequest.setURI(URI.create(BASE_URI+"/metadata/entities?query="+URLEncoder.encode(q, "UTF-8")));
		//getRequest.setURI(URI.create(BASE_URI+"/metadata/entities"));
		//getRequest.setURI(URI.create());
		System.out.println("executing request to "
				+ getRequest.getURI());

		HttpResponse httpResponse = httpClient.execute(target, getRequest);

		// System.out.println("RESPONSE:" + httpResponse.toString());
		HttpEntity entity = httpResponse.getEntity();
		String s = EntityUtils.toString(entity);
		if (entity != null) {
			System.out.println("Json Response: " + s);// EntityUtils.toString(entity));
		}
		System.out.println("-------------- End Get Request --------------");
		
		return s;
	}
}
