/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.HtmlUtils;
import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.api.session.UserSession;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.api.v1.model.YdeAnswer;
import com.yida.modules.yde.entity.*;
import com.yida.modules.yde.report.ReportBuilder;
import com.yida.modules.yde.report.ReportBuilderFactory;
import com.yida.modules.yde.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 益达人才测评模块
 *
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/parts")
@Api(value = "益达人才测评", description = "测评部分接口")
@Slf4j
public class RestPartController extends BaseController {

    @Autowired
    private YdePartService ydePartService;



    /**
     * 获取某个测评部分背景内容
     */
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取某个测评部分背景内容")
    @DefaultApiResponse
    public RestResponse<YdePart> getResult(
            @ApiParam(value = "部分ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        YdePart part = ydePartService.get(id);
        return new RestResponse(part);
    }

}