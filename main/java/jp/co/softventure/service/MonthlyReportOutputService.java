package jp.co.softventure.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import jp.co.softventure.dto.WorkReportsListDto;


@Service
public class MonthlyReportOutputService {
	
	// ワークブック
	XSSFWorkbook workBook = null;
	// シート
	XSSFSheet sheet = null;
	// 出力ファイル
	FileOutputStream outPutFile = null;
	// 出力ファイルパス
	String outPutFilePath = null;
	// 出力ファイル名
	String outPutFileName = null;

	public String createExcel(List <WorkReportsListDto> workReportsListDto, String userName) {
	
		// エクセルファイルの作成
		try {
	
			// ワークブックの作成
			workBook = new XSSFWorkbook();
	
			// シートの設定
			sheet = workBook.createSheet();
			String month = workReportsListDto.get(0).getDate().substring(0, 2);
			workBook.setSheetName(0, month + "月");
			sheet = workBook.getSheet(month + "月");
	
			// 初期行の作成
			XSSFCellStyle headerCellStyle = workBook.createCellStyle();
			XSSFFont headerFont = workBook.createFont();
			// ヘッダスタイル設定
			setHeaderCellStyle(headerCellStyle,headerFont);
			
			//セルにヘッダを設定
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			List<String> headerName = new ArrayList<>();
			setHeader(headerName, row, cell,headerCellStyle);
			
			// 「計算結果」のセルスタイル設定
			XSSFCellStyle resultCellStyle = workBook.createCellStyle();
			XSSFFont resultFont = workBook.createFont();
			setAttendanceListStyle(resultCellStyle, resultFont);
			
			//セルに勤怠情報を設定
			setAttendanceListResult(workReportsListDto, row, cell, resultCellStyle);
			
			// エクセルファイルを出力
			try {
	
				// 現在の日付を取得
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				String name = userName;
	
				//ファイルパス・ファイル名の指定
				String systemUser = System.getProperty("user.name"); 
				outPutFilePath = "C:\\Users\\" + systemUser + "\\Documents\\";
				outPutFileName = "勤務表_" + dateFormat.format(date).toString() + name + ".xlsx";
	
				// エクセルファイルを出力
				outPutFile = new FileOutputStream(outPutFilePath + outPutFileName);
				workBook.write(outPutFile);
	
				return "「" + outPutFilePath + outPutFileName + "」に保存しました。";
	
			}catch(IOException e) {
				System.out.println(e.toString());
				return e.toString();
			}
	
		}catch(Exception e) {
			System.out.println(e.toString());
			return e.toString();

		}
	
	}
	

	/**
	 * セルに勤怠一覧情報を設定
	 * @param workReportsListDto
	 * @param row
	 * @param cell
	 * @param resultCellStyle
	 */
	private void setAttendanceListResult(List<WorkReportsListDto> workReportsListDto, XSSFRow row, XSSFCell cell,
			XSSFCellStyle resultCellStyle) {
		
		int num = 0;
		for (int i=1; i <= workReportsListDto.size(); i++,num++) {
			row = sheet.createRow(i);
			int rowNum = 0;
			//日
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue(workReportsListDto.get(num).getDate().substring(3, 5));
			//曜日
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue(workReportsListDto.get(num).getWeek());
			//出社時間
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue(workReportsListDto.get(num).getWorkingStartTime());
			//退社時間
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue(workReportsListDto.get(num).getWorkingEndTime());
			//休憩時間
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue(workReportsListDto.get(num).getBreakTime());
			//時間内休憩
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue("");
			//修正
//			cell = row.createCell(j++);
//			cell.setCellStyle(resultCellStyle);
//			cell.setCellValue("");
			//時間内勤務
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue(workReportsListDto.get(num).getDutyTime());
			//時間外残業
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue("");
			//深夜残業
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue("");
			//法定休日時間外残業
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue("");
			//法定休日深夜残業
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue("");
			//残業時間合計
			cell = row.createCell(rowNum++);
			cell.setCellStyle(resultCellStyle);
			cell.setCellValue("");
			//履歴
//			cell = row.createCell(j++);
//			cell.setCellStyle(resultCellStyle);
//			cell.setCellValue("");
			//申請
//			cell = row.createCell(j);
//			cell.setCellStyle(resultCellStyle);
//			cell.setCellValue("");
		}

	}


	/**
	 * 「勤怠一覧」のセルスタイル設定
	 * @param resultCellStyle
	 * @param resultFont
	 */
	private void setAttendanceListStyle(XSSFCellStyle resultCellStyle, XSSFFont resultFont) {
		
		resultFont.setFontName("ＭＳ ゴシック");
		resultFont.setFontHeightInPoints((short)11);
		resultCellStyle.setFont(resultFont);
//		resultCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		resultCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		resultCellStyle.setAlignment(HorizontalAlignment.CENTER);
//		resultCellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
		resultCellStyle.setBorderTop(BorderStyle.MEDIUM);
		resultCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		resultCellStyle.setBorderRight(BorderStyle.MEDIUM);
		resultCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		resultCellStyle.setWrapText(true);
	}


	/**
	 * ヘッダのスタイル設定
	 * @param headerCellStyle
	 * @param headerFont
	 */
	private void setHeaderCellStyle(XSSFCellStyle headerCellStyle, XSSFFont headerFont) {
		headerFont.setFontName("ＭＳ ゴシック");
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)11);
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
		headerCellStyle.setBorderTop(BorderStyle.MEDIUM);
		headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerCellStyle.setBorderRight(BorderStyle.MEDIUM);
		headerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		headerCellStyle.setWrapText(true);
	}

	/**
	 * ヘッダ項目名セット
	 * @param headerName
	 * @param row
	 * @param cell
	 * @param headerCellStyle
	 */
	private void setHeader(List<String> headerName, XSSFRow row, XSSFCell cell, XSSFCellStyle headerCellStyle) {
		
		headerName.add("日");
		headerName.add("曜日");
		headerName.add("出社");
		headerName.add("退社");
		headerName.add("休憩時間");
		headerName.add("時間内\n休憩");
//		headerName.add("修正");
		headerName.add("時間内\n勤務");
		headerName.add("時間外\n残業");
		headerName.add("深夜残業");
		headerName.add("法定休日\n時間外\n残業");
		headerName.add("法定休日\n深夜\n残業");
		headerName.add("残業時間\n合計");
//		headerName.add("履歴");
//		headerName.add("申請");
		
		row = sheet.createRow(0);
		row.setHeightInPoints((float)57);
		for(int i=0; i<headerName.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerName.get(i));
			cell.setCellStyle(headerCellStyle);
		}
		
	}
}
