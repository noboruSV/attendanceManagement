package jp.co.softventure.web.workreportslist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.dto.WorkReportsListDto;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.service.WorkReportsListService;

@Controller
@SessionAttributes({"scopedTarget.sessionBean", "loginDataInfo", "workReportsListForm"})
public class WorkReportsListController {

	@Autowired
	private WorkReportsListService workReportsListService;
	
	@Autowired
	public SessionBean sessionBean;
	
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
	@RequestMapping(value="/list")
	public String viewList(@ModelAttribute LoginDataInfo loginDataInfo, WorkReportsListForm workReportsListForm,
			BindingResult result, Model model) {
		
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

	/**
	 * 更新画面へ遷移
	 * @return
	 */
	@RequestMapping(value="/list", params = "update")
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
	 * メニュー画面へ
	 * @return
	 */
	@RequestMapping(value="/list", params = "backMenu")
	public String toMenu() {
		return "menu/menu";
	}


	/**
	 * ログアウトへ
	 * @param loginDataInfo
	 * @param sessionStatus
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="/list", params = "logout")
	public ModelAndView toLogout(@ModelAttribute LoginDataInfo loginDataInfo, SessionStatus sessionStatus, ModelAndView mav) {
		
		mav.addObject("dispUserName", loginDataInfo);
		
		//セッション破棄処理
		sessionStatus.setComplete();
		mav.setViewName("logout");
		return mav;
	}
	
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
