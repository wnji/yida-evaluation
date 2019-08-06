/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.yida.modules.yde.dto.YdeQuestionOption;
import com.yida.modules.yde.entity.YdeQuestion;

import java.util.List;

/**
 * 题目设置DAO接口
 * @author Kevin
 * @version 2019-05-02
 */
@MyBatisDao
public interface YdeQuestionDao extends CrudDao<YdeQuestion> {

    List<YdeQuestionOption> findListWithOption(YdeQuestion questionWhere);
}