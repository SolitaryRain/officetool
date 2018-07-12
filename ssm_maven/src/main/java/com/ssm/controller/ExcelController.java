package com.ssm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ssm.entity.FileEntity;
import com.ssm.entity.User;
import com.ssm.service.IExcelService;
import com.ssm.utils.ExcelUtil;
import com.ssm.utils.PathUtil;

/**
 * Excel处理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {
	@Resource
	private IExcelService excelService;
	
	private DateFormat folderDf = new SimpleDateFormat("yyyyMMdd");
	
	
	/**
	 * 上传
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deal", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> saveExcel(@RequestParam(value = "file") MultipartFile file,	HttpServletRequest request) {  
		Map<String, Object> map = new HashMap<String, Object>();
		String postFix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		File countFile = new File(PathUtil.getWebDriveDir()+ File.separator+PathUtil.EXCELFOLDERPATH+ File.separator+"COUNT_"+folderDf.format(new Date())+postFix);
		if(countFile.exists()){
			map.put("message", "E");// 文件上传失败
			return map;
		}
	
		String rootPath = PathUtil.getWebDriveDir()+ File.separator+PathUtil.EXCELFOLDERPATH+ File.separator+folderDf.format(new Date());
		File folfer =new File(rootPath);
		 if  (!folfer .exists()  && !folfer .isDirectory())      
		     folfer .mkdir(); 
		User u = (User) request.getSession().getAttribute("user");
		
		String FileServerName = u.getUsername()+postFix;
		if(StringUtils.equals("admin", u.getUsername()))
			FileServerName = file.getOriginalFilename();
		
		//String randomNum = new SimpleDateFormat("HHmmss").format(new Date());
		rootPath = rootPath+File.separator+FileServerName;
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(rootPath));
			
			FileEntity entity = ExcelUtil.calculateAvg(FileServerName);
			User user = (User) request.getSession().getAttribute("user");
			entity.setUpPath(rootPath);
			entity.setUpName(user.getUsername());
			if(StringUtils.equals("admin", u.getUsername()))
				entity.setUpName(file.getOriginalFilename().subSequence(0, file.getOriginalFilename().lastIndexOf(".")).toString());
			excelService.save(entity); //入库
			map.put("message", "Y");// 文件上传成功
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", e.getMessage());// 文件上传失败
			new File(rootPath).delete();
		}
		return map;
	}
	
	/**
	 * 合并导出
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/merge", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> mergeExcel(@RequestParam Map<String, Object> map) throws Exception {  
        //获取该目录下的所有文件  
		map.put("fileNum", ExcelUtil.mergeExcel(folderDf.format(new Date())));
		map.put("mergeFlag", true);
		return map;
	}
	
	
	/**
	 * 下载文件
	 * @param req
	 * @return
	 * @throws IOException
	 */
    @RequestMapping("download")  
    public ResponseEntity<byte[]> download(HttpServletRequest req) throws IOException {  
    	String rootPath = PathUtil.getWebDriveDir()+ File.separator+PathUtil.EXCELFOLDERPATH;
    	String fileName = req.getParameter("fileName");
    	File file = null;
    	if(StringUtils.isBlank(fileName)){
    		fileName=new String(("COUNT_"+folderDf.format(new Date())+".xlsx").getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
    	}else if(StringUtils.equals("initLogin", fileName)){
    		rootPath = PathUtil.getWebLocPath()+File.separator+"tempFile";
    		fileName = new String("templet.xlsx".getBytes("UTF-8"),"iso-8859-1");
    	}else{
    		fileName= folderDf.format(new Date())+File.separator+fileName;
    	}
		file=new File(rootPath+File.separator+fileName);
        HttpHeaders headers = new HttpHeaders();  
        headers.setContentDispositionFormData("attachment", fileName); 
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); 
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),  
                                          headers, HttpStatus.CREATED);  
    }  
    
    /**
	 * 晓霞汇总上传 (临时使用)
	 * @param file
	 * @param request
	 * @return
     * @throws IOException 
	 */
	@RequestMapping(value = "/tempdeal", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	@Deprecated
	public Map<String, Object> tempdeal(@RequestParam(value = "file[]") MultipartFile[] files,	HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Float> countMap = new HashMap<String, Float>();
		DecimalFormat decimalFormat = new DecimalFormat(".000");
		for (MultipartFile file : files) {
			Workbook fromExcel = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = fromExcel.getSheetAt(0);
			String vale_12;
			for(int i=4;i<16;i++){
				vale_12 = ExcelUtil.getCellValue(sheet.getRow(11).getCell(i));
				if(StringUtils.isBlank(vale_12))
					vale_12="0";
				String mapKey = "count_12_"+i;
				if(!countMap.containsKey(mapKey))
					countMap.put(mapKey, 0.00f);
				float countValue = countMap.get(mapKey)+Float.parseFloat(vale_12);
				countMap.put(mapKey, countValue);
			}
			
			String vale_13;
			for(int i=4;i<16;i++){
				vale_13 = ExcelUtil.getCellValue(sheet.getRow(12).getCell(i));
				if(StringUtils.isBlank(vale_13))
					vale_13="0";
				String mapKey = "count_13_"+i;
				if(!countMap.containsKey(mapKey))
					countMap.put(mapKey, 0.00f);
				float countValue = countMap.get(mapKey)+Float.parseFloat(vale_13);
				countMap.put(mapKey, countValue);
			}
			fromExcel.close();
		}
		
		XSSFWorkbook newExcelCreat = new XSSFWorkbook(); 
		Sheet  newSheet = newExcelCreat.createSheet("count");
		Cell cell =null;
		for(int j = 0;j<2;j++){
			newSheet.createRow(j);
			for(int i = 4;i<16;i++){
				cell = newSheet.getRow(j).getCell(i);
				if(cell==null)
					cell = newSheet.getRow(j).createCell(i);
				String strIndex  = "count_"+(j+12)+"_"+i;
				cell.setCellValue(decimalFormat.format(countMap.get(strIndex)));
			}
		}
		String allFileName="E:"+File.separator+"temp_"+folderDf.format(new Date())+".xlsx";
		 FileOutputStream fileOut = new FileOutputStream(allFileName);
		 newExcelCreat.write(fileOut); 
		 fileOut.flush(); 
		 fileOut.close();
		 map.put("message", "Y");// 文件上传成功
		return map;
	}
}
