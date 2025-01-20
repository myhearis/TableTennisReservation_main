//基础数据映射关系
var candidate_reserve_mapping={
    0:'候补中',
    1:'候补成功'
}
//初始化订单数据
var roadOrderList = function (pageNo){
    //获取工具栏表单数据
    var form_data = get_data_for_form($('#tool_bar_form'));
    //获取状态
    var type = form_data.type;
    if (isNull(type)){
        type='0';//如果为空值，则默认为进行中的候补单
    }
    $.ajax({
        url: "/candidateReserve/user/"+pageNo,
        data:{
            status:type
        },
        method: "GET",
        dataType: "json",
        success: function(data) {
            // 处理响应数据
            var t_body = $('#orders_tbody');
            //清空表格数据
            t_body.html('');
            var pageResult=data.data;
            //重新生成表格内容
            var list = pageResult.dataList;
            var pageNoList=pageResult.pageNoList;
            var pageNo = pageResult.pageNo;
            var prePageNo=pageResult.prePageNo;
            var nextPageNo = pageResult.nextPageNo;
            for (var i=0;i<list.length;i++){
                var order = list[i];
                var tr=$('<tr></tr>');
                $('<td></td>').text(order.tableCode).appendTo(tr);
                $('<td></td>').text(order.startDate).appendTo(tr);
                $('<td></td>').text(order.useTime).appendTo(tr);
                $('<td></td>').text(order.payAmt).appendTo(tr);
                $('<td></td>').text(candidate_reserve_mapping[order.status]).appendTo(tr);
                if ('0'==order.status){
                    $('<button type="button" class="btn btn-outline-danger" style="margin-top: 4px"></button>')
                        .text('取消订单').attr('guid',order.guid).addClass("cancel_btn").appendTo(tr);
                }
                t_body.append(tr);
            }
            //重新生成页码导航
            create_page_no_nav(pageNoList,pageNo,prePageNo,nextPageNo);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//确认模态框成功回调方法
function confirm_modal_success() {
    //获取映射
    //获取guid
    var mapping = $('#common_confirm_modal').attr('mapping');
    //删除候补单
    if ('delete'==mapping){
        delete_hb_order(select_candidate_reserve_id);
    }
    //重置映射(否则会重复发送请求，暂时不知道为什么,可能是其他页面存在引用，同时会进行调用的问题)
    $('#common_confirm_modal').attr('mapping','');
}
//删除候补单
function delete_hb_order(guid) {
    $.ajax({
        url: "/candidateReserve/"+guid,
        method: "DELETE",
        dataType: "json",
        success: function(data) {
            if (data.success==true){
                //关闭模态框
                hidden_confirm_modal();
                getToastMsg("候补单取消成功!");
                //刷新表格
                roadOrderList(1);
            }
            else {
                getToastMsg(data.msg);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            //关闭模态框
            hidden_confirm_modal();
            getToastMsg(errorThrown);
        }
    });
}
//给所有删除按钮添加删除订单点击事件
function add_delete_btn_event(){
    $('#orders_tbody').on('click','.cancel_btn',function () {
        //获取订单id
        var guid = $(this).attr('guid');
        //设置选中
        select_candidate_reserve_id = guid;
        //拉起确认框
        create_confirm(confirm_modal_success,'delete','提示','确认取消该候补单吗？',undefined);
    });
}
//增加工具栏更新事件信息
function add_tool_bar_event(){
    //页签改变事件
    $('#tool_bar_form').on('change','.type_radio',function () {
        //获取表单
        var form = $('#tool_bar_form');
        var form_data = form.serializeObject();
        roadOrderList(1);
    });

}
$(function () {
    roadOrderList(1);
    add_delete_btn_event();//取消订单事件
    add_change_pageNo_click_event(roadOrderList);//绑定页码切换事件
    add_tool_bar_event();//工具栏切换事件
});