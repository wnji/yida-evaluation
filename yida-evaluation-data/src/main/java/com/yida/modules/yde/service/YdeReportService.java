/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeReport;
import com.yida.modules.yde.dao.YdeReportDao;

/**
 * 测评报告Service
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly=true)
public class YdeReportService extends CrudService<YdeReportDao, YdeReport> {
	
	/**
	 * 获取单条数据
	 * @param ydeReport
	 * @return
	 */
	@Override
	public YdeReport get(YdeReport ydeReport) {
		return super.get(ydeReport);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeReport 查询条件
	 * @return
	 */
	@Override
	public Page<YdeReport> findPage(YdeReport ydeReport) {
		return super.findPage(ydeReport);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeReport
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeReport ydeReport) {
		super.save(ydeReport);
	}
	
	/**
	 * 更新状态
	 * @param ydeReport
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeReport ydeReport) {
		super.updateStatus(ydeReport);
	}
	
	/**
	 * 删除数据
	 * @param ydeReport
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeReport ydeReport) {
		super.delete(ydeReport);
	}
	
}