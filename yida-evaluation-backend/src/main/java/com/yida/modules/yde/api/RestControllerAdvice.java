package com.yida.modules.yde.api;

import com.yida.modules.yde.api.v1.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice(basePackages = {"com.yida.modules.yde.api.v1.operation"})
@Slf4j
public class RestControllerAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RestResponse exceptionHandler(Exception ex){
        RestResponse response = new RestResponse();
        response.setMsg(ex.getMessage());

        response.setCode(String.valueOf(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value()));

        log.error(ex.getMessage(), ex);

        return response;
    }
}
