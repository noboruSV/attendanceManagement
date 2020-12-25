package jp.co.softventure.domain;

public class DailyReport {

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
	//休憩時間
	private String breakTime;
	//レコード作成者
	private String recRegstUser;
	//レコード最終更新者
	private String recUpdtUser;
	//データ取得日付
	private String working_date_s;
	//データ取得日付
	private String working_date_e;
	//add n.matsu start
	//勤務時間
	private String workingHour;
	//add n.matsu end
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
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String break_time) {
		this.breakTime = break_time;
	}
	
	public String getRecRegstUser() {
		return recRegstUser;
	}
	public void setRecRegstUser(String recRegstUser) {
		this.recRegstUser = recRegstUser;
	}
	
	public String getRecUpdtUser() {
		return recUpdtUser;
	}
	public void setRecUpdtUser(String recUpdtUser) {
		this.recUpdtUser = recUpdtUser;
	}
	public String getWorking_date_s() {
		return working_date_s;
	}
	public void setWorking_date_s(String working_date_s) {
		this.working_date_s = working_date_s;
	}
	public String getWorking_date_e() {
		return working_date_e;
	}
	public void setWorking_date_e(String working_date_e) {
		this.working_date_e = working_date_e;
	}
	//add n.matsu start
	public String getWorkingHour() {
		return workingHour;
	}
	public void setWorkingHour(String workingHour) {
		this.workingHour = workingHour;
	}
	//add n.matsu end	
}
