/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.yida.modules.yde.entity.YdeEvaluationReport;
import com.yida.modules.yde.entity.YdeUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表DAO接口
 * @author James
 * @version 2019-05-07
 */
@MyBatisDao
public interface YdeUserDao extends CrudDao<YdeUser> {
   List<YdeEvaluationReport> findEvaluationReports(@Param("userId") long userId);

    YdeUser selectByUserNo(@Param("yidaUserNo") String yidaUserNo);
}