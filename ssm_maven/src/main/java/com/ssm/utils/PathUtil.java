package com.ssm.utils;

import org.apache.commons.lang.StringUtils;

public class PathUtil {

	private static String PATH;
	
	public final static String EXCELFOLDERPATH = "ExcelFile";
	
	static {
		String str0 = new PathUtil().getClass().getClassLoader()
				.getResource("/").getPath();
		
		String str1 = StringUtils.substringAfter(str0, "/");
		
		String str2 = StringUtils.substringBefore(str1, "/WEB-INF");
		
		PATH = str2;
		
	}
 
	/**
	 * 获取web 应用的名字
	 * 
	 * @return web 应用名字 exceldev
	 */
	public static String getWebName() {
		return StringUtils.substringAfterLast(PATH, "/");
	}
 
	/**
	 * 获取web 应用所在服务器磁盘的目录
	 * 
	 * @return E:/JavaSoft/apache-tomcat-7.0.88/apache-tomcat-7.0.88/webapps
	 */
	public static String getWebLocDir() {
		return StringUtils.substringBeforeLast(PATH, "/");
	}
	
	/**
	 * 获取web 应用所在服务器磁盘盘符 
	 * @return D:
	 */
	public static String getWebDriveDir(){
		return  StringUtils.substringBefore(PATH, "/");
	}
		
 
	/**
	 * 获取web 应用的物理路径
	 * 
	 * @return 字符串：E:/JavaSoft/apache-tomcat-7.0.88/apache-tomcat-7.0.88/webapps/exceldev
	 */
	public static String getWebLocPath() {
		return PATH;
	}
 
}

