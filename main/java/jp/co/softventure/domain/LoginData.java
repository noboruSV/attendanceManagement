package jp.co.softventure.domain;

public class LoginData {
	private String id;
	private String password;
	private String userName;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPw() {
		return password;
	}
	
	public void setPw(String pw) {
		this.password = pw;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String uName) {
		this.userName = uName;
	}
}
