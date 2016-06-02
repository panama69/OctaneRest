package memberSearch;

public class Member {

	private int householdId;
	private int memberId;
	private String ssn;
	
	Member(){}
	
	Member (int ahouseholdId, int amemberId, String assn){
		householdId = ahouseholdId;
		memberId = amemberId;
		ssn = assn;
	}

	public int getHouseholdId() {
		return householdId;
	}
	public void setHouseholdId(int householdId) {
		this.householdId = householdId;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	@Override
	public String toString(){
		return getHouseholdId() + ", "+getMemberId()+", "+getSsn();
	}
}

