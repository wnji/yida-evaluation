/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeScoreRule;
import com.yida.modules.yde.dao.YdeScoreRuleDao;

/**
 * 叠加统计评分规则Service
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly=true)
public class YdeScoreRuleService extends CrudService<YdeScoreRuleDao, YdeScoreRule> {
	
	/**
	 * 获取单条数据
	 * @param ydeScoreRule
	 * @return
	 */
	@Override
	public YdeScoreRule get(YdeScoreRule ydeScoreRule) {
		return super.get(ydeScoreRule);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeScoreRule 查询条件
	 * @return
	 */
	@Override
	public Page<YdeScoreRule> findPage(YdeScoreRule ydeScoreRule) {
		return super.findPage(ydeScoreRule);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeScoreRule
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeScoreRule ydeScoreRule) {
		super.save(ydeScoreRule);
	}
	
	/**
	 * 更新状态
	 * @param ydeScoreRule
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeScoreRule ydeScoreRule) {
		super.updateStatus(ydeScoreRule);
	}
	
	/**
	 * 删除数据
	 * @param ydeScoreRule
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeScoreRule ydeScoreRule) {
		super.delete(ydeScoreRule);
	}
	
}