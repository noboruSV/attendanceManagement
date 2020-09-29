package jp.co.softventure.web.workReportsList;

import java.util.List;

import jp.co.softventure.domain.DailyReport;

public class WorkReportsListForm {
	private List<DailyReport> list;
	private List<WorkReportsListDto> dailyReportResult;
	
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
	//年
	private String year;
	//月日
	private String date;
	//曜日
	private String week;
	//作業内容
	private String workingContents;
	//勤務時間（出勤から退勤までの時間）
	private String dutyTime;
	//休憩時間
	private String breakTime;
	//	作業時間（勤務時間 － 休憩）
	private String workTime;


	public List<DailyReport> getList() {
		return list;
	}

	public void setList(List<DailyReport> list) {
		this.list = list;
	}

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

	public List<WorkReportsListDto> getDailyReportResult() {
		return dailyReportResult;
	}

	public void setDailyReportResult(List<WorkReportsListDto> dailyReportResult) {
		this.dailyReportResult = dailyReportResult;
	}

	

	
}
