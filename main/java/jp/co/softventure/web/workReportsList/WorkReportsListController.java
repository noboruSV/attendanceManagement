package jp.co.softventure.web.workReportsList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.service.WorkReportsListService;
import jp.co.softventure.web.login.LoginForm;
import jp.co.softventure.web.menu.MenuForm;

@Controller
public class WorkReportsListController {

	@Autowired
	private WorkReportsListService workReportsListService;
	
	@ModelAttribute("workReportsListForm")
	public WorkReportsListForm setForm(Model model) {
		return new WorkReportsListForm();
	}

	@ModelAttribute(value="loginForm")
	public LoginForm loginForm(LoginForm loginForm) {
		return new LoginForm();
	}
	
	@RequestMapping(value="/workReportsList")
	public String workReportsList(
			@ModelAttribute("workReportsListForm")
			WorkReportsListForm workReportsListForm, 
			LoginForm loginForm,
			MenuForm menuForm,
			Model model) {
		
		String user = loginForm.getId();

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	    String date = sdf.format(new Date());

		//daily_reportを取得
		List<DailyReport> list = workReportsListService.selectDailyReport(workReportsListForm, user, date);
		
		//取得した値をDtoに詰め替える
		List <WorkReportsListDto> dtoList = setWorkReportsListDto(list);
		
		workReportsListForm.setDailyReportResult(dtoList);;
		

		// Modelオブジェクトに検索結果を格納
//		model.addAttribute("dailyReport", list);
		model.addAttribute("workReportsListForm", workReportsListForm);
		
		return "workReportsList/workReportsList";
		
	}

	/**
	 * 更新
	 * @return
	 */
	@RequestMapping(value="/work_reports_list", params = "upDate")
	public String toUpdate(
			@ModelAttribute("workReportsListForm")
			WorkReportsListForm workReportsListForm, 
			Model model) {
		
		String user = "0001";
		String date= "20200901";

		//daily_reportを取得
		List<DailyReport> list = workReportsListService.selectDailyReport(workReportsListForm, user, date);
		
		//取得した値をDtoに詰め替える
		List <WorkReportsListDto> dtoList = setWorkReportsListDto(list);
		
		workReportsListForm.setDailyReportResult(dtoList);;

		return "workReportsList/workReportsList";
	}
	
	
	/**
	 * メニュー画面へ
	 * @return
	 */
	@RequestMapping(value="/workReportsList", params = "backMenu")
	public String toMenu(
			) {
		return "forward:menu";
	}

	/**
	 * ログアウトへ
	 * @return
	 */
	@RequestMapping(value="/workReportsList", params = "logout")
	public String toLogout() {
		return "logout/logout";
	}
	
	/**
	 * 画面表示値をセット
	 * @param list
	 * @return
	 */
//	private List <WorkReportsListDto> setWorkReportsListDto(WorkReportsListDto dto, List<DailyReport> list) {
	private List <WorkReportsListDto> setWorkReportsListDto(List<DailyReport> list) {
//		return workReportsListService.setWorkReportsListDto(dto, list);
		return workReportsListService.setWorkReportsListDto(list);
	}
}
