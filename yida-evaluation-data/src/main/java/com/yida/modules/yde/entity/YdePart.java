/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 部分Entity
 *
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name = "yde_part", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "部分ID", isPK = true, isInsert = false, isUpdate = false),
        @Column(name = "evaluation_id", attrName = "evaluationId", label = "测评ID"),
        @Column(name = "section", attrName = "section", label = "部分名称", comment = "部分名称（第一部分，第二部分）"),
        @Column(name = "content", attrName = "content", label = "背景内容"),
        @Column(includeEntity = DataEntity.class),
}, orderBy = "a.section ASC"
)
@ApiModel(description = "部分")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdePart extends DataEntity<YdePart> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "测评ID 不能为空")
    @ApiModelProperty(value = "测评ID", position = 1, example = "1")
    private Long evaluationId;

    @Length(min = 0, max = 45, message = "部分名称 长度不能超过45 个字符")
    @ApiModelProperty(value = "部分名称（第一部分，第二部分）", position = 2, example = "第一部分")
    private String section;

    @ApiModelProperty(value = "背景内容", position = 3, example = "")
    private String content;


    public YdePart() {
        this(null);
    }

    public YdePart(String id) {
        super(id);
    }

}