package jp.co.softventure.web.logout;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.softventure.web.login.LoginForm;

import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LogoutController {
	
	@ModelAttribute
	public LoginForm setUserName() {
		return new LoginForm();
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "logout";
	}
	
}