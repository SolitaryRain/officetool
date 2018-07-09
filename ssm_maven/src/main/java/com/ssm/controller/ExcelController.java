package com.ssm.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

@Controller
@RequestMapping("/excel")
public class ExcelController {
	@Resource
	private IExcelService excelService;
	
	private DateFormat folderDf = new SimpleDateFormat("yyyyMMdd");
	
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
		 String randomNum = new SimpleDateFormat("HHmmss").format(new Date());
		User u = (User) request.getSession().getAttribute("user");
		
		rootPath = rootPath+File.separator+u.getUsername()+randomNum+postFix;
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(rootPath));
			
			ExcelUtil.calculateAvg(u.getUsername()+randomNum+postFix);
			User user = (User) request.getSession().getAttribute("user");
			FileEntity entity = new FileEntity();
			entity.setUpPath(rootPath);
			entity.setUpName(user.getUsername());
			excelService.save(entity);
			map.put("message", "Y");// 文件上传成功
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", e.getMessage());// 文件上传失败
			new File(rootPath).delete();
		}
		return map;
	}
	
	@RequestMapping(value = "/merge", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> mergeExcel(@RequestParam Map<String, Object> map) {  
		
		try {
			ExcelUtil.mergeExcel(folderDf.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("mergeFlag", true);
		return map;
	}
	
	
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
}
