package com.ssm.controller;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ssm.entity.User;
import com.ssm.service.IUserService;
import com.ssm.utils.ExcelUtil;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private IUserService userService;

	/**
	 * 
	 * @param req
	 * @param mv
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest req, ModelAndView mv) {

		Map<String, String> map = new HashMap<String, String>();
		Object tokenObj = req.getSession().getAttribute("userToken"); 
		if(tokenObj!=null){
			mv.addObject("checkMsg","已有账户登录,重启一下浏览器");
			mv.setViewName("login");
			return mv;
		}
		/*Object sessionObj = req.getSession().getAttribute("user");
	    if(sessionObj!=null) {   
	    	return new ModelAndView("redirect:/user/home");
	    }*/ 
		String username =  req.getParameter("username");
		if(StringUtils.isBlank(username)){
			mv.setViewName("login");
			return mv;
		}
		map.put("username", req.getParameter("username").toUpperCase());
		map.put("password", req.getParameter("password"));
		User user = userService.login(map);
		if (user != null) {
			DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			String tokenStr = buildToken(req.getParameter("username")+df.format(new Date()),"UTF-8");
			req.getSession().setAttribute("userToken",tokenStr);
			req.getSession().setAttribute("user", user);
			return new ModelAndView("redirect:/user/home");
			
		}else{
			mv.addObject("checkMsg","账户密码验证失败");
			mv.setViewName("login");
		}
		return mv;

	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest req, ModelAndView mv) {
		req.getSession().removeAttribute("user");
		req.getSession().removeAttribute("userToken");
		mv.setViewName("login");
		return mv;

	}
	
	/**
	 * 
	 * @param req
	 * @param mv
	 * @return
	 */
	@RequestMapping("/home")
	public ModelAndView toHome(HttpServletRequest req, ModelAndView mv) {
		mv.setViewName("index");
		return mv;

	}
	
	@RequestMapping(value = "/revisePw", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public  Map<String, Object> revisePw(HttpServletRequest req) {  
		 Map<String, Object> map = new HashMap<>();
		String password = req.getParameter("password");
		User u = (User) req.getSession().getAttribute("user");
		u.setPassword(password);
		u.setStatus(1);
		userService.activate(u);
		return map;
	}
	
	private  String buildToken(String origin ,String charsetName){
		if(origin == null )
			return null ;
		
		StringBuilder sb = new StringBuilder() ;
		MessageDigest digest = null ;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
		
		//生成一组length=16的byte数组
		byte[] bs = digest.digest(origin.getBytes(Charset.forName(charsetName))) ;
		
		for (int i = 0; i < bs.length; i++) {
			int c = bs[i] & 0xFF ; //byte转int为了不丢失符号位， 所以&0xFF
			if(c < 16){ //如果c小于16，就说明，可以只用1位16进制来表示， 那么在前面补一个0
				sb.append("0");
			}
			sb.append(Integer.toHexString(c)) ;
		}
		return sb.toString() ;
	}


}
