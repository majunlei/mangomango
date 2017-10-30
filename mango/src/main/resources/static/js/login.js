layui.use(['layer', 'cookies'], function(){
    var layer = layui.layer;
    var $ = layui.jquery;
    layer.ready(function(){
        var username = $.cookie('username');
        if (isEmpty(username)) {
            layer.open({
                type: 1
                , title: false //不显示标题栏
                , closeBtn: false
                , area: '300px;'
                , shade: 0.5
                , id: 'LAY_layuipro' //设定一个id，防止重复弹出
                , btn: ['登录', '我就看看']
                , btn1: function () {
                    login();
                }, btnAlign: 'c'
                , moveType: 1 //拖拽模式，0或者1
                , content: '<div style="padding: 30px; background-color: #393D49; color: #fff; font-size: 14px">' +
                '登录后方可编辑内容<br/><br/><input id="username" type="text" placeholder="用户名" class="layui-input"/><br/><input id="password" type="password" placeholder="密码" class="layui-input"/></div>'
                // ,success: function(layero){
                //     var btn = layero.find('.layui-layer-btn');
                //     btn.find('.layui-layer-btn0').attr({
                //         href: 'http://www.layui.com/'
                //         ,target: '_blank'
                //     });
                // }
            })
        }
    });
});

function login(username, password) {
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
                layer.msg("登录失败");
            }
        })
    })
}