/**
 * Author M.Yoshida
 */
package jp.co.softventure.persistence;

import jp.co.softventure.domain.WorkingReport;

public interface WorkingReportMapper { 
 
    public void insert(WorkingReport workingReport); 
    
	public void update(WorkingReport workingReport );

} 