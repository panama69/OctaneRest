package google;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GoogleMaps {

	public static void main(String[] args) throws ParseException, IOException {
		String hpsso_cookie_key = null;
		String lwsso_cookie_key = null;
		String octane_cookie_key = null;
		
	    HttpClient httpClient = HttpClientBuilder.create().build(); 
	    try {
	     // specify the host, protocol, and port
	    	//http://maps.googleapis.com/maps/api/geocode/json?address=chicago&sesnor=false
	      HttpHost target = new HttpHost("maps.googleapis.com", 80, "http");
	       
	      // specify the get request
	      HttpGet getRequest = new HttpGet("/maps/api/geocode/json?address=chicago&sesnor=false");
	      getRequest.addHeader("Content-Type", "application/json");

	      System.out.println("executing request to " + target);
	 
	      HttpResponse httpResponse = httpClient.execute(target, getRequest);
	      
	      System.out.println("RESPONSE:"+httpResponse.toString());
	      HttpEntity entity = httpResponse.getEntity();
	 
	      System.out.println("----------------------------------------");
	      System.out.println(httpResponse.getLocale());
	      System.out.println(httpResponse.getStatusLine());
	      Header[] headers = httpResponse.getAllHeaders();
	      for (int i = 0; i < headers.length; i++) {
	        System.out.println(headers[i]);
	      }
	      System.out.println("----------------------------------------");
	 
	      if (entity != null) {
	        System.out.println(EntityUtils.toString(entity));
	      }
	    }
	    finally {
	    }
	}
}
