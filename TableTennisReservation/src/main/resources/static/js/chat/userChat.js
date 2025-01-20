var socket_ip_port=sys_ip_port;// 内网穿透ip端口映射：http://8n9012x221.vicp.fun
//生成键值
var allChatUserListKey=user_id+'_'+user_role_id+'_allUserList';//最近联系人
//保存用户头像映射的key
var userImgKey=user_id+'_'+user_role_id+'_userAndImg';
//获取目标本地聊天记录
function get_user_chat_key(targetId) {
    return user_id+'_'+user_role_id+'_'+targetId+'_chathistory';
}
//根据选中的数据，从本地中加载数据到聊天框中
//targetId:目标用户
// not_read_id_set：当前用户发送的未读消息id，用于展示目标用户的未读角标
function reLoadChatHistory(targetId,not_read_id_set) {
    if (targetId==undefined||targetId.length==0){
        return false;
    }
    //获取key
    var key=get_user_chat_key(targetId);
    //本地获取聊天记录 list
    var str = localStorage.getItem(key);
    //如果存在历史记录
    if (str!=undefined&&str.length>0){
        //清空聊天框内容
        $("#messages_view").html('');
        var msg_list = JSON.parse(str);
        //遍历加载到聊天框中
        for (var i=0;i<msg_list.length;i++){
            var msg_data=msg_list[i];
            //判断消息的读取状态
            if (not_read_id_set!=undefined&&not_read_id_set.length>0&&not_read_id_set.has(msg_data.guid)){
                //将消息状态设置为未读状态
                msg_data.isRead=0;
            }
            else {
                msg_data.isRead=1;
            }
            insertMsgInView(msg_data,false,true);
        }
    }
}
//分页加载聊天历史数据
function reLoadChatHistoryPage(targetId,not_read_id_set,pageNo,pageSize) {
    //起始索引=(pageNo-1)*pageSize
    var startIndex = (pageNo-1)*pageSize;
    //尾索引
    var endIndex = startIndex + pageSize;
    if (targetId==undefined||targetId.length==0){
        return false;
    }
    //获取key
    var key=get_user_chat_key(targetId);
    //本地获取聊天记录 list
    var str = localStorage.getItem(key);
    //如果存在历史记录
    if (str!=undefined&&str.length>0){
        //清空聊天框内容
        $("#messages_view").html('');
        var msg_list = JSON.parse(str);
        //防止越界
        if (endIndex+1>msg_list.length){
            endIndex=msg_list.length-1;
            startIndex=endIndex-pageSize+1;
        }
        if (startIndex<0){
            startIndex=0;
        }
        //遍历加载到聊天框中
        for (var i=startIndex;i<=endIndex;i++){
            var msg_data=msg_list[i];
            //判断消息的读取状态
            if (not_read_id_set!=undefined&&not_read_id_set.length>0&&not_read_id_set.has(msg_data.guid)){
                //将消息状态设置为未读状态
                msg_data.isRead=0;
            }
            else {
                msg_data.isRead=1;
            }
            insertMsgInView(msg_data,false,true);
        }
    }
}
//从本地加载聊天用户信息
function reloadChatUserList(){
    var user_list = get_local_user_list(user_id,user_role_id);//加载本地用户数据
    var view_ul = $('#user_list_view_ul');
    view_ul.html('');
    //生成数据展示到联系人列表中
    for (var i=0;i<user_list.length;i++){
        var li=$(' <li class="list-group-item list-group-item-action  d-flex justify-content-between align-items-center"></li>')
            .attr('guid',user_list[i].guid).attr('userName',user_list[i].userName).addClass('user_li').text(user_list[i].userName)
        ;
        li.append($('<img class="direct-chat-img"  alt="message user image">').prop('src',img_url+getUserImg(user_list[i].guid)));
        //是否存在选中用户
        if (select_target_id==user_list[i].guid){
            li.addClass('active');
            //选中的同时，加载历史聊天数据
            //从本地加载聊天记录信息
            var list = get_not_read_msg_list(user_list[i].guid);
            var not_read_id_set=new Set(list);//未读取消息的id set集合
            // reLoadChatHistory(select_target_id,not_read_id_set);
            reLoadChatHistoryPage(select_target_id,not_read_id_set,9999,MAX_VIEW_MSG_COUNT);
        }
        view_ul.append(li);
    }
}
//给用户添加点击事件
function user_tr_click_add_event(){
    $('#user_list_view_ul').on('click','.user_li',function () {
        //获取目标用户的guid和name
        var guid = $(this).attr('guid');
        var userName = $(this).attr('userName');
        //取消历史选中
        $('#user_list_view_ul .user_li').removeClass('active');
        //设置当前为选中状态
        $(this).addClass('active');
        //取消右侧未读消息角标
        $(this).find('.not_read_count_view').remove();
        //设置为目标用户
        select_target_id=guid;
        select_target_name=userName;
        //从本地加载聊天记录信息
        var list = get_not_read_msg_list(guid);
        var not_read_id_set=new Set(list);//未读取消息的id set集合
        // reLoadChatHistory(select_target_id,not_read_id_set);
        reLoadChatHistoryPage(select_target_id,not_read_id_set,9999,MAX_VIEW_MSG_COUNT);
    });
}
//发送给选择的target用户的离线消息id列表
function get_not_read_msg_list(targetId) {
    var ids=[];//初始化列表
    $.ajax({
        url: "/userChat/notRead/"+targetId,
        method: "GET",
        dataType: "json",
        // contentType: "application/json",
        success: function(data) {
            if (data.success==true){
                ids=data.data;
            }
            else {
                // 处理错误情况
                getToastMsg(data.msg);
            }
            return ids;

        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            getToastMsg(errorThrown);
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
    //如果是给自己发送消息，则不需要进行后端推送消息，直接展示在前端即可
    if (select_target_id==user_id){
        insertMsgInView(message,true,true);
        //清空聊天框内容
        $('#send_form')[0].reset();
        return ;
    }
    //发送ajax请求到controller中处理消息
    $.ajax({
        url: "/userChat/msg",
        method: "POST",
        data:JSON.stringify(message),
        dataType: "json",
        contentType: "application/json",
        success: function(data) {
            if (data.success==true){
                //后端回传的消息显示在自己的聊天记录中
                var re_msg=data.data;
                //判断当前展示的消息数量是否超过了最大数量，如果超过，则移除第一个消息
                insertMsgInView(re_msg,true,true);
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
            // socket_ip_port='192.168.1.105:8000';
            socket = new WebSocket('ws://'+socket_ip_port+'/websocket/userChat/' + user_id);
            socket.onopen = function () {
                console.log('WebSocket连接已建立');
                //测试向服务器发送消息
                socket.send(user_id + '用户请求聊天长连接');
            };
            //监听接受到消息后的事件
            socket.onmessage = function (event) {
                var message = event.data;
                var msg_data = $.parseJSON(message); // 将 JSON 字符串转化为 JSON 对象
                //判断是否是新用户的消息
                var id_set = get_local_user_id_set(user_id,user_role_id);
                //如果不存在，说明是新用户
                if (!id_set.has(msg_data.originId)){
                    //保存新用户到本地
                    save_local_user(msg_data.originId,msg_data.originName,user_id,user_role_id);
                    //先更新用户头像基础数据
                    reloadUserImg();
                    //展示在第一个
                    var view_ul = $('#user_list_view_ul');
                    var li=$(' <li class="list-group-item list-group-item-action  d-flex justify-content-between align-items-center"></li>')
                        .attr('guid',msg_data.originId).attr('userName',msg_data.originName).addClass('user_li').text(msg_data.originName)
                    ;
                    li.append($('<img class="direct-chat-img"  alt="message user image">').prop('src',img_url+getUserImg(msg_data.originId)));
                    view_ul.prepend(li);
                }
                //判断是否为当前选中联系人的消息
                if (select_target_id==msg_data.originId){
                    insertMsgInView(msg_data,true,true,select_target_id);
                }
                //如果不是当前选中的联系人，则保存到local中但是不展示,并保存到对应本地缓存中
                else {
                    insertMsgInView(msg_data,true,false,msg_data.originId);
                    set_user_not_read_count_view(msg_data.originId,1,true);//追加未读消息
                }
                var prompt='收到来自['+msg_data.originName+']用户的消息:'+msg_data.value;
                getToastMsg(prompt);
            };

        },
        error: function (xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
}
//插入一条消息到消息视图中，决定是否缓存
//msg消息对象
//isSaveLocalStorage是否保存到本地缓存中
//isViewMsg 是否展示消息到聊天框
//targetId 关于哪个目标用户的聊天记录信息
function  insertMsgInView(msg,isSaveLocalStorage,isViewMsg,targetId){
    //补充加载该消息的源用户头像
    var imgPath = getUserImg(msg.originId);
    msg['imgPath']=imgPath;
    //将发送的记录显示在自己的聊天记录中
    if (isViewMsg){
        var messages_view = $('#messages_view');
        var chat_msg = create_chat_msg(user_id,msg);
        //判断展示的消息数量是否超过了最大数量
        if (messages_view.find('.direct-chat-msg').length>=MAX_VIEW_MSG_COUNT){
            //移除第一条消息
            $('#messages_view').find('.direct-chat-msg:first').remove();
        }
        messages_view.append(chat_msg);
    }
    // 将消息框滚动到最底部
    $("#messages_view").scrollTop($("#messages_view")[0].scrollHeight);
    debugger;
    if (isSaveLocalStorage){
        var chatKey = get_user_chat_key(select_target_id);
        if (!isNull(targetId)){
            chatKey=get_user_chat_key(targetId);
        }
        var msg_json_str = localStorage.getItem(chatKey);
        if (!isNull(msg_json_str)){
            // 解析 JSON 字符串为 JavaScript 对象（如果存储的是对象）
            var msg_data_list = JSON.parse(msg_json_str);
            msg_data_list[msg_data_list.length]=msg;
            localStorage.setItem(chatKey, JSON.stringify(msg_data_list));
        }
        //如果数据为空，则新建一个,代表为新联系人
        else {
            var history_msg_list=[];
            //将当前消息保存进去
            history_msg_list[0]=msg;
            //存入local
            localStorage.setItem(chatKey, JSON.stringify(history_msg_list));
            reloadUserImg();//重新加载一遍用户头像
        }
    }
}
//将消息保存到客户端本地,并返回操作的消息id
function saveMsgToLocal(msgList,targetId) {
  var op_ids=[];//操作的id
  var history_msg_list=[];//历史数据列表
  var chatKey =  get_user_chat_key(targetId);
  var msg_json_str = localStorage.getItem(chatKey);
  if (msg_json_str){
        // 解析 JSON 字符串为 JavaScript 对象（如果存储的是对象）
        history_msg_list = JSON.parse(msg_json_str);
  }
  var index=history_msg_list.length;//保存的索引位置
    //存入消息列表到历史数据中
    for (var i=0;i<msgList.length;i++){
        history_msg_list[index+i]=msgList[i];//最新消息保存在后面
        op_ids.push(msgList[i].guid);
    }
    //存入local
    localStorage.setItem(chatKey, JSON.stringify(history_msg_list));
    return op_ids;
}
//加载当前用户离线消息到客户端本地
function loadOffLineMsgToLocal(){
    $.ajax({
        url: "/userChat/msg",
        method: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function(data) {
            if (data.success==true){
                //获取消息map
                var mapList=data.data;
                var delete_ids=[];
                //获取当前本地保存的用户id集合
                var id_set = get_local_user_id_set(user_id,user_role_id);
                if (mapList!=undefined){
                    $.each(mapList, function (targetId, list)
                    {
                        //判断id是否存在于本地，如果不存在，则添加新用户
                        if (!id_set.has(targetId)){
                            //取目标用户的第一条消息的内容作为保存的数据
                            save_local_user(list[0].originId,list[0].originName,user_id,user_role_id);
                            //展示用户在第一个
                            var view_ul = $('#user_list_view_ul');
                            var li=$(' <li class="list-group-item list-group-item-action  d-flex justify-content-between align-items-center"></li>')
                                .attr('guid',list[0].originId).attr('userName',list[0].originName).addClass('user_li').text(list[0].originName)
                            ;
                            view_ul.prepend(li);
                        }
                       var op_ids = saveMsgToLocal(list,targetId);
                       for (var i=0;i<op_ids.length;i++){
                           delete_ids.push(op_ids[i]);
                       }
                       //设置未读消息数量
                        set_user_not_read_count_view(targetId,list.length,false);
                    });
                }
                //将要删除的消息id传入后端删除
                deleteMsgList(delete_ids);
                //这里再加载一遍联系人头像信息，防止存在新增的联系人没加载投向
                reloadUserImg();
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
//删除消息
function deleteMsgList(ids){
    var data={
        'ids':ids
    }
    $.ajax({
        url: "/userChat/msg",
        method: "DELETE",
        dataType: "json",
        data:JSON.stringify(data),
        contentType: "application/json",
        success: function(data) {
            if (data.success==true){
                getToastMsg('离线消息初始化完成,消息总数:'+ids.length);
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
//给指定用户列表设置未读消息角标
//is_append：是否追加，即在原来的未读数量上追加
function set_user_not_read_count_view(user_id,count,is_append){
    //获取用户无序列表
    var selector = '#user_list_view_ul .user_li[guid="' + user_id + '"]';
    //在原本基础上追加数量
    if (is_append){
        //判断是否存在
       var list = $(selector).find('.not_read_count_view');
       if (list!=undefined&&list.length==1){
           var pre_count = list.text();
           list.text(parseInt(pre_count,10)+parseInt(count,10));
       }
       //如果不存在，则新增
       else {
           var count_view=$('<span class="badge badge-primary badge-pill"></span>').text(count).addClass('not_read_count_view');
           //先取消历史未读消息角标
           $(selector).find('.not_read_count_view').remove();
           //再新增展示
           $(selector).append(count_view);
       }
    }
    //重新刷入数据
    else {
        var count_view=$('<span class="badge badge-primary badge-pill"></span>').text(count).addClass('not_read_count_view');
        //先取消历史未读消息角标
        $(selector).find('.not_read_count_view').remove();
        //再新增展示
        $(selector).append(count_view);
    }
}
//加载一遍联系人的头像信息，使用localstore存储
function reloadUserImg(){
    //获取本地联系人
    var user_list = get_local_user_list(user_id,user_role_id);//加载本地用户数据
    var ids=[];
    for (var i=0;i<user_list.length;i++){
        ids[i]=user_list[i].guid;
    }
    //补充，包含当前登录用户的头像
    ids.push(user_id);
    var data={
        'ids':ids
    };
    //发送ajax请求
    $.ajax({
        async: false,//采用同步方式加载，防止基础数据加载不及时导致图片没有加载
        url: "/user/getUserImgPath",
        method: "POST",
        dataType: "json",
        data:JSON.stringify(data),
        contentType: "application/json",
        success: function(data) {
            if (data.success==true){
                var map = data.data;
                localStorage.setItem(userImgKey,JSON.stringify(map));
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
//获取对应用户头像url
function getUserImg(userId){
    var user_img_map=undefined;
    var str = localStorage.getItem(userImgKey);
    if (str!=undefined&&str.length>0){
        user_img_map = JSON.parse(str);
        return user_img_map[userId];
    }
    return  '';
}
$(function () {
    //如果当前有选中用户，可能是新用户，先保存到本地
    if (!isNull(select_target_id)&&!isNull(select_target_name)){
        save_local_user(select_target_id,select_target_name,user_id,user_role_id);
    }
    else {
        select_target_id=undefined;
        select_target_name=undefined;
    }
    reloadUserImg();//根据已有的联系人，从服务器重新加载用户头像信息,该方法是同步方法，防止没加载完成无法展示用户头像信息
    createChatWebSocket();//建立webSocket连接
    user_tr_click_add_event();
    send_msg_click_event();
    reloadChatUserList(); //加载联系人信息
    loadOffLineMsgToLocal();//加载发送给当前用户的离线消息

});