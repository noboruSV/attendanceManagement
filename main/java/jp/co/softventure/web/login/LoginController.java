package jp.co.softventure.web.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.service.LoginService;

/**
 * 
 * @author n.matsu
 * 2020/09/29 新規作成
 * 2020/10/08 n.matsu コントローラの記述削減対応
 *
 */
@Controller
@SessionAttributes("loginDataInfo")
public class LoginController {
	//del n.matsu コントローラの記述削減対応 start
//	@Autowired
//	private DBLoginDataService service;
	//del n.matsu コントローラの記述削減対応end
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	public SessionBean sessionBean;
	
	//del n.matsu コントローラの記述削減対応 start
//	@ModelAttribute(value = "searchLoginDataForm")
//	public SearchLoginDataForm setForm() {
//		return new SearchLoginDataForm();
//	}
	//del n.matsu コントローラの記述削減対応 end

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
	public String login(LoginForm loginForm) {
		return "login";
	}
	
	/**
	 * ログイン判定し画面遷移
	 * @param loginDto
	 * @param loginForm
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login", params="jdg")
	//mod n.matsu コントローラの記述削減対応 start
//	public String loginFailed(
//	@ModelAttribute("searchLoginDataForm") SearchLoginDataForm searchLoginDataForm,
//	@Validated LoginForm loginForm, BindingResult result, Model model) {
	public String loginConf(@ModelAttribute LoginDataInfo loginDataInfo,
			@Validated LoginForm loginForm, BindingResult result, Model model) {
	//mod n.matsu コントローラの記述削減対応 end

		if (result.hasErrors()) {
			return "login";
		}
		
		//del n.matsu コントローラの記述削減対応 start
//		loginForm.setLoginFlg((short)0);
//		sessionBean.setId(loginForm.getId());
//		
//		//loginを取得する
//		// データ検索に利用するドメインクラスのインスタンス化
//		SearchLoginData searchLoginData = new SearchLoginData();
//		// Formクラスの値をドメインクラスにコピー
//		BeanUtils.copyProperties(loginForm, searchLoginData);
//		// 検索を行うためのサービス処理呼び出し
//		List<LoginData> list = service.searchLoginData(searchLoginData);
//		sessionBean.setId(loginForm.getId());
//		sessionBean.setUserName(loginForm.getUserName());
//		
//		if (list!=null && list.size()!=0) {
//			// Modelオブジェクトに検索結果を格納
//			// model.addAttribute("loginDataList", list);
//			String id = list.get(0).getId();
//			String pw = list.get(0).getPw();
//
//			loginForm.setId(list.get(0).getId());
//			loginForm.setUserName(list.get(0).getUserName());
//
//			MenuForm menuForm = new MenuForm();
//			menuForm.setId(id);
//			menuForm.setUserName(list.get(0).getUserName());
//			model.addAttribute("menuForm", menuForm);
//			
//			// 画面．パスワード ＝ login_data．パスワードの場合、
//			// 作業時間・内容　登録画面へ遷移する
//			if(StringUtils.equals(loginForm.getId(), list.get(0).getId())
//					&& StringUtils.equals(loginForm.getPw(), list.get(0).getPw())) {
//				loginForm.setPw("");
//				model.addAttribute("loginForm", loginForm);
////				return "forward:menu";
//				return "menu/menu";
//
//			} else if (!StringUtils.equals(loginForm.getPw(), list.get(0).getPw())) {
//				// ログインフラグ＝2：パスワードが違います。
//				loginForm.setLoginFlg((short)2);
//				return "login/login";
//			}
//		}
////		// ログインフラグ＝1：ログインIDが存在しません。
//		loginForm.setLoginFlg((short)1);
//		return "login/login";
		//del n.matsu コントローラの記述削減対応 end
		
		//add n.matsu コントローラの記述削減対応 start
		//ログイン認証
		loginForm = loginService.loginCheck(loginForm, model);

		sessionBean.setId(loginForm.getId());
		
		loginDataInfo = loginService.setloginDataInfo(loginForm,loginDataInfo);
		
		//ログイン判定フラグ：1 ⇒ ログイン認証OK
		//ログイン判定フラグ：2 ⇒ ログインIDが存在しない
		//ログイン判定フラグ：3 ⇒ パスワード不一致
		if (loginDataInfo.getLoginJdgFlg() == (short)1) {
			model.addAttribute("loginForm", loginForm);
			model.addAttribute("loginDataInfo", loginDataInfo);
			return "menu/menu";
		}
		return "login";
		//add n.matsu コントローラの記述削減対応 end
	}
}

