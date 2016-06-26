package octane.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
//import org.apache.http.HttpEntity;
import org.apache.http.cookie.Cookie;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
//import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.apache.http.util.EntityUtils;
import org.json.*;

//import com.hpe.apppulse.openapi.apppulseopenapi.constants;

//import com.hpe.apppulse.openapi.Utils.HttpUtils;

public class OctaneApiImp {
    
	private static HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore()).build();;
	private static HttpHost target = new HttpHost(constants.OCTANE_URL_PREFIX, constants.PORT, "https");
	private static CookieStore cookieStore = new BasicCookieStore();
	private static HttpClientContext localContext = HttpClientContext.create();
	
	public static List<org.apache.http.cookie.Cookie> authenticate (String client_id, String client_secret) throws IOException {
		System.out.println("Starting authenticate ... ");	

		HttpResponse httpResponse = null;
		//List<HttpCookie> cookies = new ArrayList<HttpCookie>();
		List<org.apache.http.cookie.Cookie> cookies = new ArrayList<org.apache.http.cookie.Cookie>();

		// Bind custom cookie store to the local context
	    localContext.setCookieStore(cookieStore);

		try {
			// specify the get request
			HttpPost postRequest = new HttpPost(constants.SIGN_IN_URI);
			postRequest.addHeader("Content-Type", "application/json;charset=UTF-8");
			JSONObject apiKey = new JSONObject();
			apiKey.put("client_id", constants.CLIENT_ID);
			apiKey.put("client_secret", constants.CLIENT_SECRET);
			//HPECLIENTTPE: HPE_REST_API_BETA needs to be added to use the beta rest api
			postRequest.addHeader("HPECLIENTTYPE", "HPE_REST_API_BETA");

			try {
				StringEntity param = new StringEntity(apiKey.toString());
				postRequest.setEntity(param);
			} catch (UnsupportedEncodingException e){
				System.out.printf("Failed to set entity: %s%s%s\n", apiKey.toString(), System.lineSeparator(), e.getMessage());
			}

			System.out.println("---------------- Post Response ----------------");
			System.out.println("Executing post request to: " + target);

            try{
            	//httpResponse = httpClient.execute(target, postRequest, localContext);
            	httpResponse = httpClient.execute(target, postRequest, localContext);
            }
            catch(IOException e){
                System.out.printf(String.format("Failed to execute authenticate:%s%s", System.lineSeparator(), e.getMessage()));
                throw e;
            }

            // Analyze response
            if(httpResponse.getStatusLine().getStatusCode() != 200){
            	System.err.println("Failed to execute getHeaderCookie got status code: " + httpResponse.getStatusLine().getStatusCode() +
            			System.lineSeparator() + "Reason: " + httpResponse.getStatusLine().getReasonPhrase() +
            			System.lineSeparator());
            	return cookies;
            }

            cookies = cookieStore.getCookies();

			System.out.println(httpResponse.getStatusLine());

			System.out.println("---------------- End Post Response ----------------");
			return cookies;
        } finally {
            if (httpResponse != null) {
                httpResponse.getEntity().getContent().close();
            }
            System.out.println("... authentication - Completed");
        }
	}
	
	public static void getDefects (List<org.apache.http.cookie.Cookie> cookies) throws IOException{
		System.out.println("---------------- Get Request ----------------");
		HttpGet getRequest = new HttpGet();
		//localContext.setCookieStore(cookieStore);
		
		//HPECLIENTTPE: HPE_REST_API_BETA needs to be added to use the beta rest api
		//getRequest.addHeader("HPECLIENTTYPE", cookies.getHpClientType());
		//getRequest.addHeader("Accept", "application/json");
		//getRequest.addHeader("Content-Type", "application/json");
		//for (HttpCookie cookie:cookies){
		//	getRequest.addHeader(cookie.getName(), cookie.getValue());
		//}
		
		System.out.println("Executing get request to: " + target);

		getRequest.setURI(URI.create (constants.OCTANE_WORKSPACE_URI+ "/defects?fields=logical_name,name"));	//&order_by=id&limit=2"
		
		System.out.println("executing request to " + target
				+ getRequest.getURI());
		
		HttpResponse httpResponse = null;
        try{
        	httpResponse = httpClient.execute(target, getRequest, localContext);
        }
        catch(IOException e){
            System.out.printf(String.format("Failed to execute getDefects:%s%s", System.lineSeparator(), e.getMessage()));
            throw e;
        }

        // Analyze response
        if(httpResponse.getStatusLine().getStatusCode() != 200){
        	System.err.println("Failed to execute getHeaderCookie got status code: " + httpResponse.getStatusLine().getStatusCode() +
        			System.lineSeparator() + "Reason: " + httpResponse.getStatusLine().getReasonPhrase() +
        			System.lineSeparator());
        	//return ;
        }

		// System.out.println("RESPONSE:" + httpResponse.toString());

		String s = EntityUtils.toString(httpResponse.getEntity());
		if (s != null) {
			System.out.println("Json Response: " + s);// EntityUtils.toString(entity));
		}
		System.out.println("---------------- End Get Request ----------------");

//		checkIfObjectOrArray (s);
		
		JSONObject jo = new JSONObject(s);
//		printElementNames (jo);

//		for (String elementName: JSONObject.getNames(jo)){
//			if (isJSONArray(jo.get(elementName))){
//				System.out.println("Processing array...");
//				processJSONArray (jo.getJSONArray(elementName));
//			} else
//				System.out.println("Element: '"+elementName+"' is not an array");
//		}

	}

	public static void putDefects (List<org.apache.http.cookie.Cookie> cookies) throws IOException{

		System.out.println("Starting putDefects ... ");	
		//localContext.setCookieStore(cookieStore);
		
		HttpResponse httpResponse = null;
		try {
			// specify the get request
			HttpPost postRequest = new HttpPost(constants.OCTANE_WORKSPACE_URI+"/Defects");
			postRequest.addHeader("Content-Type", "application/json;charset=UTF-8");
			//HPECLIENTTPE: HPE_REST_API_BETA needs to be added to use the beta rest api
			//postRequest.addHeader("HPECLIENTTYPE", cookies.getHpClientType());
			postRequest.addHeader("Accept", "application/json");
			
			//for (Cookie c:cookies){
			//	postRequest.addHeader(c.getName(), c.getValue());
			//}
			//create json to send
			JSONObject jo = new JSONObject();
			JSONObject parent =new JSONObject();
			JSONObject detected_by =new JSONObject();
			JSONObject severity =new JSONObject();
			JSONObject phase =new JSONObject();
			jo.put("type", "defect");
			jo.put("name", "corndog100");
			jo.put("description", "<html><body>\\ncorndogs are delicious\\n</body></html>");
			parent.put("type","work_item_root");
			parent.put("id", 1001);
			jo.put("parent", parent);
			detected_by.put("type", "workspace_user");
			detected_by.put("id", 2003);
			jo.put("detected_by", detected_by);
			severity.put("type", "list_node");
			severity.put("id", 1002);
			jo.put("severity", severity);
			phase.put("type", "phase");
			phase.put("id", 1001);
			jo.put("phase", phase);
			
			JSONArray ja = new JSONArray();
			ja.put(0, jo);
			
			postRequest.setEntity(new StringEntity(ja.toString(), "UTF-8"));
			System.out.println("---------------- Post ----------------");
			System.out.println("Executing post request to: " + target);
			for(int x=0; x< postRequest.getAllHeaders().length; x++){
				System.out.println("Header: " +postRequest.getAllHeaders()[x].toString());
			}
			
			System.out.println("using: "+postRequest.toString());
		

            try{
            	List<org.apache.http.cookie.Cookie> cs = cookieStore.getCookies();
            	for (Cookie c:cs){
            		System.out.println("My Cookie: "+c.getName()+" --- "+c.getValue());
            	}
            	httpResponse = httpClient.execute(target, postRequest, localContext);
            }
            catch(IOException e){
                System.out.printf(String.format("Failed to execute getHeaderCookies:%s%s", System.lineSeparator(), e.getMessage()));
                throw e;
            }

            // Analyze response
            if(httpResponse.getStatusLine().getStatusCode() != 201){
            	System.err.println("Failed to execute getHeaderCookie got status code: " + httpResponse.getStatusLine().getStatusCode() +
            			System.lineSeparator() + "Reason: " + httpResponse.getStatusLine().getReasonPhrase() +
            			System.lineSeparator());
            	//return new Cookies ("no cookies","no cookies");
            }
		} finally {}
	}
	
	
	public static void test () throws IOException{
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
        // Create a local instance of cookie store
        CookieStore cookieStore = new BasicCookieStore();

        // Create local HTTP context
        HttpClientContext localContext = HttpClientContext.create();
        // Bind custom cookie store to the local context
        localContext.setCookieStore(cookieStore);

        //HttpGet httpget = new HttpGet("http://httpbin.org/cookies");
        HttpGet httpget = new HttpGet("https://www.google.com");
        System.out.println("Executing request " + httpget.getRequestLine());

        // Pass local context as a parameter
        CloseableHttpResponse response = httpclient.execute(httpget, localContext);
        try {
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("Local cookie: " + cookies.get(i));
            }
            EntityUtils.consume(response.getEntity());
        } finally {
            response.close();
        }
    } finally {
        httpclient.close();
    }
	}
}
