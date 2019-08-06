/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * 项目自定义的公共JavaScript，可覆盖jeesite.js里的方法
 */

function batchDelete() {
    if ($('#dataGrid tr[role=row][aria-selected=true]').length > 0) {
        let ids = "";
        $('#dataGrid tr[role=row][aria-selected=true]').each(function () {
            ids += this.id + ",";
        });
        ids = ids.substring(0, ids.length-1);
        js.confirm('你确认要删除这些数据吗？', "batchDelete?ids=" + ids, null, handleDeleteResult);
    } else {
        js.showMessage("没有选中的数据！", "提醒", "warning");
    }

}

function handleDeleteResult(data) {
    if (data.result) {
        $.each(data.data, function () {
            $('#dataGrid tr#'+this).fadeOut(300, function () {
                $(this).remove();
            });
        });
        js.showMessage(data.message, "删除成功", "success");
    } else {
        js.showMessage(data.message, "删除错误", "error");
    }
    js.closeLoading(0, true)
}

function ajaxRequest(type, url, data, title) {
    $.ajax({
        type: type,
        url: url,
        data: data,
        success: function (e) {
            if(e.result == 'true'){
                $("#dataGrid").trigger('reloadGrid');
                js.showMessage(e.message, title, "success");
            }else {
                js.showMessage(e.message, title, "error");
            }
        }
    });
}