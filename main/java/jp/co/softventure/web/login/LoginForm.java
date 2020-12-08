package jp.co.softventure.web.login;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 
 * @author n.matsu
 * 2020/09/29 新規作成
 */
public class LoginForm {

	//ID
	@NotEmpty
	@Pattern(regexp="^[0-9a-zA-Z]+$", message="{0}は半角数字で入力してください。")
	@Size(max=30, message="{0}は半角数字30桁で入力してください。")
	private String id;
	
	//パスワード
	@NotEmpty
	@Pattern(regexp="^[0-9a-zA-Z]+$", message="{0}は半角英数で入力してください。")
	@Size(max=30, message="{0}は半角英数30桁で入力してください。")
	private String pw;
	
	//ユーザーネーム
	private String userName;
	
	//ログイン判定フラグ
	private short loginJdgFlg;
	
	//管理者権限
	private boolean administrativeRight;
	
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public short getLoginJdgFlg() {
		return loginJdgFlg;
	}
	
	public void setLoginJdgFlg(short loginJdgFlg) {
		this.loginJdgFlg = loginJdgFlg;
	}

	public boolean getAdministrativeRight() {
		return administrativeRight;
	}

	public void setAdministrativeRight(boolean administrativeRight) {
		this.administrativeRight = administrativeRight;
	}
	
}
