package jp.co.softventure.domain;

import java.sql.Date;
import java.sql.Time;

public class RegistrationInfo {
	
	private Date workingDate;
	private Time workingTime;
	private Time workingStartTime;
	private Time workingEndTime;
	private String workingContents;
	private String workingSonota;
	private String shokan;
	private String id;

	public Date getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}

	public Time getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(Time workingTime) {
		this.workingTime = workingTime;
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

	public String getWorkingContents() {
		return workingContents;
	}

	public void setWorkingContents(String workingContents) {
		this.workingContents = workingContents;
	}

	public String getWorkingSonota() {
		return workingSonota;
	}

	public void setWorkingSonota(String workingSonota) {
		this.workingSonota = workingSonota;
	}

	public String getShokan() {
		return shokan;
	}

	public void setShokan(String shokan) {
		this.shokan = shokan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}