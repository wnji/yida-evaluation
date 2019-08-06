/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.yida.modules.yde.entity.YdeModule;
import org.apache.ibatis.annotations.Param;

/**
 * 测评模块DAO接口
 * @author Kevin
 * @version 2019-05-02
 */
@MyBatisDao
public interface YdeModuleDao extends CrudDao<YdeModule> {
    void setEvaluationEnable(@Param("moduleId") Long moduleId, @Param("evaluationIds") Long[] evaluationIds, @Param("isEnabled") boolean isEnabled);
}