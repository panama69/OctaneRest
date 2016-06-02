package octane;

import java.io.IOException;
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
import org.json.JSONObject;
import org.json.*;
import com.google.gson.*;

// Read https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientFormLogin.java
public class Defects {

	public static void main(String[] args) throws ParseException, IOException {
		String hpsso_cookie_key = null;
		String lwsso_cookie_key = null;
		String octane_cookie_key = null;

		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpHost target = new HttpHost("10.0.0.13", 8080, "http");

			// specify the get request
			/*
			 * Client ID: Pogo_5g21wdy5dqnnjuno339qnwv04 Client secret:
			 * %97db984e797540c2N
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

			JSONObject jo = new JSONObject(s);
			System.out.println("Json: " + jo.toString());
			JSONArray ar = jo.getJSONArray("data");
			System.out.println("Array Size: " + ar.length());
			System.out.println("Arr:" + ar.toString());
			for (int z = 0; z < ar.length(); z++) {
				System.out.println("Defect Name: ("
						+ ar.getJSONObject(z).get("id") + ")"
						+ ar.getJSONObject(z).getString("name"));
			}

		} finally {
		}
	}
}
