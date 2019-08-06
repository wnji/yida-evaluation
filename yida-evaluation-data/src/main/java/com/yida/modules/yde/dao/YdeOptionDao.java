/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.yida.modules.yde.entity.YdeOption;

/**
 * 选项DAO接口
 * @author Kevin
 * @version 2019-05-02
 */
@MyBatisDao
public interface YdeOptionDao extends CrudDao<YdeOption> {
	
}