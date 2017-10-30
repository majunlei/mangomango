"use strict";
layui.use('cookies', function(){
});

function loginout() {
    layui.use('layer', function(){
        var layer = layui.layer;
        var $ = layui.jquery;
        var username = $.cookie('username');
        if (isEmpty(username)) {
            layer.open({
                type: 1
                , title: false //不显示标题栏
                , closeBtn: false
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
                , closeBtn: false
                , area: '300px;'
                , shade: 0.5
                , id: 'LAY_logout' //设定一个id，防止重复弹出
                , btn: ['退出', '取消']
                , btn1: function () {
                     logout();
                }, btnAlign: 'c'
                , moveType: 1 //拖拽模式，0或者1
                , content: '<div style="padding: 30px; background-color: #393D49; color: #fff; font-size: 14px">' +
                '你好，亲爱的' + username + '<br/>确认退出吗？<br/><br/></div>'
            })
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
        $.getJSON('/user/login?username=' + username + '&password=' + password, function (res) {
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