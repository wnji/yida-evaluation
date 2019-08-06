/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;

import com.yida.modules.yde.dto.YdeEvaluationCount;
import com.yida.modules.yde.entity.YdeReport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeEvaluationRecord;
import com.yida.modules.yde.dao.YdeEvaluationRecordDao;

/**
 * 用户测评记录Service
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly=true)
public class YdeEvaluationRecordService extends CrudService<YdeEvaluationRecordDao, YdeEvaluationRecord> {
	
	/**
	 * 获取单条数据
	 * @param ydeEvaluationRecord
	 * @return
	 */
	@Override
	public YdeEvaluationRecord get(YdeEvaluationRecord ydeEvaluationRecord) {
		return super.get(ydeEvaluationRecord);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeEvaluationRecord 查询条件
	 * @return
	 */
	@Override
	public Page<YdeEvaluationRecord> findPage(YdeEvaluationRecord ydeEvaluationRecord) {
		return super.findPage(ydeEvaluationRecord);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeEvaluationRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeEvaluationRecord ydeEvaluationRecord) {
		super.save(ydeEvaluationRecord);
	}
	
	/**
	 * 更新状态
	 * @param ydeEvaluationRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeEvaluationRecord ydeEvaluationRecord) {
		super.updateStatus(ydeEvaluationRecord);
	}
	
	/**
	 * 删除数据
	 * @param ydeEvaluationRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeEvaluationRecord ydeEvaluationRecord) {
		super.delete(ydeEvaluationRecord);
	}

	public List<YdeReport> findReportsByEvaluationRecord(YdeEvaluationRecord evaluationRecord) {

		return dao.findReportsByEvaluationRecord(evaluationRecord);
	}

	@Transactional(readOnly=false)
    public void resetLastEvaluation(Long userId, Long evaluationId) {
		YdeEvaluationRecord where = new YdeEvaluationRecord();
		where.setUserId(userId);
		where.setEvaluationId(evaluationId);
		where.setIsLastEvaluation(Boolean.TRUE);
		List<YdeEvaluationRecord> records = findList(where);
		records.forEach(r->{
			r.setIsLastEvaluation(Boolean.FALSE);
			save(r);
		});

    }

	public List<YdeEvaluationCount> findEvaluationCountsByUserId(String userId) {
		return dao.findEvaluationCountsByUserId(userId);
	}

	public List<YdeReport> findLastReports(String userId) {
		return dao.findLastReports(userId);
	}
}