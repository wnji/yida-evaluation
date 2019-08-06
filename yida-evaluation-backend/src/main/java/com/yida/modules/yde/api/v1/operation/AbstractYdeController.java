package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.entity.YdeEvaluationRecord;
import com.yida.modules.yde.service.YdeEvaluationRecordService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractYdeController extends BaseController {
    @Autowired
    protected YdeEvaluationRecordService ydeEvaluationRecordService;

    protected Long getUserEvaluatedCount(Long userId, Long evaluationId) {
        YdeEvaluationRecord evaluationRecord = new YdeEvaluationRecord();
        evaluationRecord.setEvaluationId(evaluationId);
        evaluationRecord.setUserId(userId);
        return ydeEvaluationRecordService.findCount(evaluationRecord);
    }
}
