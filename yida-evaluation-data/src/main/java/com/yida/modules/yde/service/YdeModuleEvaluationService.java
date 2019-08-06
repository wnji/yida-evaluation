/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeModuleEvaluation;
import com.yida.modules.yde.dao.YdeModuleEvaluationDao;

/**
 * 模块测评对应关系Service
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly=true)
public class YdeModuleEvaluationService extends CrudService<YdeModuleEvaluationDao, YdeModuleEvaluation> {
	
	/**
	 * 获取单条数据
	 * @param ydeModuleEvaluation
	 * @return
	 */
	@Override
	public YdeModuleEvaluation get(YdeModuleEvaluation ydeModuleEvaluation) {
		return super.get(ydeModuleEvaluation);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeModuleEvaluation 查询条件
	 * @return
	 */
	@Override
	public Page<YdeModuleEvaluation> findPage(YdeModuleEvaluation ydeModuleEvaluation) {
		return super.findPage(ydeModuleEvaluation);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeModuleEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeModuleEvaluation ydeModuleEvaluation) {
		super.save(ydeModuleEvaluation);
	}
	
	/**
	 * 更新状态
	 * @param ydeModuleEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeModuleEvaluation ydeModuleEvaluation) {
		super.updateStatus(ydeModuleEvaluation);
	}
	
	/**
	 * 删除数据
	 * @param ydeModuleEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeModuleEvaluation ydeModuleEvaluation) {
		super.delete(ydeModuleEvaluation);
	}
	
}