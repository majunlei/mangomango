"use strict";

$(function(){
    $("#fileUpload").click(function(){
        var fileUpload = $("#fileUpload");
        fileUpload.attr("disabled", "disabled");
        fileUpload.text("正在上传...");
        var form = new FormData();
        var files = $('#files')[0].files;
        for(var i = 0; i < files.length; i++){
            form.append("file", files[i]);
        }
        $.ajax({
            url: 'upload',
            type: 'POST',
            cache: false,
            data: form,
            dataType: "json",
            processData: false,
            contentType: false
        }).done(function(res) {
            fileUpload.removeAttr("disabled");
            fileUpload.text("上传");
            if (res.code == 0) {
                alert("上传成功");
                var pageNum = parseInt($("#currentPageNum").text());
                var pageSize = parseInt($("#pageSize").text());
                refresh(pageNum, pageSize);
            } else {
                alert(res.msg);
            }
        }).fail(function(res) {
            fileUpload.removeAttr("disabled");
            fileUpload.text("上传");
            alert("请求失败");
        });
    });
});