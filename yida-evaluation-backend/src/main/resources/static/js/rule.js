let toolbars = [['undo', 'redo', '|', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough',
    'removeformat', 'formatmatch', '|', 'forecolor', 'backcolor',
    'insertorderedlist', 'insertunorderedlist', '|', 'simpleupload']];

$(function () {
    if (parts.length > 0) {
        for (let i = 0; i < parts.length; i++) {
            let part = parts[i];
            if (i == 0) {
                $("#part-group").find("input[name=part_id_1]").val(part.id);
                let ue = UE.getEditor('part_box_1UE');
                ue.ready(function () {
                    ue.setContent(part.content);
                });
            } else {
                addPart(i + 1, part.id, part.content);
            }
        }
    }
    if ($(".checked input[name=part]").val() == "1") {
        $("#part-group").show();
    }

    if (rules.length == 0) {
        addScoreRule(null, null, null, null);
        addTypeRule(1, null, null, null);
        addTypeRule(1, null, null, null);
    } else {
        for (let i = 0; i < rules.length; i++) {
            let rule = rules[i];
            if (ruleType == "1") {
                addScoreRule(rule.scoreFrom, rule.scoreTo, rule.content, rule.id);
                addTypeRule(1, null, null, null);
                addTypeRule(1, null, null, null);
            } else if (ruleType == "2") {
                addTypeRule(rule.groupNo, rule.type, rule.questionNos, rule.id);
                addScoreRule(null, null, null, null);
            }
        }
    }

    changeRuleType(ruleType);
    showReportType(reportType);
});

function removeScore(elem) {
    let $scoreBox = $(elem).parents(".score-box");
    $scoreBox.remove();
}

function changeRuleType(ruleType) {
    if (ruleType == "1") {
        $(".rule-type-score-sum").show();
        $(".rule-type-group-sum").hide();
    } else if (ruleType == "2") {
        $(".rule-type-group-sum").show();
        $(".rule-type-score-sum").hide();
    }
}

function addScoreRule(from, to, typeContent, id) {
    let $scoreRange = $("#score-range-tpl").clone();
    $scoreRange.removeAttr("id");
    $scoreRange.addClass("score-box");
    $scoreRange.attr("rule-id", id);
    $scoreRange.find("#scoreFrom").val(from);
    $scoreRange.find("#scoreTo").val(to);
    $scoreRange.find("#typeContent").val(typeContent);

    $scoreRange.show();
    $("#score-range").append($scoreRange);
}

function addTypeRule(groupNo, type, questionNos, id) {
    let $groupRange = $("#type-range-tpl").clone();
    let groupHtml = $groupRange[0].outerHTML;
    groupHtml = groupHtml.replace(/#groupNo/g, groupNo);

    $groupRange = $(groupHtml);

    $groupRange.removeAttr("id");
    $groupRange.attr("rule-id", id);
    $groupRange.attr("group-no", groupNo);
    $groupRange.addClass("score-box");
    $groupRange.find("input[name=type]").val(type);
    $groupRange.find("input[name=questionNo]").val(questionNos);
    $groupRange.find(".tagsinput").tagsInput();
    $groupRange.show();
    if ($("#type-group-" + groupNo).length == 0) {
        $("#type-range").append($groupRange.wrap("<div class='group-box' group-no='" + groupNo + "' id='type-group-" + groupNo + "'>").parent());
        $groupRange.parents('.group-box').prepend("<div class='close-group'><i onclick='removeGroup(this);' class='icon-close'></i></div>")
    } else {
        $("#type-group-" + groupNo).append($groupRange);
    }

    let currentOptionCount = $groupRange.parent().find(".score-box").length;
    let yesNo = currentOptionCount == 1 ? "/是" : currentOptionCount == 2 ? "/否" : "";
    let optionName = "选项" + String.fromCharCode(currentOptionCount + 64) + yesNo;
    $groupRange.find(".option-name").html(optionName)
}

function removeGroup(elem) {
    $(elem).parents(".group-box").remove();
}


function getScoreRuleJson(form) {
    let $scoreBoxes = $("#score-range").find(".score-range-row");
    let scoreRules = new Array();
    for (let i = 0; i < $scoreBoxes.length; i++) {
        let ruleId = $($scoreBoxes[i]).attr("rule-id");
        let from = $($scoreBoxes[i]).find("input#scoreFrom").val();
        let to = $($scoreBoxes[i]).find("input#scoreTo").val();
        let content = $($scoreBoxes[i]).find("input#typeContent").val();
        scoreRules.push({ruleId: ruleId, from: from, to: to, content: content});
    }

    return JSON.stringify(scoreRules);
}

function getPartsJson(form) {
    let $partBox = $(form).find(".part-box");
    let parts = new Array();
    for (let i = 0; i < $partBox.length; i++) {
        let partNo = $($partBox[i]).attr("part-no");
        let partId = $($partBox[i]).find("input[name=part_id_" + partNo + "]").val();
        let partContent = UE.getEditor('part_box_' + partNo + 'UE').getContent()

        parts.push({partId: partId, part: partContent});
    }

    return JSON.stringify(parts);
}

function getTypeRuleJson(form) {
    let $typeRule = $("#type-range").find(".type-range-row");
    let typeRules = new Array();
    for (let i = 0; i < $typeRule.length; i++) {
        let ruleId = $($typeRule[i]).attr("rule-id");
        let groupNo = $($typeRule[i]).parents(".group-box").attr("group-no");
        let type = $($typeRule[i]).find("input[name=type]").val();
        let questionNo = $($typeRule[i]).find("input[name=questionNo]").val();

        typeRules.push({ruleId: ruleId, groupNo: groupNo, type: type, questionNo: questionNo});
    }

    return JSON.stringify(typeRules);
}

function addGroup() {
    let groupCount = $(".group-box").length;
    addTypeRule(groupCount + 1, null, null, null);
    addTypeRule(groupCount + 1, null, null, null);
}

function addAnalysisGroup(analysisId) {
    js.layer.open({
        type: 1,
        area: ['600px', '450px'],
        title: '编辑分析',
        resize: true,
        scrollbar: true,
        success: function (layero, index) {
            let editorHTML = $("#analysis_editor")[0].outerHTML;
            editorHTML = editorHTML.replace(/analysis_tpl/g, "analysis");
            let $editorBox = $(editorHTML);
            $editorBox.removeAttr("id");
            $editorBox.show();
            let holder = layero.find(".belongTo").parent();
            holder.append("<script src=\"/yida-evaluation-backend/static/ueditor/1.4/ueditor.config.js?V1.0-07231158\"></script>");
            holder.append("<script src=\"/yida-evaluation-backend/static/ueditor/1.4/ueditor.all.js?V1.0-07231158\"></script>");
            holder.append($editorBox);
            let ue = window.parent.UE.getEditor('analysisUE');

            if (analysisId != undefined) {
                $.ajax({
                    type: "GET",
                    url: "getAnalysis?analysisId=" + analysisId,
                    success: function (e) {
                        layero.find("#id").val(analysisId);
                        layero.find("#ruleId").val(e.ruleId);
                        layero.find("#type1").val(e.type1);
                        layero.find("#type2").val(e.type2);
                        layero.find("#type3").val(e.type3);
                        layero.find("#type4").val(e.type4);
                        layero.find("#belongTo").val(e.belongTo);

                        ue.ready(function () {
                            if (e.analysis) {
                                ue.setContent(e.analysis);
                            }
                        });
                    }
                });
            } else {
                layero.find("#ruleId").val(ruleId);
                ue.ready(function () {
                    ue.setContent("");
                });
            }
        },
        content: $('#group-analysis-tpl').html(),
        btn: ['<i class="fa fa-check"></i> 确定', ' 取消'],
        btn1: function (index, layero) {
            let ue = window.parent.UE.getEditor('analysisUE');
            js.ajaxSubmit("saveAnalysis", {
                "id": analysisId ? analysisId : "",
                "ruleId": layero.find("#ruleId").val(),
                "type1": layero.find("#type1").val(),
                "type2": layero.find("#type2").val(),
                "type3": layero.find("#type3").val(),
                "type4": layero.find("#type4").val(),
                "belongTo": layero.find("#belongTo").val(),
                "analysis": "<!--HTML-->" + ue.getContent()
            }, function (e) {
                if (e.result == 'true') {
                    $("#analysisGrid").trigger('reloadGrid');
                    js.showMessage(e.message, "", "success");
                } else {
                    js.showMessage(e.message, "", "error");
                }
            });
            return true;
        }
    });
}

function removeAnalysisGrp(analysisId) {
    js.confirm('你确认要删除这些数据吗？',
        "deleteAnalysis",
        {
            "id": analysisId
        }, function (e) {
            if (e.result == 'true') {
                $("#analysisGrid").trigger('reloadGrid');
                js.showMessage(e.message, "", "success");
            } else {
                js.showMessage(e.message, "", "error");
            }
        });
}

function showReportType(reportType) {
    $(".charts").find("img").hide();
    if (reportType == "2") {
        $(".charts").find("#piechart").show();
    } else if (reportType == "3") {
        $(".charts").find("#radarchart").show();
    } else if (reportType == "4") {
        $(".charts").find("#dashboard").show();
    }
}

function addPart(partNo, partId, content) {
    let $partBox = $("#part-box-tpl").clone();
    let partHtml = $partBox.html();
    partHtml = partHtml.replace(/#part_box_no#/g, partNo);
    partHtml = partHtml.replace(/#next_part_box_no#/g, (partNo + 1));
    partHtml = partHtml.replace(/part_box_tpl/g, "part_box_" + partNo);

    $partBox = $(partHtml);
    $partBox.removeAttr("id");
    $partBox.attr("part-no", partNo);
    $partBox.addClass("part-box");
    $partBox.find("input[name=part_id_" + partNo + "]").val(partId);
    $partBox.show();
    $("#part-group").append($partBox);

    let ue = UE.getEditor('part_box_' + partNo + "UE");
    ue.options.toolbars = toolbars;
    ue.ready(function () {
        if (content) {
            ue.setContent(content);
        }
        while ($partBox.find(".edui-editor").length > 1) {
            $($partBox.find(".edui-editor")[0]).remove();
        }
    });
}

function removePart(elem) {
    let $partBox = $(elem).parents('.part-box');
    $partBox.remove();
}