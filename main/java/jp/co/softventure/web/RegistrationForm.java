package jp.co.softventure.web;

import javax.validation.constraints.*;

public class RegistrationForm {
	
	@NotEmpty(message = "※数値を入力してください。")
	@Pattern(regexp = "^((20([2468][048]|[3579][26])-02-29)|(20[2-9][0-9]-02-(0[1-9]|1[0-9]|2[0-8]))|(20[2-9][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[2-9][0-9]-(0[469]|11)-(0[1-9]|[12][0-9]|30)))$", message = "※無効な値です。") 
	private String workingDate;
	
	private String workingTime;
	
	@NotEmpty(message = "※数値を入力してください。")
	@Pattern(regexp = "^(([01][0-9]|2[0-3]):[0-5][0-9])$", message = "※無効な値です。") 
	private String workingStartTime;
	
	@NotEmpty(message = "数値を入力してください。")
	@Pattern(regexp = "^(([01][0-9]|2[0-3]):[0-5][0-9])$", message = "※無効な値です。") 
	private String workingEndTime;
	
	@Size(min = 1, max = 300, message = "※1～300字で入力してください。")
	private String workingContents;
	
	@Size(min = 1, max = 150, message = "※1～150字で入力してください。")
	private String workingSonota;
	
	@Size(min = 1, max = 100, message = "※1～100字で入力してください。")
	private String shokan;
	
	private String id;

	public String getWorkingDate() {
		return workingDate;
	}

	public String setWorkingDate(String workingDate) {
		this.workingDate = workingDate;
		return this.workingDate;
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