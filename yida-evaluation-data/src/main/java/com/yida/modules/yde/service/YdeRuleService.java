/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeRule;
import com.yida.modules.yde.dao.YdeRuleDao;

/**
 * 测评设置Service
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly=true)
public class YdeRuleService extends CrudService<YdeRuleDao, YdeRule> {
	
	/**
	 * 获取单条数据
	 * @param ydeRule
	 * @return
	 */
	@Override
	public YdeRule get(YdeRule ydeRule) {
		return super.get(ydeRule);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeRule 查询条件
	 * @return
	 */
	@Override
	public Page<YdeRule> findPage(YdeRule ydeRule) {
		return super.findPage(ydeRule);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeRule
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeRule ydeRule) {
		super.save(ydeRule);
	}
	
	/**
	 * 更新状态
	 * @param ydeRule
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeRule ydeRule) {
		super.updateStatus(ydeRule);
	}
	
	/**
	 * 删除数据
	 * @param ydeRule
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeRule ydeRule) {
		super.delete(ydeRule);
	}
	
}