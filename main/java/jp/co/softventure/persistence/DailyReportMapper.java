
package jp.co.softventure.persistence;

import java.util.List;
import jp.co.softventure.domain.DailyReport;
import jp.co.softventure.domain.UpdateDailyReport;
import jp.co.softventure.domain.WorkingReport;
import jp.co.softventure.domain.dto.DailyReportDto;

/**
 * 
 * @author n.matsu
 * 2020/10/09 �V�K�쐬
 */
public interface DailyReportMapper {
	//�o�^
	public void insert(DailyReport  dailyReport);
	
	//1�������̃f�[�^���擾
	public List<DailyReport> select(DailyReport dailyReport);
	
	//�Αӎ����擾
	public List<DailyReport> selectAttendance(DailyReport dailyReport);
//	public List<DailyReportDto> selectAttendance(DailyReportDto dto);
	
	//ID,���t��������1�����̃f�[�^���擾
	public List<DailyReport> selectIdDate(DailyReport dailyReport);
	
	//�X�V
	public void update(UpdateDailyReport updateDailyReport);

	
}
