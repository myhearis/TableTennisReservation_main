var socket=undefined;//socket连接
var connect_user_id=undefined;//当前连接到的用户
var wait_connect_user_id=undefined;//准备连接到的用户
var now_msg_data=undefined;
//建立webSocket连接
function createChatWebSocket() {
    //发送请求获取当前用户id
    //发送ajax请求获取当前登录用户信息
    $.ajax({
        type: 'get',
        url: "/user/getUserId",
        dataType: 'json',
        success: function (data) {
            var user_id = data.data;
            //建立webSocket连接
            socket = new WebSocket('ws://'+sys_ip_port+'/websocket/chat/' + user_id);
            socket.onopen = function () {
                console.log('WebSocket连接已建立');
                //测试向服务器发送消息
                socket.send(user_id + '用户请求聊天长连接');
            };
            //监听接受到消息后的事件
            socket.onmessage = function (event) {
                var message = event.data;
                var msg_data = $.parseJSON(message); // 将 JSON 字符串转化为 JSON 对象
                //判断是否为客服确认连接消息
                if ('3'==msg_data.category){
                    //用户请求客服连接
                    //确认框-连接到用户
                    now_msg_data=msg_data;
                    create_confirm(send_confirm,'connect_yh','提示','['+msg_data.originName+']用户请求客服服务，是否连接?',cancel_confirm);
                }
                //客服取消连接消息
                else if ('4'==msg_data.category){
                    //弹出框提示用户
                    getToastMsg('客服已结束连接!');
                    //设置禁止发送消息
                    is_allow_send=false;
                }
                //客服确认连接的回应消息
                else if ('5'==msg_data.category){
                    //允许发送消息
                    is_allow_send=true;
                    getToastMsg('已成功连接到客服!');
                }
                else {
                    getToastMsg(message);
                    console.log('收到后端消息：', message);
                    insertMsgInView(msg_data,true);
                    // var messages_view = $('#messages_view');
                    // var chat_msg = create_chat_msg(user_id,msg_data);
                    // messages_view.append(chat_msg);
                    // // 将消息框滚动到最底部
                    // $("#messages_view").scrollTop($("#messages_view")[0].scrollHeight);
                }
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
        error: function (xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
}
//提交方法
function submitInfo() {
    //封装消息
    var form_data = get_data_for_form($('#send_form'));
    //构造发送的消息
    var message = {
        "originId": user_id,
        "originName": user_name,
        "targetId": select_target_id,
        "targetName": select_target_name,
        "value": form_data.message,
        "createTime":getNowDateStr()
    }
    //发送ajax请求到controller中处理消息
    $.ajax({
        url: "/message/customerService/msg",
        method: "POST",
        data:JSON.stringify(message),
        dataType: "json",
        contentType: "application/json",
        success: function(data) {
            if (data.success==true){
                //将发送的记录显示在自己的聊天记录中
                insertMsgInView(message,true);
                // var messages_view = $('#messages_view');
                // var chat_msg = create_chat_msg(user_id,message);
                // messages_view.append(chat_msg);
                // // 将消息框滚动到最底部
                // $("#messages_view").scrollTop($("#messages_view")[0].scrollHeight);
                //清空聊天框内容
                $('#send_form')[0].reset();
            }
            else {
                // 处理错误情况
                getToastMsg(data.msg);
            }

        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            getToastMsg(errorThrown);
        }
    });
}
//结束会话方法
function close_session() {
    //发送ajax请求结束会话
    $.ajax({
        url: "/message/customerService/cancelConnect",
        method: "GET",
        dataType: "json",
        success: function(data) {
            getToastMsg('会话已结束');
        },
        error: function(jqXHR, textStatus, errorThrown) {
        }
    });
}
//结束按钮添加点击事件
function close_btn_click_event() {
    $('#cancel_session_btn').on('click',function () {
        close_session();
    });
}
//给聊天框的发送按钮增加提交事件
function send_msg_click_event(){
    $('#chat_card').on('click','.send_btn',function () {
        submitInfo();
        return false;
    });
    // 监听文本框的 keydown 事件
    $('#send_form .msg_text').keydown(function(event) {
        // 检查是否按下 Enter 键（keyCode 为 13）
        if (event.keyCode === 13) {
            // 在这里执行按下 Enter 键时的操作
            submitInfo();
            // 阻止默认的 Enter 键行为（如提交表单）
            event.preventDefault();
        }
    });

}

//用户发送连接客服请求方法
function send_confirm(){
   //获取映射
    var mapping =  $('#common_confirm_modal').attr('mapping');
    //用户请求连接客服(用户角色使用)
    if ('connect_kf'==mapping){
        //发送客服连接请求
        $.ajax({
            url: "/message/customerService/connect",
            method: "GET",
            dataType: "json",
            // contentType: "application/json",
            success: function(data) {
                if (data.success==true){
                    var kf_user_id=data.data;
                    //设置选中信息
                    select_target_id=kf_user_id;
                    // select_target_name=user.userName;
                    hidden_confirm_modal();
                }
                else {
                    hidden_confirm_modal();
                    getToastMsg(data.msg);
                }

            },
            error: function(jqXHR, textStatus, errorThrown) {
                // 处理错误情况
                hidden_confirm_modal();
            }
        });
    }
    //客服用户确认连接
    else if ('connect_yh'){
        //设置选中信息
        select_target_id=now_msg_data.originId;
        hidden_confirm_modal();
        //发送用户连接请求
        $.ajax({
            url: "/message/customerService/kfConfirmConnection",
            method: "GET",
            dataType: "json",
            // contentType: "application/json",
            success: function(data) {
                getToastMsg('连接成功!');
                hidden_confirm_modal();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // 处理错误情况
                hidden_confirm_modal();
            }
        });
    }
}
//确认框取消回调方法
function cancel_confirm(){
    //获取映射
    var mapping =  $('#common_confirm_modal').attr('mapping');
    //客服调用
    if ('connect_yh'==mapping){
        //如果客服取消,发送ajax请求给后台，后台会响应给用户最终连接状态
        $.ajax({
            url: "/message/customerService/cancelConnect",
            method: "GET",
            dataType: "json",
            success: function(data) {
                hidden_confirm_modal();
                getToastMsg('已拒绝该用户的连接请求');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // 处理错误情况
                hidden_confirm_modal();
            }
        });
    }
}
//获取当前登录用户的localStorage的key_pre
function getLocalStorageChatKey(){
    return user_id+'_'+user_role_id+'_'+'kfchat';
}
//从本地缓存中加载保存的聊天记录信息
function loadHistoryMsg(){
    //清空展示的所有信息
    $('#messages_view').html('');
    var chatKey=getLocalStorageChatKey();
    var msg_json_str = localStorage.getItem(chatKey);
    if (msg_json_str){
        // 解析 JSON 字符串为 JavaScript 对象（如果存储的是对象）
        var msg_data_list = JSON.parse(msg_json_str);
        //遍历信息，生成内容到视图中
        for (var i=0;i<msg_data_list.length;i++){
            insertMsgInView(msg_data_list[i],false);
        }
    }
}
//插入一条消息到消息视图中，决定是否缓存
//msg消息对象
//isSaveLocalStorage是否保存到本地缓存中
function  insertMsgInView(msg,isSaveLocalStorage){
    //将发送的记录显示在自己的聊天记录中
    var messages_view = $('#messages_view');
    var chat_msg = create_chat_msg(user_id,msg);
    messages_view.append(chat_msg);
    // 将消息框滚动到最底部
    $("#messages_view").scrollTop($("#messages_view")[0].scrollHeight);
    if (isSaveLocalStorage){
        var chatKey = getLocalStorageChatKey();
        var msg_json_str = localStorage.getItem(chatKey);
        if (msg_json_str){
            // 解析 JSON 字符串为 JavaScript 对象（如果存储的是对象）
            var msg_data_list = JSON.parse(msg_json_str);
            msg_data_list[msg_data_list.length]=msg;
            localStorage.setItem(chatKey, JSON.stringify(msg_data_list));
        }
        //如果数据为空，则新建一个
        else {
            var history_msg_list=[];
            //将当前消息保存进去
            history_msg_list[0]=msg;
            //存入local
            localStorage.setItem(chatKey, JSON.stringify(history_msg_list));
        }
    }
}
$(function () {
    createChatWebSocket();//建立socket连接
    send_msg_click_event();//发送消息点击事件
    close_btn_click_event();//结束会话事件
    loadHistoryMsg();//加载本地历史数据
    //如果是普通用户
    if ('normal'==user_role_id){
        //确认框
        create_confirm(send_confirm,'connect_kf','提示','是否连接客服？');
        //拉起模态框
        load_confirm_modal();
    }
    else {

    }
});