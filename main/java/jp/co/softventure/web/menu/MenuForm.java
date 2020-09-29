package jp.co.softventure.web.menu;

import jp.co.softventure.web.login.LoginForm;

public class MenuForm {

	private String id;
	private String userName;
	private int number;
	private LoginForm loginForm;

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public LoginForm getLoginForm() {
		return loginForm;
	}
	public void setLoginForm(LoginForm loginForm) {
		this.loginForm = loginForm;
	}
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
