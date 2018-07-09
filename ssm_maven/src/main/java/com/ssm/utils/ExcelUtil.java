package com.ssm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	private static String ROOTPATH = PathUtil.getWebDriveDir()+ File.separator+PathUtil.EXCELFOLDERPATH+ File.separator;
	private static DateFormat folderDf = new SimpleDateFormat("yyyyMMdd");
	public class XSSFDateUtil extends DateUtil {  

    } 
	
	/**
	 * 合并Excel
	 * @throws Exception
	 */
	public static void mergeExcel(String folderPath) throws Exception{
		//生成countSheet
		XSSFWorkbook newExcelCreat = new XSSFWorkbook(); 
		Sheet  newSheet = newExcelCreat.createSheet("count");
		 //创建File对象  
        File dirFile = new File(ROOTPATH+folderDf.format(new Date()));  
        //获取该目录下的所有文件  
        String[] fileNameList = dirFile.list();
		for(String fromExcelName:fileNameList) {//遍历每个源excel文件，fileNameList为源文件的名称集合
		     InputStream in = new FileInputStream(ROOTPATH+folderDf.format(new Date())+ File.separator+fromExcelName);
		     XSSFWorkbook fromExcel = new XSSFWorkbook(in);
			 Sheet  oldSheet = fromExcel.getSheetAt(fromExcel.getNumberOfSheets()-1);;
			 copySheet(newExcelCreat, oldSheet,newSheet);
		 }
		 String allFileName=ROOTPATH+File.separator+"COUNT_"+folderDf.format(new Date())+".xlsx";
		 FileOutputStream fileOut = new FileOutputStream(allFileName);
		 newExcelCreat.write(fileOut); 
		 fileOut.flush(); 
		 fileOut.close();
		 //删除各个源文件
		 /*for(String fromExcelName:fileNameList) {//遍历每个源excel文件
		     File file=new File(tempUrl+File.separator+fromExcelName);
		     if(file.exists()){
		         file.delete();
		     } 
		 }*/
	}
	
	/**
	 * 拷贝样式
	 * @param fromStyle
	 * @param toStyle
	 */
	public static void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {

        toStyle.cloneStyleFrom(fromStyle);//此一行代码搞定 
    }  
	
	/**
	 * 复制 合并的单元格
	 * @param fromSheet
	 * @param toSheet
	 */
    public static void mergeSheetAllRegion(Sheet  fromSheet, Sheet  toSheet) {//合并单元格
        int num = fromSheet.getNumMergedRegions();
        CellRangeAddress cellR = null;
        int rowNum=toSheet.getLastRowNum();
        //下延  留总计
        if(rowNum!=0)
        	rowNum=rowNum+1;
        for (int i = 0; i < num; i++) {
            cellR = fromSheet.getMergedRegion(i);
            cellR.setFirstRow(cellR.getFirstRow()+rowNum);
            cellR.setLastRow(cellR.getLastRow()+rowNum);
            try{
            	toSheet.addMergedRegion(cellR);
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
    } 

    /**
     * 拷贝普通单元格
     * @param wb
     * @param fromCell
     * @param toCell
     */
    public static void copyCell(Workbook wb,Cell fromCell, Cell toCell) {  
        CellStyle newstyle=wb.createCellStyle();  
        copyCellStyle(fromCell.getCellStyle(), newstyle);  
        //toCell.setEncoding(fromCell.getEncoding());  
        //样式  
        toCell.setCellStyle(newstyle);   
        if (fromCell.getCellComment() != null) {  
            toCell.setCellComment(fromCell.getCellComment());  
        }  
        // 不同数据类型处理  
        int fromCellType = fromCell.getCellType();  
        toCell.setCellType(fromCellType);  
        if (fromCellType == Cell.CELL_TYPE_NUMERIC) {  
            if (XSSFDateUtil.isCellDateFormatted(fromCell)) {  
                    toCell.setCellValue(fromCell.getDateCellValue());  
                } else {  
                    toCell.setCellValue(fromCell.getNumericCellValue());  
                }  
            } else if (fromCellType == Cell.CELL_TYPE_STRING) {  
                toCell.setCellValue(fromCell.getRichStringCellValue());  
            } else if (fromCellType == Cell.CELL_TYPE_BLANK) {  
                // nothing21  
            } else if (fromCellType == Cell.CELL_TYPE_BOOLEAN) {  
                toCell.setCellValue(fromCell.getBooleanCellValue());  
            } else if (fromCellType == Cell.CELL_TYPE_ERROR) {   
                toCell.setCellErrorValue(fromCell.getErrorCellValue());  
            } else if (fromCellType == Cell.CELL_TYPE_FORMULA) {  
                toCell.setCellFormula(fromCell.getCellFormula());  
            } else { // nothing29  
            }  

    }  

    public static void copyRow(Workbook wb,Row oldRow,Row toRow){  
        toRow.setHeight(oldRow.getHeight());
        for (Iterator cellIt = oldRow.cellIterator(); cellIt.hasNext();) {  
            Cell tmpCell = (Cell) cellIt.next();  
            Cell newCell = toRow.createCell(tmpCell.getColumnIndex());  
            copyCell(wb,tmpCell, newCell);  
        }  
    }  
    
    /**
     * 拷贝sheet
     * @param wb
     * @param fromSheet
     * @param toSheet
     */
    public static void copySheet(Workbook wb,Sheet  fromSheet, Sheet  toSheet) {   
        mergeSheetAllRegion(fromSheet, toSheet);     
        int rowNum=toSheet.getLastRowNum();
        //下延 留总计
        if(rowNum!=0)
        	rowNum=rowNum+1;
        for(int i=0;i<=fromSheet.getRow(fromSheet.getFirstRowNum()).getLastCellNum();i++){ 
            toSheet.setColumnWidth(i+rowNum,fromSheet.getColumnWidth(i)); 
        } 
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {  
            Row oldRow = (Row) rowIt.next(); 
            Row newRow = toSheet.createRow(oldRow.getRowNum()+rowNum); 
            copyRow(wb,oldRow,newRow);  
        }  
    }

    /**
     * 求和平均数计算
     * @throws IOException
     */
	public static void calculateAvg(String filePath) throws IOException {
		//保留两位小数
		DecimalFormat decimalFormat = new DecimalFormat(".000");
		InputStream in = new FileInputStream(ROOTPATH+File.separator+folderDf.format(new Date())+ File.separator +filePath);

		Workbook fromExcel = new XSSFWorkbook(in);
		//判断文件格式 
	    /*boolean isExcel2003 = filePath.toLowerCase().endsWith("xls")?true:false;
		if(isExcel2003){
			fromExcel = new HSSFWorkbook(in);
		}else{
			fromExcel = new XSSFWorkbook(in);
		}*/
	    Sheet sheet = fromExcel.getSheetAt(fromExcel.getNumberOfSheets()-1);
	    
	    int rowNum = 0;
    	//rowNum =  countRowNum(sheet);
	    rowNum = sheet.getLastRowNum();
    	//检查
	    String valString_9 = getCellValue(sheet.getRow(1).getCell(9));
	    String valString_10 = getCellValue(sheet.getRow(0).getCell(10));
	    String valString_13 = getCellValue(sheet.getRow(1).getCell(13));
	    String valString_14 = getCellValue(sheet.getRow(0).getCell(14));
	    String formMsg=null;
	    if(valString_9==null||valString_9!=""){
	    	formMsg = "格式不对,J列为计算平均总金额,需要留空";
	    }
	    if(valString_10==null){
	    	formMsg = "格式不对,K列必须为总金额";
	    }
	    if(valString_13==null||valString_13!=""){
	    	formMsg = "格式不对,N列为计算平均预计可得,需要留空";
	    }
	    if(valString_14==null){
	    	formMsg = "格式不对,O列必须为预计可得金额";
	    }
	    if(StringUtils.isNotBlank(formMsg))
	    	throw new NumberFormatException(formMsg);	
	    
	    int num = sheet.getNumMergedRegions();
        CellRangeAddress cellR = null;
        //合并单元格集合 首行,尾行
        Map<Integer,Integer> cosRowNum = new  HashMap<Integer,Integer>();
        
        //处理合并的单元格计算
        
	    for (int i = 0; i < num; i++) {
	    	try{
	            cellR = sheet.getMergedRegion(i);
	            int columnNum = cellR.getFirstColumn();
	            int firstRow = cellR.getFirstRow();
	            int lastRow = cellR.getLastRow();
	            if(columnNum==10){
	            	int colNum = cellR.getLastRow() - firstRow +1;
	            	Row fRow = sheet.getRow(firstRow);
					Cell fCell_10 = fRow.getCell(columnNum);
					Cell fCell_14 = fRow.getCell(columnNum+4);
					float  countValue_10 = Float.parseFloat(getCellValue(fCell_10));
					float  countValue_14 = Float.parseFloat(getCellValue(fCell_14));
					float countNum = 0;
					float avgValue_10 ; 
					float avgValue_14 ; 
					for(int j = 0;j<colNum;j++){
						cosRowNum.put(firstRow, lastRow);
						countNum+=Float.parseFloat(getCellValue(sheet.getRow(firstRow+j).getCell(columnNum-2)));
					}
					
					
					avgValue_10 = countValue_10/countNum;
					avgValue_14 = countValue_14/countNum;
					Cell cell ;
					//计算平均总金额和平均预计所得  数量*平均值
					for(int j = 0;j<colNum;j++){
						float cellValue_10 = Float.parseFloat(getCellValue(sheet.getRow(firstRow+j).getCell(columnNum-2)))*avgValue_10;
						float cellValue_14 = Float.parseFloat(getCellValue(sheet.getRow(firstRow+j).getCell(columnNum-2)))*avgValue_14;
						
						cell = sheet.getRow(firstRow+j).getCell(columnNum-1);
						if(cell==null)
							cell = sheet.getRow(firstRow+j).createCell(columnNum-1);
						cell.setCellValue(decimalFormat.format(cellValue_10));
						
						cell = sheet.getRow(firstRow+j).getCell(columnNum+3);
						if(cell==null)
							cell = sheet.getRow(firstRow+j).createCell(columnNum+3);
						cell.setCellValue(decimalFormat.format(cellValue_14));
					}
	            }
	        }catch(NumberFormatException e){
	    		String errorMsg ="未知异常信息"+e.getMessage();
	    		if(StringUtils.contains(e.getMessage(), ":"))
	    			errorMsg= "第"+i+"行的数据"+e.getMessage().substring(e.getMessage().lastIndexOf(":"))+"不符合要求";
	    		if(StringUtils.contains(e.getMessage(), "empty"))
	    			errorMsg= "表格总行数为:"+rowNum+",第"+(i+1)+"行的数据出现空值不符合要求";
	    		throw new NumberFormatException(errorMsg);
	    	}catch(NullPointerException e){
	    		String errorMsg = "第"+(i+1)+"行的数据不符合要求,有单元格为空值";
	    		e.printStackTrace();
	    		throw new NullPointerException(errorMsg);
	    	}
	    }
	    
	    //处理普通单元格计算
	    int sheetCountNum = 0; //总计
	    float sheetcountValue_10=0;
	    float sheetcountValue_14=0;
	    int lastRow = 0;
	    for(int i = 1;i < rowNum;i++){
	    	
	    	try{
	    		//每行金额数 不排除合并的
	    		int singleNum = (int) Float.parseFloat(getCellValue(sheet.getRow(i).getCell(8)));
		    	float singleValue_10=0;
		    	if(StringUtils.isNotEmpty(getCellValue(sheet.getRow(i).getCell(10))))
		    		  singleValue_10 = Float.parseFloat(getCellValue(sheet.getRow(i).getCell(10)));
		    	
		    	float singleValue_14=0;
		    	if(StringUtils.isNotEmpty(getCellValue(sheet.getRow(i).getCell(14))))
		    		  singleValue_14 = Float.parseFloat(getCellValue(sheet.getRow(i).getCell(14)));
		    	
		    	sheetcountValue_10+=singleValue_10;
		    	sheetcountValue_14+=singleValue_14;
		    	sheetCountNum+=singleNum;
		    	
		    	//普通单元格复制，需要排除合并的
		    	if(cosRowNum.containsKey(i)){
		    		lastRow = cosRowNum.get(i);
		    		continue;
		    	}
		    	
		    	//处理单元格重复值问题
		    	if(lastRow!=0){
		    		if(singleValue_10!=0||singleValue_14!=0)
		    			throw new IndexOutOfBoundsException("第"+(i+1)+"行金额数据异常,怀疑是合并单元格存在两个值");
		    		if(lastRow==i)
		    			lastRow=0;
		    		continue;
		    	}
		    	
		    	
				Cell cell = sheet.getRow(i).getCell(9);
				if(cell==null)
					cell =  sheet.getRow(i).createCell(9);
				sheet.getRow(i).getCell(9).setCellValue(decimalFormat.format(singleValue_10));//平均总计所得
				
				cell = sheet.getRow(i).getCell(13);
				if(cell==null)
					cell =  sheet.getRow(i).createCell(13);
				sheet.getRow(i).getCell(13).setCellValue(decimalFormat.format(singleValue_14));//平均预计所得
	    	}catch(NumberFormatException e){
	    		String errorMsg ="未知异常信息"+e.getMessage();
	    		if(StringUtils.contains(e.getMessage(), ":"))
	    			errorMsg= "第"+(i+1)+"行的数据"+e.getMessage().substring(e.getMessage().lastIndexOf(":"))+"不符合要求";
	    		if(StringUtils.contains(e.getMessage(), "empty"))
	    			errorMsg= "表格总行数为:"+rowNum+",第"+(i+1)+"行的数据出现空值不符合要求";
	    		e.printStackTrace();
	    		throw new NumberFormatException(errorMsg);
	    	}catch(NullPointerException e){
	    		String errorMsg = "第"+(i+1)+"行的数据不符合要求,有单元格为空值";
	    		throw new NullPointerException(errorMsg);
	    	}
	    }
	    
	    //处理最后一行总计
	    Row row = sheet.getRow(rowNum);
	    Cell cell = row.getCell(10);
	    if(cell==null)
	    	cell =  row.createCell(10);
	    cell.setCellValue(sheetCountNum);
	    
	    row = sheet.getRow(rowNum);
	    cell = row.getCell(9);
	    if(cell==null)
	    	cell =  row.createCell(9);
	    cell.setCellValue(decimalFormat.format(sheetcountValue_10));
	    
	    row = sheet.getRow(rowNum);
	    cell = row.getCell(10);
	    if(cell==null)
	    	cell =  row.createCell(10);
	    if(cell.getCellType() == Cell.CELL_TYPE_FORMULA)
	    	throw new NumberFormatException("第"+(rowNum+1)+"行K列总金额不需要求和,把求和公式删了");
	    cell.setCellValue(decimalFormat.format(sheetcountValue_10));
	    
		row = sheet.getRow(rowNum);
	    cell = row.getCell(13);
	    if(cell==null)
	    	cell =  row.createCell(13);
	    cell.setCellValue(decimalFormat.format(sheetcountValue_14));
		
		row = sheet.getRow(rowNum);
	    cell = row.getCell(14);
	    if(cell==null)
	    	cell =  row.createCell(14);
	    cell.setCellValue(decimalFormat.format(sheetcountValue_14));
		
		row = sheet.getRow(rowNum);
	    cell = row.getCell(1);
	    if(cell==null)
	    	cell =  row.createCell(1);
	    cell.setCellValue(recursionCode(sheet,rowNum));
	    
	    FileOutputStream out = null;
        out = new FileOutputStream(ROOTPATH+File.separator+folderDf.format(new Date())+ File.separator +filePath);
        fromExcel.write(out);
        out.flush();
        out.close();
        in.close();
	}  
	
	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell){
		
		if(cell == null) return "";
		
		if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			
			return cell.getStringCellValue().trim();
			
		}else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
			
			return String.valueOf(cell.getBooleanCellValue()).trim();
			
		}else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
			
			return cell.getCellFormula() ;
			
		}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			
			return String.valueOf(cell.getNumericCellValue()).trim();
			
		}
		
		return "";
	}
	
	/**
	 * 计算总行数
	 * @param sheet
	 * @return
	 */
	private static int countRowNum(Sheet sheet){
		int rowNum = sheet.getLastRowNum();
		Row row = sheet.getRow(rowNum);
		if(row==null)
			throw new NullPointerException("格式错误,总行数"+rowNum);
		Cell cell = sheet.getRow(rowNum-1).getCell(0);
		if(StringUtils.contains(getCellValue(cell), "总计")){
			return rowNum;
		}else{
			throw new NullPointerException("格式错误,总行数"+rowNum+",所以第"+(rowNum-1)+"行必须为总计");
		}
	}
	
	/**
	 * 获取店铺代码
	 * @param sheet
	 * @param rowNum
	 * @return
	 */
	private static String recursionCode(Sheet sheet,int rowNum){
		String shopCode = getCellValue(sheet.getRow(rowNum-1).getCell(1));
		if(shopCode!="")
			return shopCode;
		 return recursionCode(sheet,rowNum-1);
	}


}

