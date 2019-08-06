/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeTypeResult;
import com.yida.modules.yde.dao.YdeTypeResultDao;

/**
 * 分批统计评分结果Service
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly=true)
public class YdeTypeResultService extends CrudService<YdeTypeResultDao, YdeTypeResult> {
	
	/**
	 * 获取单条数据
	 * @param ydeTypeResult
	 * @return
	 */
	@Override
	public YdeTypeResult get(YdeTypeResult ydeTypeResult) {
		return super.get(ydeTypeResult);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeTypeResult 查询条件
	 * @return
	 */
	@Override
	public Page<YdeTypeResult> findPage(YdeTypeResult ydeTypeResult) {
		return super.findPage(ydeTypeResult);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeTypeResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeTypeResult ydeTypeResult) {
		super.save(ydeTypeResult);
	}
	
	/**
	 * 更新状态
	 * @param ydeTypeResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeTypeResult ydeTypeResult) {
		super.updateStatus(ydeTypeResult);
	}
	
	/**
	 * 删除数据
	 * @param ydeTypeResult
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeTypeResult ydeTypeResult) {
		super.delete(ydeTypeResult);
	}
	
}