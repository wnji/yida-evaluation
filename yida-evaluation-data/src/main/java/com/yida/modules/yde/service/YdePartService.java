/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdePart;
import com.yida.modules.yde.dao.YdePartDao;

/**
 * 部分Service
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly=true)
public class YdePartService extends CrudService<YdePartDao, YdePart> {
	
	/**
	 * 获取单条数据
	 * @param ydePart
	 * @return
	 */
	@Override
	public YdePart get(YdePart ydePart) {
		return super.get(ydePart);
	}
	
	/**
	 * 查询分页数据
	 * @param ydePart 查询条件
	 * @return
	 */
	@Override
	public Page<YdePart> findPage(YdePart ydePart) {
		return super.findPage(ydePart);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydePart
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdePart ydePart) {
		super.save(ydePart);
	}
	
	/**
	 * 更新状态
	 * @param ydePart
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdePart ydePart) {
		super.updateStatus(ydePart);
	}
	
	/**
	 * 删除数据
	 * @param ydePart
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdePart ydePart) {
		dao.phyDelete(ydePart);
	}
	
}