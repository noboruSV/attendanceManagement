package jp.co.softventure.dto;

import java.util.Date;

//import java.util.Date;

public class DailyReportDto {

	//ID
	private String id;
	//作業日時
	private Date workingDate;
	//作業時間
	private Date workingTime;
	//出勤時間
	private Date workingStartTime;
	//退勤時間
	private Date workingEndTime;
	//庶務・その他
	private String shomuSonota;
	//所感
	private String shokan;
	//作業内容
	private String workingContents;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getWorkingDate() {
		return workingDate;
	}
	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}
	public Date getWorkingTime() {
		return workingTime;
	}
	public void setWorkingTime(Date workingTime) {
		this.workingTime = workingTime;
	}
	public Date getWorkingStartTime() {
		return workingStartTime;
	}
	public void setWorkingStartTime(Date workingStartTime) {
		this.workingStartTime = workingStartTime;
	}
	public Date getWorkingEndTime() {
		return workingEndTime;
	}
	public void setWorkingEndTime(Date workingEndTime) {
		this.workingEndTime = workingEndTime;
	}
	public String getShomuSonota() {
		return shomuSonota;
	}
	public void setShomuSonota(String shomuSonota) {
		this.shomuSonota = shomuSonota;
	}
	public String getShokan() {
		return shokan;
	}
	public void setShokan(String shokan) {
		this.shokan = shokan;
	}
	public String getWorkingContents() {
		return workingContents;
	}
	public void setWorkingContents(String workingContents) {
		this.workingContents = workingContents;
	}

	
	
}
