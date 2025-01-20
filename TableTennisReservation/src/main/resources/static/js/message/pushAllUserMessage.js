//初始化消息数据
var roadOrderList = function (pageNo){
    //判断是查看用户消息还是全体消息
    var url='';
    //获取表单
    var form = $('#tool_bar_form');
    var form_data = form.serializeObject();
    if (form_data.category==''){
        form_data.category=undefined;
    }
    url='/message/all/';
    $.ajax({
        url: url+pageNo,
        method: "GET",
        data:form_data,
        dataType: "json",
        success: function(data) {
            // 处理响应数据
            var message_view = $('#message_view');
            //清空数据
            message_view.html('');
            var pageResult=data.data;
            //重新生成表格内容
            var list = pageResult.dataList;
            var pageNoList=pageResult.pageNoList;
            var pageNo = pageResult.pageNo;
            var prePageNo=pageResult.prePageNo;
            var nextPageNo = pageResult.nextPageNo;
            for (var i=0;i<list.length;i++){
                var message=list[i];
                //构造消息体
                var type_text = message_type[message.type];
                var category_text = message_category[message.category];//消息类别【可以不展示】
                var card =  $('<div class="card w-75 message_card "></div>').attr('guid',message.guid);
                var card_body=$('<div class="card-body d-flex justify-content-between "></div>');
                var card_body_value = $('<div></div>')
                    .append($(' <h5 class="card-title"></h5>').text(category_text))
                    .append($(' <p class="card-text"></p>').text(message.value))
                    .append($('<p class="card-text"></p>').append($('<small class="text-muted"></small>').text(message.createTime)))
                    .appendTo(card_body)
                ;
                card.append(card_body);
                //如果是系统消息
                if ('2'==message.type&&'1'==message.category){
                    card_body.append($('<a href="#" class="card-link set_notice_btn">置为公告</a>').attr('guid',message.guid));
                }
                else if ('2'==message.type&&'7'==message.category){
                    // card_body.append($('<a href="#" class="card-link set_notice_btn">置为系统消息</a>').attr('guid',message.guid));
                }
                message_view.append(card);
            }
            //重新生成页码导航
            create_page_no_nav(pageNoList,pageNo,prePageNo,nextPageNo);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//设置为公告方法
function set_notice_event(){
    //设置为已读方法
    $('#message_view').on('click','.set_notice_btn',function () {
        var data={};
        var ids=[];
        var guid =  $(this).attr('guid');
        ids[0]=guid;
        data["ids"]=ids;
        var jsonData = JSON.stringify(data);
        // console.log(json);
        //发送ajax请求
        $.ajax({
            type: 'put',
            url: "/message/setToNotice/"+guid,
            dataType:'json',
            // contentType: "application/json",
            // data: jsonData,
            success: function (data) {
                if (data.success==true){
                    getToastMsg("该消息已设置为公告消息!");
                }
                else {
                    getToastMsg(data.msg);
                }
                //重新加载数据
                roadOrderList(1);
            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                console.log("失败！");
                console.log(error);
            }
        });
        return false;
    });
}
//增加工具栏更新事件信息
function add_tool_bar_event(){
    //页签改变事件
    $('#tool_bar_form').on('change','.type_radio',function () {
        //获取表单
        var form = $('#tool_bar_form');
        var form_data = form.serializeObject();
        if ('2'==form_data.type){
            //如果是全体消息，隐藏是否未读,设置为全部消息
            //将未读的内容设置为全体消息
            $('.is_read_form-check').hide();
        }
        else {
            $('.is_read_form-check').show();
        }

        roadOrderList(1);
    });
    //单选框改变事件
    $('#tool_bar_form').on('change','.category_radio',function () {
        roadOrderList(1);
    });
    //日期改变事件
    $('#tool_bar_form').on('change','.create_time',function () {
        roadOrderList(1);
    });

}
//添加公告按钮事件
function add_msg_event(){
    //添加公告按钮
    $('#add_msg_btn').on('click',function () {
        //拉起模态框
        $('#form_update_msg_info')[0].reset();  //清空模态框数据
        $("#msgInfoModal").modal({
            backdrop:"static",
            keyboard:true
        });
    });
    //保存按钮事件
    $('#msgInfoModal').on('click','.save_btn',function () {
        //执行提交方法
        submitInfo();
    });
}
//提交保存信息
function submitInfo(){
    //获取表单数据
    var form_data = get_data_for_form($('#form_update_msg_info'));
    form_data.category='1';//系统消息 消息类别
    form_data.type='2';//全体消息  消息类型
    //保存
    $.ajax({
        type: 'POST',
        url: "/message/saveAllUserMsg/",
        data:JSON.stringify(form_data),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            if (data.success==true){
                //关闭模态框
                $('#msgInfoModal').modal('hide');
                getToastMsg("推送全体消息成功！");
                roadOrderList(1);//重新加载数据
            }
            else {
                getToastMsg(data.msg);
            }

        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            getToastMsg('保存成功!');
        }
    });
}
$(function () {
    add_tool_bar_event();
    roadOrderList(1);
    add_msg_event();
    set_notice_event();//设置为公告事件
    add_change_pageNo_click_event(roadOrderList);  //绑定页码切换事件
});