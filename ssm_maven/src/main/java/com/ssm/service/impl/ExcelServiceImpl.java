package com.ssm.service.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssm.dao.FileEntityMapper;
import com.ssm.entity.FileEntity;
import com.ssm.entity.FileEntityExample;
import com.ssm.service.IExcelService;


@Service("ExcelService")
public class ExcelServiceImpl implements IExcelService {
	
	@Resource
	private FileEntityMapper fileEntityMapper;

	@Override
	public List<FileEntity> query() {
		FileEntityExample example = new FileEntityExample();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dataStr = df.format(new Date());
		example.createCriteria().andUpDateEqualTo(dataStr);
		return fileEntityMapper.selectByExample(example);
	}

	@Override
	public void save(FileEntity entity) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
		entity.setUpDate(df.format(new Date()));
		entity.setUpTime(df2.format(new Date()));
		fileEntityMapper.insert(entity);
	}

	

}
