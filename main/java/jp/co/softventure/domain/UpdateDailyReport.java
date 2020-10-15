package jp.co.softventure.domain;

public class UpdateDailyReport {
	//ID
	private String id;
	//作業日時
	private String workingDate;
	//作業時間
	private String workingTime;
	//出勤時間
	private String workingStartTime;
	//退勤時間
	private String workingEndTime;
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

	public String getWorkingDate() {
		return workingDate;
	}
	public void setWorkingDate(String workingDate) {
		this.workingDate = workingDate;
	}
	public String getWorkingTime() {
		return workingTime;
	}
	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}
	public String getWorkingStartTime() {
		return workingStartTime;
	}
	public void setWorkingStartTime(String workingStartTime) {
		this.workingStartTime = workingStartTime;
	}
	public String getWorkingEndTime() {
		return workingEndTime;
	}
	public void setWorkingEndTime(String workingEndTime) {
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
