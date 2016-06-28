package apppulse.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

//import com.hpe.apppulse.openapi.Utils.HttpUtils;
//import com.hpe.apppulse.openapi.Utils.JsonUtils;
//import com.hpe.apppulse.openapi.v1.bl.beans.AppsResponseBean;

import octane.api.constants;

public class OctaneApppulseApiImp {

	private static CookieStore cookieStore = new BasicCookieStore();
	private static HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();;
	private static HttpHost target = new HttpHost("apppulse-mobile.saas.hpe.com", constants.PORT, constants.HTTPTYPE);
	private static HttpClientContext localContext = HttpClientContext.create();
	private static String token = null;
	
	public static List<org.apache.http.cookie.Cookie> authenticate (String client_id, String client_secret) throws IOException {
		System.out.println("Starting authenticate ... ");	

		HttpResponse httpResponse = null;
		List<org.apache.http.cookie.Cookie> cookies = new ArrayList<org.apache.http.cookie.Cookie>();

		// Bind custom cookie store to the local context
	    localContext.setCookieStore(cookieStore);


		try {
			// specify the get request
	        final String getTokenUrl = String.format("/mobile/openapi/rest/%s/%s/oauth/token", constants.OPEN_API_VERSION, "672149734");

			HttpPost postRequest = new HttpPost(getTokenUrl);
			postRequest.addHeader("Content-Type", "application/json;charset=UTF-8");
			JSONObject apiKey = new JSONObject();
			apiKey.put("clientId", "672149734#C1");
			apiKey.put("clientSecret", "99696968-001a-4144-bff9-cc34698e8265");

			System.out.println("Using url: " + getTokenUrl);
			try {
				StringEntity param = new StringEntity(apiKey.toString());
				postRequest.setEntity(param);
			} catch (UnsupportedEncodingException e){
				System.out.printf("Failed to set entity: %s%s%s\n", apiKey.toString(), System.lineSeparator(), e.getMessage());
			}

			System.out.println("---------------- Post Response ----------------");
			System.out.println("Executing post request to: " + target);

            try{
            	httpResponse = httpClient.execute(target, postRequest, localContext);
        	    List<Cookie> cl = cookieStore.getCookies();
        	    for (Cookie x:cl){
        	    	System.out.println("returned cookies: "+x.getName()+"---"+x.getValue()+"--"+x.getPath());
        	    }
            } catch(IOException e){
                System.out.printf(String.format("Failed to execute getTokenFromAppPulseOpenAPI:getToken%s%s", System.lineSeparator(), e.getMessage()));
                throw e;
            }

            // Analyze response
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                //JsonUtils.ResponseJsonReader<AppsResponseBean.MetaDataBean> jsonReader = new JsonUtils.ResponseJsonReader<>();

                System.err.println("Failed to execute getTokenFromAppPulseOpenAPI:getToken got status code: " + httpResponse.getStatusLine().getStatusCode() +
                        System.lineSeparator() + "Reason: " + httpResponse.getStatusLine().getReasonPhrase() 
                        //System.lineSeparator() + jsonReader.readFromJson(response, AppsResponseBean.MetaDataBean.class)
                        );
                return cookies;
            }


            // Analyze response
           // if(httpResponse.getStatusLine().getStatusCode() != 200){
           // 	System.err.println("Failed to execute getHeaderCookie got status code: " + httpResponse.getStatusLine().getStatusCode() +
           // 			System.lineSeparator() + "Reason: " + httpResponse.getStatusLine().getReasonPhrase() +
           // 			System.lineSeparator());
           // 	return cookies;
           // }

            cookies = cookieStore.getCookies();
            
            System.out.println("Cookies returned");
            for (Cookie c:cookies){
            	System.out.println(c.getName()+":"+c.getValue());
            }

			System.out.println(httpResponse.getStatusLine());
			String s = EntityUtils.toString(httpResponse.getEntity());
			if (s != null) {
				System.out.println("Json Response: " + s);// EntityUtils.toString(entity));
				JSONObject jo = new JSONObject(s);
				System.out.println("Token to use: "+ jo.getString("token"));
				token = jo.getString("token");
				System.out.println("Token value:"+token);
				System.out.println("Expires: "+ jo.getLong("expirationTime"));
			}
			System.out.println("---------------- End Post Response ----------------");
			return cookies;
        } finally {
            if (httpResponse != null) {
                httpResponse.getEntity().getContent().close();
            }
            System.out.println("... authentication - Completed");
        }
	}

	
	//public static List<String> getApplications(String token, String tenantId) throws IOException{
	public static void getApplications(String tenantId) throws IOException{

       final String getApplicationsUrl = String.format("https://apppulse-mobile.saas.hpe.com/mobile/openapi/rest/%s/%s/applications", constants.OPEN_API_VERSION, 672149734);
       System.out.println("Starting getApplications using url: " + getApplicationsUrl);

       HttpGet getRequest = new HttpGet(getApplicationsUrl);
 
       //HttpUtils.prepareRequest(token, getApiRestCall);
       //getRequest.setURI(URI.create (getApplicationsUrl));
       getRequest.setHeader("Authorization", "Bearer " + token);
       getRequest.addHeader("Accept", "application/json");
       
       for (int i=0; i < getRequest.getHeaders("Authorization").length; i++){
    	   System.out.println(getRequest.getHeaders("Authorization")[i].getName()+ "<-->"+
    			   getRequest.getHeaders("Authorization")[i].getValue());
       }
       
       
       //System.out.println("executing request to " + target + getRequest.getURI());
       System.out.println("executing request to " + getRequest.getURI());

       // Execute HTTP Post Request
       HttpResponse httpResponse = null;
       try{
           try{
        	   httpResponse = httpClient.execute (getRequest);
               //httpResponse = httpClient.execute(target, getRequest, localContext);
           }
           catch(IOException e){
               System.err.printf("Failed to execute getSample: %s%s%s%n", getRequest.toString(), System.lineSeparator(), e.getMessage());
               throw e;
           }

           // Analyze response
           if(httpResponse.getStatusLine().getStatusCode() != 200){
               System.err.printf("Failed to execute getSample, got status code: %d%s%s%n", httpResponse.getStatusLine().getStatusCode(), System.lineSeparator(), getRequest.toString());
               //return Collections.emptyList();
           }

           System.out.println(httpResponse.getStatusLine());
           String s = EntityUtils.toString(httpResponse.getEntity());
           System.out.println(s);
           //JsonUtils.ResponseJsonReader<AppsResponseBean> jsonReader = new JsonUtils.ResponseJsonReader<>();
           //final List<AppsResponseBean.ApplicationsDataBean.ApplicationBean> applications = jsonReader.readFromJson(response, AppsResponseBean.class).getData().getApplications();
           //return applications.stream().map(AppsResponseBean.ApplicationsDataBean.ApplicationBean::getApplicationId).collect(Collectors.toList());
       }
       finally{
           if(httpResponse != null){
        	   httpResponse.getEntity().getContent().close();
           }
           System.out.println("... getApplications - Completed");
       }
   }
}
