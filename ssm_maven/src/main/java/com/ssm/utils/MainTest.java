package com.ssm.utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	          
		File file = new File("HelloWorld.java");
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") );
        PathUtil pu = new PathUtil();
        System.out.println(PathUtil.getWebDriveDir()); 
        System.out.println(PathUtil.getWebLocDir()); 
        System.out.println(PathUtil.getWebLocPath()); 
        System.out.println(PathUtil.getWebName()); 
	    }  
}