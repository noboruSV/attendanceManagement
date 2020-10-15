package jp.co.softventure.domain.dto;

public class WorkReportsListDto {
	//年
	private String year;
	//月日
	private String date;
	//曜日
	private String week;
	//作業内容
	private String workingContents;
	//出勤時刻
	private String workingStartTime;
	//退勤時刻
	private String workingEndTime;
	//勤務時間（退勤時刻 － 出勤時刻）
	private String dutyTime;
	//休憩時間
	private String breakTime;
	//	作業時間（勤務時間 － 休憩）
	private String workTime;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
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
	public String getDutyTime() {
		return dutyTime;
	}
	public void setDutyTime(String dutyTime) {
		this.dutyTime = dutyTime;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	
}
