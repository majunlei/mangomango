layui.use(['layer', 'jquery'], function a(){
    var layer = layui.layer;
    var $ = layui.jquery;

    var arr = ["/audio/田馥甄 - 小幸运.mp3", "/audio/范玮琪 - 最重要的决定.mp3", "/audio/范玮琪 - 我们的纪念日.mp3"
        , "/audio/Martin Jensen - Solo Dance.mp3", "/audio/G.E.M. 邓紫棋 - 喜欢你.mp3","/audio/周杰伦 - 告白气球.mp3","/audio/张宇 - 给你们.mp3"];
    var index = 0;

    var width = window.innerWidth > 500 ? "420px" : "80%";
    var audioBox = $('#audioBox');
    audioBox.append('<audio id="audioPlayer" preload="preload" controls="controls" style="width: ' + width + '" src="' + arr[index] + '"></audio>');
    var audioPlayer = $('#audioPlayer');
    audioPlayer.bind('ended', next);
    audioBox.append('<i id="previousI" class="layui-icon">&#xe603;</i>');
    audioBox.append('<i id="nextI" class="layui-icon">&#xe602;</i>');
    audioBox.append('<i id="closeAudioI" class="layui-icon">&#x1006;</i>');
    $('#previousI').bind('click', previous);
    $('#nextI').bind('click', next);
    $('#closeAudioI').bind('click', closeAudio);
    function previous() {
        index--;
        if (index < 0) {
            index = arr.length - 1;
        }
        audioPlayer.attr("src", arr[index]);
        audioPlayer[0].play();
    }

    function next() {
        index++;
        if (index >= arr.length) {
            index = 0;
        }
        audioPlayer.attr("src", arr[index]);
        audioPlayer[0].play();
    }

    function closeAudio() {
        document.getElementById("audioBox").remove();
        $('.layui-tab-item').css("margin-top", "40px");
    }
});

