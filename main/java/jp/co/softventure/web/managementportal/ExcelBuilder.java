package jp.co.softventure.web.managementportal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import jp.co.softventure.domain.ManagementPortalInfo;

public class ExcelBuilder {
	
	public String createExcelFile(Workbook workBook, List<ManagementPortalInfo> list, ManagementPortalInfo managementPortalInfo) {
		//ファイル名の作成
		String fileName = "work_report(" + managementPortalInfo.getUserName() + ").xlsx";
		//ファイル名のエンコード
		String encodedFileName = "encodedFileName";
		try { 
			encodedFileName = URLEncoder.encode(fileName, "UTF-8");
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
		try {
		    // シートの設定
			Sheet sheet = workBook.createSheet();
			workBook.setSheetName(0, "work_report");
		    sheet = workBook.getSheet("work_report");
		    // 初期行の作成
		    Row row = sheet.createRow(0);
		    row.createCell(0).setCellValue("作業日時");
		    row.createCell(1).setCellValue("始業時刻");
		    row.createCell(2).setCellValue("終業時刻");
		    row.createCell(3).setCellValue("休憩時間");
		    row.createCell(4).setCellValue("業務内容");
		    //DBから取得したデータを2行目以降に書き込み  
		    for ( int i = 0; i < list.size(); i++ ) {
		    	managementPortalInfo = list.get(i);
		    	row = sheet.createRow(1 + i);
	            row.createCell(0).setCellValue(managementPortalInfo.getWorkingDate().toString());
			    row.createCell(1).setCellValue(managementPortalInfo.getWorkingStartTime().toString().substring(0, 5));
			    row.createCell(2).setCellValue(managementPortalInfo.getWorkingEndTime().toString().substring(0, 5));
			    row.createCell(3).setCellValue(managementPortalInfo.getBreakTime());
			    row.createCell(4).setCellValue(managementPortalInfo.getWorkingContents());
		    }   
		} catch ( Exception e ) {
            e.printStackTrace();
        }
		return encodedFileName;
	}
	
}