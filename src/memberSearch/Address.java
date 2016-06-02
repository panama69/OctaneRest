package memberSearch;

public class Address {
	//address":{"city":"Smithfield London W1","country":"Great Britain","line1":"56B Whitehaven Mansions","line2":"Charterhouse Square","state":"England","zip":null}"

	private String city;
	private String country;
	private String line1;
	private String line2;
	private String state;
	private String zip;
	
	public String getCity (){
		return city;
	}
	public void setCity (String s){
		city = s;
	}
}
