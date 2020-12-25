package jp.co.softventure.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.domain.UpdateDailyReport;

import jp.co.softventure.dto.WorkReportsListDto;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.persistence.DailyReportMapper;
import jp.co.softventure.persistence.WorkingReportMapper;
import jp.co.softventure.web.RegistrationForm;
import jp.co.softventure.web.workreportslist.WorkReportsListForm;

@Service
public class WorkReportsListService {
	@Autowired
	private DBDailyReportService dBDailyReportService;
	
	@Autowired
	WorkingReportMapper mapper ;
	
	@Autowired
	DailyReportMapper dailyReportMapper;
	
	@Autowired
	MonthlyReportOutputService monthlyReportOutputService;
	
	/**
	 * 	 * 更新処理
	 * daily_reportDBにIDと日付が存在する場合、更新する
	 * @param registrationForm
	 */
	public void updateDailyReport(RegistrationForm registrationForm) {
		
		//IDと日付からdaily_reportDBを取得
		DailyReport dailyReport = new DailyReport();
		dailyReport.setId(registrationForm.getId());
		dailyReport.setWorkingDate(registrationForm.getWorkingDate());
		
		List<DailyReport> list = dailyReportMapper.selectIdDate(dailyReport);
		
		//取得できた場合、更新処理を進める
		if (list != null && list.size() != 0) {
			updateDB(registrationForm);
		}
	}
	
	
	/**
	 * ダウンロード
	 * @param workReportsListForm
	 * @param id
	 * @return
	 */
	public String createExcel(WorkReportsListForm workReportsListForm, LoginDataInfo loginDataInfo) {
		
		List<DailyReport> list = selectDailyReport(workReportsListForm, loginDataInfo.getId());
		List <WorkReportsListDto> dtoList = setWorkReportsListDto(list);
		String str = monthlyReportOutputService.createExcel(dtoList, loginDataInfo.getUserName());
		
		return str;
	}
	
	
	/**
	 * daily_reportを取得する
	 * @param wrlForm
	 * @param id
	 * @param month
	 * @return
	 */
	public List<DailyReport> selectDailyReport(WorkReportsListForm wrlForm, String id) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		Date dateS = new Date();
		Date dateE = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateE);;
		calendar.add(Calendar.MONTH, 1);
		dateE = calendar.getTime();
		
		DailyReport dailyReport = new DailyReport();
//		BeanUtils.copyProperties(wrlForm, dailyReport);
		dailyReport.setId(id);
		dailyReport.setWorking_date_s(sdf.format(dateS).concat("01"));
		dailyReport.setWorking_date_e(sdf.format(dateE).concat("01"));
		
		List<DailyReport> list = dBDailyReportService.searchDailyReport(dailyReport);
		
		return list;
	}
	
	
	/**
	 * 曜日取得
	 * @param ymd(yyyy-MM-dd形式)
	 * @return
	 */
	/**
	 */
	public String getYobi(String ymd){
		try{
			//曜日
			String yobi[] = {"(日)","(月)","(火)","(水)","(木)","(金)","(土)"};
			//日付チェック
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			ymd = ymd.replace("/","");
			ymd = ymd.replace("-","");
			sdf.parse(ymd);

			//年・月を取得する
			int y = Integer.parseInt(ymd.substring(0,4));
			int m = Integer.parseInt(ymd.substring(4,6))-1;
			int d = Integer.parseInt(ymd.substring(6,8));

			//取得した年月の最終年月日を取得する
			Calendar cal = Calendar.getInstance();
			cal.set(y, m, d);

			//YYYYMMDD形式にして変換して返す
			return yobi[cal.get(Calendar.DAY_OF_WEEK)-1];
	  }catch(Exception ex){
		  return "";
	  }
	}
	

	/**
	 * 取得した値をDtoにセット
	 * @param list
	 * @return
	 */
	public List <WorkReportsListDto> setWorkReportsListDto(List<DailyReport> list) {
		List<WorkReportsListDto> wrlDto = new ArrayList<>();
		//カレンダーをセット
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int lastDate = getLastDayOfMonth(calendar, year, month);
		
		for (int day=1; day<=lastDate; day++) {
			WorkReportsListDto dto = new WorkReportsListDto();
			//日付
			dto.setDate(String.valueOf(day));
			//曜日
			dto.setWeek(getWeek(calendar, year, month, day));
			
			for(int j=0; j < list.size(); j++) {
				if (StringUtils.equals(dto.getDate(), list.get(j).getWorkingDate().substring(8, 10))) {
					//作業内容
					dto.setWorkingContents(list.get(j).getWorkingContents());
					//開始時間
					String start = list.get(j).getWorkingStartTime().substring(0, 5);
					dto.setWorkingStartTime(start);
					//終了時間
					String end = endTime(list.get(j).getWorkingEndTime().substring(0, 5));
					dto.setWorkingEndTime(end);
					//勤務時間
					String dutyS = list.get(j).getWorkingDate()+start;
					String dutyE = list.get(j).getWorkingDate()+end;
					BigDecimal dutyTime = getDutyTime(dutyS,dutyE);
					dto.setDutyTime(dutyTime.toString()); 
					//otameshi
//					BigDecimal dutyTime = null;
//					String dutyTimes = getWorkingHour(list.get(j).getWorkingHour());
//					dto.setDutyTime(dutyTimes);
					//otameshi
					//休憩時間
					BigDecimal breakTime = getBreakTime(dutyTime);
					dto.setBreakTime(String.valueOf(breakTime));
					//作業時間
					BigDecimal workTime = dutyTime.subtract(breakTime);
					dto.setWorkTime(String.valueOf(workTime));
				}
			}
			wrlDto.add(dto);
		}
		
		
//いったんコメントアウト
//		for(int i=0; i < list.size(); i++) {
////			List<WorkReportsListDto>dto2 = new ArrayList<>();
//			//DBから取得した日付が画面日付と同じ場合セット
//			int value = Integer.parseInt(list.get(i).getWorkingDate().substring(8, 10))-1;
//			if (StringUtils.equals(wrlDto.get(value).getDate(), list.get(i).getWorkingDate().substring(8, 10))) {
////				WorkReportsListDto dto = new WorkReportsListDto();
//				//年
//				dto.setYear(list.get(i).getWorkingDate().substring(0, 4));
//				//日付
//				//dto.setDate(list.get(i).getWorkingDate().substring(5, 10));
//				//曜日
//				//dto.setWeek(getYobi(list.get(i).getWorkingDate()));
//				//作業内容
//				dto.setWorkingContents(list.get(i).getWorkingContents());
//				//開始時間
//				String start = list.get(i).getWorkingStartTime().substring(0, 5);
//				dto.setWorkingStartTime(start);
//				//終了時間
//				String end = list.get(i).getWorkingEndTime().substring(0, 5);
//				dto.setWorkingEndTime(end);
//				//勤務時間
//				String dutyS = list.get(i).getWorkingDate()+start;
//				String dutyE = list.get(i).getWorkingDate()+end;
//				
//				BigDecimal dutyTime = getDutyTime(dutyS,dutyE);
//				dto.setDutyTime(dutyTime.toString()); 
//				//休憩時間
//				BigDecimal breakTime = getBreakTime(dutyTime);
//				dto.setBreakTime(String.valueOf(breakTime));
//				//作業時間
//				BigDecimal workTime = dutyTime.subtract(breakTime);
//				dto.setWorkTime(String.valueOf(workTime));
////				wrlDto.set(value, dto);
//				wrlDto.add(dto);
//			}
//		}
//いったんコメントアウト
		return wrlDto;
	}
	/**
	 * 
	 * @param endTime
	 * @return
	 */
	private String endTime(String endTime) {
		if(StringUtils.isEmptyOrWhitespace(endTime) || StringUtils.equals("00:00", endTime)) {
			endTime = "";
		}
		return endTime;
	}


	/**
	 * 勤務時間を取得
	 * @param start
	 * @param end
	 * @return
	 */
	public  BigDecimal getDutyTime(String start, String end) {
		start = start.replace("/","");
		start = start.replace("-","");
		end = end.replace("/","");
		end = end.replace("-","");

		//出勤・退勤時間をLocalDateTime型へ変換
		List<Integer> iListStart = getYMdhm(start);
		List<Integer> iListEnd = getYMdhm(end);
		LocalDateTime workStart = getYMdhmList(iListStart);
		LocalDateTime workEnd = getYMdhmList(iListEnd);
		
		//勤務時間を取得
		int dTime= (int)ChronoUnit.MINUTES.between(workStart, workEnd);
		
		BigDecimal dutyTime = BigDecimal.valueOf((double)dTime/60);
		//小数第三位を切り捨て
		dutyTime = dutyTime.setScale(2, RoundingMode.DOWN);

		return dutyTime;
	}

/**
 * 年、月、日、時、分に分割
 * @param yMdhm
 * @return
 */
	private List<Integer> getYMdhm(String yMdhm) {
		List<Integer> list = new ArrayList<>() ;
		yMdhm = yMdhm.replace(":","");
		list.add(Integer.parseInt(yMdhm.substring(0,4)));
		list.add(Integer.parseInt(yMdhm.substring(4,6)));
		list.add(Integer.parseInt(yMdhm.substring(6,8)));
		list.add(Integer.parseInt(yMdhm.substring(8,10)));
		list.add(Integer.parseInt(yMdhm.substring(10,12)));
		return list;
	}
	/**
	 * 時間計算のためLocalDateTimeに変換
	 * 例(yyyy,MM,dd,hh,mm,0)
	 * @param yyyyMMddhhmm
	 * @return
	 */
	private LocalDateTime getYMdhmList(List<Integer> yyyyMMddhhmm) {
		
		LocalDateTime ldate = LocalDateTime.of(
				yyyyMMddhhmm.get(0).intValue(),
				yyyyMMddhhmm.get(1).intValue(),
				yyyyMMddhhmm.get(2).intValue(),
				yyyyMMddhhmm.get(3).intValue(),
				yyyyMMddhhmm.get(4).intValue(),
				0
				);
		return ldate;
	}
	
	/**
	 * 休憩時間取得
	 * @param dutyTime
	 * @return
	 */
	public BigDecimal getBreakTime(BigDecimal dutyTime) {
		BigDecimal seven = new BigDecimal(7);
		BigDecimal nine = new BigDecimal(9);
//		BigDecimal breakTime = new BigDecimal(45);
		BigDecimal breakTime = new BigDecimal(0.75);
		
//		勤務時間が7時間以内	なし
//		勤務時間が7～9時間	45分以上
//		勤務時間が9時間以上	60分以上 
		
		if (dutyTime.compareTo(seven)>0) {
			if (dutyTime.compareTo(nine)<0) {
				return breakTime ;
			}
//			return breakTime = new BigDecimal(60);
			return breakTime = new BigDecimal(1.00);
		}else{
			return breakTime = new BigDecimal(0);
		}
 	}

	/**
	 * 日報DB更新
	 * @param registrationForm
	 */
	public void updateDB(RegistrationForm registrationForm) {
		
		UpdateDailyReport updateDailyReport = new UpdateDailyReport();
		updateDailyReport.setId(registrationForm.getId());
		updateDailyReport.setWorkingDate(registrationForm.getWorkingDate());
		updateDailyReport.setWorkingStartTime(registrationForm.getWorkingStartTime());
		updateDailyReport.setWorkingEndTime(registrationForm.getWorkingEndTime());
		updateDailyReport.setWorkingContents(registrationForm.getWorkingContents());

		updateDailyReport.setWorkingTime("00:00:00");
		updateDailyReport.setShomuSonota("");
		updateDailyReport.setShokan("");
		
		dBDailyReportService.updateDailyReport(updateDailyReport);

	}
	
	public void updateDB(WorkReportsListForm workReportsListForm) {
		
		UpdateDailyReport updateDailyReport = new UpdateDailyReport();
		updateDailyReport.setId(workReportsListForm.getId());
		updateDailyReport.setWorkingDate(workReportsListForm.getWorkingDate());
		updateDailyReport.setWorkingStartTime(workReportsListForm.getWorkingStartTime());
		updateDailyReport.setWorkingEndTime(workReportsListForm.getWorkingEndTime());
		updateDailyReport.setWorkingContents(workReportsListForm.getWorkingContents());
		
		updateDailyReport.setWorkingTime("00:00:00");
		updateDailyReport.setShomuSonota("");
		updateDailyReport.setShokan("");
		
		dBDailyReportService.updateDailyReport(updateDailyReport);

	}
	
	/**
	 * 勤怠時刻取得
	 */
	public void selectAttendance() {
		//String型のworkingDate
		DailyReport dailyReport = new DailyReport();
		dailyReport.setWorkingDate("2020-10-01");
		List<DailyReport> list = dailyReportMapper.selectAttendance(dailyReport);
		
		String workTime = list.get(0).getWorkingTime();
		
		//hhmmssの場合
		if (workTime.length()<5) {
			
		}
		//hmmssの場合
		else {
		}
	}
	
	/**
	 * 月末日取得
	 * @return 月末日
	 */
//	private String getLastDay() {
//		Date date = new Date();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);;
//		calendar.add(Calendar.MONTH, 1);
//		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
//		
//		String lastDay = String.valueOf(lastDayOfMonth);
//		return lastDay;
//	}
		
	/**
	 * 日付と曜日を取得
	 * @param calendar
	 * @param year
	 * @param month
	 * @param day
	 * @return "(日)"or"(月)"or"(火)"or"(水)"or"(木)"or"(金)"or"(土)"
	 */
	private String getWeek(Calendar calendar, int year, int month, int day) {
		String yobi[] = {"(日)","(月)","(火)","(水)","(木)","(金)","(土)"};
		calendar.set(year, month, day);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		return yobi[week -1];
	}
	
	/**
	 * 指定年月の末日を取得
	 * @param calendar
	 * @param year
	 * @param month
	 * @return
	 */
	private int getLastDayOfMonth(Calendar calendar,int year, int month) {
		calendar.set(year, month, 1);
		calendar.add(Calendar.MONTH, 1);
		int lastDay = calendar.getActualMaximum(Calendar.DATE);
		return lastDay;
	}
	
	
	/**
	 * 出勤ボタン・退勤ボタン押下時の処理
	 * @param workReportsListForm
	 * @param user
	 */
	public void clockIn(WorkReportsListForm workReportsListForm, String user) {
		LocalDateTime dateTime = LocalDateTime.now();
		//当日の出勤・退勤時刻を取得
		List<DailyReport> list = selectTodayInfo(dateTime, user);
		//データが存在しない場合、もしくはブランクの場合
		if (list.size() == 0 || StringUtils.isEmptyOrWhitespace(list.get(0).getWorkingStartTime())) {
			//出勤時間を登録
			insert(user, dateTime);
			workReportsListForm.setWorkingStartTime(getNowTime(dateTime));
		}
	}
	
	/**
	 * 退勤ボタン押下時の処理
	 * @param workReportsListForm
	 * @param user
	 */
	public void clockOut(WorkReportsListForm workReportsListForm, String user) {
		LocalDateTime dateTime = LocalDateTime.now();
		//当日の出勤・退勤時刻を取得
		List<DailyReport> list = selectTodayInfo(dateTime, user);
		//データが存在する場合、かつ"00:00:00"の場合
		if (list.size() > 0 && StringUtils.equals("00:00:00", list.get(0).getWorkingEndTime())) {
			//出勤時間を登録
			update(user, dateTime);
			workReportsListForm.setWorkingEndTime(getNowTime(dateTime));
		}
	}

	//時刻を返す
	private String getNowTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return dateTime.format(formatter);
	}

	// add n.matsu start
	/**
	 * 当日日付・当日出勤・退勤時刻を取得
	 * @param workReportsListForm
	 * @param userId
	 * @return 当日出勤・退勤時刻
	 */
	public WorkReportsListForm getTodayInfo(WorkReportsListForm workReportsListForm, String user) {

		LocalDateTime dateTime = LocalDateTime.now();
		
		//画面表示項目を設定
		//年
		workReportsListForm.setYear(String.valueOf(dateTime.getYear()));
		//月
		workReportsListForm.setMonth(String.valueOf(dateTime.getMonthValue()));
		
		List<DailyReport> list = selectTodayInfo(dateTime, user);
		//出勤
		if (list.size() > 0 && !StringUtils.isEmptyOrWhitespace(list.get(0).getWorkingStartTime())) {
			//画面に設定
			workReportsListForm.setWorkingStartTime(list.get(0).getWorkingStartTime().substring(0,5));
		}
		//退勤
		if (list.size() > 0 && !StringUtils.isEmptyOrWhitespace(list.get(0).getWorkingEndTime())
				&& !StringUtils.equals("00:00:00", list.get(0).getWorkingEndTime())) {
			//画面に設定
			workReportsListForm.setWorkingEndTime(list.get(0).getWorkingEndTime().substring(0,5));
		}
		return workReportsListForm;
	}
	
	

	/**
	 * 当日勤怠時刻を取得
	 * @param dateTime
	 * @param user
	 * @return
	 */
	private List<DailyReport> selectTodayInfo(LocalDateTime dateTime, String user) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DailyReport dailyReport = new DailyReport();
		dailyReport.setId(user);
		dailyReport.setWorkingDate(dateTime.format(formatter));
		List<DailyReport> list = dBDailyReportService.searchToday(dailyReport);
		return list;
	}


	/**
	 * 出勤時刻を登録
	 * @param user
	 * @param date
	 * @param workStartTime
	 */
	public void insert(String user, LocalDateTime dateTime) {
		DailyReport dailyReport = new DailyReport();
		setInitDailyReport(dailyReport);
		DateTimeFormatter fmtYmd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter fmtHms = DateTimeFormatter.ofPattern("HH:mm:ss");

		dailyReport.setId(user);
		dailyReport.setWorkingDate(dateTime.format(fmtYmd));
		dailyReport.setWorkingStartTime(dateTime.format(fmtHms));
		dailyReport.setRecRegstUser(user);
		dailyReport.setRecUpdtUser(user);
		
		dBDailyReportService.insertDailyReport(dailyReport);
		
	}

	/**
	 * 退勤時刻を登録
	 * @param user
	 * @param date
	 * @param workStartTime
	 */
	public void update(String user, LocalDateTime dateTime) {
		DailyReport dailyReport = new DailyReport();
		setInitDailyReport(dailyReport);
		DateTimeFormatter fmtYmd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter fmtHms = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		//ユーザーID
		dailyReport.setId(user);
		//作業日時
		dailyReport.setWorkingDate(dateTime.format(fmtYmd));
		//作業終了時刻
		dailyReport.setWorkingEndTime(dateTime.format(fmtHms));
		dailyReport.setRecUpdtUser(user);
		dBDailyReportService.updateDailyReport(dailyReport);
		//作業時間を登録
		updateWorkTime(dateTime, user);
	}
	
	//作業時間を登録
	private void updateWorkTime(LocalDateTime dateTime, String user) {
		DailyReport dailyReport = new DailyReport();
		setInitDailyReport(dailyReport);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		dailyReport.setId(user);
		dailyReport.setWorkingDate(dateTime.format(formatter));
		//勤務時間を取得（作業終了時間-作業開始時間）
		List<DailyReport> list = dBDailyReportService.selectWorkingHour(dailyReport);
		String workingHour = list.get(0).getWorkingHour();
		if (workingHour.length() > 2) {
			//秒以下を切り捨て
			workingHour = workingHour.substring(0, 3);
		} else {
			workingHour = "0";
		}
		
		//作業時間を登録
		dailyReport.setWorkingHour(workingHour);
		dailyReport.setRecUpdtUser(user);
		dBDailyReportService.updateWorkingTime(dailyReport);
	}
	
	//勤務時間を設定
	private String getWorkingHour(String workingHour) {
		if (!StringUtils.isEmptyOrWhitespace(workingHour) && Integer.valueOf(workingHour) > 0 
				&& workingHour.length() > 2) {
			//秒以下を切り捨て
			workingHour = workingHour.substring(0, 3);
		} else {
			workingHour = "0";
		}
		return workingHour;
	}
	/**
	 * DailyReport初期値設定
	 * @param dailyReport
	 */
	private void setInitDailyReport(DailyReport dailyReport) {
		dailyReport.setId("");
		dailyReport.setWorkingDate("");
		dailyReport.setWorkingTime("00:00:00");
		dailyReport.setWorkingStartTime("00:00:00");
		dailyReport.setWorkingEndTime("00:00:00");
		dailyReport.setWorkingContents("");
		dailyReport.setShomuSonota("");
		dailyReport.setShokan("");
		dailyReport.setBreakTime("00");
		dailyReport.setRecRegstUser("00000");
		dailyReport.setRecUpdtUser("00000");
	}
	// add n.matsu end

	//初期画面表示値を設定
	public void setWorkReportsListForm(WorkReportsListForm workReportsListForm, String user) {
		workReportsListForm = getTodayInfo(workReportsListForm, user);
	}

}
