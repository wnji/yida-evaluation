/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.yida.modules.yde.dto.YdeEvaluationCount;
import com.yida.modules.yde.entity.YdeEvaluationRecord;
import com.yida.modules.yde.entity.YdeReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户测评记录DAO接口
 * @author Kevin
 * @version 2019-05-01
 */
@MyBatisDao
public interface YdeEvaluationRecordDao extends CrudDao<YdeEvaluationRecord> {

    List<YdeReport> findReportsByEvaluationRecord(YdeEvaluationRecord evaluation);

    int countEvaluationByUser(@Param("userId") long userId);

    List<YdeEvaluationCount> findEvaluationCountsByUserId(@Param("userId") String userId);

    List<YdeReport> findLastReports(String userId);
}