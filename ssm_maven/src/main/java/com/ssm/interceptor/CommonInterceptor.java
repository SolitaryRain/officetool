package com.ssm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CommonInterceptor implements HandlerInterceptor  {  
	  private final String ADMINSESSION = "userToken";  
	  //拦截前处理  
	  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {  
		if(StringUtils.contains(request.getRequestURI(), "login"))
			return true;
	    Object sessionObj = request.getSession().getAttribute(ADMINSESSION);  
	    if(sessionObj!=null) {   
	      return true;  
	    }
	    
	    response.sendRedirect("/exceldev");  
	    return false;  
	  }  
	  //拦截后处理  
	  @Override
	  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception { }  
	  
	  //全部完成后处理  
	  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception { }
	
	}  