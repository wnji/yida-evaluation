/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeLayout;
import com.yida.modules.yde.dao.YdeLayoutDao;

/**
 * 展示模式Service
 * @author Kevin
 * @version 2019-05-02
 */
@Service
@Transactional(readOnly=true)
public class YdeLayoutService extends CrudService<YdeLayoutDao, YdeLayout> {
	
	/**
	 * 获取单条数据
	 * @param ydeLayout
	 * @return
	 */
	@Override
	public YdeLayout get(YdeLayout ydeLayout) {
		return super.get(ydeLayout);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeLayout 查询条件
	 * @return
	 */
	@Override
	public Page<YdeLayout> findPage(YdeLayout ydeLayout) {
		return super.findPage(ydeLayout);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeLayout
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeLayout ydeLayout) {
		super.save(ydeLayout);
	}
	
	/**
	 * 更新状态
	 * @param ydeLayout
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeLayout ydeLayout) {
		super.updateStatus(ydeLayout);
	}
	
	/**
	 * 删除数据
	 * @param ydeLayout
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeLayout ydeLayout) {
		super.delete(ydeLayout);
	}
	
}