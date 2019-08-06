package com.yida.modules.yde.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "用户测评记录统计")
@Data
public class YdeEvaluationCount {

    @ApiModelProperty(value = "测评ID", position = 1, example = "1")
    private Long evaluationId;

    @ApiModelProperty(value = "测评名称", position = 2, example = "霍兰德职业兴趣测评")
    private String evaluationName;

    @ApiModelProperty(value = "测评次数", position = 3, example = "3")
    private Integer count;
}
