package jp.co.softventure.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.domain.UpdateDailyReport;
import jp.co.softventure.dto.DailyReportDto;
import jp.co.softventure.persistence.DailyReportMapper;
/**
 * 
 * @author n.matsu
 * 2020/10/09 新規作成
 * 
 */
@Service
public class DBDailyReportService {
	
	@Autowired
	private DailyReportMapper mapper;
	
	/**
	 * 登録処理
	 * @param dailyReport
	 */
	@Transactional
	public void insertDailyReport(DailyReport dailyReport) {
		mapper.insert(dailyReport);
	}
	
	/**
	 * 検索処理
	 * @param dailyReport
	 * @return
	 */
	public List<DailyReport> searchDailyReport(DailyReport dailyReport) {
		List<DailyReport> list = mapper.select(dailyReport);
		return list;
	}

	/**
	 * 勤怠時刻検索処理
	 * @param dailyReport
	 * @return
	 */
	public List<DailyReport> selectAttendance(DailyReport dailyReport) {
		List<DailyReport> list = mapper.selectAttendance(dailyReport);
		return list;
	}
	
	//
	public List<DailyReportDto> selectDailyReportDto(DailyReportDto dto) {
		List<DailyReportDto> list = mapper.selectDailyReportDto(dto);
		return list;
	}

	/**
	 * 更新処理
	 * @param dailyReport
	 */
	@Transactional
	public void updateDailyReport(UpdateDailyReport updateDailyReport) {
		 mapper.update(updateDailyReport);
	}
	
	/**
	 * 勤務時間を取得する  (退勤時間-出勤時間)
	 * @param start
	 * @param end
	 * @return
	 */
	public  int getDutyTime(String start, String end) {
		start = start.replace("/","");
		end = end.replace("/","");

		//出勤・退勤時間をLocalDateTime型へ変換
		List<Integer> iListStart = getYMdhm(start);
		List<Integer> iListEnd = getYMdhm(end);
		LocalDateTime workStart = getYMdhmList(iListStart);
		LocalDateTime workEnd = getYMdhmList(iListEnd);
		
		//勤務時間を取得
		int dutyTime= (int)ChronoUnit.MINUTES.between(workStart, workEnd);
		
		return dutyTime;
	}
	
	/**
	 * yyyyMMdd hhmmを年、月、日、時、分に分割
	 * @param str
	 * @return
	 */
	private List<Integer> getYMdhm(String str) {
		List<Integer> iList = new ArrayList<>() ;
		iList.add(Integer.parseInt(str.substring(0,4)));
		iList.add(Integer.parseInt(str.substring(4,6)));
		iList.add(Integer.parseInt(str.substring(6,8)));
		iList.add(Integer.parseInt(str.substring(8,10)));
		iList.add(Integer.parseInt(str.substring(10,12)));
		return iList;
	}
	/*getYMdhmメソッドで分割したものをリストにつめる*/
	private LocalDateTime getYMdhmList(List<Integer> yyyyMMddhhmm) {
		
		LocalDateTime ldate = LocalDateTime.of(//2020,9,16,8,45,0
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
	 * 当日の出勤時刻と退勤時刻を取得
	 * @param dailyReport
	 * @return
	 */
	public List<DailyReport> searchToday(DailyReport dailyReport) {
		List<DailyReport> list = mapper.selectToday(dailyReport);
		return list;
	}

	//退勤時刻を設定
	public void updateWorkEndTime(DailyReport dailyReport) {
		mapper.updateEndTime(dailyReport);
	}
	
	//作業時間を取得
	public List<DailyReportDto> selectWorkingHour(DailyReport dailyReport) {
		List<DailyReportDto> list = mapper.selectWorkingHour(dailyReport);
		return list;
	}
	//作業時間を登録
	public void updateWorkingTime(DailyReport dailyReport) {
		mapper.updateEndTime(dailyReport);
	}
}
