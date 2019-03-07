package com.example.demotracking.classes;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReportGenerator {
	public byte[] generateReport(ConnectionManager manager) {
		ObjectConstructor foo = new ObjectConstructor();
		
		manager.connect();
		List<DemoOrder> data = foo.constructDueOrders(manager);
		manager.disconnect();
		
		return formatReport(data);
	}
	
	private byte[] formatReport(List<DemoOrder> data) {
		int maxNumChar = ((int)(15 * 1.14388)) * 256;
		int NUMBER_OF_COLUMNS = 10;
		
		//function final output stored here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		//create the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		//create styles
		HSSFFont defaultFont= workbook.createFont();
		defaultFont.setFontHeightInPoints((short)10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(false);
		defaultFont.setItalic(false);

		HSSFFont font= workbook.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);
		
		CellStyle columnStyle=workbook.createCellStyle();
		columnStyle.setAlignment(HorizontalAlignment.CENTER);
		columnStyle.setBorderLeft(BorderStyle.THIN);
		columnStyle.setBorderRight(BorderStyle.THIN);
		//columnStyle.setBorderTop(BorderStyle.MEDIUM);
		columnStyle.setBorderBottom(BorderStyle.MEDIUM);
		columnStyle.setFont(font);

		CellStyle cellStyle=workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setFont(defaultFont);
        cellStyle.setWrapText(true);
        
        CellStyle itemStyle = workbook.createCellStyle();
        itemStyle.setAlignment(HorizontalAlignment.LEFT);
        itemStyle.setBorderLeft(BorderStyle.THIN);
		itemStyle.setBorderRight(BorderStyle.THIN);
		itemStyle.setBorderBottom(BorderStyle.THIN);
		itemStyle.setBorderTop(BorderStyle.THIN);
		itemStyle.setFont(defaultFont);
        itemStyle.setWrapText(true);
        

		//use these to refer to rows and cells in iterations
		HSSFRow row = null;
		HSSFCell cell = null;
		
		//loop starts here//
		//create sheet and set sheet name
		HSSFSheet sheet = workbook.createSheet();
		
		workbook.setSheetName(0, "Orders Due");
		
		//initialize sheet header
		row = sheet.createRow(0);
		
		cell = row.createCell(0);
		cell.setCellValue("Date Ordered");
		
		cell = row.createCell(1);
		cell.setCellValue("Date Due");
		
		cell = row.createCell(2);
		cell.setCellValue("Remarks");
		
		cell = row.createCell(3);
		cell.setCellValue("RFD");
		
		cell = row.createCell(4);
		cell.setCellValue("ARD/WDAF");
		
		cell = row.createCell(5);
		cell.setCellValue("Account Manager");
		
		cell = row.createCell(6);
		cell.setCellValue("Client");
		
		cell = row.createCell(7);
		cell.setCellValue("Items");
		
		cell = row.createCell(8);
		cell.setCellValue("Remarks");
		
		for (int i = 0; i<NUMBER_OF_COLUMNS-1; i++) {
			row.getCell(i).setCellStyle(columnStyle);
		}
		
		sheet.createFreezePane(0, 1);
		//end initialize header//
		
		//fill up data rows
		int currentRow = 2; //skip 1 row
		int itemFirstRow = currentRow;
		int itemFinalRow = currentRow;
		for (int i = 0; i < data.size(); i++) {
			DemoOrder order = data.get(i);
			
			row = sheet.createRow(currentRow);
			itemFirstRow = currentRow;
			itemFinalRow = currentRow;
			
			//populate order info
			cell = row.createCell(3);
			cell.setCellValue(order.getRfd());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(4);
			cell.setCellValue(order.getArd());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(5);
			cell.setCellValue(order.getAccountManager());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(6);
			cell.setCellValue(order.getClient());
			cell.setCellStyle(cellStyle);
			
			//populate schedules
			for (int j = 0; j < order.getSchedule().size(); j++) {
				//fill in info
				cell = row.createCell(0);
				cell.setCellValue(order.getSchedule().get(j).getStartDate().toString());
				cell.setCellStyle(cellStyle);

				cell = row.createCell(1);
				cell.setCellValue(order.getSchedule().get(j).getEndDate().toString());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(2);
				cell.setCellValue(order.getSchedule().get(j).getRemarks());
				cell.setCellStyle(cellStyle);
				
				//go to next row
				currentRow++;
				row = sheet.createRow(currentRow);
			}
			
			//go back to the start of the order
			itemFinalRow = currentRow;
			currentRow = itemFirstRow;
			
			row = sheet.getRow(currentRow);
			
			//populate items
			for (OrderItem item : order.getItems()) {
				cell = row.createCell(7);
				cell.setCellValue(item.toString());
				cell.setCellStyle(itemStyle);
				
				cell = row.createCell(8);
				cell.setCellValue(item.getRemarks());
				cell.setCellStyle(itemStyle);
				
				cell = row.createCell(9);
				cell.setCellValue(item.getStatus());
				cell.setCellStyle(itemStyle);
				
				//go to next row
				currentRow++;
				row = sheet.getRow(currentRow);
				//create if it doesn't exist yet
				if (row == null) row = sheet.createRow(currentRow);
				
				//fill out the serial number
				cell = row.createCell(7);
				cell.setCellValue(String.format("SN#: %s", item.getSerial()));
				cell.setCellStyle(itemStyle);
				
				//fill in parts if it has any
				if (item.isUnit()) {
					for (OrderItemPart part : item.getParts()) {
						//go to next row
						currentRow++;
						row = sheet.getRow(currentRow);
						//create if it doesn't exist yet
						if (row == null) row = sheet.createRow(currentRow);
						
						cell = row.createCell(7);
						cell.setCellValue(part.getName());
						cell.setCellStyle(itemStyle);
					}
				}
				
				//go to next row
				currentRow++;
				row = sheet.getRow(currentRow);
				//create if it doesn't exist yet
				if (row == null) row = sheet.createRow(currentRow);
			}
			
			/*
			if (i > 0) {
				cellStyle.setBorderTop(BorderStyle.THIN);
			}
			for (int j = 0; j<NUMBER_OF_COLUMNS; j++) {
				row.getCell(j).setCellStyle(cellStyle);
			}
			*/
			
			//set currentRow to whichever is higher between the current row and the deepest row so far
			currentRow = (currentRow > itemFinalRow) ? currentRow : itemFinalRow;
			//row = sheet.getRow(currentRow);
			//if (row == null) row = sheet.createRow(currentRow);
			
			//fill in all the rows with cell borders
			for (int k = itemFirstRow; k < currentRow+1; k++) {
				row = sheet.getRow(k);
				if (row == null) row = sheet.createRow(k);
				
				for (int j = 0; j<7; j++) {
					cell = row.getCell(j);
					if (cell == null) cell = row.createCell(j);
					
					cell.setCellStyle(cellStyle);
				}
				for (int j = 7; j<9; j++) {
					cell = row.getCell(j);
					if (cell == null) cell = row.createCell(j);
					
					cell.setCellStyle(itemStyle);
				}
				cell = row.getCell(9);
				if (cell == null) cell = row.createCell(9);
				
				cell.setCellStyle(cellStyle);
			}
			
			row.getCell(0).setCellValue("** NOTHING FOLLOWS **");
			//skip 1 row, start on the one after
			currentRow += 3;
		}
		//finish data rows//
		
		//resize the columns
		for (int k = 0; k < NUMBER_OF_COLUMNS - 1; k ++) {
			if (k < NUMBER_OF_COLUMNS - 2) sheet.setColumnWidth(k, maxNumChar);
			else sheet.setColumnWidth(k, ((int)(20 * 1.14388)) * 256);
		}
		//loop ends here//
		
		try {
			workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}
