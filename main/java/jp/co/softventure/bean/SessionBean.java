package jp.co.softventure.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;
//import org.springframework.context.annotation.Scope;
//import org.springframework.context.annotation.ScopedProxyMode;
//import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
@Component
@SessionScope
public class SessionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private String id;
	private String userName;
	private Timestamp recUpdtDt;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

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

	public Timestamp getRecUpdtDt() {
		return recUpdtDt;
	}

	public void setRecUpdtDt(Timestamp recUpdtDt) {
		this.recUpdtDt = recUpdtDt;
	}
	
}