
package jp.co.softventure.persistence;

import java.util.List;
import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.domain.UpdateDailyReport;
import jp.co.softventure.domain.WorkingReport;
import jp.co.softventure.domain.dto.DailyReportDto;

/**
 * 
 * @author n.matsu
 * 2020/10/09 新規作成
 */
public interface DailyReportMapper {
	//登録
	public void insert(DailyReport  dailyReport);
	
	//1か月分のデータを取得
	public List<DailyReport> select(DailyReport dailyReport);
	
	//勤怠時刻取得
	public List<DailyReport> selectAttendance(DailyReport dailyReport);
//	public List<DailyReportDto> selectAttendance(DailyReportDto dto);
	
	//ID,日付を条件に1日分のデータを取得
	public List<DailyReport> selectIdDate(DailyReport dailyReport);
	
	//更新
	public void update(UpdateDailyReport updateDailyReport);

	
}
