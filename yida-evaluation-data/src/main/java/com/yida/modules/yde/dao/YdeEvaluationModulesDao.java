/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.yida.modules.yde.entity.YdeEvaluationModules;

/**
 * 测评模块表DAO接口
 * @author James
 * @version 2019-05-07
 */
@MyBatisDao
public interface YdeEvaluationModulesDao extends CrudDao<YdeEvaluationModules> {
    // 激活的测试套题个数
	int countEvaluation();
}