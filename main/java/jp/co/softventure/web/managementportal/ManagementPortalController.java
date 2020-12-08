package jp.co.softventure.web.managementportal;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;
import org.springframework.web.bind.support.SessionStatus;

import jp.co.softventure.bean.SessionBean;
import jp.co.softventure.domain.ManagementPortalInfo;
import jp.co.softventure.service.ManagementPortalService;
import jp.co.softventure.web.login.LoginController;

@Controller
@SessionAttributes("scopedTarget.sessionBean")
public class ManagementPortalController {

	@Autowired
	private ManagementPortalService service;
	
	@Autowired
	private SessionBean sessionBean;
	
	@Autowired
	private SessionBean userInfoHolder;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	LoginController loginController;
	
	@ModelAttribute(value = "managementPortal")
	public ManagementPortalForm managementPortal() {
		return new ManagementPortalForm();
	}
	
	@ModelAttribute(value = "managementPortalInsert")
	public ManagementPortalForm managementPortalInsert() {
		return new ManagementPortalForm();
	}
	
	@ModelAttribute(value = "managementPortalUpdate")
	public ManagementPortalForm managementPortalUpdate() {
		return new ManagementPortalForm();
	}
	
	@ModelAttribute(value = "managementPortalDelete")
	public ManagementPortalForm managementPortalDelete() {
		return new ManagementPortalForm();
	}
	
	//「管理者画面」に社員一覧を表示 
	@RequestMapping(value = "managementportal/managementPortal")
	public ModelAndView dispUserNames(@ModelAttribute("managementPortal") ManagementPortalForm managementPortalForm, ModelAndView mav) {
		//アクセス認証チェック
		if ( !accessCheck() ) {
			mav.setViewName("redirect:../login");
			return mav;
		}
		ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
		List<ManagementPortalInfo> nonmemberList = new ArrayList<>();
		List<ManagementPortalInfo> toRemove = new ArrayList<>();
		List<ManagementPortalInfo> memberlist = service.selectUserData(managementPortalInfo);
		memberlist = service.selectLoginData(managementPortalInfo);
		//もし退職者がいる場合、削除予定リストに追加する。
		//employmentStatus: 1 ⇒ 在職中
		//                  2 ⇒ 退職済み
		//                  3 ⇒ 退職後30日経過(データ削除可能)
		for ( ManagementPortalInfo nonmember : memberlist ) {
			if ( !nonmember.getEmploymentStatus().equals("1") ) {
				nonmemberList.add(nonmember);
				toRemove.add(nonmember);
				mav.addObject("isNonmembers", true);
				mav.addObject("nonmembers", nonmemberList);
				//削除予定者の削除可能日を計算
				String deletableDate = calcDeletableDate(nonmember.getRecUpdtDt());
				mav.addObject("deletableDate", deletableDate);
				//削除可能日を経過していたら、employmentStatusを3に変更する。
				if ( deletableDate.equals("(削除可能)") && !nonmember.getEmploymentStatus().equals("3") ) {
					nonmember.setEmploymentStatus("3");
					service.updateEmploymentStatus(nonmember);
				}
			}
		}
		memberlist.removeAll(toRemove);
		mav.addObject("members", memberlist);
		mav.setViewName("managementportal/managementPortal");
		return mav;
	} 
	
	//「管理者画面」で「追加」押下時の処理
	@RequestMapping(value = "/managementportal/managementPortal", params = "add_btn")
	public ModelAndView toManagementPortalInsert(@ModelAttribute("managementPortalInsert") ManagementPortalForm managementPortalForm, ModelAndView mav) {
		//最新のIDを取得して、それに1を加えてフォームに表示
		List<ManagementPortalInfo> list = service.selectId();
		if ( list.size() == 1 ) {
			int latestId = Integer.parseInt(list.get(0).getId());
			String newId = String.valueOf(latestId + 1);
			for ( ; newId.length() < 5; ) {
				newId = "0" + newId;
			}
			managementPortalForm.setId(newId);
		}
		mav.setViewName("managementportal/managementPortalInsert");
		return mav;
	}
	
	//「社員情報新規追加画面」で「生成」押下時の処理
	@RequestMapping(value = "/managementPortalInsert", params ="pwgen_btn")
	public ModelAndView dispGeneratedPassword(@ModelAttribute("managementPortalInsert")ManagementPortalForm managementPortalForm, ModelAndView mav) {
		String password = passwordGenerator();
		managementPortalForm.setPassword(password);
		mav.setViewName("managementportal/managementPortalInsert");
		return mav;
	}
	
	//「社員情報新規追加画面」で「追加」押下時の処理
	@RequestMapping(value = "/managementPortalInsert", params ="insert_btn")
	public ModelAndView insert(
	@ModelAttribute("managementPortalInsert") @Validated ManagementPortalForm managementPortalForm, BindingResult result,
	@RequestParam(value="admin",required=false) boolean admin, ModelAndView mav) {
		//ID重複登録チェック
		ManagementPortalInfo managementPortalInfoChecker = new ManagementPortalInfo();
		BeanUtils.copyProperties(managementPortalForm, managementPortalInfoChecker);
		List<ManagementPortalInfo> list = service.selectUserData(managementPortalInfoChecker);
		if ( list.size() == 1 &&  StringUtils.equals(list.get(0).getId(), managementPortalForm.getId()) ) {
			mav.addObject("errmsg", "既に同じIDで登録済みです。");
			mav.setViewName("managementportal/managementPortalInsert");
			return mav;
		}
		ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
		managementPortalForm.setAdministrativeRight(admin);
		managementPortalForm.setRecRgstUser(sessionBean.getId());
		BeanUtils.copyProperties(managementPortalForm, managementPortalInfo);
		if ( !result.hasErrors() ) {
			//パスワードのハッシュ化
			managementPortalInfo.setPassword(passwordEncoder.encode(managementPortalInfo.getPassword()));
			service.insertLoginData(managementPortalInfo);
			sendEmail(managementPortalForm.getEmailAddress(), managementPortalForm.getId(), managementPortalForm.getPassword());
			mav.setViewName("redirect:managementportal/managementPortal?finish");
		} else {
			mav.setViewName("managementportal/managementPortalInsert");
		}
		return mav;
	}
	
	//「社員情報新規追加画面」で「追加」押下処理後のPRGメソッド
	@RequestMapping(value = "/managementportalInsert", params = "finish")
	public String finishInsert() {
		return "managementportal/managementPortal";
	}
	
	//「管理者画面」で「修正」押下時の処理
	//「社員情報修正画面」にユーザー情報を表示
	@RequestMapping(value = "managementportal/managementPortal", params ="revise_btn")
	public ModelAndView toManagementPortalUpdate(@ModelAttribute("managementPortalUpdate") ManagementPortalForm managementPortalForm, ModelAndView mav) {
		userInfoHolder.setUserName(managementPortalForm.getUserName());
		if ( userInfoHolder.getUserName() == null ) {
			mav.addObject("errmsg", "※チェックボックスにチェックを入れて下さい。");
			mav = dispUserNames(managementPortalForm, mav);
		} else {
			ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
			managementPortalInfo.setUserName(userInfoHolder.getUserName());
			List<ManagementPortalInfo> list = service.selectUserData(managementPortalInfo);
			if ( list.size() == 1 ) {
				managementPortalInfo = list.get(0);
				sessionBean.setRecUpdtDt(managementPortalInfo.getRecUpdtDt());
				BeanUtils.copyProperties(managementPortalInfo, managementPortalForm);
				//ハッシュ化されたパスワードをフォームに非表示にする。
				managementPortalForm.setPassword("");
				mav.setViewName("managementportal/managementPortalUpdate");
			} else {
				mav.setViewName("managementportal/managementPortal");
			}
		}
		return mav;
	}
	
	//「社員情報新規追加画面」で「生成」押下時の処理
	@RequestMapping(value = "/managementPortalUpdate", params ="pwgen_btn")
	public ModelAndView dispGeneratedPasswordForUpdate(@ModelAttribute("managementPortalUpdate")ManagementPortalForm managementPortalForm, ModelAndView mav) {
		String password = passwordGenerator();
		managementPortalForm.setPassword(password);
		mav.setViewName("managementportal/managementPortalUpdate");
		return mav;
	}
		
	//「社員情報修正画面」で「修正」押下時の処理
	@RequestMapping(value = "/managementPortalUpdate", params ="update_btn")
	public ModelAndView update(
	@ModelAttribute("managementPortalUpdate") @Validated ManagementPortalForm managementPortalForm, BindingResult result,
	@RequestParam(value = "admin", required = false) boolean admin, ModelAndView mav) {
		ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
		managementPortalForm.setAdministrativeRight(admin);
		managementPortalForm.setRecUpdtUser(sessionBean.getId());
		//managementPortalForm.setId(managementPortalForm.getId().substring(managementPortalForm.getId().length() - 6));
		BeanUtils.copyProperties(managementPortalForm, managementPortalInfo);
		if ( !result.hasErrors() ) {
			//パスワードのハッシュ化
			managementPortalInfo.setPassword(passwordEncoder.encode(managementPortalInfo.getPassword()));
			//排他制御
			boolean processingResult = service.updateLoginData(managementPortalInfo);
			if ( !processingResult ) {
				mav.addObject("errmsg", "処理が失敗しました。処理中に情報が更新された可能性があります。");
				mav.setViewName("managementportal/managementPortalInsert");
			} else {
				sendEmail(managementPortalForm.getEmailAddress(), managementPortalForm.getId(), managementPortalForm.getPassword());
				mav.setViewName("redirect:managementportal/managementPortal?finish");
			}
		} else {
			mav.setViewName("managementportal/managementPortalUpdate");
		}
		return mav;
	}
	
	//「社員情報修正画面」で「修正」押下処理後のPRGメソッド
	@RequestMapping(value = "/managementportaUpdate", params = "finish")
	public String finishUpdate() {
		return "managementportal/managementPortal";
	}
	
	//「管理者画面」で「削除」押下時の処理
	//「社員情報削除画面」にユーザー情報を表示
	@RequestMapping(value = "managementportal/managementPortal", params ="erase_btn")
	public ModelAndView toManagementPortalDelete(@ModelAttribute("managementPortalDelete") ManagementPortalForm managementPortalForm, ModelAndView mav) {
		userInfoHolder.setUserName(managementPortalForm.getUserName());
		if ( userInfoHolder.getUserName() == null ) {
			mav.addObject("errmsg", "※チェックボックスにチェックを入れて下さい。");
			mav = dispUserNames(managementPortalForm, mav);	
		} else {
			ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
			managementPortalInfo.setUserName(userInfoHolder.getUserName());
			List<ManagementPortalInfo> list = service.selectUserData(managementPortalInfo);
			if ( list.size() == 1 ) {
				managementPortalInfo = list.get(0);
				BeanUtils.copyProperties(managementPortalInfo, managementPortalForm);
				//employmentStatusが1ならば、該当メンバーを削除予定リストに追加する。
				if ( managementPortalForm.getEmploymentStatus().equals("1") ) {
					managementPortalInfo.setEmploymentStatus("2");
					service.updateEmploymentStatus(managementPortalInfo);
					mav = dispUserNames(managementPortalForm, mav);
				//employmentStatusが2ならば、メッセージのみを表示する。
				} else if ( managementPortalForm.getEmploymentStatus().equals("2") ){
					mav.addObject("errmsg", "※削除可能日に達していません。");
					mav = dispUserNames(managementPortalForm, mav);
				//employmentStatusが3ならば、「社員情報削除画面」に遷移する。
				} else if ( managementPortalForm.getEmploymentStatus().equals("3") ) {
					mav.setViewName("managementportal/managementPortalDelete");
				} else {
					mav.addObject("errmsg", "※ステータスが異常です。");
					mav = dispUserNames(managementPortalForm, mav);
				}
			} else {
				mav.setViewName("managementportal/managementPortal");
			}
		}
		return mav;
	}
	
	//「社員情報削除画面」で「削除」押下時の処理
	@RequestMapping(value = "managementportal/managementPortal", params ="delete_btn")
	public void delete(
	@ModelAttribute("managementPortalDelete") ManagementPortalForm managementPortalForm, HttpServletResponse response, ModelAndView mav) 
	throws IOException {
		ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
		BeanUtils.copyProperties(managementPortalForm, managementPortalInfo);
		exportExcelFile(response, managementPortalInfo, true);
		//mav.setViewName("redirect:managementportal/managementPortal");
		//return mav;
	}
	
	//削除予定者リストが存在する場合、「管理者画面」で「戻す」押下時の処理
	@RequestMapping(value = "managementportal/managementPortal", params ="restore_btn")
	public ModelAndView restoreResignee(@ModelAttribute("managementPortal") ManagementPortalForm managementPortalForm, ModelAndView mav) {
		userInfoHolder.setUserName(managementPortalForm.getUserName());
		if ( userInfoHolder.getUserName() == null ) {
			mav.addObject("errmsg", "※チェックボックスにチェックを入れて下さい。");
			mav = dispUserNames(managementPortalForm, mav);	
		} else {
			ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
			managementPortalInfo.setUserName(userInfoHolder.getUserName());
			List<ManagementPortalInfo> list = service.selectUserData(managementPortalInfo);
			if ( list.size() == 1 ) {
				managementPortalInfo = list.get(0);
				BeanUtils.copyProperties(managementPortalInfo, managementPortalForm);
				managementPortalInfo.setEmploymentStatus("1");
				service.updateEmploymentStatus(managementPortalInfo);
				mav = dispUserNames(managementPortalForm, mav);	
			}
		}
		return mav;
	}
	
	//「管理者画面」で「出力」押下時の処理
	@RequestMapping(value = "managementportal/managementPortal", params ="export_btn")
	public ModelAndView export(
	@ModelAttribute("managementPortal") ManagementPortalForm managementPortalForm, HttpServletResponse response, ModelAndView mav)
	throws IOException {
		userInfoHolder.setUserName(managementPortalForm.getUserName());
		if ( userInfoHolder.getUserName() == null ) {
			mav.addObject("errmsg", "※チェックボックスにチェックを入れて下さい。");
			dispUserNames(managementPortalForm, mav);
			return mav;
		} else {
			ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();
			managementPortalInfo.setUserName(userInfoHolder.getUserName());
			exportExcelFile(response, managementPortalInfo, false);
			return null;
		}
		//dispUserNames(managementPortalForm, mav);
		//return mav;
	}
	
	
	//「管理者画面」で「ログアウト」押下時の処理
	@RequestMapping(value = "managementportal/managementPortal", params ="logout_btn")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, SessionStatus sessionStatus, ModelAndView mav) throws Exception {
		loginController.logout(request, response, sessionStatus);
		mav.setViewName("redirect:../login");
		return mav;
	}
	/*
		sessionStatus.setComplete();
		Cookie[] cookies = request.getCookies();
	    for ( Cookie cookie : cookies ) {
	        if ( cookie.getName().equals("SVAMSALID") ) {
	        	cookie.setMaxAge(0);
	        	cookie.setValue("");
	            response.addCookie(cookie);
	        }
	    }
	    mav.setViewName("managementportal/managementPortal");
	    //mav.setViewName("redirect:../login");
		return mav;
	*/
	
	//エクセル出力処理(第三引数によって"通常の出力処理"または"削除時の出力処理"に分岐)
	public void exportExcelFile(HttpServletResponse response, ManagementPortalInfo managementPortalInfo, boolean deletingMode) {
		List<ManagementPortalInfo> list = service.selectUserData(managementPortalInfo);
		if ( list.size() == 1 ) {
			managementPortalInfo = list.get(0);
			list = service.selectWorkingReport(managementPortalInfo);
			//ワークブック作成
			try ( Workbook workBook = new XSSFWorkbook(); OutputStream outputStream = response.getOutputStream(); ) {
				ExcelBuilder excelBuilder = new ExcelBuilder();
				String encodedFileName = excelBuilder.createExcelFile(workBook, list, managementPortalInfo);
				//レスポンスヘッダにエクセルファイル情報を付加
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader("content-disposition", "attachment; filename*=UTF-8''" + encodedFileName);
				response.setCharacterEncoding("UTF-8");
				//削除モード時にデータベースから削除
				if ( deletingMode ) {
					service.deleteUserData(managementPortalInfo);
					//クッキー作成(削除モード時にクッキーをトリガーとして、JavaScriptで画面遷移できるようにするため)
					Cookie cookie = new Cookie("complete", "1");
					cookie.setMaxAge(60);
					response.addCookie(cookie);
				}
				workBook.write(outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//削除予定者の削除可能日を計算
	public String calcDeletableDate(Timestamp recUpdtDt) {
		Date date1 = new Date();
		Date date2 = new Date(recUpdtDt.getTime());
		String str = diffDays(date1, date2);
		return str;
	}
	
	//ふたつの日付を差分するメソッド
	public String diffDays(Date date1,Date date2) {
	    long datetime1 = date1.getTime();
	    long datetime2 = date2.getTime();
	    long oneDateTime = 1000 * 60 * 60 * 24;
	    long diffDays = (datetime1 - datetime2) / oneDateTime;
	    String str;
	    if ( diffDays >= 30 ) {
	    	 str = "(削除可能)";
	    } else {
	    	 str = "(あと" + String.valueOf(30 - diffDays) + "日で削除可能)";
	    }
	    return str;
	}
	
	//アクセス認証チェック
	public boolean accessCheck() {
		boolean accessFlag = true;
		ManagementPortalInfo managementPortalInfo = new ManagementPortalInfo();	
		managementPortalInfo.setId(sessionBean.getId());
		List<ManagementPortalInfo> list = service.selectUserData(managementPortalInfo);
		if ( sessionBean.getId() == null || !(list.size() == 1) ) {
			accessFlag = false;
		} else {
			managementPortalInfo = list.get(0);
			if ( !managementPortalInfo.getAdministrativeRight() ) {
				accessFlag = false;
			}
		}
		return accessFlag;
	}
	
	//メール送信
	@Async
	public void sendEmail(String emailAddress, String id, String password) {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setTo(emailAddress);
		smm.setSubject("日報システム ― アカウント情報");
		smm.setText("アカウント情報:"+ "\r\n-ID: " + id + "\r\n-パスワード: " + password + "\r\nログイン: URLリンク");
		mailSender.send(smm);
	}
	
	//パスワード生成
	public static String passwordGenerator() {
		List<CharacterRule> rules = Arrays.asList(
			new CharacterRule(EnglishCharacterData.UpperCase, 1),
			new CharacterRule(EnglishCharacterData.LowerCase, 1),
			new CharacterRule(EnglishCharacterData.Digit, 1)
		);
		PasswordGenerator generator = new PasswordGenerator();
		String password = generator.generatePassword(10, rules);
		return password;
	}
	
}