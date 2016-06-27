package octane.api;

public class constants {

	public static final String OCTANE_URL_PREFIX="hackathon.almoctane.com";
	//public static final String OCTANE_URL_PREFIX="10.0.0.35";
	public static int PORT = 443;
	//public static int PORT = 8080;
	//public static String HTTPTYPE="http";
	public static String HTTPTYPE="https";
	public static final String OPEN_API_VERSION = "v1";
    public static final String NO_ANSWER = "";
    
	public static String SHAREDSPACE_ID = "1001";
	public static String WORKSPACE_ID = "1002";
	// These values are what Octane gave you when you created the API Access in Octane admin area
	//for docker image
	//Client ID: pogo_35lvq196mxo4ds7zv0966kpyo
	//Client secret: #b3ed66ef3a9fccG
	//public static String CLIENT_ID = "pogo_35lvq196mxo4ds7zv0966kpyo";
	//public static String CLIENT_SECRET = "#b3ed66ef3a9fccG";
	
	//for hackathon https://hackathon.almoctane.com/
	//hackathon@user Mission-impossible
	//Client ID: flynn_9q7rewxl8y5kksn72737g01yz
	//Client secret: @e754f645df745fdR
	public static String CLIENT_ID = "flynn_9q7rewxl8y5kksn72737g01yz";
	public static String CLIENT_SECRET = "@e754f645df745fdR";

	public static String SIGN_IN_URI = "/authentication/sign_in";
	public static String BASE_SHAREDSPACES_URI = "/api/shared_spaces/";
	public static String SHAREDSPACES_URI = "/api/shared_spaces/"+SHAREDSPACE_ID;
	public static String BASE_WORKSPACES_URI = "/workspaces/";
	public static String WORKSPACES_URI = "/workspaces/"+WORKSPACE_ID;
	public static String OCTANE_WORKSPACE_URI = SHAREDSPACES_URI + WORKSPACES_URI;
	public static String META_DATA = "/metadata/entities";
}
