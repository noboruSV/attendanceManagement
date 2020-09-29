/**
 * Author N.Matsumoto
 */
package jp.co.softventure.persistence;

import java.util.List;

import jp.co.softventure.domain.DailyReport;

public interface DailyReportMapper {
	public void insert(DailyReport  dailyReport);

	public List<DailyReport> select(DailyReport dailyReport);
}
