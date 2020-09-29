/**
 * Author M.Yoshida
 */
package jp.co.softventure.domain;

public class WorkingReport {


	    private String working_date; 
	    private String working_start_time; 
	    private String working_end_time;
	    private String working_contents;
	 
	  
	    public String getWorking_date() { 
	        return working_date; 
	    } 
	 
	    public void setWorking_date(String working_date) { 
	        this.working_date= working_date; 
	    } 
	 
	    public String getWorking_start_time() { 
	        return working_start_time; 
	    } 
	 
	    public void setWorking_start_time(String working_start_time) { 
	        this.working_start_time = working_start_time; 
	    } 
	 
	    public String getWorking_end_time() { 
	        return working_end_time; 
	    } 
	 
	    public void setWorking_end_time(String working_end_time) { 
	        this.working_end_time = working_end_time; 
	    } 
	    public String getWorking_contents() { 
	        return working_contents; 
	    } 
	 
	    public void setWorking_contents(String working_contents) { 
	        this.working_contents = working_contents; 
	    } 
	} 