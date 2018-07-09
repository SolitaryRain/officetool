package com.ssm.service;

import java.util.List;

import com.ssm.entity.FileEntity;

public interface IExcelService {

	List<FileEntity> query();

	void save(FileEntity entity);

}
