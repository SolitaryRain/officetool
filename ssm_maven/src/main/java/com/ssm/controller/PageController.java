package com.ssm.controller;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ssm.entity.FileEntity;
import com.ssm.entity.User;
import com.ssm.service.IExcelService;
import com.ssm.utils.PathUtil;

@Controller
@RequestMapping("/page")
public class PageController {

	@Resource
	private IExcelService excelService;
	
	
	@RequestMapping("/redir")
	public ModelAndView redirectingPage(HttpServletRequest req, ModelAndView mv) {
		
		String pageFlag = req.getQueryString();
		if(StringUtils.isNotBlank(pageFlag)){
			mv.setViewName(pageFlag);
			if(StringUtils.equals("index_v1", pageFlag)){
				Map<String,FileEntity> excelMap = new HashMap<String, FileEntity>();
				excelMap.put("A", new FileEntity(false));
				excelMap.put("S2", new FileEntity(false));
				excelMap.put("S4", new FileEntity(false));
				excelMap.put("D", new FileEntity(false));
				excelMap.put("Z", new FileEntity(false));
				excelMap.put("C", new FileEntity(false));
				excelMap.put("J1", new FileEntity(false));
				excelMap.put("J2", new FileEntity(false));
				excelMap.put("J5", new FileEntity(false));
				excelMap.put("S1", new FileEntity(false));
				excelMap.put("S3", new FileEntity(false));
				excelMap.put("S6", new FileEntity(false));
				excelMap.put("S5", new FileEntity(false));
				excelMap.put("SY", new FileEntity(false));
				excelMap.put("J4", new FileEntity(false));
				excelMap.put("J3", new FileEntity(false));
				excelMap.put("R3", new FileEntity(false));
				excelMap.put("SF", new FileEntity(false));
				excelMap.put("E", new FileEntity(false));
				excelMap.put("R1", new FileEntity(false));
				excelMap.put("F", new FileEntity(false));
				excelMap.put("N1", new FileEntity(false));
				excelMap.put("N2", new FileEntity(false));
				excelMap.put("N3", new FileEntity(false));
				excelMap.put("N5", new FileEntity(false));
				excelMap.put("N6", new FileEntity(false));
				List<FileEntity> feList = excelService.queryList();
				for(FileEntity fe:feList){
					if(excelMap.containsKey(fe.getUpName())){
						fe.setUpFlag(true);
						fe.setUpPath(fe.getUpPath().substring(StringUtils.lastIndexOf(fe.getUpPath(), "\\")));
						excelMap.put(fe.getUpName(), fe);
					}
				}
				mv.addObject("shopMap",excelMap);
				
				User u = (User) req.getSession().getAttribute("user");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				FileEntity fe = null;
				if(StringUtils.equals("admin", u.getUsername())){
					fe = excelService.queryCount();
					if(fe!=null){
						DecimalFormat dfMath = new DecimalFormat("#.00");
						fe.setTotalActual(dfMath.format(fe.getActual()));
						fe.setTotalExpect(dfMath.format(fe.getExpect()));
						fe.setCountNum(fe.getNum());
					}
				}else{
					fe = excelService.queryEntity(u.getUsername(), df.format(new Date()));
				}
				if(null!=fe)
					mv.addObject("shop", fe);
			}
		}
		return mv;
		
	}
}
