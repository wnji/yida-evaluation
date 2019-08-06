package com.yida.modules.yde.api.v1.model;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })

@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "无效ID"),
        @ApiResponse(code = 401, message = "未授权"),
        @ApiResponse(code = 403, message = "没有权限访问"),
        @ApiResponse(code = 404, message = "查无记录")
})
public @interface DefaultApiResponse {
}
