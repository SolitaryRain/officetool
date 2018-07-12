package com.ssm.service;

import java.util.List;

import com.ssm.entity.FileEntity;

public interface IExcelService {

	/**
	 * 查询当天所有店铺数据
	 */
	List<FileEntity> queryList();

	/**
	 * 保存计算后的数据
	 * @param entity
	 */
	void save(FileEntity entity);

	/**
	 * 查询店铺当日数据
	 */
	FileEntity queryEntity(String userName,String date);

	/**
	 * 查询当日总计
	 * @return
	 */
	FileEntity queryCount();
}
