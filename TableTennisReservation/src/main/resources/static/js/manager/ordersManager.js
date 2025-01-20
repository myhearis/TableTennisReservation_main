var params={};//要提交的参数信息
//初始化订单数据
function roadOrderList(pageNo){
    var url='';//最终访问的url
    //获取工具栏表单数据
    var form_data = get_data_for_form($('#tool_bar_form'));
    //获取状态
    var type = form_data.type;
    if (isNull(type)){
        type='1';//如果为空值，则默认为进行中的订单
    }
    //进行中
    if ('1'==type){
        url='/reverse/getProgressReservePage/'+pageNo;
    }
    //待确认
    else if ('2'==type){
        url='/reverse/getWaitConfirmReservePage/'+pageNo;
    }
    //已过期
    else if ('3'==type){
        url='/reverse/getExpiredReservePage/'+pageNo;
    }
    else if ('4'==type){
        url='/reverse/getUserWaitPayOrder/'+pageNo;
    }
    $.ajax({
        // url: "/reversePage/"+pageNo,
        url: url,
        method: "GET",
        dataType: "json",
        data: params,
        success: function(data) {
            // 处理响应数据
           var t_body = $('#orders_tbody');
            //清空表格数据
            t_body.html('');
            //重新生成表格内容
            var pageResult=data.data;
            var list = pageResult.dataList;
            for (var i=0;i<list.length;i++){
                var order = list[i];
                // var tr=$('<tr></tr>');
                // $('<td></td>').append(  $('<input type="checkbox" class="btn btn-outline-danger " style="margin-top: 4px"/>')
                //     .attr('guid',order.guid).val(order.guid).addClass("checkbox_order")).appendTo(tr);
                // $('<td></td>').text(order.tableCode).appendTo(tr);
                // $('<td></td>').text(order.startDate).appendTo(tr);
                // $('<td></td>').text(order.useTime).appendTo(tr);
                // $('<td></td>').text(order.payAmt).appendTo(tr);
                // $('<td></td>').text(reserve_status_mapping[order.reserveStatus]).appendTo(tr);
                // $('<button type="button" class="btn btn-outline-danger" style="margin-top: 4px"></button>')
                //     .text('取消订单').attr('guid',order.guid).addClass("cancel_btn").appendTo(tr);
                var tr = createOrderRow(order,type);
                t_body.append(tr);
            }
            //生成页码信息
            var pageNoList = data.data.pageNoList;
            var pageNo = data.data.pageNo;//当前页
            var prePageNo=data.data.prePageNo;//上一页
            var nextPageNo=data.data.nextPageNo;//下一页
            create_page_no_nav(pageNoList,pageNo,prePageNo,nextPageNo);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//根据信息生成订单行元素
function createOrderRow(order,type) {
    var tr=$('<tr></tr>');
    $('<td></td>').append(  $('<input type="checkbox" class="btn btn-outline-danger " style="margin-top: 4px"/>')
        .attr('guid',order.guid).val(order.guid).addClass("checkbox_order")).appendTo(tr);
    $('<td></td>').text(order.tableCode).appendTo(tr);
    $('<td></td>').text(order.startDate).appendTo(tr);
    $('<td></td>').text(order.useTime).appendTo(tr);
    $('<td></td>').text(order.payAmt).appendTo(tr);
    $('<td></td>').text(reserve_status_mapping[order.reserveStatus]).appendTo(tr);
    if ('1'==type){
        $('<button type="button" class="btn btn-outline-danger" style="margin-top: 4px"></button>')
            .text('取消订单').attr('guid',order.guid).addClass("cancel_btn").appendTo(tr);
    }
    //待确认的订单
    else if ('2'==type){
        $('<button type="button" class="btn btn-outline-primary" style="margin-top: 4px"></button>')
            .text('确认订单').attr('guid',order.guid).addClass("confirm_btn").appendTo(tr);
    }
    //已过期的订单
    else if ('3'==type){
        $('<button type="button" class="btn btn-outline-secondary" style="margin-top: 4px"></button>')
            .text('删除订单').attr('guid',order.guid).addClass('delete_btn').appendTo(tr);
    }
    //待支付的订单
    else if ('4'==type){
        $('<button type="button" class="btn btn-outline-secondary" style="margin-top: 4px"></button>')
            .text('取消订单').attr('redisKey',order.outTradeNo).addClass('wait_pay_cancel_btn').appendTo(tr);
        $('<button type="button" class="btn btn-outline-primary" style="margin-top: 4px;margin-left: 4px"></button>')
            .text('去支付').attr('redisKey',order.outTradeNo).addClass('wait_pay_pay_btn').appendTo(tr);
    }
    return tr;

}
//增加工具栏更新事件信息
function add_tool_bar_event(){
    //页签改变事件
    $('#tool_bar_form').on('change','.type_radio',function () {
        //获取表单
        var form = $('#tool_bar_form');
        var form_data = get_data_for_form(form);
        params=form_data;
        roadOrderList(1);//重新加载数据
    });
    //日期改变事件
    $('#tool_bar_form').on('change','.create_time',function () {
        var form = $('#tool_bar_form');
        var form_data = get_data_for_form(form);
        params=form_data;
        console.log(params);
        roadOrderList(1);
    });

}
//给所有取消订单按钮添加取消订单点击事件
function add_cancel_btn_event(){
    $('#orders_tbody').on('click','.cancel_btn',function () {
        //获取订单id
       var guid = $(this).attr('guid');
       select_order_id=guid;
        //拉起确认框
        create_confirm(confirm_modal_success,'cancel','取消订单','取消订单会扣除部分服务费，是否确认取消订单？',undefined);
    });
}
//批量取消订单(暂未使用)
function batch_cancel_order_event(){
    $('#batch_cancel_btn').on('click',function () {
        //获取要发布匹配的订单信息
        var selectedValues = $('.checkbox_order:checked').map(function() {
            return this.value;
        }).get();
        //构造json数据
        var data={
            ids:selectedValues
        }
        console.log(data);
    })
}
//取消订单
function cancel_order(guid) {
    $.ajax({
        url: "/reverse/processCancelOrder/"+guid,
        method: "DELETE",
        dataType: "json",
        success: function(data) {
            if (data.success==true){
                hidden_confirm_modal();  //隐藏确认框
                getToastMsg("订单取消成功!");
                //刷新表格
                roadOrderList(1);
            }
            else {
                hidden_confirm_modal();  //隐藏确认框
                getToastMsg(data.msg);
            }

        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            hidden_confirm_modal();  //隐藏确认框
            getToastMsg(errorThrown);
        }
    });
}
//删除订单
function delete_order(guid) {
    $.ajax({
        url: "/reverse/"+guid,
        method: "DELETE",
        dataType: "json",
        success: function(data) {
            if (data.success==true){
                hidden_confirm_modal();  //隐藏确认框
                getToastMsg("订单删除成功!");
                //刷新表格
                roadOrderList(1);
            }
            else {
                hidden_confirm_modal();  //隐藏确认框
                getToastMsg(data.msg);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            hidden_confirm_modal();  //隐藏确认框
            getToastMsg(errorThrown);
        }
    });
}
//确认订单
function confirm_order(guid) {
    $.ajax({
        url: "/reverse/processConfirmReserve/"+guid,
        method: "PUT",
        dataType: "json",
        success: function(data) {
            if (data.success==true){
                hidden_confirm_modal();//关闭确认框
                getToastMsg("订单确认成功!");
                //刷新表格
                roadOrderList(1);
            }
            else {
                hidden_confirm_modal();//关闭确认框
                getToastMsg(data.msg);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            hidden_confirm_modal();//关闭确认框
            // 处理错误情况
            getToastMsg(errorThrown);
        }
    });
}
//确认框映射成功方法
function confirm_modal_success(){
    //获取guid
    var mapping = $('#common_confirm_modal').attr('mapping');
    //确认预订单
    if ('confirm'==mapping){
        confirm_order(select_order_id);
    }
    //取消预订单
    else if ('cancel'==mapping){
        cancel_order(select_order_id);
    }
    //删除预订单
    else if ('delete'==mapping){
        delete_order(select_order_id);
    }
    //重置映射(否则会重复发送请求，暂时不知道为什么,可能是其他页面存在引用，同时会进行调用的问题)
    $('#common_confirm_modal').attr('mapping','');
}
//确认订单事件
function add_confirm_order_event(){
    $('#orders_tbody').on('click','.confirm_btn',function () {
        //获取订单id
        select_order_id = $(this).attr('guid');
        console.log(select_order_id);
        //创建确认框
        create_confirm(confirm_modal_success,'confirm','提示','是否确认订单？如果您是匹配主单用户，该操作将退还差价',undefined);
    });
}
//删除订单事件
function add_delete_order_event(){
    $('#orders_tbody').on('click','.delete_btn',function () {
        //获取订单id
        select_order_id = $(this).attr('guid');
        console.log(select_order_id);
        //创建确认框
        create_confirm(confirm_modal_success,'delete','提示','是否确认删除该订单？',undefined);
    });
}
//待支付订单相关
function wait_pay_order_event(){
    //去支付
    $('#orders_tbody').on('click','.wait_pay_pay_btn',function () {
        //获取key
        var redisKey = $(this).attr('redisKey');
        //直接重定向
        document.location.href=base_url+'/pay/processPayReserve/'+redisKey;
    });
    //取消待支付的订单
    $('#orders_tbody').on('click','.wait_pay_cancel_btn',function () {
        //获取key
        var redisKey = $(this).attr('redisKey');
        //发送删除待支付订单请求 /reverse/processCancelUserWaitPayOrder/{redisKey}
        $.ajax({
            url: "/reverse/processCancelUserWaitPayOrder/"+redisKey,
            method: "delete",
            dataType: "json",
            success: function(data) {
                if (data.success==true){
                    // hidden_confirm_modal();//关闭确认框
                    getToastMsg("订单取消成功!");
                    //刷新表格
                    roadOrderList(1);

                }
                else {
                    // hidden_confirm_modal();//关闭确认框
                    getToastMsg(data.msg);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                hidden_confirm_modal();//关闭确认框
                // 处理错误情况
                getToastMsg(errorThrown);
            }
        });
    });
}
$(function () {
    roadOrderList(1);
    add_change_pageNo_click_event(roadOrderList);//绑定切换页码加载数据事件
    add_cancel_btn_event();
    batch_cancel_order_event();//批量取消订单
    add_tool_bar_event();//工具栏改变事件
    add_confirm_order_event();//确认订单事件
    add_delete_order_event();//删除订单事件
    wait_pay_order_event();//待支付订单相关事件
});