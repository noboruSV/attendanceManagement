package jp.co.softventure.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import jp.co.softventure.dto.WorkReportsListDto;

/**
 * 
 * @author n.matsu
 * 2020/09/29 新規作成
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class LoginDataInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String userName;
	private short loginJdgFlg;
	private Date date; 
	private List<WorkReportsListDto> workReportsListDto;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public short getLoginJdgFlg() {
		return loginJdgFlg;
	}
	public void setLoginJdgFlg(short loginJdgFlg) {
		this.loginJdgFlg = loginJdgFlg;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<WorkReportsListDto> getWorkReportsListDto() {
		return workReportsListDto;
	}
	public void setWorkReportsListDto(List<WorkReportsListDto> workReportsListDto) {
		this.workReportsListDto = workReportsListDto;
	}
	
}
