package jp.co.softventure.web.login;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import jp.co.softventure.domain.LoginData;
import jp.co.softventure.domain.SearchLoginData;
import jp.co.softventure.service.DBLoginDataService;
import jp.co.softventure.web.db.SearchLoginDataForm;
import jp.co.softventure.web.menu.MenuForm;
import jp.co.softventure.bean.SessionBean;

@Controller
public class LoginController {
	
	@Autowired
	private DBLoginDataService service;

	@Autowired
	protected SessionBean sessionBean;
	
	@ModelAttribute(value = "searchLoginDataForm")
	public SearchLoginDataForm setForm() {
		return new SearchLoginDataForm();
	}
	
	@ModelAttribute(value = "loginForm")
	public LoginForm setLoginForm() {
		return new LoginForm();
	}

	/**
	 * ログイン画面表示
	 * @param loginForm
	 * @return
	 */
	@RequestMapping("/login")
	public String login(LoginForm loginForm) {

		return "login/login";

	}

	
	@RequestMapping(value="/login", params="jdg")
	public String loginFailed(
			@ModelAttribute("searchLoginDataForm") SearchLoginDataForm searchLoginDataForm,
			@Validated LoginForm loginForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "login/login";
		}
		
		loginForm.setLoginFlg((short)0);
		sessionBean.setId(loginForm.getId());
		
		//loginを取得する
		// データ検索に利用するドメインクラスのインスタンス化
		SearchLoginData searchLoginData = new SearchLoginData();
		// Formクラスの値をドメインクラスにコピー
		BeanUtils.copyProperties(loginForm, searchLoginData);
		// 検索を行うためのサービス処理呼び出し
		List<LoginData> list = service.searchLoginData(searchLoginData);
		
		if (list!=null && list.size()!=0) {
			// Modelオブジェクトに検索結果を格納
			// model.addAttribute("loginDataList", list);
			String id = list.get(0).getId();
			String pw = list.get(0).getPw();

			loginForm.setId(id);
			loginForm.setUserName(list.get(0).getUserName());

			MenuForm menuForm = new MenuForm();
			menuForm.setId(id);
			menuForm.setUserName(list.get(0).getUserName());
			model.addAttribute("menuForm", menuForm);
			
			// 画面．パスワード ＝ login_data．パスワードの場合、
			// 作業時間・内容　登録画面へ遷移する
			if(StringUtils.equals(loginForm.getId(), id)
					&& StringUtils.equals(loginForm.getPw(), pw)) {
				loginForm.setPw("");
				model.addAttribute("loginForm", loginForm);
//				return "forward:menu";
				return "menu/menu";

			} else if (!StringUtils.equals(loginForm.getPw(), pw)) {
				// ログインフラグ＝2：パスワードが違います。
				loginForm.setLoginFlg((short)2);
				return "login/login";
			}
		}
		// ログインフラグ＝1：ログインIDが存在しません。
		loginForm.setLoginFlg((short)1);
		return "login/login";
	}

}
