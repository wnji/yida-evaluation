package com.yida.modules.yde.api.v1.model;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})

@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", example = "2", value = "查询页码(0..N)"),
        @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", example = "5", value = "每页记录数目"),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", example = "asc", paramType = "query", value = "升降序(asc|desc)")
})
public @interface ApiPageable {
}
