package com.yida.modules.yde.api.v1.model;

import com.jeesite.common.collect.ListUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.jeesite.common.entity.Page;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ApiModel(description = "返回结果")
@Data
public class RestResponse<T> implements Serializable {

    @ApiModelProperty(value = "错误代码, 可以采用http的status code，如200表示正常，401表示需认证",
            allowableValues = "200,401", example = "200")
    private String code = "200";

    @ApiModelProperty(value = "错误信息", allowEmptyValue = true, example = "OK")
    private String msg;

    @ApiModelProperty(value = "返回的数据", allowEmptyValue = true)
    private List<T> data;

    public RestResponse(){
        this(ListUtils.newArrayList());
    }

    public RestResponse(T... data){
        this(Arrays.asList(data));
    }

    public RestResponse(List<T> data){
        this(data, "200", null);
    }

    public RestResponse(Page<T> page){
        this(page.getList(), "200", null);
    }

    public RestResponse(List<T> data, String code, String message){
        this.data = data;
        this.code = code;
        this.msg = message;
    }
}
