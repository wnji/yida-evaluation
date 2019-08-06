/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 测评报告Entity
 *
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name = "yde_report", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "ID", isPK = true, isInsert = false, isUpdate = false),
        @Column(name = "title", attrName = "title", label = "标题"),
        @Column(name = "score", attrName = "score", label = "分值"),
        @Column(name = "total_score", attrName = "totalScore", label = "总分"),
        @Column(name = "report_type", attrName = "reportType", label = "结果展现类型", comment = "结果展现类型, 1:文字描述, 2:饼图, 3:雷达图, 4:仪表盘"),
        @Column(name = "type_content", attrName = "typeContent", label = "类型描述"),
        @Column(name = "report_content", attrName = "reportContent", label = "结果分析内容HTML"),
        @Column(name = "box_content", attrName = "boxContent", label = "颜色框内容HTML,无内容时不显示颜色框"),
        @Column(includeEntity = DataEntity.class)
}
,joinTable = {
        @JoinTable(type = JoinTable.Type.JOIN, entity = YdeEvaluationRecord.class, alias = "b",
                on = "a.id = b.report_id",
                columns = {
                        @Column(name = "evaluation_id", attrName = "evaluationId", label = "测评ID"),
                }),
        @JoinTable(type = JoinTable.Type.JOIN, entity = YdeRule.class, alias = "c",
                on = "b.evaluation_id = c.evaluation_id",
                columns = {
                        @Column(name = "id", attrName = "id", label = "规则ID"),
                })
}, orderBy = "a.update_date DESC"
)
@ApiModel(description = "测评报告")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeReport extends DataEntity<YdeReport> {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private YdeEvaluationRecord ydeEvaluationRecord;

    @JsonIgnore
    private YdeRule ydeRule;

    @ApiModelProperty(value = "标题", position = 1, example = "EQ")
    private String title;

    @ApiModelProperty(value = "分值", position = 2, example = "90")
    private Integer score;

    @ApiModelProperty(value = "总分", position = 3, example = "100")
    private Integer totalScore;

    @ApiModelProperty(value = "结果展现类型, 1:文字描述, 2:饼图, 3:雷达图, 4:仪表盘", position = 4, example = "1")
    private Integer reportType;

    @ApiModelProperty(value = "类型描述", position = 5, example = "<p><span style=\"font-family:'Arial Normal', 'Arial';font-weight:400;\">ISTJ（内倾、感觉、思维和判断）——从容不迫地做好自己的工作</span></p>")
    private String typeContent;

    @ApiModelProperty(value = "结果分析内容HTML", position = 6, example = "<p>0-33分，那空间想象力就不太好；</p>" +
            "<p> 34-40分，分空间想象里一半；</p>" +
            "<p> 41-47分，空间想象力良好；</p>" +
            "<p> 48-60分，你的空间想象力相当优秀 </p>")
    private String reportContent;

    @ApiModelProperty(value = "颜色框内容HTML,无内容时不显示颜色框", position = 7, example = "<p><strong>匹配分析</strong></p>" +
            "<p>RIA: 技术类、设计类</p>")
    private String boxContent;

    @ApiModelProperty(value = "分数档位", position = 8, example = "")
    private List<YdeScoreRule> scores;


    public YdeReport() {
        this(null);
    }

    public YdeReport(String id) {
        super(id);
    }

}