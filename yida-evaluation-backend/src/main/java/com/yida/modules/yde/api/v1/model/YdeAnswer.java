package com.yida.modules.yde.api.v1.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "单个题目回答")
@Data
public class YdeAnswer {

    @ApiModelProperty(value = "题目ID", position = 1, example = "1")
    private Long questionId;

//    @ApiModelProperty(value = "题号", position = 2, example = "10")
//    private Integer questionNo;

    @ApiModelProperty(value = "回答，选择题填A,B,C...;填空题填答案", position = 2, example = "A")
    private String answer;

    @ApiModelProperty(value = "分数", position = 3, example = "3")
    private Integer points;


    public String toYesNoAnswer(){
        if("是".equals(answer)){
            return "A";
        }else if("否".equals(answer)){
            return "B";
        }
        return answer;
    }
}
