package jp.co.softventure.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.web.workReportsList.WorkReportsListDto;
import jp.co.softventure.web.workReportsList.WorkReportsListForm;



@Service
public class WorkReportsListService {
	@Autowired
	private DBDailyReportService dBDailyReportService;
	
	
	/***
	 * daily_reportを取得する
	 * @param wrlForm
	 * @param id
	 * @param month
	 * @return
	 */
	public List<DailyReport> selectDailyReport(WorkReportsListForm wrlForm, String id, String date) {

		DailyReport dailyReport = new DailyReport();
		BeanUtils.copyProperties(wrlForm, dailyReport);
		dailyReport.setId(id);
		dailyReport.setWorkingDate(date);
		List<DailyReport> list = dBDailyReportService.searchDailyReport(dailyReport);
		
		return list;
	}
	
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
//	public List <WorkReportsListDto> setWorkReportsListDto(WorkReportsListDto dto, List<DailyReport> list) {
	public List <WorkReportsListDto> setWorkReportsListDto(List<DailyReport> list) {

//	int i = 0;

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
}
