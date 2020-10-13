package jp.co.softventure.web; 
 
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller; 
import org.springframework.validation.BindingResult; 
import org.springframework.validation.annotation.Validated; 
import org.springframework.web.bind.annotation.ModelAttribute; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import jp.co.softventure.bean.SessionBean;

import jp.co.softventure.domain.RegistrationInfo;
import jp.co.softventure.domain.WorkingReport;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.service.DBLoginDataService;
import jp.co.softventure.service.DBService;
import jp.co.softventure.service.RegistrationService;
import jp.co.softventure.web.login.LoginForm; 
 
@Controller 
@SessionAttributes("scopedTarget.sessionBean")
public class RegistrationController2 { 
	@Autowired
	private DBService dbService;
	@Autowired
	private RegistrationService service;
    @Autowired
	public SessionBean sessionBean;
    @Autowired
	private DBLoginDataService dbLoginDataService;
 
    @ModelAttribute(value = "registrationForm") 
    public RegistrationForm setForm() { 
        return new RegistrationForm(); 
    } 
    @ModelAttribute(value = "loginForm")
    public LoginForm setLoginForm() {
    	return new LoginForm(); 
    }
    
 //初期画面
    @RequestMapping(value = "/registration2")
    public String input() { 
        return "regist/registration2"; 
    } 
 
    @RequestMapping(value = "/registration2Conf") 
    public String conf(@Validated @ModelAttribute("registrationForm") RegistrationForm form 
            , BindingResult result) { 
        // エラー判定 
        if(result.hasErrors()) { 
            return "regist/registration2"; 
        } 
        return "regist/registration2Conf"; 
    } 
 
    // 「戻る」ボタン押下時の処理メソッド 
    @RequestMapping(value = "/registration2End", params = "back_btn") 
    public String back(@ModelAttribute("registrationForm") RegistrationForm form) { 
        return "regist/registration2"; 
    } 
 
    // 「登録」ボタン押下時の処理メソッド 
    @RequestMapping(value = "/registration2End", params = "insert_btn") 
    public String toRegist(@ModelAttribute("registrationForm") RegistrationForm form,
    		
    		BindingResult result ,
    		SessionStatus sessionStatus, ModelAndView mav) {
		
         // データ登録に利用するドメインクラスのインスタンス化 
    	WorkingReport workingReport = new WorkingReport(); 
    
           // Formクラスの値をドメインクラスにコピー 
           BeanUtils.copyProperties(form, workingReport); 
           workingReport.setId(sessionBean.getId());
           workingReport.setWorking_date(form.getWorkingDate());
           workingReport.setWorking_time("00:00:00");
           workingReport.setWorking_start_time(form.getWorkingStartTime());
           workingReport.setWorking_end_time(form.getWorkingEndTime());   
           workingReport.setWorking_contents(form.getWorkingContents());  
           workingReport.setShokan("");
           workingReport.setShomu_sonota("");
           
           // データ登録を行うためのサービス処理呼び出し 
           dbService.insertWorkingReport(workingReport);
    
           // 完了画面へのリダイレクト 
           return "redirect:/registration2End?finish"; 
       } 
    
    //    public String insert(@ModelAttribute("insertForm") InsertForm form) { 
//        // データ登録に利用するドメインクラスのインスタンス化 
//    	WorkingReport workingReport = new WorkingReport(); 
// 
//        // Formクラスの値をドメインクラスにコピー 
//        BeanUtils.copyProperties(form, workingReport); 
//        workingReport.setId("1234");
//        workingReport.setWorking_date("2020-"+workingReport.getWorking_date());
//        workingReport.setWorking_time("00:00:00");
//        workingReport.setShokan("");
//        workingReport.setShomu_sonota("");
//        
//        // データ登録を行うためのサービス処理呼び出し 
//        service.insertWorkingReport(workingReport); 
// 
//        // 完了画面へのリダイレクト 
//        return "redirect:/insert-end?finish"; 
//    } 

    
    // リダイレクト後に呼び出される処理メソッド 
    @RequestMapping(value = "/registration2End", params = "finish") 
    public String endFinish() { 
        return "regist/registration2End"; 
    } 
    //ログイン画面戻る処理メソッド
    @RequestMapping(value = "/registration2", params = "menu")
    public String toMenu() {
    	return "menu/menu";
    }
} 