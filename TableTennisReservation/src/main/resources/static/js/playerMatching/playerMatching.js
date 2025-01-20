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
//根据页码加载球友匹配单数据
function loadBallTableDataList(pageNo){
    //获取模糊查询表单信息
    var form = $('#selected_form');
    var form_data = form.serializeObject();
    var code=form_data.code;
    var startDate=form_data.startDate;
    //发送ajax请求数据
    $.ajax({
        type: 'get',
        url: "/playerMatching/getMacthList/"+pageNo,
        data: {
            code:code,
            startDate:startDate,
            status:0 // 只查询状态为0的，即[待匹配]的信息
        },
        cache: false,
        dataType:'json',
        // processData: false,
        // contentType: 'application/json',
        success: function (data) {
            debugger;
            var list=data.data.dataList;
            //生成展示标签
            // var view_ul = $('#match_info_view_div');
            // view_ul.html('');
            // for (var i=0;i<list.length;i++){
            //     var matchInfo=list[i];
            //     var ball_table=matchInfo.ballTable;
            //     //生成item
            //     var li = $('<li class="list-group-item"></li>');
            //     //构造card
            //     var card = $('<div class="card" style="width: 18rem; overflow: hidden;"></div>');
            //     var guid_input=$('<input type="hidden" name="guid" >').val(ball_table.guid);
            //     card.append(guid_input);
            //     var table_img_container=$('<div class="table_img_container"></div>')
            //         .append($('<img class="card-img-top img_ball_table">').prop('src',img_url+ball_table.imgPath));
            //     card.append(table_img_container);
            //     var card_body=$('<div class="card-body"></div>')
            //         .append($('<h5 class="card-title" ></h5>').text(ball_table.code+'号桌'))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">发布用户：</font>')).append($('<a href="#" class="card-link chat_user" style="margin-left: 10px"></a>').text(matchInfo.billUserName).attr('userName',matchInfo.billUserName).attr('targetId',matchInfo.billUserId)))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">技术等级：</font>')).append($('<font color="red"></font>').text(''+get_val(match_level_mapping[matchInfo.level]))))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">使用日期：</font>')).append($('<font></font>').text(''+get_val(matchInfo.startDate))))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">使用时长(小时)：</font>')).append($('<font></font>').text(''+get_val(matchInfo.useTime))))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">状态：</font>')).append($('<font></font>').text(''+get_val(match_status_mapping[matchInfo.status]))))
            //         .append($('<p class="card-text " ></p>').append($('<font color="#888888">备注：</font>')))
            //         .append($('<p class="card-text " ></p>').append($('<textarea class="custom-textarea form-control" readonly="readonly" style=" height: 100px;overflow-y: auto;"></textarea>').text(''+get_val(matchInfo.billRemark))))
            //             .append($('<a href="#" class="btn btn-primary apply_matching_btn"></a>').attr('guid',matchInfo.guid).text('匹配'))
            //             .append($('<a href="#" class="card-link chat_user" style="margin-left: 10px"></a>').text('聊一聊').attr('userName',matchInfo.billUserName).attr('targetId',matchInfo.billUserId))
            //     ;
            //     card.append(card_body);
            //     //组装li
            //     li.append(card);
            //     li.appendTo(view_ul);
            // }
            reloadPlayerMatchingView(list,false);
            //加载生成页码信息
            var pageNoList=data.data.pageNoList;
            var page_ul = $('.page_nav .pagination');
            page_ul.innerHTML='';
            page_ul.html('');
            //生成上一页
            createPageNvl(data.data.pageNo,data.data.prePageNo,true,false).appendTo(page_ul);
            for (var i=0;i<pageNoList.length;i++){
                createPageNvl(data.data.pageNo,pageNoList[i],false,false).appendTo(page_ul);
            }
            // $('.page_nav .nextPageNo').attr('pageNo',data.data.nextPageNo);//后一页
            createPageNvl(data.data.pageNo,data.data.nextPageNo,false,true).appendTo(page_ul);

        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });

}
//增加聊一聊的事件
function add_chat_user_event() {
    $('#match_info_view_div').on('click','.chat_user',function () {
        //将当前选中的用户保存进去
       var save_user_name = $(this).attr('userName');
       var save_user_id=$(this).attr('targetId');
       //跳转到聊天界面
        window.location.href = base_url+"/userChat/?targetId="+save_user_id+'&targetName='+save_user_name;
    });
    //匹配单操作中的聊一聊点击
    $("#myUpdateModal").on('click','.chat_user',function () {
        //关闭模态框（如果是在匹配单中的聊一聊）
        $("#myUpdateModal").modal('hide');
        //将当前选中的用户保存进去
        var save_user_name = $(this).attr('userName');
        var save_user_id=$(this).attr('targetId');
        //跳转到聊天界面
        window.location.href = base_url+"/userChat/?targetId="+save_user_id+'&targetName='+save_user_name;
    });
}
//生成页码
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
//绑定查询按钮
function add_seacher_event(){
    //获取按钮
    $('#selected_form').on('click','.select_btn',function () {
        //重新加载内容
        loadBallTableDataList(PAGE_NO);
    });
    //禁止提交
    return false;
}
//拉起模态框
function loadModal(button_name,guid) {
    cleanModalData();
    //拉起模态框
    $("#select_order_modal").modal({
        backdrop:"static",
        keyboard:true
    });
    //如果是更新，则回显数据
    if ('update'==button_name){
        //发送请求，回显数据
        $.ajax({
            type: 'get',
            url: "/ballTable/"+guid,
            // data: guid,
            dataType: "json",
            // contentType: "application/json",
            success: function (data) {
                var ballTable=data.data;
                //图片回显
                $('.img_ball_table').prop('src',data.imgUrl+ballTable.imgPath);
                $('#form_update_ball_table .code').val(ballTable.code);
                //选中状态
                var status_check = $('#form_update_ball_table .status');
                for(var i=0;i<status_check.length;i++){
                    if (status_check[i].value==ballTable.status){
                        status_check[i].checked = true;
                        break;
                    }
                }
                $('#form_update_ball_table .guid').val(ballTable.guid);
                $('#form_update_ball_table .address').val(ballTable.address);
                $('#form_update_ball_table .description').val(ballTable.description);
                $('#form_update_ball_table .img_path').val(ballTable.imgPath);
            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                console.log("请求失败！");
                console.log(error);
            }
        });
    }
}
//初始化（当前用户）可发布匹配订单数据
function roadOrderList(){
    $.ajax({
        url: "/reverse/getUserCanMatchReserve",
        method: "GET",
        dataType: "json",
        data:{
            //获取状态为【已付款】的订单信息
            status:3
        },
        success: function(data) {
            // 处理响应数据
            var t_body = $('#orders_tbody');
            //清空表格数据
            t_body.html('');
            //重新生成表格内容
            var list = data.data;
            for (var i=0;i<list.length;i++){
                var order = list[i];
                var tr=$('<tr></tr>');
                $('<td></td>').text(order.tableCode).appendTo(tr);
                $('<td></td>').text(order.startDate).appendTo(tr);
                $('<td></td>').text(order.useTime).appendTo(tr);
                $('<td></td>').text(order.payAmt).appendTo(tr);
                $('<td></td>').text(reserve_status_mapping[order.reserveStatus]).appendTo(tr);
                $('<input type="checkbox" class="btn btn-outline-danger checkbox_order" style="margin-top: 4px"></input>')
                    .text('取消订单').attr('guid',order.guid).val(order.guid).addClass("cancel_btn").appendTo(tr);
                t_body.append(tr);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//发布匹配按钮点击事件
function create_matching_click_event(){
    $('#create_match_btn').on('click',function () {
        roadOrderList();
        //拉起模态框
        $("#select_order_modal").modal({
            backdrop:"static",
            keyboard:true
        });

    });
}
//发布匹配的确认按钮事件
function  confirm_create_matching(){
    //获取模态框确认按钮
    $('#btn_update_submit').on('click',function () {
        //获取要发布匹配的订单信息
        var selectedValues = $('.checkbox_order:checked').map(function() {
            return this.value;
        }).get();
        //构造json数据
        var data={
            ids:selectedValues
        }
        var json_data = JSON.stringify(data);
        //发送ajax请求，修改预订单状态
        $.ajax({
            url: "/playerMatching/createMatching",
            method: "PUT",
            dataType: "json",
            contentType: "application/json",//发送json数据
            data:json_data,
            success: function(data) {
                //隐藏模态框，弹出提示信息
                $("#select_order_modal").modal('hide');
                //弹出通知
                getToastMsg('发布成功!可以到《我的发布》中编辑具体信息');
                //刷新列表
                loadBallTableDataList(PAGE_NO);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // 处理错误情况
            }
        });

    });
}
//通用模态框确认事件
function confirm_modal_sucess_event() {
    //获取映射
    var mapping =  $('#common_confirm_modal').attr('mapping');
    console.log(mapping);
    //申请匹配事件
    if ('apply_matching'==mapping){
        //发送匹配支付请求
        document.location.href=base_url+'/pay/processApplyMatchingPayReserve/'+select_match_info_id;
    }
}
//申请匹配按钮点击事件
function add_match_btn_click_event(){
    $('#match_info_view_div').on('click','.apply_matching_btn',function () {
        //选中id
        select_match_info_id=$(this).attr('guid');
       //弹出确认框
        create_confirm(confirm_modal_sucess_event,'apply_matching','提示','匹配拼桌需要双方各承担一半金额，是否确定匹配？',undefined);
    });
}
//获取当前用户申请匹配信息
function user_application_matching_event(){
    $('#user_application_matching_btn').on('click',function () {
        roadApplicationMatchInfoList();
        //拉起模态框
        $("#myUpdateModal").modal({
            backdrop:"static",
            keyboard:true
        });
    });
}
//初始化（当前用户）申请的匹配单数据
function roadApplicationMatchInfoList(){
    $.ajax({
        url: "/playerMatching/getThisUserApplicationMatchInfoList",
        method: "GET",
        dataType: "json",
        data:{
            //获取状态为【待同意】的匹配单信息
            status:1
        },
        success: function(data) {
            // 处理响应数据
            var t_body = $('#match_orders_tbody');
            //清空表格数据
            t_body.html('');
            //重新生成表格内容
            var list = data.data;
            for (var i=0;i<list.length;i++){
                var matchInfo = list[i];
                var tr=$('<tr></tr>');
                $('<td></td>').text(matchInfo.billUserName).appendTo(tr);
                $('<td></td>').text(matchInfo.tableCode).appendTo(tr);
                $('<td></td>').text(matchInfo.startDate).appendTo(tr);
                $('<td></td>').text(matchInfo.useTime).appendTo(tr);
                $('<td></td>').text(matchInfo.matchUserNeedPayAmt).appendTo(tr);
                $('<td></td>').text(match_status_mapping[matchInfo.status]).appendTo(tr);
                $('<td></td>').append(
                    $('<input type="checkbox" class="btn btn-outline-danger checkbox_order" style="margin-top: 4px"></input>')
                        .text('取消订单').attr('guid',matchInfo.guid).val(matchInfo.guid).addClass("cancel_btn")).appendTo(tr);
                $('<td></td>').append($('<a href="#" class="card-link chat_user" style="margin-left: 10px"></a>').text('聊一聊').attr('userName',matchInfo.billUserName).attr('targetId',matchInfo.billUserId)).appendTo(tr);
                t_body.append(tr);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            getToastMsg(errorThrown);
        }
    });
}
//用户取消匹配点击事件
function  user_cancel_matching_event_add(){
    //获取模态框确认按钮
    $('#btn_reject_submit').on('click',function () {
        //获取要发布匹配的订单信息
        var selectedValues = $('.checkbox_order:checked').map(function() {
            return this.value;
        }).get();
        //构造json数据
        var data={
            ids:selectedValues
        }
        var json_data = JSON.stringify(data);
        console.log(data);
        //发送ajax请求，修改预订单状态
        $.ajax({
            url: "/playerMatching/matchUserCancelMatch",
            method: "POST",
            dataType: "json",
            contentType: "application/json",//发送json数据
            data:json_data,
            success: function(data) {
                if (data.success==true){
                    //隐藏模态框，弹出提示信息
                    $("#myUpdateModal").modal('hide');
                    //弹出通知
                    getToastMsg('操作成功!');
                    //刷新列表
                    loadMatchInfoDataList(PAGE_NO);
                }
                else {
                    //隐藏模态框，弹出提示信息
                    $("#myUpdateModal").modal('hide');
                    //弹出通知
                    getToastMsg(data.msg);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // 处理错误情况
                getToastMsg(errorThrown);
            }
        });

    });
}
$(function () {
    //初始化日期选择器
    loadBallTableDataList(1);//初始化默认加载第一页
    add_change_pageNo_click_event(loadBallTableDataList);//绑定切换页码事件
    add_seacher_event();//搜索按钮事件
    create_matching_click_event();//发布匹配信息事件
    confirm_create_matching();//发布匹配确认事件
    add_chat_user_event();//聊一聊
    add_match_btn_click_event();//申请匹配按钮点击事件
    user_application_matching_event();//用户申请明细点击事件
    user_cancel_matching_event_add();//用户取消匹配事件
});