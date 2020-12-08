package jp.co.softventure.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
//import org.thymeleaf.util.StringUtils;

import jp.co.softventure.domain.LoginData;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.web.login.LoginForm;

/**
 * 
 * @author n.matsu
 * 2020/09/29 新規作成
 * 2020/10/08 n.matsu コントローラの記述削減対応
 * 
 */
@Service
public class LoginService {

	@Autowired
	private DBLoginDataService dbLoginDataService;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	/**
	 * ログイン認証処理
	 * @param loginForm
	 * @param model
	 * @param loginDto
	 * @return
	 */
	public LoginForm loginCheck(LoginForm loginForm, Model model) {
		
		loginForm.setLoginJdgFlg((short)0);
		
		//loginDBを取得する
		LoginData loginData = new LoginData();
		loginData.setId(loginForm.getId());
		List<LoginData> resultList = dbLoginDataService.selectLoginData(loginData);

		//ログイン認証OK				⇒		ログイン判定フラグ：1
		//ログインIDが存在しない		⇒		ログイン判定フラグ：2
		//パスワード不一致				⇒		ログイン判定フラグ：3
		if(resultList == null || !(resultList.size() == 1)) {
			loginForm.setLoginJdgFlg((short)2);
		} else if (!passwordEncoder.matches(loginForm.getPw(), resultList.get(0).getPassword()))
		/*else if (!StringUtils.equals(loginForm.getPw(), resultList.get(0).getPassword())) */{
			loginForm.setLoginJdgFlg((short)3);
		} else {
			loginData = resultList.get(0);
			loginForm.setLoginJdgFlg((short)1);
			BeanUtils.copyProperties(loginData, loginForm);
		}
		return loginForm;
		
	}

	/**
	 * ログイン情報をセット
	 * @param loginForm
	 * @param loginInfoDto
	 * @return
	 */
	public LoginDataInfo setloginDataInfo(LoginForm loginForm, LoginDataInfo loginDataInfo) {
		loginDataInfo.setId(loginForm.getId());
		loginDataInfo.setUserName(loginForm.getUserName());
		loginDataInfo.setLoginJdgFlg(loginForm.getLoginJdgFlg());
		return loginDataInfo;
	}

}
