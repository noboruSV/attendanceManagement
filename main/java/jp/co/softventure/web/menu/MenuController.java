package jp.co.softventure.web.menu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.co.softventure.web.login.LoginForm;

@Controller
public class MenuController {
	@ModelAttribute(value="menuForm")
	public MenuForm menuForm() {
		return new MenuForm();
	}
	@ModelAttribute(value="loginForm")
	public LoginForm loginForm(LoginForm loginForm) {
		return new LoginForm();
	}

	@RequestMapping(value="/menu")
	public String menu1(@ModelAttribute("loginForm") LoginForm loginForm, Model model) {
			return "menu/menu";
	}
	@RequestMapping(value="/menu", params = "jikoku")
	public String menu() {
		return "jikoku/jikokuInputt";
	}

	@RequestMapping(value="/menu", params = "workReportList" )
	public String toWorkReportList(@ModelAttribute("loginForm") LoginForm loginform, MenuForm menuform, BindingResult result, Model model) {
		//一覧画面へ遷移
//		model.addAttribute("model", model);
		return "forward:workReportsList";
	}

	@RequestMapping(value="/menu", params = "logout" )
	public String toLogout(@ModelAttribute("loginForm") LoginForm loginform, MenuForm menuform, BindingResult result, Model model) {
			return "logout/logout";
	}

}
