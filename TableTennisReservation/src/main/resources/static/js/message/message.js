//初始化消息数据
var roadOrderList = function (pageNo){
    //判断是查看用户消息还是全体消息
    var url='';
    //获取表单
    var form = $('#tool_bar_form');
    var form_data = form.serializeObject();
    if (form_data.isRead==''){
        form_data.isRead=undefined;
    }
    if ('1'==form_data.type){
        url='/message/user/';
    }
    else {
        url='/message/all/';
    }
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
                //  <div class="card w-75 message_card">
                //                             <div class="card-body d-flex justify-content-between">
                //                                 <div>
                //                                     <h5 class="card-title">系统消息</h5>
                //                                     <p class="card-text">这里是消息内容，支持展示更多相关内容。</p>
                //                                 </div>
                //                                 <div class="text-end text-muted">2024-03-01 12:00:00</div>
                //                             </div>
                //                         </div>
              var card =  $('<div class="card w-75 message_card "></div>').attr('guid',message.guid);
              var card_body=$('<div class="card-body d-flex justify-content-between "></div>');
              var card_body_value = $('<div></div>')
                  .append($(' <h5 class="card-title"></h5>').text(category_text))
                  .append($(' <p class="card-text"></p>').text(message.value))
                  .append($('<p class="card-text"></p>').append($('<small class="text-muted"></small>').text(message.createTime)))
                  .appendTo(card_body)
              ;
              // var time = $('<div class="text-end text-muted"></div>').text(message.createTime)
              //     .appendTo(card_body);
              card.append(card_body);
              //单点消息且为未读消息时
              if ('0'==message.isRead&&'1'==message.type){
                   card_body.append($('<a href="#" class="card-link set_read_btn">置为已读</a>').attr('guid',message.guid));
                   card_body.addClass("text-success");
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
    $('#tool_bar_form').on('change','.is_read_radio',function () {
        roadOrderList(1);
    });
    //日期改变事件
    $('#tool_bar_form').on('change','.create_time',function () {
        roadOrderList(1);
    });

}
function set_read_event(){
    //设置为已读方法
    $('#message_view').on('click','.set_read_btn',function () {
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
            url: "/message/user/",
            dataType:'json',
            contentType: "application/json",
            data: jsonData,
            success: function (data) {
                if (data.success==true){
                    getToastMsg("已标记为已读");
                }
                else {
                    getToastMsg(data.msg);
                }
                //重新加载数据
                roadOrderList(1);
            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                console.log("保存失败！");
                console.log(error);
            }
        });
      return false;

    });
    //一键已读按钮点击事件
    $('#tool_bar_form').on('click','.set_all_read_btn',function () {
        $.ajax({
            type: 'put',
            url: "/message/setThisUserSystemMessageRead",
            dataType:'json',
            success: function (data) {
                if (data.success==true){
                    getToastMsg("已标记为已读");
                }
                else {
                    getToastMsg(data.msg);
                }
                //重新加载数据
                roadOrderList(1);
            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                console.log("保存失败！");
                console.log(error);
            }
        });
    });
}
$(function () {
    add_tool_bar_event();
    roadOrderList(1);
    set_read_event();
    //绑定页码切换事件
    add_change_pageNo_click_event(roadOrderList);
});