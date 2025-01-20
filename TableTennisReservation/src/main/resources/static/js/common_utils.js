/**
 * 序列化表单
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
//获取指定cookie
function getCookie(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');
    for(var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i].trim();
        if (cookie.indexOf(name) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}
function create_confirm(sucess_function,mapping,title,text,cancel_function){
    if (cancel_function!=undefined){
        //绑定取消方法
        $('#common_confirm_modal').on('click','.cancel_btn',cancel_function);
    }
    if (sucess_function!=undefined){
        //绑定确认方法
        $('#common_confirm_modal').on('click','.btn-primary',sucess_function);
    }
    //获取确认模态框
    var modal = $('#common_confirm_modal');
    //设置映射
    modal.attr('mapping',mapping);
    //设置标题
    $('#common_confirm_modal .modal-title').text(title);
    //设置文本
    $('#common_confirm_modal .modal-body-text').text(text);
    //拉起模态框
    $('#common_confirm_modal').modal({
        backdrop:"static",
        keyboard:true
    });
}
//拉起模态框
function load_confirm_modal() {
    //拉起模态框
    $("#common_confirm_modal").modal({
        backdrop:"static",
        keyboard:true
    });
}
//隐藏确认框
function hidden_confirm_modal() {
    //拉起模态框
    $("#common_confirm_modal").modal('hide');
}

//根据传入的页码信息，生成一个页码块
function createPageNvl(nowPageNo,pageNo,isPrePage,isNextPage){
    var re=undefined;
    if (isPrePage){
        var pre=$('<li class="page-item"></li>');
        var a_page = $('<a class="page-link pre_page_no a_page" href="#" aria-label="Previous"></a>').attr('pageNo',pageNo);
        a_page.append($('<span aria-hidden="true">&laquo;</span>'));
        pre.append(a_page);
        re=pre;
    }
    else if (isNextPage){
        var next=$('<li class="page-item"></li>');
        var a_page = $('<a class="page-link pre_page_no a_page" href="#" aria-label="Previous"></a>').attr('pageNo',pageNo);
        a_page.append($('<span aria-hidden="true">&raquo;</span>'));
        next.append(a_page);
        re=next;
    }
    //普通页码
    else {
        var next=$('<li class="page-item"></li>');
        var a_page = $('<a class="page-link pre_page_no a_page" href="#" aria-label="Previous"></a>').attr('pageNo',pageNo).text(pageNo);
        //活化页码
        if (pageNo==nowPageNo){
            next.addClass('active');
        }
        next.append(a_page);
        re=next;
    }

    return re;
}
//生成页码导航栏
function create_page_no_nav(pageNoList,pageNo,prePageNo,nextPageNo) {
    var page_ul = $('.page_nav .pagination');
    page_ul.innerHTML='';
    page_ul.html('');
    //生成上一页《
    createPageNvl(pageNo,prePageNo,true,false).appendTo(page_ul);
    // 生成所有页码
    for (var i=0;i<pageNoList.length;i++){
        createPageNvl(pageNo,pageNoList[i],false,false).appendTo(page_ul);
    }
    //生成下一页》
    createPageNvl(pageNo,nextPageNo,false,true).appendTo(page_ul);
}
//切换页码事件
function add_change_pageNo_click_event(load_data_page_function) {
    $('.page_nav').on('click','.a_page',function () {
        var pageNo = $(this).attr('pageNo');//获取当前要跳转的页码
        //重新加载数据
        load_data_page_function(pageNo);
        return false;
    })
}
// 将 JSON 数据设置到表单中
function set_data_to_form(data,form_element) {
    $.each(data, function(key, value) {
        form_element.find('[name="' + key + '"]').val(value);
    });
}
//获取表单数据
function get_data_for_form(form_element) {
    return form_element.serializeObject();
}
//根据菜单数据，生成菜单列表
//menuData:菜单树数据
//is_open_tree：是否展开菜单树
function generateMenu(menuData,is_open_tree) {
    var menuHtml = '';
    for (var i = 0; i < menuData.length; i++) {
        var menuItem = menuData[i];
        // if (menuItem.children && menuItem.children.length > 0) {
        //     // 有子菜单的情况
        //     //li中增加menu-is-opening menu-open为展开菜单，跟下面的ul一起使用
        //     menuHtml += '<li class="nav-item  menu-is-opening menu-open">';
        //     menuHtml += '<a href="' + menuItem.url + '" class="nav-link">';
        //     menuHtml += '<i class="nav-icon ' + menuItem.icon + '"></i>';
        //     menuHtml += '<p>' + menuItem.name +'    <i class="right fas fa-angle-left"></i>'+ '</p>';
        //     menuHtml += '</a>';
        //     //style="display: block" 展开所有菜单
        //     menuHtml += '<ul class="nav nav-treeview" style="display: block">' + generateMenu(menuItem.children) + '</ul>';
        //     menuHtml += '</li>';
        // } else {
        //     // 没有子菜单的情况
        //     menuHtml += '<li class="nav-item">';
        //     menuHtml += '<a href="' + menuItem.url + '" class="nav-link">';
        //     menuHtml += '<i class="nav-icon ' + menuItem.icon + '"></i>';
        //     menuHtml+='  <i class="nav-icon fas fa-file-alt"></i>';
        //     menuHtml += '<p>' + menuItem.name + '</p>';
        //     menuHtml += '</a>';
        //     menuHtml += '</li>';
        // }
        if (menuItem.children && menuItem.children.length > 0) {
            // 有子菜单的情况
            //li中增加menu-is-opening menu-open为展开菜单，跟下面的ul一起使用
            menuHtml += '<li class="nav-item  menu-is-opening menu-open">';
            menuHtml += '<a href="' + menuItem.url + '" class="nav-link">';
            menuHtml += '<i class="nav-icon ' + menuItem.icon + '"></i>';
            menuHtml += '<p>' + menuItem.name +'    <i class="right fas fa-angle-left"></i>'+ '</p>';
            menuHtml += '</a>';
            //style="display: block" 展开所有菜单
            menuHtml += '<ul class="nav nav-treeview" style="display: block">' + generateMenu(menuItem.children) + '</ul>';
            menuHtml += '</li>';
        } else {
            // 没有子菜单的情况
            menuHtml += '<li class="nav-item">';
            menuHtml += '<a href="' + menuItem.url + '" class="nav-link">';
            menuHtml += '<i class="nav-icon ' + menuItem.icon + '"></i>';
            menuHtml+='  <i class="nav-icon fas fa-file-alt"></i>';
            menuHtml += '<p>' + menuItem.name + '</p>';
            menuHtml += '</a>';
            menuHtml += '</li>';
        }
    }
    return menuHtml;
}
//根据后台权限加载菜单数据到前端展示
function loadMenuView() {
    $.ajax({
        url: "/menu/user/menuTree",
        method: "GET",
        dataType: "json",
        success: function(data) {
            var menuData=data.data;
            var menuHtml = generateMenu(menuData);//生成菜单列表html
            //加载到左侧菜单
            $('#menu_view_ul').html('');//清空菜单
            $('#menu_view_ul').append(menuHtml);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//根据消息内容，生成一条消息展示
//                                    <div class="direct-chat-msg">
//                                         <div class="direct-chat-infos clearfix">
//                                             <span class="direct-chat-name float-left">Alexander Pierce</span>
//                                             <span class="direct-chat-timestamp float-right">23 Jan 2:00 pm</span>
//                                         </div>
//                                         <!-- /.direct-chat-infos -->
//                                         <img class="direct-chat-img" th:src="@{/dist/img/megasteel.png}" alt="message user image">
//                                         <!-- /.direct-chat-img -->
//                                         <div class="direct-chat-text">
//                                             Is this template really for free? That's unbelievable!
//                                         </div>
//                                         <!-- /.direct-chat-text -->
//
//                                     </div>
// now_user_id当前登录用户id
// message消息json对象
function create_chat_msg(now_user_id,message){
   var msg_div = $(' <div class="direct-chat-msg"></div>');
   var chat_infos = $('<div class="direct-chat-infos clearfix"></div>');
   var chat_name=$('<span class="direct-chat-name"></span>').text(message.originName);
   var chat_timestamp = $('  <span class="direct-chat-timestamp"></span>').text(message.createTime);
   var img=$('<img class="direct-chat-img" src="/dist/img/megasteel.png" alt="message user image">');
   var chat_text=$('<div class="direct-chat-text"></div>').text(message.value);
   var status_icon = $('<i class="fas"></i>');
   //如果存在用户头像信息，则使用，否则使用默认头像信息
   if (!isNull(message.imgPath)) {
       img.prop('src',img_url+message.imgPath);
   }
   else {
       img.prop('src',"/dist/img/megasteel.png");
   }
   if (now_user_id==message.targetId){
        chat_name.addClass('float-left');
        chat_timestamp.addClass('float-right');
   }
   else {
       chat_name.addClass('float-right');
       chat_timestamp.addClass('float-left');
       msg_div.addClass('right');
       // 添加已读/未读文字标识  (只有展示自己的消息时，才会显示消息状态)
       if (message.isRead == '1') {
           status_icon.addClass('fa-check-circle text-success').text('已读');
       } else {
           status_icon.addClass('fa-clock text-muted').text('未读');
       }
   }
   //拼装
    chat_infos.append(chat_name).append(chat_timestamp).append(status_icon);
    msg_div.append(chat_infos).append(img).append(chat_text);
    return msg_div;
}
//获取当前时间
function getNowDateStr(){
    // 获取当前时间
    var currentDate = new Date();

// 格式化日期时间为 "yyyy-mm-dd HH:MM:ss" 格式
    var formattedDateTime = currentDate.getFullYear() + '-' +
        ('0' + (currentDate.getMonth() + 1)).slice(-2) + '-' +
        ('0' + currentDate.getDate()).slice(-2) + ' ' +
        ('0' + currentDate.getHours()).slice(-2) + ':' +
        ('0' + currentDate.getMinutes()).slice(-2) + ':' +
        ('0' + currentDate.getSeconds()).slice(-2);
        return formattedDateTime;
}
//获取本地缓存的所有用户信息
function get_local_user_list_key(user_id,user_role_id){
    return user_id+'_'+user_role_id+'_allUserList';//最近联系人key
}
function get_local_user_list_id_set_key(user_id,user_role_id){
    return get_local_user_list_key(user_id,user_role_id)+'_id_set';//最近联系人key
}
//获取本地缓存的所有用户列表
function get_local_user_list(user_id,user_role_id){
   var user_list=[];
   var key = get_local_user_list_key(user_id,user_role_id);
   var str = localStorage.getItem(key);
   if (str!=undefined&&str.length>0){
       user_list = JSON.parse(str);
   }
   return user_list;
}
//增加保存本地用户
function save_local_user(save_user_id,save_user_name,now_user_id,now_user_role_id){
    var user_list=[];
    var id_list=[];
    //获取key
    var key = get_local_user_list_key(now_user_id,now_user_role_id);
    //加存一个id  set集合，用于去重
    var set_key = key+'_id_set';
    //获取id集合
    var set_str = localStorage.getItem(set_key);
    if (set_str!=undefined&&set_str.length>0){
        id_list=JSON.parse(set_str);
    }
    var str = localStorage.getItem(key);
    if (str!=undefined&&str.length>0){
        user_list = JSON.parse(str);
    }
    //保存
    var save_user={
        'guid':save_user_id,
        'userName':save_user_name
    };
    debugger;
    //将list转化成set集合，判断该用户是否存在
    var id_set=new Set(id_list);
    //不存在时才保存
    if (!id_set.has(save_user_id)){
        //展示到第一个位置
        user_list.push(save_user)
        //存入id list中
        id_list.push(save_user_id);
    }
    //存入本地文件
    localStorage.setItem(key,JSON.stringify(user_list));
    localStorage.setItem(set_key,JSON.stringify(id_list));
}
//获取本地保存的用户set集合
function get_local_user_id_set(now_user_id,now_user_role_id){
    var id_list=[];
    //获取key
    var key = get_local_user_list_id_set_key(now_user_id,now_user_role_id);
    //获取list数据
    var str= localStorage.getItem(key);
    if (!isNull(str)){
        id_list=JSON.parse(str);
    }
    return new Set(id_list);
}
//判空方法
function isNull(str){
    if (str==undefined||str.length==0||str==''||str==null||str=='null')
        return true;
    return false;
}
//取值方法，如果为空，则设置为空串
function get_val(value){
    if (isNull(value)){
        return '';
    }
    return value;
}
//拉取系统公告展示到组件中
function load_sys_notice(){
    var pageNo=1;
    //判断是查看用户消息还是全体消息
    //获取表单
    var form_data = {};
    form_data.category=7;
    $.ajax({
        url: '/message/all/'+pageNo,
        method: "GET",
        data:form_data,
        dataType: "json",
        success: function(data) {
            var pageResult=data.data;
            //重新生成表格内容
            var list = pageResult.dataList;
            if (list!=undefined&&list.length==1){
                message=list[0];
                //展示信息到公告模态框中
                $('#common_system_notice_modal .modal-body-text').text(message.value);
                //拉起模态框展示
                $("#common_system_notice_modal").modal({
                    backdrop:"static",
                    keyboard:true
                });
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//生成球友匹配单展示组件
//matchInfo: 匹配单列表
//isManager:是否为管理卡片
function createPlayerMatchingItem(matchInfo,isManager){
    var ball_table=matchInfo.ballTable;
    var card = $('<div class="mb-3 d-flex align-items-center border-info flex-grow-1 " style="border: 1px solid black"></div>');
    //图片
    $(' <img height="150" src="http://localhost:8000//uploadFile/2024/04/15/267f523b-add0-48ea-a024-58253ed33193.png">').prop('src',img_url+ball_table.imgPath).appendTo(card);
    //基础信息
    var card_body=$(' <div class="pl-3"></div>')
        .append($('<h2 class="mb-2 h6 font-weight-bold"></h2>').append($('<a class="text-dark" href="#">1号桌</a>')))
        .append($('<div class="card-text text-muted small">发布用户：</div>').append($('<a href="#" class="card-link chat_user" style="margin-left: 10px"></a>').text(matchInfo.billUserName).attr('userName',matchInfo.billUserName).attr('targetId',matchInfo.billUserId)))
        .append($('<div class="card-text text-muted small">技术等级：</div>').append($('<font color="red"></font>').text(''+get_val(match_level_mapping[matchInfo.level]))))
        .append($('<div class="card-text text-muted small">使用日期：</div>').append($('<font></font>').text(''+get_val(matchInfo.startDate))))
        .append($('<div class="card-text text-muted small">使用时长(小时)：</div>').append($('<font></font>').text(''+get_val(matchInfo.useTime))))
        .append($('<div class="card-text text-muted small">状态：</div>').append($('<font></font>').text(''+get_val(match_status_mapping[matchInfo.status]))))
    ;
    //单独备注
    var remark_body=$(' <div class="pl-3"></div>')
        .append($('<div class="card-text text-muted small">备注：</div>'))
        .append($('<textarea class="custom-textarea form-control font-italic " readonly="readonly" style=" height: 100px;overflow-y: auto;"></textarea>').text(''+get_val(matchInfo.billRemark)))
    ;
    //按钮组
    var btn_body=undefined;
    //如果是管理用户自己发布的匹配单
    if (isManager){
        btn_body=$(' <div class="pl-3"></div>')
            .append($('<div class="card-text text-muted small "></div>').append($('<button type="button" class="btn btn-primary editor_btn btn-sm"></button>').text('编辑').attr("guid",matchInfo.guid)))
            .append($('<div class="card-text text-muted small "></div>').append( $('<button type="button" class="btn btn-outline-danger btn-sm delete_btn" style="margin-top: 10px"></button>').text('删除').attr("guid",matchInfo.guid)))
        ;
    }
    else {
        btn_body=$(' <div class="pl-3"></div>')
            .append($('<div class="card-text text-muted small "></div>').append($('<a href="#" class="btn btn-primary apply_matching_btn btn-sm"></a>').attr('guid',matchInfo.guid).text('匹配')))
            .append($('<div class="card-text text-muted small "></div>').append( $('<button type="button" class="btn btn-outline-info btn-sm chat_user" style="margin-top: 10px"></button>').text('聊一聊').attr('userName',matchInfo.billUserName).attr('targetId',matchInfo.billUserId)))
        ;
    }
    card.append(card_body).append(remark_body).append(btn_body);
    return card;
}
//根据匹配单列表，展示到前端id= match_info_view_div 中
function reloadPlayerMatchingView(matchInfoList,isManager){
    //将原有信息置空
    var view_div = $('#match_info_view_div');
    view_div.html('');
    //开始渲染数据
    for (var i=0;i<matchInfoList.length;i++){
        var matchInfo=matchInfoList[i];
        var card = createPlayerMatchingItem(matchInfo,isManager);
        view_div.append(card);
    }
}
//加载上侧的导航栏的未读消息数量
function reloadMessageCount(){
    //发送请求获取当前用户未读消息的数量
    $.ajax({
        url: '/message/getThisUserChatMessageCount',
        method: "GET",
        dataType: "json",
        success: function(data) {
            var count=data.data;
            //加载到消息图标中
            var common_message_menu = $('#common_message_menu');
            //先清空数据
            common_message_menu.html('');
            var msg_item = $('<a href="/userChat/" class="dropdown-item dropdown-footer"></a>').text('用户消息');
            if (count>0){
                msg_item.append($('<font></font>').text('['+count+'条未读]'));
            }
            common_message_menu.append(msg_item);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//获取用户未读消息
function getViewUserChatNotReadMessageCount(){
    //发送请求获取当前用户未读消息的数量
    $.ajax({
        url: '/message/getThisUserChatMessageCount',
        method: "GET",
        dataType: "json",
        success: function(data) {
            var count=data.data;
            if (count>0){
                getToastMsg('您有'+count+'条用户聊天未读，请注意查看！');
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//加载菜单数据
$(function () {
    //加载左侧菜单
    loadMenuView();
    reloadMessageCount();
});
