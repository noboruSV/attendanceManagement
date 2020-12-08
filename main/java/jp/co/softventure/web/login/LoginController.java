package jp.co.softventure.web.login;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.domain.LoginData;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.service.DBLoginDataService;
import jp.co.softventure.service.LoginService;
import jp.co.softventure.web.managementportal.ManagementPortalController;

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
	private DBLoginDataService dbLoginDataService;
	
	@Autowired
	public SessionBean sessionBean;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
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
	 * @param svamsalidValue
	 * @param loginForm
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	
	@RequestMapping("/login")
	public String login(@CookieValue(name = "SVAMSALID", required = false) String svamsalidValue, 
	LoginForm loginForm, HttpServletRequest request, HttpServletResponse response, Model model) {
		LoginData loginData = new LoginData();
		//cookieの値を取得してauto_loginに設定
		loginData.setAutoLogin(svamsalidValue);
		List<LoginData> list = dbLoginDataService.selectLoginDataByAutoLogin(loginData);
		//直前にログアウトしておらず(auto_loginが初期化されておらず)、かつ管理者権限がない場合、日報一覧画面に遷移
		if ( list.size() == 1 && !list.get(0).getAutoLogin().equals("0") && !list.get(0).getAdministrativeRight() ) {
			sessionBean.setId(list.get(0).getId());
			addCookie(response);
			return "redirect:workreportslist/workReportsList";
		//直前にログアウトしておらず(auto_loginが初期化されておらず)、かつ管理者権限がある場合、管理者画面に遷移
		} else if ( list.size() == 1 && !list.get(0).getAutoLogin().equals("0") && list.get(0).getAdministrativeRight() ) {
			sessionBean.setId(list.get(0).getId());
			addCookie(response);
			return "redirect:managementportal/managementPortal";
		}
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
			@Validated LoginForm loginForm, BindingResult result, HttpServletResponse response, Model model) {
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
		
		loginDataInfo = loginService.setloginDataInfo(loginForm,loginDataInfo);
		
		//ログイン判定フラグ：1 ⇒ ログイン認証OK
		//ログイン判定フラグ：2 ⇒ ログインIDが存在しない
		//ログイン判定フラグ：3 ⇒ パスワード不一致
		if (loginDataInfo.getLoginJdgFlg() == (short)1) {
			model.addAttribute("loginForm", loginForm);
			model.addAttribute("loginDataInfo", loginDataInfo);
			sessionBean.setId(loginForm.getId());
			addCookie(response);
			return "redirect:workreportslist/workReportsList";
		}
		return "login";
		//add n.matsu コントローラの記述削減対応 end
	}
	
	//管理者画面ログイン用処理
	@RequestMapping(value="/login", params="toManagementPortal")
	public String loginConfToManagementPortal(
	@ModelAttribute("loginForm") @Validated LoginForm loginForm, BindingResult result, HttpServletResponse response, Model model) {
		if ( !result.hasErrors() ) {
			loginForm = loginService.loginCheck(loginForm, model);
			model.addAttribute("loginForm", loginForm);
			if ( loginForm.getLoginJdgFlg() != (short)2 && loginForm.getLoginJdgFlg() != (short)3 && !loginForm.getAdministrativeRight() ) {
				//ログイン判定フラグ：4 ⇒ 管理者権限不保持
				loginForm.setLoginJdgFlg((short)4);
			}
			if ( loginForm.getLoginJdgFlg() == (short)1 ) {
				sessionBean.setId(loginForm.getId());
				addCookie(response);
				return "redirect:managementportal/managementPortal";
			}
		}
		return "/login";
	}
	
	//ログイン情報をcookieに保存
	public void addCookie(HttpServletResponse response) {
		LoginData loginData = new LoginData();
		//ハッシュ化した文字列をauto_login値に設定
		loginData.setAutoLogin(passwordEncoder.encode(ManagementPortalController.passwordGenerator()));
		loginData.setId(sessionBean.getId());
		dbLoginDataService.updateAutoLogin(loginData);
		//auto_login値をvalueに設定したcookieを生成
		Cookie cookie = new Cookie("SVAMSALID", loginData.getAutoLogin());
		//30日期限
		cookie.setMaxAge(30 * 24 * 60 * 60);
		cookie.setHttpOnly(true);
		//cookie.setSecure(true);
		response.addCookie(cookie);
	}
	
	//ログアウト処理
	public void logout(HttpServletRequest request, HttpServletResponse response, SessionStatus sessionStatus) {
		//auto_login値を初期化
		LoginData loginData = new LoginData();
		loginData.setId(sessionBean.getId());
		loginData.setAutoLogin("0");
		dbLoginDataService.updateAutoLogin(loginData);
		//セッション破棄
		sessionStatus.setComplete();
		//cookie情報を破棄
		Cookie[] cookies = request.getCookies();
		for ( Cookie cookie : cookies ) {
	        if ( cookie.getName().equals("SVAMSALID") ) {
	        	cookie.setMaxAge(0);
	        	cookie.setValue("");
	        	cookie.setPath("/");
	        	response.addCookie(cookie);
	        }
	    } 
	}
	
}