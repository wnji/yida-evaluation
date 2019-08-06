/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yida.modules.yde.entity.YdeEvaluation;
import com.yida.modules.yde.service.YdeEvaluationService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.entity.YdeModule;
import com.yida.modules.yde.service.YdeModuleService;

import java.util.LinkedList;
import java.util.List;

/**
 * 测评模块Controller
 * @author Kevin
 * @version 2019-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeModule")
public class YdeModuleController extends BaseController {

	@Autowired
	private YdeModuleService ydeModuleService;

	@Autowired
	private YdeEvaluationService ydeEvaluationService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public YdeModule get(String id, boolean isNewRecord) {
		return ydeModuleService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("yde:ydeModule:view")
	@RequestMapping(value = {"list", ""})
	public String list(YdeModule ydeModule, Model model) {
		model.addAttribute("ydeModule", ydeModule);
		return "modules/yde/ydeModuleList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("yde:ydeModule:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<YdeModule> listData(YdeModule ydeModule, HttpServletRequest request, HttpServletResponse response) {
		ydeModule.setPage(new Page<>(request, response));
		Page<YdeModule> page = ydeModuleService.findPage(ydeModule);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("yde:ydeModule:view")
	@RequestMapping(value = "form")
	public String form(YdeModule ydeModule, Model model, HttpServletRequest request) {
		logger.error(request.getServletContext().getRealPath(request.getRequestURI()));
		model.addAttribute("ydeModule", ydeModule);

        List<YdeEvaluation> evaluations;

        if(ydeModule.getIsEvaluationLimited()==null){
			ydeModule.setIsEvaluationLimited(false);
		}

		YdeEvaluation param = new YdeEvaluation();
		if(ydeModule.getId()!=null) {
            param.setModuleId(Long.valueOf(ydeModule.getId()));

            evaluations = ydeEvaluationService.findList(param);
        }
        else{
            evaluations = new LinkedList<>();
        }

		model.addAttribute("evaluations", evaluations);

		List<YdeEvaluation> activeEvaluations = new LinkedList<>();
		for(YdeEvaluation evaluation: evaluations){
			if("1".equals(evaluation.getEnabled())){
				activeEvaluations.add(evaluation);
			}
		}

		model.addAttribute("activeEvaluations", activeEvaluations);

		return "modules/yde/ydeModuleForm";
	}

	/**
	 * 保存测评模块
	 */
	@RequiresPermissions("yde:ydeModule:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated YdeModule ydeModule, String[] evaluation) {
	    Long[] evaluationIds = null;
	    if(evaluation!=null){
            evaluationIds = (Long[])ConvertUtils.convert(evaluation, Long.class);
        }

		ydeModuleService.saveAndEnableEvaluations(ydeModule, evaluationIds);
		return renderResult(Global.TRUE, text("保存测评模块成功！"));
	}
	
	/**
	 * 删除测评模块
	 */
	@RequiresPermissions("yde:ydeModule:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(YdeModule ydeModule) {
		ydeModuleService.delete(ydeModule);
		return renderResult(Global.TRUE, text("删除测评模块成功！"));
	}


	/**
	 * 批量删除测评模块
	 */
	@RequiresPermissions("yde:ydeModule:edit")
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	public String batchDelete(String[] ids) {
		for(String id : ids){
			YdeModule ydeModule = new YdeModule();
			ydeModule.setId(id);
			ydeModuleService.delete(ydeModule);
		}
		return renderResult(Global.TRUE, text("批量删除测评模块成功！"), ids);
	}
	
}