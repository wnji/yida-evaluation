/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeModule;
import com.yida.modules.yde.dao.YdeModuleDao;

/**
 * 测评模块Service
 * @author Kevin
 * @version 2019-05-02
 */
@Service
@Transactional
public class YdeModuleService extends CrudService<YdeModuleDao, YdeModule> {
	
	/**
	 * 获取单条数据
	 * @param ydeModule
	 * @return
	 */
	@Override
	public YdeModule get(YdeModule ydeModule) {
		return super.get(ydeModule);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeModule 查询条件
	 * @return
	 */
	@Override
	public Page<YdeModule> findPage(YdeModule ydeModule) {
		return super.findPage(ydeModule);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeModule
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeModule ydeModule) {
		super.save(ydeModule);
	}
	
	/**
	 * 更新状态
	 * @param ydeModule
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeModule ydeModule) {
		super.updateStatus(ydeModule);
	}
	
	/**
	 * 删除数据
	 * @param ydeModule
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeModule ydeModule) {
		super.delete(ydeModule);
	}

	/**
	 * 保存测评模块和模块对应的测评的激活状态
	 * @param ydeModule 测评模块
	 * @param evaluation 需要激活的测评（当测评模块的isEvaluationLimited为false时，激活所有测评）
	 * @return
	 */
	public void saveAndEnableEvaluations(YdeModule ydeModule, Long[] evaluation) {
		this.save(ydeModule);

		if(!ydeModule.getIsEvaluationLimited()){
			dao.setEvaluationEnable(Long.valueOf(ydeModule.getId()), null, true);
		}
		else{
			dao.setEvaluationEnable(Long.valueOf(ydeModule.getId()), null, false);

			if(evaluation!=null && evaluation.length>0) {
				dao.setEvaluationEnable(Long.valueOf(ydeModule.getId()), evaluation, true);
			}
		}
	}
}