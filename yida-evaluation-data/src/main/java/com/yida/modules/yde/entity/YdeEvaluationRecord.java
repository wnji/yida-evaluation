/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户测评记录Entity
 *
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name = "yde_evaluation_record", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "测评记录ID", isPK = true, isInsert = false, isUpdate = false),
        @Column(name = "user_id", attrName = "userId", label = "测评系统内部用户ID"),
        @Column(name = "evaluation_id", attrName = "evaluationId", label = "测评ID"),
        @Column(name = "report_id", attrName = "reportId", label = "测评报告ID"),
        @Column(name = "evaluate_time", attrName = "evaluateTime", label = "测评时间"),
        @Column(name = "is_last_evaluation", attrName = "isLastEvaluation", label = "是否最近一次测评"),
        @Column(includeEntity = DataEntity.class)
}, joinTable = {
        @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = YdeReport.class, attrName = "report", alias = "b",
                on = "a.report_id = b.id",
                columns = {
                        @Column(includeEntity = YdeReport.class),
                })
},
        orderBy = "a.evaluate_time DESC"
)
@ApiModel(description = "用户测评记录")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeEvaluationRecord extends DataEntity<YdeEvaluationRecord> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "益达用户ID 不能为空")
    private Long userId;

    @ApiModelProperty(value = "测评ID", position = 1, example = "1")
    private Long evaluationId;

    @ApiModelProperty(value = "测评名称", position = 2, example = "霍兰德职业兴趣测评")
    private String evaluationName;

    @ApiModelProperty(value = "测评模块", position = 3, example = "职业方向")
    private String moduleName;

    @ApiModelProperty(value = "测评报告ID", position = 4, example = "1")
    private Long reportId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "测评时间", position = 5, example = "2019-01-01 00:00:00")
    private Date evaluateTime;

    @ApiModelProperty(value = "最新测评", position = 6, example = "true")
    private Boolean isLastEvaluation;

    @JsonIgnore
    private YdeReport report;

    public YdeEvaluationRecord() {
        this(null);
    }

    public YdeEvaluationRecord(String id) {
        super(id);
    }

}