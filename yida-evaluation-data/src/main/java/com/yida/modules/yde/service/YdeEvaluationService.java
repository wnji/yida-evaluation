/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeEvaluation;
import com.yida.modules.yde.dao.YdeEvaluationDao;

/**
 * 测评设置Service
 * @author Kevin
 * @version 2019-05-02
 */
@Service
@Transactional(readOnly=true)
public class YdeEvaluationService extends CrudService<YdeEvaluationDao, YdeEvaluation> {
	
	/**
	 * 获取单条数据
	 * @param ydeEvaluation
	 * @return
	 */
	@Override
	public YdeEvaluation get(YdeEvaluation ydeEvaluation) {
		return super.get(ydeEvaluation);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeEvaluation 查询条件
	 * @return
	 */
	@Override
	public Page<YdeEvaluation> findPage(YdeEvaluation ydeEvaluation) {
		return super.findPage(ydeEvaluation);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeEvaluation ydeEvaluation) {
		super.save(ydeEvaluation);
	}
	
	/**
	 * 更新状态
	 * @param ydeEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeEvaluation ydeEvaluation) {
		super.updateStatus(ydeEvaluation);
	}
	
	/**
	 * 删除数据
	 * @param ydeEvaluation
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeEvaluation ydeEvaluation) {
		super.delete(ydeEvaluation);
	}
	
}