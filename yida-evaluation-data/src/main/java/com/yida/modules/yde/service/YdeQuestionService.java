/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeesite.common.entity.DataEntity;
import com.yida.modules.yde.dto.YdeQuestionOption;
import com.yida.modules.yde.entity.YdeOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.entity.YdeQuestion;
import com.yida.modules.yde.dao.YdeQuestionDao;

/**
 * 题目设置Service
 * @author Kevin
 * @version 2019-05-02
 */
@Service
@Transactional(readOnly=true)
public class YdeQuestionService extends CrudService<YdeQuestionDao, YdeQuestion> {
	
	/**
	 * 获取单条数据
	 * @param ydeQuestion
	 * @return
	 */
	@Override
	public YdeQuestion get(YdeQuestion ydeQuestion) {
		return super.get(ydeQuestion);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeQuestion 查询条件
	 * @return
	 */
	@Override
	public Page<YdeQuestion> findPage(YdeQuestion ydeQuestion) {
		return super.findPage(ydeQuestion);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeQuestion
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeQuestion ydeQuestion) {
		super.save(ydeQuestion);
	}
	
	/**
	 * 更新状态
	 * @param ydeQuestion
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeQuestion ydeQuestion) {
		super.updateStatus(ydeQuestion);
	}
	
	/**
	 * 删除数据
	 * @param ydeQuestion
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeQuestion ydeQuestion) {
		super.delete(ydeQuestion);
	}

	/**
	 * 查找问题并包含选项
	 * @param question
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<YdeQuestion> findListWithOption(YdeQuestion question) {
		if(question.getStatus() == null){
			question.setStatus(DataEntity.STATUS_NORMAL);
		}
		List<YdeQuestionOption> questionOptions = dao.findListWithOption(question);
		return YdeQuestionOption.toQuestions(questionOptions);
	}

	public List<String> convertNoToId(Long evaluationId, List<String> questionNoList) {
		YdeQuestion where = new YdeQuestion();
		where.setEvaluationId(evaluationId);
		List<YdeQuestion> questions = findList(where);
		return questions.stream().filter(q-> questionNoList.contains(String.valueOf(q.getNo())))
				.map(YdeQuestion::getId).collect(Collectors.toList());
	}

	public List<String> convertIdToNo(Long evaluationId, List<String> questionIdList) {
		YdeQuestion where = new YdeQuestion();
		where.setEvaluationId(evaluationId);
		List<YdeQuestion> questions = findList(where);
		return questions.stream().filter(q-> questionIdList.contains(q.getId()))
				.map(q-> String.valueOf(q.getNo())).collect(Collectors.toList());
	}
}