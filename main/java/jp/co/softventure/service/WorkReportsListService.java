package jp.co.softventure.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.domain.UpdateDailyReport;
import jp.co.softventure.dto.DailyReportDto;
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
		
		java.util.Date dateS = new java.util.Date();
		java.util.Date dateE = new java.util.Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateE);;
		calendar.add(Calendar.MONTH, 1);
		dateE = calendar.getTime();
		
		DailyReport dailyReport = new DailyReport();
		dailyReport.setId(id);
		dailyReport.setWorking_date_s(sdf.format(dateS).concat("01"));
		dailyReport.setWorking_date_e(sdf.format(dateE).concat("01"));
		
		List<DailyReport> list = dBDailyReportService.searchDailyReport(dailyReport);
		
		return list;
	}
	
	//daily_reportを取得
	public List<DailyReportDto> selectDailyReportTest (String id) {

		LocalDate localDate = LocalDate.now();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, localDate.getYear());
		int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
		
		Date startDate = Date.valueOf(localDate);
		startDate = Date.valueOf(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1));
		Date endDate = Date.valueOf(localDate);
		endDate = Date.valueOf(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), lastDayOfMonth));

		DailyReportDto dto = new DailyReportDto();
		dto.setId(id);
		dto.setWorkDateStart(startDate);
		dto.setWorkDateEnd(endDate);
		List<DailyReportDto> list = dBDailyReportService.selectDailyReportDto(dto);
		return list;
	}
	
	/**
	 * 曜日取得
	 * @param ymd(yyyy-MM-dd形式)
	 * @return
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
	 * 取得した値をWorkReportsListDtoにセット
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
					//勤務時間 ※終了時間-開始時間
					String dutyTime = getWorkingHour(list.get(j).getWorkingHour());
					dto.setDutyTime(dutyTime);
					//休憩時間
					dto.setBreakTime(chkBreakTime(list.get(j).getBreakTime()));
					//作業時間
					dto.setWorkTime(chkWorkTime(list.get(j).getWorkingTime()));
				}
			}
			wrlDto.add(dto);
		}
		return wrlDto;
	}
	
	/**
	 * 作業時間の値変換
	 * @param workTime
	 * @return
	 */
	private String chkWorkTime(String workTime) {
		if (workTime == null || StringUtils.isEmptyOrWhitespace(workTime) 
				|| StringUtils.equals("00:00:00", workTime)) {
			workTime = "";
		}
		return workTime;
	}


	/**
	 * 休憩時間を変換
	 * @param breakTime
	 * @return
	 */
	private String chkBreakTime(String breakTime) {
		if (breakTime == null || breakTime == "00") {
			breakTime = "";
		}
		return breakTime;
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
		//BigDecimal breakTime = new BigDecimal(45);
		BigDecimal breakTime = new BigDecimal(0.75);
		
		//勤務時間が7時間以内	なし
		//勤務時間が7～9時間	45分以上
		//勤務時間が9時間以上	60分以上 
		
		if (dutyTime.compareTo(seven)>0) {
			if (dutyTime.compareTo(nine)<0) {
				return breakTime ;
			}
			//return breakTime = new BigDecimal(60);
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
			//退勤時間を登録
			updWorkEndTime(user, dateTime);
			//作業時間を登録
			updateWorkTime(dateTime, user);
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
	public void updWorkEndTime(String user, LocalDateTime dateTime) {
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
		dBDailyReportService.updateWorkEndTime(dailyReport);
	}
	
	//作業時間を登録
	private void updateWorkTime(LocalDateTime dateTime, String user) {
		DailyReport dailyReport = new DailyReport();
		setInitDailyReport(dailyReport);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		dailyReport.setId(user);
		dailyReport.setWorkingDate(dateTime.format(formatter));
		//勤務時間を取得（作業開始時間と作業終了時間）
		List<DailyReportDto> list = dBDailyReportService.selectWorkingHour(dailyReport);
		//変数.workingHour （作業終了時間-作業開始時間）
		String workingHour = getWorkHour(list.get(0).getWorkingStartTime(), list.get(0).getWorkingEndTime());
		//変数.workingTime (変数.workingHour - BreakTime)
		Time workTime = Time.valueOf(workingHour);
		Time breakTime = Time.valueOf(list.get(0).getBreakTime());
		String workingTime = getWorkTime(workTime, breakTime).toString();
		if (workingHour.length() > 2) {
			//秒以下を切り捨て
			workingHour = workingHour.substring(0, 3);
		} else {
			workingHour = "0";
		}
		
		//作業時間を登録
		dailyReport.setWorkingTime(workingHour);
		dailyReport.setRecUpdtUser(user);
		dBDailyReportService.updateWorkingTime(dailyReport);
	}
	
	//勤務時間を設定
//	private String getWorkingHour(String workingHour) {
//		if (!StringUtils.isEmptyOrWhitespace(workingHour) && Integer.valueOf(workingHour) > 0 
//				&& workingHour.length() > 2) {
//			//秒以下を切り捨て
//			workingHour = workingHour.substring(0, 3);
//		} else {
//			workingHour = "";
//		}
//		return workingHour;
//	}
	private String getWorkingHour(String workingHour) {
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
	
	//勤務開始時間と勤務終了時間を15分単位でまとめる
	private String roundMinutes(String minutes) {
		
		return null;
	}
	
	/**
	 * 取得した値をWorkReportsListDtoにセット
	 * @param list
	 * @return
	 */
	public List <WorkReportsListDto> setWorkReportsListDto2(List<DailyReportDto> list) {
		List<WorkReportsListDto> wrlDto = new ArrayList<>();
		//カレンダーをセット
		LocalDate localDate = LocalDate.now();
		Calendar calendar = Calendar.getInstance();
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int lastDate = localDate.lengthOfMonth();
		calendar.set(year, month, 1);

		//画面表示様の日付と曜日を設定
		for (int day=1; day<=lastDate; day++) {
			WorkReportsListDto workReportsListDto = new WorkReportsListDto();
			//日付
			workReportsListDto.setDate(String.valueOf(day));
			//曜日
			workReportsListDto.setWeek(getWeek(calendar, year, month, day));
			//画面表示日付と同日のデータが存在する場合、画面項目を設定
			for(int j=0; j < list.size(); j++) {
				if (StringUtils.equals(workReportsListDto.getDate(), workDateToString(list.get(j).getWorkingDate()))) {
					//作業内容
					workReportsListDto.setWorkingContents(list.get(j).getWorkingContents());
					//開始時間
					String start = timeToString(list.get(j).getWorkingStartTime());
					workReportsListDto.setWorkingStartTime(start);
					//終了時間
					String end = timeToString(list.get(j).getWorkingEndTime());
					workReportsListDto.setWorkingEndTime(end);
					//勤務時間 ※終了時間-開始時間
					String dutyTime = getWorkHour(list.get(j).getWorkingStartTime(),list.get(j).getWorkingEndTime());
					workReportsListDto.setDutyTime(dutyTime);
					//休憩時間
					workReportsListDto.setBreakTime(chkBreakTime(list.get(j).getBreakTime()));
					//作業時間
					workReportsListDto.setWorkTime(String.valueOf(list.get(j).getWorkingTime()));
					//otameshi
				}
			}
			wrlDto.add(workReportsListDto);
		}
		return wrlDto;
	}
	

	//勤務時間(作業開始時間-作業終了時間)を取得
	private String getWorkHour(Time workingStartTime, Time workingEndTime) {
		SimpleDateFormat fmtHour = new SimpleDateFormat("HH");
		SimpleDateFormat fmtMin = new SimpleDateFormat("mm");

		int startHour = Integer.valueOf(fmtHour.format(workingStartTime));
		int startMin = Integer.valueOf(fmtMin.format(workingStartTime));
		int endHour = Integer.valueOf(fmtHour.format(workingEndTime));
		int endMin = Integer.valueOf(fmtHour.format(workingEndTime));

		LocalTime sTime = LocalTime.of(startHour, startMin);
		sTime = marumeTime(sTime);
		LocalTime eTime = LocalTime.of(endHour, endMin);
		eTime = marumeTime(eTime);
		long minutes = ChronoUnit.MINUTES.between(sTime, eTime);
		return String.valueOf(minutes);
	}
	
	//勤務時間-休憩時間
	private Time getWorkTime(Time workTime, Time breakTime) {
		SimpleDateFormat fmtHour = new SimpleDateFormat("HH");
		SimpleDateFormat fmtMin = new SimpleDateFormat("mm");

		int startHour = Integer.valueOf(fmtHour.format(workTime));
		int startMin = Integer.valueOf(fmtMin.format(workTime));
		int endHour = Integer.valueOf(fmtHour.format(breakTime));
		int endMin = Integer.valueOf(fmtHour.format(breakTime));

		LocalTime wTime = LocalTime.of(startHour, startMin);
		wTime = marumeTime(wTime);
		LocalTime bTime = LocalTime.of(endHour, endMin);
		bTime = marumeTime(bTime);
		long minutes = ChronoUnit.MINUTES.between(wTime, bTime);
		return Time.valueOf(String.valueOf(minutes));
	}

	//15分で丸める
	private LocalTime marumeTime(LocalTime time) {
		//1~15分→15分
		if (time.getMinute() > 0 && time.getMinute() < 16) {
			return LocalTime.of(time.getHour(), 15);
		}
		//16~30分→30分
		else if (time.getMinute() > 15 && time.getMinute() < 31) {
			return LocalTime.of(time.getHour(), 30);
		}
		//31~45分→45分
		else if (time.getMinute() > 30 && time.getMinute() < 46) {
			return LocalTime.of(time.getHour(), 45);
		}
		//46~00分→00分
		else if (time.getMinute() > 45 && time.getMinute() == 00) {
			return LocalTime.of(time.getHour(), 00);
		}
		return time;
	}


	private String timeToString(Time time) {
		String strTime = "";
		if(time == null) {
			return "";
		} else if (StringUtils.equals("00:00:00", String.valueOf(time))) {
			return "";
		} else {
			strTime = String.valueOf(time);
			strTime = strTime.substring(0, 5);
		}
		return strTime;
	}


	private String workDateToString(Date workingDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("d");
		return sdf.format(workingDate.getTime());
	}
	
}
