package jp.co.softventure.bean;

import java.io.Serializable;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)

public class SessionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String userName;

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

}