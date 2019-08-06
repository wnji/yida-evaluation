/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 测评报告
 * @author James
 * @version 2019-05-16
 */
@Data
public class YdeEvaluationReport  implements Serializable {

	private static final long serialVersionUID = 1L;

	private YdeEvaluation evaluation;

	private YdeReport report;

	public YdeEvaluationReport() {

	}
}