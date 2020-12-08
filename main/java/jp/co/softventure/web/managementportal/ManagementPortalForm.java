package jp.co.softventure.web.managementportal;

import java.sql.Timestamp;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ManagementPortalForm {
	
	@Pattern(regexp="^[0-9a-zA-Z]+$", message="※半角数字と半角アルファベットのみ使用できます。")
	@Size(min = 1, max = 5, message = "※1～5字で入力してください。")
	private String id;
	
	@Pattern(regexp="^[0-9a-zA-Z]+$", message="※半角数字と半角アルファベットのみ使用できます。")
	@Size(min = 1, max = 60, message = "※1～10字で入力してください。")
	private String password;
	
	@Pattern(regexp="^[0-9a-zA-Z]+$", message="※半角数字と半角アルファベットのみ使用できます。")
	@Size(min = 1, max = 60, message = "※1～10字で入力してください。")
	private String passwordConf;
	
	@NotEmpty(message = "※氏名を入力してください。")
	private String userName;
	
	@Pattern(regexp="^[0-9a-zA-Z@._]+$", message="※半角数字と半角アルファベットと@._のみ使用できます。")
	@NotEmpty(message = "※メールアドレスを入力してください。")
	private String emailAddress;
	
	private String employmentStatus;
	private boolean administrativeRight;
	private Timestamp recRgstDt;
	private String recRgstUser;
	private Timestamp recUpdtDt;
	private String recUpdtUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConf() {
		return passwordConf;
	}

	public void setPasswordConf(String passwordConf) {
		this.passwordConf = passwordConf;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public boolean getAdministrativeRight() {
		return administrativeRight;
	}

	public void setAdministrativeRight(boolean administrativeRight) {
		this.administrativeRight = administrativeRight;
	}
	
	public Timestamp getRecRgstDt() {
		return recRgstDt;
	}

	public void setRecRgstDt(Timestamp recRgstDt) {
		this.recRgstDt = recRgstDt;
	}

	public String getRecRgstUser() {
		return recRgstUser;
	}

	public void setRecRgstUser(String recRgstUser) {
		this.recRgstUser = recRgstUser;
	}

	public Timestamp getRecUpdtDt() {
		return recUpdtDt;
	}

	public void setRecUpdtDt(Timestamp recUpdtDt) {
		this.recUpdtDt = recUpdtDt;
	}

	public String getRecUpdtUser() {
		return recUpdtUser;
	}

	public void setRecUpdtUser(String recUpdtUser) {
		this.recUpdtUser = recUpdtUser;
	}
	
}
