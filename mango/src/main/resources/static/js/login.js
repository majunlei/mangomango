"use strict";

function loginout() {
    layui.use('upload', function(){
        var layer = layui.layer;
        var $ = layui.jquery;
        var upload = layui.upload;

        var username = $.cookie('username');
        var headPhoto = $.cookie('headPhoto');
        var photo = '';
        if (!isEmpty(headPhoto)) {
            photo = '<div style="width: 100px; margin-bottom: 10px"><img src="' + headPhoto + '"/></div>';
        }
        if (isEmpty(username)) {
            layer.open({
                type: 1
                , title: false //不显示标题栏
                , area: '300px;'
                , shade: 0.5
                , id: 'LAY_login' //设定一个id，防止重复弹出
                , btn: ['登录', '我就看看']
                , btn1: function () {
                    login();
                }, btnAlign: 'c'
                , moveType: 1 //拖拽模式，0或者1
                , content: '<div style="padding: 30px; background-color: #393D49; color: #fff; font-size: 14px">' +
                '登录后方可编辑内容<br/><br/><input id="username" type="text" placeholder="用户名" class="layui-input"/><br/><input id="password" type="password" placeholder="密码" class="layui-input"/></div>'
            })
        } else {
            layer.open({
                type: 1
                , title: false //不显示标题栏
                , area: '300px;'
                , shade: 0.5
                , id: 'LAY_logout' //设定一个id，防止重复弹出
                , btn: ['退出', '设置头像']
                , btn1: function () {
                    logout();
                }, btnAlign: 'c'
                , moveType: 1 //拖拽模式，0或者1
                , content: '<div style="padding: 30px; background-color: #393D49; color: #fff; font-size: 14px">' + photo +
                '亲爱的&nbsp;<span style="font-weight: bold">' + username + '：</span><br/>你好，祝你今天有个好心情<br/><br/></div>'
            });
            //执行实例
            var uploadInst = upload.render({
                elem: $('.layui-layer-btn1') //绑定元素
                ,url: '/user/set/photo' //上传接口
                ,done: function(res){
                    if (res.code === 0) {
                        layer.msg("设置成功");
                        location.reload();
                    } else {
                        layer.msg(res.msg);
                    }
                }
                ,error: function(){
                    //请求异常回调
                    layer.msg("请求异常");
                }
            });
        }
    })
}

function login() {
    layui.use(['layer', 'jquery'], function() {
        var layer = layui.layer;
        var $ = layui.jquery;
        var username = $('#username')[0].value;
        var password = $('#password')[0].value;
        if (isEmpty(username) || isEmpty(password)) {
            layer.alert("用户名或密码不能为空");
        }
        $.getJSON('/user/login', {username: username, password: password}, function (res) {
            if (res.code === 0) {
                layer.msg("登录成功");
                layer.closeAll();
                location.reload();
            } else {
                if (!isEmpty(res.msg)) {
                    layer.msg(res.msg);
                } else {
                    layer.msg("登录出现错误");
                }
            }
        })
    })
}

function logout() {
    layui.use(['layer', 'jquery'], function() {
        var layer = layui.layer;
        var $ = layui.jquery;
        $.cookie("username", null, {expires:0,path: '/'});
        $.cookie("token", null, {expires:0,path: '/'});
        layer.msg("已退出");
        layer.closeAll();
        location.reload();
    })
}