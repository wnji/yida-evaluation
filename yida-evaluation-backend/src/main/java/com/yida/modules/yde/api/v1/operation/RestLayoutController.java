/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.entity.YdeEvaluation;
import com.yida.modules.yde.entity.YdeLayout;
import com.yida.modules.yde.entity.YdeModule;
import com.yida.modules.yde.service.YdeLayoutService;
import com.yida.modules.yde.service.YdeModuleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 益达人才测评模块
 *
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/layouts")
@Api(value = "展示模式", description = "展示模式,根据设置的测评模块数量，对应选择相等数量的版块")
public class RestLayoutController extends BaseController {

    @Autowired
    private YdeLayoutService ydeLayoutService;

    @Autowired
    private YdeModuleService ydeModuleService;

    /**
     * 获取展示模式
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取当前展示模式", notes = "获取当前展示模式", httpMethod = "GET")
    @DefaultApiResponse
    public RestResponse<YdeLayout> get(
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        YdeLayout ydeLayout = ydeLayoutService.get(new YdeLayout("1"));
        String[] moduleIds = ydeLayout.getModuleIds().split(",");
        //TODO use more efficient query
        List<YdeModule> modules = ListUtils.newArrayList();
        for(String moduleId : moduleIds){
            modules.add(ydeModuleService.get(moduleId));
        }
        ydeLayout.setModules(modules);
        return new RestResponse(ydeLayout);
    }
}