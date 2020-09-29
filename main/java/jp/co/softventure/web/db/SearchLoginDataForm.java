package jp.co.softventure.web.db;

public class SearchLoginDataForm {

	private String searchId;
	private String searchUserName;
//	private Integer searchMinPrice;
//	private Integer searchMaxPrice;
	
	public String getSearchId() {
		return searchId;
	}
	
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	
	public String getSearchUserName() {
		return searchUserName;
	}
	
	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}

}
