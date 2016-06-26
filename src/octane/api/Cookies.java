package octane.api;

public class Cookies {
	private String hpsso_cookie_key = null;
	private String lwsso_cookie_key = null;
	private String octane_cookie_key = null;
	//key pair needed to access beta apis
	//"HPECLIENTTYPE", "HPE_REST_API_BETA"
	private String hpclienttype_key = "HPE_REST_API_BETA"; 
	
	
	Cookies () {}
	Cookies (String hpsso_cookie, String lwsso_cookie){
		hpsso_cookie_key = hpsso_cookie;
		lwsso_cookie_key = lwsso_cookie;
	}
	public String getHpssoCookie (){
		return hpsso_cookie_key;
	}
	public void setHpssoCookie (String hpsso_cookie){
		hpsso_cookie_key=hpsso_cookie;
	}
	
	public String getLwssoCookie() {
		return lwsso_cookie_key;
	}
	public void setLwssoCookie(String lwsso_cookie) {
		lwsso_cookie_key=lwsso_cookie;
	}
	
	public String getOctaneCookie() {
		return octane_cookie_key;
	}
	public void setOctaneCookie(String octane_cookie) {
		octane_cookie_key=octane_cookie;
	}
	
	public String getHpClientType(){
		return hpclienttype_key;
	}
}
