/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.dao.YdeEvaluationDao;
import com.yida.modules.yde.dao.YdeEvaluationModulesDao;
import com.yida.modules.yde.dao.YdeUserDao;
import com.yida.modules.yde.entity.YdeEvaluationReport;
import com.yida.modules.yde.entity.YdeReport;
import com.yida.modules.yde.entity.YdeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

/**
 * 用户表Service
 * @author James
 * @version 2019-05-07
 */
@Service
@Transactional(readOnly=true)
public class YdeUserService extends CrudService<YdeUserDao, YdeUser> {
	@Autowired
	private YdeEvaluationModulesDao evaluationModulesDao;

	@Autowired
	private YdeEvaluationDao evaluationDao;

	/**
	 * 获取单条数据
	 * @param ydeUser
	 * @return
	 */
	@Override
	public YdeUser get(YdeUser ydeUser) {
		return super.get(ydeUser);
	}
	
	/**
	 * 查询分页数据
	 * @param ydeUser 查询条件
	 * @return
	 */
	@Override
	public Page<YdeUser> findPage(YdeUser ydeUser) {
		return super.findPage(ydeUser);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param ydeUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(YdeUser ydeUser) {
		super.save(ydeUser);
	}
	
	/**
	 * 更新状态
	 * @param ydeUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(YdeUser ydeUser) {
		super.updateStatus(ydeUser);
	}
	
	/**
	 * 删除数据
	 * @param ydeUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(YdeUser ydeUser) {
		super.delete(ydeUser);
	}


	/**
	 * 返回用户的测评结果（用户画像）
	 * @param ydeUser 要返回测评结果的用户
	 * @param model 数据保存到model中返回到页面
	 * @return
	 */
	public void evaluationResult(YdeUser ydeUser, Model model){
		model.addAttribute("ydeUser", ydeUser);

		// 激活的测试套题个数
		int totalEvaluation = this.evaluationModulesDao.countEvaluation();
		// 已测的套题数
		int evaluated = 0;
		if(StringUtils.isNotBlank(ydeUser.getEvaluationModules())){
			evaluated = Integer.parseInt(ydeUser.getEvaluationModules());
		}

		int notEvaluated = totalEvaluation - evaluated;

		model.addAttribute("notEvaluatedModule", notEvaluated);
		model.addAttribute("evaluatedModule", evaluated);

		if(totalEvaluation<=evaluated){
			model.addAttribute("evaluatedModulePercent", "100%");
			model.addAttribute("notEvaluatedModulePercent", "0%");
		}
		else{
            int evaluatedModulePercent = evaluated * 100 / totalEvaluation ;
			model.addAttribute("evaluatedModulePercent", evaluatedModulePercent + "%");
			model.addAttribute("notEvaluatedModulePercent", (100 - evaluatedModulePercent) + "%");
		}

		List<YdeEvaluationReport> evaluationReports = this.dao.findEvaluationReports(Long.valueOf(ydeUser.getId()));
		String startTag = "<p>";
		String endTag = "</p>";

		for(YdeEvaluationReport evaluationReport: evaluationReports){

			YdeReport report = evaluationReport.getReport();
			if(report!=null){
				int totalScore = this.evaluationDao.findTotalScore(Integer.parseInt(evaluationReport.getEvaluation().getId()));
				report.setTotalScore(totalScore);

				String boxContent = report.getBoxContent();
				if(StringUtils.isNoneBlank(boxContent)) {
					int iStart = boxContent.lastIndexOf(startTag);
					if (iStart >= 0) {
						int iEnd = boxContent.lastIndexOf(endTag);

						iStart = iStart + startTag.length();
						boxContent = boxContent.substring(iStart, iEnd);

						report.setBoxContent(boxContent);
					}
				}
			}
		}

		model.addAttribute("evaluationReports", evaluationReports);
	}
}