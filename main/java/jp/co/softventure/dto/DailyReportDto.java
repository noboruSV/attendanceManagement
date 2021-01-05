package jp.co.softventure.dto;

import java.sql.Date;
import java.sql.Time;

public class DailyReportDto {

	//ID
	private String id;
	//作業日時
	private Date workingDate;
	//作業時間
	private Time workingTime;
	//出勤時間
	private Time workingStartTime;
	//退勤時間
	private Time workingEndTime;
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

	//データ取得開始日付
	private Date workDateStart;
	//データ取得終了日付
	private Date workDateEnd;
	
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
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
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
	public Date getWorkDateStart() {
		return workDateStart;
	}
	public void setWorkDateStart(Date workDateStart) {
		this.workDateStart = workDateStart;
	}
	public Date getWorkDateEnd() {
		return workDateEnd;
	}
	public void setWorkDateEnd(Date workDateEnd) {
		this.workDateEnd = workDateEnd;
	}
	
}
