"use strict";
layui.config({
    base: 'js/' //假设这是test.js所在的目录
}).extend({
    cookies: 'cookies'
});

layui.use('cookies', function(){
});

function like(id) {
    viewAdd(id);
    localStorage.setItem("like_" + id, "1");
    layui.use('jquery',
    function() {
        var $ = layui.jquery;
        $.getJSON('/xq/like/' + id,
        function(res) {
            if (res.code === 0) {
                var xq_like = $('#' + 'xq-like' + id);
                var xq_like_icon = $('#' + 'xq-like-icon' + id);
                var likeNum = parseInt(xq_like[0].innerHTML);
                likeNum++;
                xq_like[0].innerHTML = likeNum;
                xq_like_icon.removeAttr("onclick");
                xq_like_icon.css("color", "grey");
                layer.msg("赞+1");
            } else {
                layer.msg(res.msg);
            }
        })
    })
}

function viewAdd(id) {
    layui.use('jquery',
    function() {
        var $ = layui.jquery;
        $.getJSON('/xq/view/add/' + id,
        function(res) {})
    })
}

function comment(id) {
    viewAdd(id);
    layer.prompt({
        formType: 2,
        value: '',
        maxlength: 100,
        title: '留言',
        area: ['300px', '50px'] //自定义文本域宽高
    },
    function(value, index, elem) {
        layui.use('jquery',
        function() {
            var $ = layui.jquery;
            $.getJSON('/xq/comment/add/' + id + '?comment=' + value,
            function(res) {
                if (res.code === 0) {
                    layer.msg("评论成功");
                    location.reload();
                } else {
                    layer.msg(res.msg);
                }
            })
        });
        layer.close(index);
    });
}

function addWe() {
    layui.use('layer', function(){
        var layer = layui.layer;
        var $ = layui.jquery;
        var username = $.cookie('username');
        if (!isEmpty(username)) {
            layer.open({
                type: 1
                , title: false //不显示标题栏
                , closeBtn: false
                , area: '300px;'
                , shade: 0.3
                , id: 'LAY_addWe' //设定一个id，防止重复弹出
                , btn: ['确定', '取消']
                , btn1: function () {
                    doAddWe();
                }, btnAlign: 'c'
                , moveType: 1 //拖拽模式，0或者1
                , content: '<div style="padding: 30px; background-color: #393D49; color: #fff; font-size: 14px">' +
                '请输入日期和描述<br/><br/><input id="date" type="text" placeholder="日期" class="layui-input"/><br/><textarea id="description" class="layui-textarea" placeholder="描述"/></div>'
            })
        }
    })
}

function doAddWe() {
    layui.use(['layer', 'jquery'], function() {
        var layer = layui.layer;
        var $ = layui.jquery;
        var date = $('#date')[0].value;
        var description = $('#description')[0].value;
        if (isEmpty(date) || isEmpty(description)) {
            layer.alert("日期或描述不能为空");
        }
        $.post('/we/add', {date: date, description: description}, function (res) {
            if (res.code === 0) {
                layer.msg("添加成功");
                layer.closeAll();
                location.reload();
            } else {
                if (!isEmpty(res.msg)) {
                    layer.msg(res.msg);
                } else {
                    layer.msg("添加失败");
                }
            }
        }, 'json')
    })

}

layui.use('element',function() {
    var element = layui.element;
    var $ = layui.jquery;
    //获取hash来切换选项卡，假设当前地址的hash为lay-id对应的值
    var layid = location.hash.replace(/^#li=/, '');
    element.tabChange('tabBrief', layid);
    if (layid === "1") {
        showWe();
    }
    if (layid === "2") {
        showPhotos();
    }
    if (layid === "3") {
        showStories();
    }
    if (layid === "4") {
        showXq();
    }

    //监听Tab切换，以改变地址hash值
    element.on('tab(tabBrief)',
    function() {
        location.hash = 'li=' + this.getAttribute('lay-id');
    });
});

layui.use('upload',function() {
    var upload = layui.upload;

    //执行实例
    var uploadInst = upload.render({
        elem: '#uploadInPhotos' //绑定元素
        ,multiple: true,
        url: '/pic/upload/' //上传接口
        ,done: function(res) {
            //上传完毕回调
            if (res.code === 0) {
                layer.msg("上传成功")
            } else {
                layer.msg(res.msg);
            }
        },
        error: function() {
            //请求异常回调
            layer.msg("上传失败");
        }
    });
});

function isEmpty(param) {
    return param === null || param === "" || param === undefined;
}