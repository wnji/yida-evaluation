package com.yida.modules.yde.api.v1.operation;


import com.yida.modules.yde.api.command.LoginCommand;
import com.yida.modules.yde.api.command.LogoutCommand;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.service.YdeSecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "${v1ApiPath}")
@Api(value = "益达人才测评", description = "安全相关，如：登录，登出")
public class RestSecurityController {
    @Autowired
    private YdeSecurityService securityService;

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "登录")
    @DefaultApiResponse
    public RestResponse login(Model model, LoginCommand command, BindingResult errors) {
        return securityService.login(command);
    }

    @ResponseBody
    @RequestMapping(value = "/needLogin", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "未登录时，提示需登录")
    @DefaultApiResponse
    public RestResponse needLogin() {
        RestResponse response = new RestResponse();

        response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        response.setMsg("请先登录");

        return response;
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "登出")
    public RestResponse logout(LogoutCommand command) {
        return securityService.logout(command);
    }
}
