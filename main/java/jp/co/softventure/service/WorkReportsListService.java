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
import jp.co.softventure.domain.WorkingReport;
import jp.co.softventure.domain.dto.DailyReportDto;
import jp.co.softventure.domain.dto.WorkReportsListDto;
import jp.co.softventure.model.LoginDataInfo;
import jp.co.softventure.persistence.DailyReportMapper;
import jp.co.softventure.persistence.WorkingReportMapper;
import jp.co.softventure.web.RegistrationForm;
import jp.co.softventure.web.workReportsList.WorkReportsListForm;



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
		monthlyReportOutputService.createExcel(dtoList, loginDataInfo.getUserName());
		
		return "excel";
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
		BeanUtils.copyProperties(wrlForm, dailyReport);
		dailyReport.setId(id);
		dailyReport.setWorking_date_s(sdf.format(dateS).concat("01"));
		dailyReport.setWorking_date_e(sdf.format(dateE).concat("01"));
		
		List<DailyReport> list = dBDailyReportService.searchDailyReport(dailyReport);
		
		return list;
	}
	
	
	/**
	 * 曜日取得
	 * @param ymd
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
	 * 取得した値をDtoにセット
	 * @param list
	 * @return
	 */
	public List <WorkReportsListDto> setWorkReportsListDto(List<DailyReport> list) {
		
		List<WorkReportsListDto> wrlDto = new ArrayList<>();
		for(int i=0; i < list.size(); i++) {
			List<WorkReportsListDto>dto2 = new ArrayList<>();
			WorkReportsListDto dto = new WorkReportsListDto();
			//年
			dto.setYear(list.get(i).getWorkingDate().substring(0, 4));
			//日付
			dto.setDate(list.get(i).getWorkingDate().substring(5, 10));
			//曜日
			dto.setWeek(getYobi(list.get(i).getWorkingDate()));
			//作業内容
			dto.setWorkingContents(list.get(i).getWorkingContents());
			//開始時間
			String start = list.get(i).getWorkingStartTime().substring(0, 5);
			dto.setWorkingStartTime(start);
			//終了時間
			String end = list.get(i).getWorkingEndTime().substring(0, 5);
			dto.setWorkingEndTime(end);
			//勤務時間
			String dutyS = list.get(i).getWorkingDate()+start;
			String dutyE = list.get(i).getWorkingDate()+end;
			
			
			
			
			BigDecimal dutyTime = getDutyTime(dutyS,dutyE);
			dto.setDutyTime(dutyTime.toString()); 
			//休憩時間
			BigDecimal breakTime = getBreakTime(dutyTime);
			dto.setBreakTime(String.valueOf(breakTime));
			//作業時間
			BigDecimal workTime = dutyTime.subtract(breakTime);
			dto.setWorkTime(String.valueOf(workTime));
			wrlDto.add(dto);
		}
		return wrlDto;
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
}
