package jp.co.softventure.web.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.domain.LoginData;
import jp.co.softventure.service.DBLoginDataService;

@Controller
@SessionAttributes("scopedTarget.sessionBean")
public class MenuController {
	
	@Autowired
	private DBLoginDataService service;
	
	@Autowired
	public SessionBean sessionBean;
	
	@ModelAttribute(value = "dispUserName")
	public LoginData dispUserName() {
		return new LoginData();
	}
	
	@RequestMapping(value = "/menu", params = "registration")
	public String toRegistrationPage() {
		return "redirect:/registration";
	}
	
	@RequestMapping(value = "/menu", params = "workReportsList")
	public String toWorkReportList() {
		return "redirect:/workReportsList";
	}
	
	@RequestMapping(value = "/menu", params = "logout")
	public ModelAndView toLogout(@ModelAttribute("dispUserName") LoginData loginData, SessionStatus sessionStatus, ModelAndView mav) {
		loginData.setId(sessionBean.getId());
		List<LoginData> list = service.selectUserName(loginData);
		if ( list.size() == 1 ) {
			for ( int i = 0; i < list.size(); i++ ) {
				loginData = list.get(i);
				mav.addObject("dispUserName", loginData);
			}
		} else {
			;
		}
		//セッション破棄処理
		sessionStatus.setComplete();
		mav.setViewName("logout");
		return mav;
	}
	
}