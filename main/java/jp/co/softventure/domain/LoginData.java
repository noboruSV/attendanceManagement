package jp.co.softventure.domain;

/**
 * 
 * @author n.matsu
 * 2020/09/29 �V�K�쐬
 * 
 */
public class LoginData {
	private String id;
	private String password;
	private String userName;
	private String autoLogin;
	private boolean administrativeRight;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(String autoLogin) {
		this.autoLogin = autoLogin;
	}

	public boolean getAdministrativeRight() {
		return administrativeRight;
	}

	public void setAdministrativeRight(boolean administrativeRight) {
		this.administrativeRight = administrativeRight;
	}
	
}
