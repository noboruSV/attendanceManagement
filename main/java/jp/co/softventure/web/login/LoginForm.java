package jp.co.softventure.web.login;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginForm {

	@NotEmpty
	@Pattern(regexp="^[0-9]+$", message="{0}は半角数字で入力してください。")
	@Size(max=4, message="{0}は半角数字4桁で入力してください。")
	private String id;
	
	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@NotEmpty
	@Pattern(regexp="^[0-9a-zA-Z]+$", message="{0}は半角英数で入力してください。")
	@Size(max=6, message="{0}は半角英数6桁で入力してください。")
	private String pw;
	
	private short loginFlg;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPw() {
		return this.pw;
	}
	
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public short getLoginFlg() {
		return loginFlg;
	}
	
	public void setLoginFlg(short flg) {
		this.loginFlg = flg;
	}
}
