package com.ssm.service.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
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
	public List<FileEntity> queryList() {
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
		
		FileEntityExample example = new FileEntityExample();
		example.createCriteria()
		.andUpNameEqualTo(entity.getUpName())
		.andUpDateEqualTo(entity.getUpDate());
		
		if(CollectionUtils.isEmpty(fileEntityMapper.selectByExample(example))){
			fileEntityMapper.insert(entity);
		}else{
			fileEntityMapper.updateByExampleSelective(entity, example);
		}
	}

	@Override
	public FileEntity queryEntity(String userName, String date) {
		FileEntityExample example = new FileEntityExample();
		example.createCriteria()
		.andUpNameEqualTo(userName)
		.andUpDateEqualTo(date);
		List<FileEntity> fileList = fileEntityMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(fileList))
			return null;
		return fileList.get(0);
	}

	@Override
	public FileEntity queryCount() {
		
		return fileEntityMapper.selectCount();
	}

	

}
