package jp.co.softventure.web.workreportslist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.domain.ManagementPortalInfo;
import jp.co.softventure.dto.WorkReportsListDto;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.service.ManagementPortalService;
import jp.co.softventure.service.WorkReportsListService;
import jp.co.softventure.web.RegistrationForm;
import jp.co.softventure.web.login.LoginController;
import jp.co.softventure.web.managementportal.ManagementPortalController;

@Controller
@SessionAttributes({"scopedTarget.sessionBean", "loginDataInfo", "workReportsListForm"})
public class WorkReportsListController {

	@Autowired
	private WorkReportsListService workReportsListService;
	
	@Autowired
	private ManagementPortalService managementPortalService;
	
	@Autowired
	LoginController loginController;
	
	@Autowired
	public SessionBean sessionBean;
	
	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	
	@ModelAttribute("workReportsListForm")
	public WorkReportsListForm setForm() {
		return new WorkReportsListForm();
	}

	@ModelAttribute("loginDataInfo")
	public LoginDataInfo loginDataInfo(LoginDataInfo loginDataInfo) {
		return new LoginDataInfo();
	}
	
	/**
	 * 初期表示
	 * @param loginDataInfo
	 * @param workReportsListForm
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="workreportslist/workReportsList")
	public String viewList(@ModelAttribute LoginDataInfo loginDataInfo, WorkReportsListForm workReportsListForm,
			BindingResult result, Model model) {
		
		//直アクセス防止
		if( sessionBean.getId() == null ) {
			return "redirect:../login";
		}
		
		String user = loginDataInfo.getId();
		
		//daily_reportを取得
		List<DailyReport> list = workReportsListService.selectDailyReport(workReportsListForm, user);
		
		//取得した値をDtoに詰め替える
		List <WorkReportsListDto> dtoList = setWorkReportsListDto(list);
		
		workReportsListForm.setDailyReportResult(dtoList);;
		
		loginDataInfo.setWorkReportsListDto(dtoList);
		model.addAttribute("loginDataInfo", loginDataInfo);

		// Modelオブジェクトに検索結果を格納
		model.addAttribute("workReportsListForm", workReportsListForm);
		model.addAttribute("workReportsListDto", dtoList);
		
		return "workreportslist/workReportsList";
		
	}
	
	//管理者画面からアクセスした場合に実行されるメソッド
	@RequestMapping(value="workreportslist/workReportsList/{id}")
	public String viewList(@PathVariable String id,@ModelAttribute LoginDataInfo loginDataInfo, WorkReportsListForm workReportsListForm, Model model) {
		ManagementPortalController managementPortalController = new ManagementPortalController();
		//newしたインスタンスにDI
		beanFactory.autowireBean(managementPortalController);
		//アクセス認証チェック
		if ( !managementPortalController.accessCheck() ) {
			return "redirect:/login";		
		}
		String user = id;
		ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
		managementPortalInfo.setId(user);
		List<ManagementPortalInfo> userData = managementPortalService.selectUserData(managementPortalInfo);
		if ( userData.size() == 1 ) {
			managementPortalInfo = userData.get(0);
		}
		//報告書主を明示
		model.addAttribute("userName", "作　業　報　告　書　　　氏名：" + managementPortalInfo.getUserName());
		
		//daily_reportを取得
		List<DailyReport> list = workReportsListService.selectDailyReport(workReportsListForm, user);
				
		//取得した値をDtoに詰め替える
		List <WorkReportsListDto> dtoList = setWorkReportsListDto(list);
				
		workReportsListForm.setDailyReportResult(dtoList);;
				
		loginDataInfo.setWorkReportsListDto(dtoList);
		model.addAttribute("loginDataInfo", loginDataInfo);

		// Modelオブジェクトに検索結果を格納
		model.addAttribute("workReportsListForm", workReportsListForm);
		model.addAttribute("workReportsListDto", dtoList);
		return "workreportslist/workReportsList";
	}

	/**
	 * 更新画面へ遷移
	 * @return
	 */
	@RequestMapping(value="workreportslist/workReportsList", params = "update")
	public String viewUpdate(
			@ModelAttribute WorkReportsListForm workReportsListForm,
			BindingResult result, Model model
			) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		String workingDate = sdf.format(date);
		workReportsListForm.setWorkingDate(workingDate);

		return "workReport/update";
	}

	/**
	 * 
	 * @param workReportsListForm
	 * @param registrationForm
	 * @param loginDataInfo
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update", params = "updateConf")
	public String updateConf(@ModelAttribute("workReportsListForm") WorkReportsListForm workReportsListForm, RegistrationForm registrationForm,LoginDataInfo loginDataInfo,
			BindingResult result, Model model) {

		registrationForm.setId(loginDataInfo.getId());
		workReportsListService.updateDailyReport(registrationForm);
		
		if (registrationForm==null || ObjectUtils.isEmpty(registrationForm)) {
			return "workReport/updateConf";
		}
		return "workReport/updateComp";

	}
	
	/**
	 * 更新完了画面
	 * @param loginDataInfo
	 * @param workReportsListForm
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update", params = "updateComp")
	public String updateComp(@ModelAttribute WorkReportsListForm workReportsListForm, //RegistrationForm registrationForm, 
			BindingResult result, Model model) {
		return "workReport/updateComp";
	}
	
	/**
	 * 登録画面へ
	 * @return
	 */
	@RequestMapping(value="workreportslist/workReportsList", params = "insert")
	public String toMenu() {
		return "redirect:/workreportslist/registration";
	}


	/**
	 * ログアウトへ
	 * @param request
	 * @param response
	 * @param sessionStatus
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="workreportslist/workReportsList", params = "logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, SessionStatus sessionStatus, ModelAndView mav) throws Exception {
		loginController.logout(request, response, sessionStatus);
		mav.setViewName("redirect:../login");
		return mav;
	}
	
	/*
	public ModelAndView toLogout(@ModelAttribute LoginDataInfo loginDataInfo, SessionStatus sessionStatus, ModelAndView mav) {
		
		mav.addObject("dispUserName", loginDataInfo);
		
		//セッション破棄処理
		sessionStatus.setComplete();
		mav.setViewName("logout");
		return mav;
	}
	*/
	
	/**
	 * ダウンロード
	 * @param loginDataInfo
	 * @return
	 */
	@RequestMapping(value="/list", params = "download")
	public String download(@ModelAttribute("workReportsListForm") WorkReportsListForm workReportsListForm, 
			LoginDataInfo loginDataInfo, BindingResult result, Model model) {
		
		return "workReport/downloadConf";

	}


	/**
	 * 画面表示値をセット
	 * @param list
	 * @return
	 */
	private List <WorkReportsListDto> setWorkReportsListDto(List<DailyReport> list) {
		return workReportsListService.setWorkReportsListDto(list);
	}
	
	
}
