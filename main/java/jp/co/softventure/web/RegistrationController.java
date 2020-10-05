package jp.co.softventure.web;

import java.util.List;
import java.time.*;
import java.time.format.*;
import java.sql.Date;
import java.sql.Time;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.domain.*;
import jp.co.softventure.service.RegistrationService;
import jp.co.softventure.web.login.LoginForm;

@Controller
public class RegistrationController {
	
	@Autowired
	private RegistrationService service;
	
	@Autowired
	public SessionBean sessionBean;
	
	@ModelAttribute(value = "setForm")
	public RegistrationForm setForm() {
		return new RegistrationForm();
	}
	
	@ModelAttribute(value = "loginForm")
	public LoginForm setLoginForm() {
		return new LoginForm();
	}

	//DB登録メソッド
	@RequestMapping(value = "/registcomp", params = "insert_btn")
	public ModelAndView insert(@ModelAttribute("setForm") @Validated RegistrationForm registrationForm, BindingResult result, ModelAndView mav) {
		RegistrationInfo registrationInfo = new RegistrationInfo();
		//ログイン時のIDを取得
		registrationForm.setId(sessionBean.getId());
		//更新ボタン等を押下してIDが初期化されていた場合、ログイン画面に強制送還
		if ( registrationForm.getId() == null ) {
			mav.clear();
			mav.setViewName("redirect:/login");
			return mav;
		}
		//モデルオブジェクト用のString型からドメインクラス用のDate型に変換してドメインクラスのインスタンスにセット
		strToDate(registrationForm,registrationInfo);
		//String型からTime型に変換してドメインクラスにセット
		strToTime(registrationForm, registrationInfo, "workingStartTime");
		strToTime(registrationForm, registrationInfo, "workingEndTime");
		//モデルオブジェクトからドメインクラスのインスタンスにデータをコピー
		BeanUtils.copyProperties(registrationForm, registrationInfo);
		//バリデーションチェック
		if ( !result.hasErrors() ) {
			//該当IDのかつ入力日付のデータをDBから取得
			RegistrationInfo registrationInfoChecker = new RegistrationInfo();
			List<RegistrationInfo> list = service.selectWorkingDate(registrationInfo);
			if ( list.size() == 1 ) {
				for ( int i = 0; i < list.size(); i++ ) {
					registrationInfoChecker = list.get(i);
				}
				//同一日付のデータがある場合、エラーメッセージを表示して再度登録画面へ
				if ( StringUtils.equals(registrationInfoChecker.getWorkingDate(), registrationInfo.getWorkingDate()) ) {
					mav.addObject("errmsg", "同じ日付で登録済みです。");
					mav.setViewName("/registration");
				}
			//同一日付のデータがないかつバリデーションチェック時にエラーがないならば、登録して完了画面に遷移
			} else {
				service.insertRegistrationInfo(registrationInfo);
				mav.setViewName("redirect:/registcomp?finish");
			} 
		//バリデーションチェック時にエラーがあれば、エラーメッセージを表示して再度登録画面へ
		} else {
				mav.addObject("errmsg","登録に失敗しました。");
				mav.setViewName("/registration");
		}
		return mav;
	}
	
	//PRGメソッド
	@RequestMapping(value = "/registcomp", params = "finish")
	public String prg() {
		return "/registcomp";
	}
	
	//workingDate,workingStartTime,workingEndTime表示メソッド
	@RequestMapping(value = "/registration")
	public ModelAndView displayWorkingDateAndTime(@ModelAttribute("setForm") RegistrationForm registrationForm, ModelAndView mav) {
		RegistrationInfo registrationInfo = new RegistrationInfo();
		//ログイン画面で入力されたIDをドメインクラスのフィールドに引き渡し
		registrationInfo.setId(sessionBean.getId());
		//該当IDのかつ現在日付の勤務開始時刻と勤務終了時刻を取得
		List<RegistrationInfo> list = service.selectWorkingTime(registrationInfo);
		//もし登録データがあれば、それをドメインクラスのインスタンスからコピーしてモデルオブジェクトにセット
		if ( list.size() == 1 ) {
			for ( int i = 0; i < list.size(); i++ ) {
				registrationInfo = list.get(i);
				BeanUtils.copyProperties(registrationInfo, registrationForm);
				//ドメインクラス用のTime型からモデルオブジェクト用のString型に変換
				timeToStr(registrationInfo, registrationForm, "workingStartTime");
				timeToStr(registrationInfo, registrationForm, "workingEndTime");
			}
		} else {
			//登録データがなければ、デフォルト出退勤時刻をモデルオブジェクトにセット
			setRegularWorkingTime(registrationForm);
		}
		//現在日付を取得してモデルオブジェクトにセット
		registrationForm.setWorkingDate(getCurrentDate());
		mav.setViewName("registration");
		return mav;
	}
	
	//現在日付取得メソッド
	public String getCurrentDate() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String str = LocalDate.now().format(f);
		return str;
	}
	
	//就業規則上の出退勤時間の取得メソッド(ついでにモデルオブジェクトにセット）
	public void setRegularWorkingTime(RegistrationForm registrationForm) {
		registrationForm.setWorkingStartTime(LocalTime.parse("09:00", DateTimeFormatter.ofPattern("HH:mm")).toString());
		registrationForm.setWorkingEndTime(LocalTime.parse("17:45", DateTimeFormatter.ofPattern("HH:mm")).toString());
	}
	
	//StringからDateへの変換メソッド(ついでにドメインクラスのインスタンスにセット)
	public void strToDate(RegistrationForm registrationForm, RegistrationInfo registrationInfo) {
		registrationInfo.setWorkingDate(Date.valueOf(registrationForm.getWorkingDate()));
	}
	
	//StringからTimeへの変換メソッド(サフィックスに:00を付加し、Time型に変換したあとでついでにドメインクラスのインスタンスにセット)
	public void strToTime(RegistrationForm registrationForm, RegistrationInfo registrationInfo, String str) {
		switch ( str ) {
			case "workingStartTime":
				String workingStartTime = registrationForm.getWorkingStartTime();
				Time workingStartTimeTypeTime = Time.valueOf(workingStartTime.replace(workingStartTime,workingStartTime + ":00"));
				registrationInfo.setWorkingStartTime(workingStartTimeTypeTime);
				break;
			
			case "workingEndTime":
				String workingEndTime = registrationForm.getWorkingEndTime();
				Time workingEndTimeTypeTime = Time.valueOf(workingEndTime.replace(workingEndTime, workingEndTime + ":00"));
				registrationInfo.setWorkingEndTime(workingEndTimeTypeTime);
				break;
		}
	}
	
	//TimeからStringへの変換メソッド(String型に変換し、サフィックスの:00を取り除いたあとでついでにモデルオブジェクトにセット)
	public void timeToStr(RegistrationInfo registrationInfo, RegistrationForm registrationForm, String str) {
		switch ( str ) {
			case "workingStartTime":
				Time workingStartTimeTypeTime = registrationInfo.getWorkingStartTime();
				registrationForm.setWorkingStartTime(workingStartTimeTypeTime.toString().substring(0, 5));
			
			case "workingEndTime":
				Time workingEndTimeTypeTime = registrationInfo.getWorkingEndTime();
				registrationForm.setWorkingEndTime(workingEndTimeTypeTime.toString().substring(0, 5));
				break;
		}
	}
	
}