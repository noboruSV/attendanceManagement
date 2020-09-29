package jp.co.softventure.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.persistence.DailyReportMapper;

@Service
public class DBDailyReportService {

	
	@Autowired
	private DailyReportMapper mapper;
	
	// データ登録処理メソッド
		@Transactional
		public void insertDailyReport(DailyReport dailyReport) {
			// データ登録
			mapper.insert(dailyReport);
		}
		
		// データ検索処理メソッド
		public List<DailyReport> searchDailyReport(DailyReport dailyReport) {
			// データ検索
			List<DailyReport> list = mapper.select(dailyReport);
			return list;
		}
		
		/*勤務時間を取得する  (退勤時間-出勤時間)*/
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
		
		/*yyyyMMdd hhmmを年、月、日、時、分に分割*/
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
		
		

}
