package jp.co.softventure.domain;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ManagementPortalInfo {
	
	private String id;
	private String password;
	private String userName;
	private String emailAddress;
	private String employmentStatus;
	private boolean administrativeRight;
	private Timestamp recRgstDt;
	private String recRgstUser;
	private Timestamp recUpdtDt;
	private String recUpdtUser;
	private Date workingDate;
	private Time workingStartTime;
	private Time workingEndTime;
	private String breakTime;
	private String workingContents;

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

	public Date getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}

	public Time getWorkingStartTime() {
		return workingStartTime;
	}

	public void setWorkingStartTime(Time workingStartTime) {
		this.workingStartTime = workingStartTime;
	}

	public Time getWorkingEndTime() {
		return workingEndTime;
	}

	public void setWorkingEndTime(Time workingEndTime) {
		this.workingEndTime = workingEndTime;
	}

	public String getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}

	public String getWorkingContents() {
		return workingContents;
	}

	public void setWorkingContents(String workingContents) {
		this.workingContents = workingContents;
	}
	
}
