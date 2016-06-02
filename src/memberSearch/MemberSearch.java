package memberSearch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
public class MemberSearch {

	public static void main(String[] args) throws ParseException, IOException {
		String hpsso_cookie_key = null;
		String lwsso_cookie_key = null;
		String octane_cookie_key = null;
		
	    HttpClient httpClient = HttpClientBuilder.create().build(); 
	    try {
	     // specify the host, protocol, and port
	    	//http://maps.googleapis.com/maps/api/geocode/json?address=chicago&sesnor=false
	      HttpHost target = new HttpHost("alm-dev", 8101, "http");
	       
	      // specify the get request
	      HttpGet getRequest = new HttpGet("/ServiceSimulation/Demo/MemberAccounts/accounts");
	      getRequest.addHeader("Content-Type", "application/json");

	      System.out.println("executing request to " + target);
	 
	      HttpResponse httpResponse = httpClient.execute(target, getRequest);
	      
	      System.out.println("RESPONSE:"+httpResponse.toString());
	      HttpEntity entity = httpResponse.getEntity();
	 
	      System.out.println("----------------------------------------");
	      System.out.println(httpResponse.getStatusLine());
	      Header[] headers = httpResponse.getAllHeaders();
	      for (int i = 0; i < headers.length; i++) {
	        System.out.println(headers[i]);
	      }
	      System.out.println("----------------------------------------");
	 
	      if (entity != null) {
	    	String sJson = EntityUtils.toString(entity);
	    	//JSONArray jsonArray = new JSONArray(sJson);
	    	System.out.println(">>>>"+sJson);
	        //System.out.println(EntityUtils.toString(entity));
	    	JSONArray jsonArray = new JSONArray(sJson);
	    	
	    	
	    	List<Member> members = new ArrayList<>();
	    	//Member member = new Member();
	    	for (int x=0; x< jsonArray.length(); x++){
	    		System.out.println("member: "+jsonArray.getJSONObject(x).getInt("memberId")+
	    				"\nhouse: "+jsonArray.getJSONObject(x).getInt("householdId")+
	    				"\nssn: "+jsonArray.getJSONObject(x).getString("socialSecurityNumber"));
				//member.setHouseholdId(jsonArray.getJSONObject(x).getInt("householdId"));
				//member.setMemberId(jsonArray.getJSONObject(x).getInt("memberId"));
				//member.setSsn(jsonArray.getJSONObject(x).getString("socialSecurityNumber"));
				members.add(new Member(jsonArray.getJSONObject(x).getInt("householdId"),jsonArray.getJSONObject(x).getInt("memberId"),
						jsonArray.getJSONObject(x).getString("socialSecurityNumber")));
	    		//System.out.println(">>"+jsonArray.getJSONObject(x).toString());
	    	}
	    	System.out.println("Member count:"+members.size());
	    	for (Member m: members){
	    		System.out.println("memberID:" + m.getMemberId());
	    	}
	      }
	      
	      //JSONArray jsonArray = new JSONArray(EntityUtils.toString(entity));
	      //System.out.println(">>"+jsonArray.toString());
	      
	    }
	    finally {
	    }
	}
}
