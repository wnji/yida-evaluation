/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 叠加统计评分规则Entity
 *
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name = "yde_score_rule", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "叠加统计评分规则ID", isPK = true, isInsert = false, isUpdate = false),
        @Column(name = "rule_id", attrName = "ruleId", label = "评分规则ID"),
        @Column(name = "score_from", attrName = "scoreFrom", label = "分值从"),
        @Column(name = "score_to", attrName = "scoreTo", label = "分值到"),
        @Column(name = "content", attrName = "content", label = "规则内容"),
        @Column(includeEntity = DataEntity.class),
}, orderBy = "a.update_date DESC"
)
@ApiModel(description = "叠加统计评分规则")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeScoreRule extends DataEntity<YdeScoreRule> implements Comparable<YdeScoreRule> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long ruleId;

    @NotNull(message = "分值从 不能为空")
    @ApiModelProperty(value = "分值从", position = 2, example = "1")
    private Integer scoreFrom;

    @NotNull(message = "分值到 不能为空")
    @ApiModelProperty(value = "分值到", position = 3, example = "1")
    private Integer scoreTo;

    @NotBlank(message = "规则内容 不能为空")
    @Length(max = 2048, message = "规则内容 长度不能超过2048 个字符")
    @ApiModelProperty(value = "内容", position = 4)
    private String content;

    public YdeScoreRule() {
        this(null);
    }

    public YdeScoreRule(String id) {
        super(id);
    }

    @Override
    public int compareTo(YdeScoreRule o) {
        return new CompareToBuilder().append(this.scoreTo, o.scoreTo).build();
    }

    public boolean inRange(Integer score) {
        return score >= scoreFrom && score <= scoreTo;
    }
}