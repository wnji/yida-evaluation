/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.yida.modules.yde.entity.YdeScoreRule;

/**
 * 叠加统计评分规则DAO接口
 * @author Kevin
 * @version 2019-05-01
 */
@MyBatisDao
public interface YdeScoreRuleDao extends CrudDao<YdeScoreRule> {
	
}