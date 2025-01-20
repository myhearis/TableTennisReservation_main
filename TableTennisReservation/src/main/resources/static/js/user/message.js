//通用的消息webSocket，用于后端实时通知前端
var socket_ip_port=sys_ip_port;// 内网穿透ip端口映射：
//建立webSocket连接
function createWebSocket(){
    //发送请求获取当前用户id
    //发送ajax请求获取当前登录用户信息
    $.ajax({
        type: 'get',
        url: "/user/getUserId",
        dataType:'json',
        success: function (data) {
            var user_id=data.data;
            //建立webSocket连接
            // socket_ip_port='192.168.1.103:8000';
            var socket = new WebSocket('ws://'+socket_ip_port+'/websocket/'+user_id);
            socket.onopen = function() {
                console.log('WebSocket连接已建立');
                //测试向服务器发送消息
                socket.send(user_id+'用户请求长连接');
            };
            //监听接受到消息后的事件
            socket.onmessage = function(event) {
                var message = event.data;
                getToastMsg(message);
                console.log('收到后端消息：', message);
            };
            //
            // socket.onclose = function(event) {
            //     console.log('WebSocket连接已关闭');
            // };
            //
            // socket.onerror = function(error) {
            //     console.error('WebSocket发生错误：', error);
            // };

        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });



}
//弹出提示框
function getToastMsg(msg) {
    //展示提示框
    $('#socket_message').toast('show');
    //开启自动隐藏和自动隐藏时间
    $('#socket_message').toast({
        autohide:true,
        delay:600
    });
    //设置提示时间
    var currentTime = new Date(); // 创建一个Date对象
    var hours = currentTime.getHours(); // 获取当前小时数（0-23）
    var minutes = currentTime.getMinutes(); // 获取当前分钟数（0-59）
    var seconds = currentTime.getSeconds(); // 获取当前秒数（0-59）
    // 格式化时间
    var formattedTime = hours + ":" + minutes + ":" + seconds;
    $('#socket_message .small_text').text(formattedTime);
    //展示信息
    $('#socket_message .toast-body').text(msg);
}

$(function () {
    createWebSocket();
});