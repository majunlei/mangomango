"use strict";
layui.config({
    base: 'js/' //假设这是test.js所在的目录
}).extend({
    cookies: 'cookies'
});

function showPhotos() {
    layui.use(['layer', 'flow'],
    function() {
        var flow = layui.flow;
        var $ = layui.jquery; //不用额外加载jQuery，flow模块本身是有依赖jQuery的，直接用即可。
        var layer = layui.layer;
        var username = $.cookie("username");
        if (!isEmpty(username)) {
            $('#uploadInPhotos').css("display", "inline");
        }
        flow.load({
            elem: '#photos' //指定列表容器
            ,
            isLazyimg: true,
            done: function(page, next) { //到达临界点（默认滚动触发），触发下一页
                var lis = [];
                //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
                $.getJSON('/pic/get?page=' + page,
                function(res) {
                    if (res.code === 0) {
                        lis.push('<div id="photos-page' + page + '" class="layui-row layui-col-space5">');
                        //假设你的列表返回在data集合中
                        layui.each(res.data,
                        function(index, item) {
                            lis.push('<div class="layui-col-xs6 layui-col-sm6 layui-col-md6">' + '<img layer-src="' + item.url + '" lay-src="' + item.thumbUrl + '" layer-index="' + item.id + '" alt="' + item.id + '" width="100%"/></div>')
                        });
                        lis.push('</div>');
                        //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                        //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                        var photoPage = "photos-page" + page;
                        next(lis.join(''), page < res.pages);
                        var thisWidth = $(document.body).width() * 0.95 + 'px';
                        layer.photos({
                            photos: '#' + photoPage,
                            area: [thisWidth],
                            move: false,
                            tab: function(pic, layero) {
                                $('.layui-layer-photos').addClass('swiper-container');
                                $('#layui-layer-photos').addClass('swiper-wrapper');
                                $('.layui-layer-phimg').addClass('swiper-slide');
                                $('#layui-layer-photos').removeClass('layui-layer-content');
                                var photos = $('#' + photoPage + '>div>img');
                                var nowIndex;
                                $.each(photos,
                                function(index, item) {
                                    if (item.alt !== pic.alt) {
                                        var thumbSrc = item.currentSrc;
                                        var src = thumbSrc.replace('_thumb', '');
                                        if (item.alt > pic.alt) {
                                            $('#layui-layer-photos').append('<div class="swiper-slide"><img width="100%" src="' + src + '"/></div>');

                                        } else {
                                            $('.layui-layer-phimg').before('<div class="swiper-slide"><img width="100%" src="' + src + '"/></div>');

                                        }
                                    } else {
                                        nowIndex = index;
                                    }
                                });
                                var swiper = new Swiper('.swiper-container', {
                                    initialSlide: nowIndex,
                                });
                            }
                        });
                    }
                });
            }
        });
    })
}

function showStories() {
    layui.use(['layer', 'flow'],
    function() {
        var flow = layui.flow;
        var $ = layui.jquery; //不用额外加载jQuery，flow模块本身是有依赖jQuery的，直接用即可。
        var layer = layui.layer;
        var username = $.cookie("username");
        if (!isEmpty(username)) {
            $('#addStory').remove();
            $('#story-detail-area').after('<div id="addStory" style="float: right"><a class="layui-btn layui-btn-radius layui-btn-normal" href="/editor">写新的</a></div>');
        }
        $('#story-detail-area').empty();
        $('#stories').removeAttr("hidden");
        flow.load({
            elem: '#stories' //指定列表容器
            ,
            done: function(page, next) { //到达临界点（默认滚动触发），触发下一页
                var lis = [];
                //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
                $.getJSON('/story/get?page=' + page,
                function(res) {
                    if (res.code === 0) {
                        //假设你的列表返回在data集合中
                        layui.each(res.data,
                        function(index, item) {
                            var dt = item.dt + '';
                            var dtFmt = dt.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1/$2/$3");
                            lis.push('<p><a class="story-title" href="javascritp:void(0)" ' + 'onclick="showStoryDetail(' + item.id + ')">' + item.title + '</a><span style="float: right">' + dtFmt + '</span></p><hr/>')
                        });
                        //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                        //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                        next(lis.join(''), page < res.pages);
                    } else {
                        layer.msg(res.msg);
                    }
                });
            }
        });
    })
}

function showXq() {
    layui.use(['layer', 'flow'],
    function() {
        var flow = layui.flow;
        var $ = layui.jquery; //不用额外加载jQuery，flow模块本身是有依赖jQuery的，直接用即可。
        var layer = layui.layer;
        var username = $.cookie("username");
        if (!isEmpty(username)) {
            $('#addXq').remove();
            $('#xqs').after('<div id="addXq" style="float: right"><a class="layui-btn layui-btn-radius layui-btn-normal" href="/newxq">写新的</a></div>');
        }
        flow.load({
            elem: '#xqs' //指定列表容器
            ,
            isLazyimg: true,
            done: function(page, next) { //到达临界点（默认滚动触发），触发下一页
                var lis = [];
                //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
                $.getJSON('/xq/get?page=' + page,
                function(res) {
                    if (res.code === 0) {
                        //假设你的列表返回在data集合中
                        lis.push('<div id="xq-page' + page + '">');
                        layui.each(res.data,
                        function(index, item) {
                            var liked = localStorage.getItem("like_" + item.id);
                            var likeIcon = '<i id="xq-like-icon' + item.id + '" class="layui-icon" style="margin-left: 10%" onclick="like(' + item.id + ')">&#xe6c6</i>';
                            if (liked === "1") {
                                likeIcon = '<i id="xq-like-icon' + item.id + '" class="layui-icon" style="margin-left: 10%; color: grey">&#xe6c6</i>';
                            }
                            lis.push('<fieldset class="layui-elem-field"><legend style="font-weight: bold">' + item.author + '</legend>' + '<div class="layui-field-box layui-row layui-col-space5">' + item.xq);
                            for (var i in item.urls) {
                                lis.push('<div class="layui-col-xs4 layui-col-sm4 layui-col-md4"><img layer-src="' + item.urls[i].url + '" lay-src="' + item.urls[i].thumbUrl + '" layer-index="' + item.id + '" alt="' + item.id + '" width="100%"/></div>')
                            }
                            lis.push('</div><div style="margin-bottom: 35px"><div class="layui-col-xs3 layui-col-sm3 layui-col-md3"><span style="margin-left: 10px; font-size: 14px">' + item.timeDesc + '</span></div>' + '<div class="layui-col-xs4 layui-col-sm4 layui-col-md4">&nbsp;</div><div class="layui-col-xs5 layui-col-sm5 layui-col-md5">' + '<span class="layui-badge-rim">看</span><span style="font-size: 14px">' + item.view + '</span>' + likeIcon + '<span id="xq-like' + item.id + '"style="font-size: 14px">' + item.like + '</span>' + '<i class="layui-icon" style="margin-left: 10%" onclick="comment(' + item.id + ')">&#xe611;</i></div></div>');

                            var commentNum = 0;
                            for (var j in item.comments) {
                                commentNum++;
                                break;
                            }
                            if (commentNum > 0) {
                                lis.push('<div style="font-size: 14px; width: 85%; margin: 0 auto"><blockquote class="layui-elem-quote layui-quote-nm" style="padding: 10px">');
                            }
                            for (var k in item.comments) {
                                lis.push(item.comments[k].comment);
                                if (k < item.comments.length - 1) {
                                    lis.push('<hr/>');
                                }
                            }
                            if (commentNum > 0) {
                                lis.push('</blockquote></div>');
                            }
                            lis.push('</fieldset>');
                        });
                        lis.push('</div>');
                        //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                        //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                        var photoPage = "xq-page" + page;
                        next(lis.join(''), page < res.pages);
                        var thisWidth = $(document.body).width() * 0.95 + 'px';
                        layer.photos({
                            photos: '#' + photoPage,
                            area: [thisWidth],
                            tab: function(pic, layero) {
                                viewAdd(pic.alt);
                            }
                        });
                    } else {
                        layer.msg(res.msg);
                    }
                });
            }
        });
    })
}

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

function showStoryDetail(id) {
    layui.use('jquery',
    function() {
        var $ = layui.jquery;
        $.getJSON('/story/detail/' + id,
        function(res) {
            if (res.code === 0) {
                var title = res.data.title;
                var author = res.data.author;
                var dt = res.data.dt + '';
                var dtFmt = dt.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1/$2/$3");
                var timeStr = dtFmt;
                var content = res.data.content;
                $('#stories').attr("hidden", "");
                var preview_area = $('#story-detail-area');
                preview_area.empty();
                preview_area.append('<hr/>');
                preview_area.append('<p class="layui-layer-title" style="font-size: 28px;font-weight: bold;">' + title + '</p>');
                preview_area.append('<fieldset class="layui-elem-field layui-field-title"><legend>' + author + '&nbsp;&nbsp;' + timeStr + '</legend>');
                preview_area.append('<div class="layui-field-box">' + content + '</div></fieldset>');
            } else {
                layer.msg(res.msg);
            }
        })
    })
}

layui.use('element',function() {
    var element = layui.element;
    var $ = layui.jquery;
    //获取hash来切换选项卡，假设当前地址的hash为lay-id对应的值
    var layid = location.hash.replace(/^#li=/, '');
    element.tabChange('tabBrief', layid);
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