/**
 * 2020/10/03 新規作成 Noboru.M
 */
package jp.co.softventure.web.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thymeleaf.util.StringUtils;

import jp.co.softventure.domain.dto.LoginDto;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.service.login.LoginService;


@Controller
@SessionAttributes("loginDataInfo")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@ModelAttribute(value = "loginForm")
	public LoginForm setLoginForm() {
		return new LoginForm();
	}

	@ModelAttribute(value = "loginDataInfo")
	public LoginDataInfo loginDataInfo() {
		return new LoginDataInfo();
	}
	
	/**
	 * ログイン画面表示
	 * @param loginForm
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {

		return "login/login";

	}

	/**
	 * ログイン成功⇒メニュー画面へ遷移　
	 * ログイン失敗⇒ログイン画面へ遷移
	 * @param searchLoginDataForm
	 * @param loginForm
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login", params="jdg")
	public String loginFailed(
			@ModelAttribute LoginDataInfo loginDataInfo,
			@Validated LoginForm loginForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "login/login";
		}
		
		//ログイン認証
		loginForm = loginService.loginCheck(loginForm, model);
		
		//loginDataInfoにログイン情報を詰めて持ち回る
		loginDataInfo.setLoginJdgFlg(loginForm.getLoginJdgFlg());
		loginDataInfo.setUserId(loginForm.getUserId());
		loginDataInfo.setUserName(loginForm.getUserName());
		if (StringUtils.equals(loginDataInfo.getLoginJdgFlg(), "1")){
			model.addAttribute("loginDataInfo", loginDataInfo);
			return "forward:menu";
		}

		return "login/login";

	}

}
