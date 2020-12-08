package jp.co.softventure.web;

import java.util.List;
import java.time.*;
import java.time.format.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	private SessionBean sessionBean;
	
	@ModelAttribute(value = "setForm")
	public RegistrationForm setForm() {
		return new RegistrationForm();
	}
	
	@ModelAttribute(value = "loginForm")
	public LoginForm setLoginForm() {
		return new LoginForm();
	}
	
	//DB登録メソッド
	@RequestMapping(value = "workreportslist/registcomp", params = "insert_btn")
	public ModelAndView insert(@ModelAttribute("setForm") @Validated RegistrationForm registrationForm, BindingResult result, ModelAndView mav) {
		RegistrationInfo registrationInfo = new RegistrationInfo();
		//ログイン時のIDを取得
		registrationForm.setId(sessionBean.getId());
		if ( registrationForm.getId() == null ) {
			mav.clear();
			mav.setViewName("redirect:../login");
			return mav;
		}
		//モデルオブジェクト用のString型からドメインクラス用のDate型に変換してドメインクラスのインスタンスにセット
		strIntoDate(registrationForm,registrationInfo);
		//String型からTime型に変換してドメインクラスにセット
		strIntoTime(registrationForm, registrationInfo, "workingStartTime");
		strIntoTime(registrationForm, registrationInfo, "workingEndTime");
		//ログインIDを登録者情報にセット
		registrationForm.setRecRgstUser(registrationForm.getId());
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
					mav.setViewName("workreportslist/registration");
				}
			//同一日付のデータがないかつバリデーションチェック時にエラーがないならば、登録して完了画面に遷移
			} else {
				service.insertRegistrationInfo(registrationInfo);
				mav.setViewName("redirect:/registcomp?finish");
			} 
		//バリデーションチェック時にエラーがあれば、エラーメッセージを表示して再度登録画面へ
		} else {
			mav.addObject("errmsg","登録に失敗しました。");
			mav.setViewName("workreportslist/registration");
		}
		return mav;
	}
	
	//PRGメソッド
	@RequestMapping(value = "/registcomp", params = "finish")
	public String prg() {
		return "redirect:workreportslist/workReportsList";
	}
	
	//workingDate,workingStartTime,workingEndTime,breakTime表示メソッド
	@RequestMapping(value = "workreportslist/registration")
	public ModelAndView dispRegisteredInfo(@ModelAttribute("setForm") RegistrationForm registrationForm, ModelAndView mav) {
		//直アクセス防止
		if( sessionBean.getId() == null ) {
			mav.setViewName("redirect:../login");
			return mav;
		}
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
				timeIntoStr(registrationInfo, registrationForm, "workingStartTime");
				timeIntoStr(registrationInfo, registrationForm, "workingEndTime");
			}
		} else {
			//登録データがなければ、デフォルト出退勤時刻と休憩時間をモデルオブジェクトにセット
			setRegularWorkingTime(registrationForm);	
		}
		//現在日付を取得してモデルオブジェクトにセット
		registrationForm.setWorkingDate(getCurrentDate());
		mav.setViewName("workreportslist/registration");
		return mav;
	}
	
	//現在日付取得メソッド
	public String getCurrentDate() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String str = LocalDate.now().format(f);
		return str;
	}
	
	//就業規則上の出退勤時刻と休憩時間の取得メソッド(ついでにモデルオブジェクトにセット）
	public void setRegularWorkingTime(RegistrationForm registrationForm) {
		registrationForm.setWorkingStartTime(LocalTime.parse("09:00", DateTimeFormatter.ofPattern("HH:mm")).toString());
		registrationForm.setWorkingEndTime(LocalTime.parse("17:45", DateTimeFormatter.ofPattern("HH:mm")).toString());
		registrationForm.setBreakTime("45");
	}
	
	//StringからDateへの変換メソッド(ついでにドメインクラスのインスタンスにセット)
	public void strIntoDate(RegistrationForm registrationForm, RegistrationInfo registrationInfo) {
		registrationInfo.setWorkingDate(Date.valueOf(registrationForm.getWorkingDate()));
	}
	
	//StringからTimeへの変換メソッド(サフィックスに:00を付加し、Time型に変換したあとでついでにドメインクラスのインスタンスにセット)
	/*
	public void strIntoTime(RegistrationForm registrationForm, RegistrationInfo registrationInfo, String str) {
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
	*/
	//StringからTimeへの変換メソッド(サフィックスに:00を付加し、Time型に変換したあとでついでにドメインクラスのインスタンスにセット)※リファクタリング後
	public void strIntoTime(RegistrationForm registrationForm, RegistrationInfo registrationInfo, String fieldName) {
		String convertedFieldName = fieldName.replace(fieldName, fieldName.substring(0,1).toUpperCase() + fieldName.substring(1));
		try {
			Method registrationFormMethod = registrationForm.getClass().getMethod("get" + convertedFieldName);
			fieldName = (String)registrationFormMethod.invoke(registrationForm);
			Time time = Time.valueOf(fieldName.replace(fieldName,fieldName + ":00"));
			Method registrationInfoMethod = registrationInfo.getClass().getMethod("set" + convertedFieldName, Time.class);
			registrationInfoMethod.invoke(registrationInfo, time);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
	/*
	//TimeからStringへの変換メソッド(String型に変換し、サフィックスの:00を取り除いたあとでついでにモデルオブジェクトにセット)
	public void timeIntoStr(RegistrationInfo registrationInfo, RegistrationForm registrationForm, String str) {
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
	*/
	//TimeからStringへの変換メソッド(String型に変換し、サフィックスの:00を取り除いたあとでついでにモデルオブジェクトにセット)※リファクタリング後
	public void timeIntoStr(RegistrationInfo registrationInfo, RegistrationForm registrationForm, String fieldName) {
		String convertedFieldName = fieldName.replace(fieldName, fieldName.substring(0,1).toUpperCase() + fieldName.substring(1));
		try {
			Method registrationInfoMethod = registrationInfo.getClass().getMethod("get" + convertedFieldName);
			Time time = (Time)registrationInfoMethod.invoke(registrationInfo);
			Method registrationFormMethod = registrationForm.getClass().getMethod("set" + convertedFieldName, String.class);
			registrationFormMethod.invoke(registrationForm, time.toString().substring(0, 5));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
	
}