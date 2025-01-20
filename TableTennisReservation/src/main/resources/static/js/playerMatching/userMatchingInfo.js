var params={}
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
function loadMatchInfoDataList(pageNo){
    //获取模糊查询表单信息
    // var form = $('#selected_form');
    // var form_data = form.serializeObject();
    // var code=form_data.code;
    // var startDate=form_data.processStartDate;
    // var status=form_data.status;

    //发送ajax请求数据
    $.ajax({
        type: 'get',
        url: "/playerMatching/getThisUserMacthList/"+pageNo,
        // data: {
        //     code:code,
        //     startDate:startDate,
        //     status: status
        // },
        data: params,
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
            //     // var card_body=$('<div class="card-body"></div>')
            //     //     .append($('<h5 class="card-title" ></h5>').text(ball_table.code+'号桌'))
            //     //     .append($('<p class="card-text" ></p>').text(ball_table.description))
            //     //     .append($('<p class="card-text" ></p>').text('预订时间：'+reserve.startDate))
            //     //     .append($('<p class="card-text" ></p>').text('使用时长(小时)：'+reserve.useTime))
            //     //     .append($('<a href="#" class="btn btn-primary"></a>').text('匹配').prop("href",base_url+"/reverse/"+ball_table.guid))
            //
            //     var card_body=$('<div class="card-body"></div>')
            //         .append($('<h5 class="card-title" ></h5>').text(ball_table.code+'号桌'))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">技术等级：</font>')).append($('<font color="red"></font>').text(''+get_val(match_level_mapping[matchInfo.level]))))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">使用日期：</font>')).append($('<font></font>').text(''+get_val(matchInfo.startDate))))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">使用时长(小时)：</font>')).append($('<font></font>').text(''+get_val(matchInfo.useTime))))
            //         .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">状态：</font>')).append($('<font></font>').text(''+get_val(match_status_mapping[matchInfo.status]))))
            //         .append($('<p class="card-text " ></p>').append($('<font color="#888888">备注：</font>')))
            //         .append($('<p class="card-text " ></p>').append($('<textarea class="custom-textarea form-control" readonly="readonly" style=" height: 100px;overflow-y: auto; border: none;"></textarea>').text(''+get_val(matchInfo.billRemark))))
            //         .append($('<button type="button" class="btn btn-primary editor_btn"></button>').text('编辑').attr("guid",matchInfo.guid))
            //         .append($('<button type="button" style="margin-left: 10px" class="btn btn-danger delete_btn"></button>').text('删除').attr("guid",matchInfo.guid))
            //     ;
            //     //根据状态来添加信息 如果是待同意,初始化信息为匹配用户
            //     if (1==matchInfo.status){
            //         // $('<button type="button" class="btn btn-info" data-toggle="tooltip" data-placement="top"></button>').attr('title','fdsaf').text('同意').attr("guid",matchInfo.guid)
            //         // var title='同意来自用户['+matchInfo.matchUserName+']的匹配';
            //         // card_body .append($('<button style="margin-left: 10px" type="button" class="btn btn-info confirm_match_btn" data-toggle="tooltip" data-placement="top"></button>').attr('title',title).text('同意').attr("guid",matchInfo.guid));
            //         // card_body .append($('<button style="margin-left: 10px" type="button" class="btn btn-info confirm_match_btn" data-toggle="tooltip" data-placement="top"></button>').attr('title',title).text('同意').attr("guid",matchInfo.guid));
            //         // card_body.append($('<a href="#" class="card-link chat_user" style="margin-left: 10px"></a>').text('聊一聊').attr('userName',matchInfo.matchUserName).attr('targetId',matchInfo.matchUserId))
            //     }
            //     card.append(card_body);
            //     //组装li
            //     li.append(card);
            //     li.appendTo(view_ul);
            // }
            reloadPlayerMatchingView(list,true);
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
//主单用户同意匹配事件
function confirm_match() {

}
//绑定查询按钮
function add_seacher_event(){
    //获取按钮
    $('#selected_form').on('click','.select_btn',function () {
        //重新加载内容
        loadMatchInfoDataList(PAGE_NO);
    });
    //禁止提交
    return false;
}
//拉起模态框
function loadModal(button_name,guid) {
    cleanModalData();
    //拉起模态框
    $("#myUpdateModal").modal({
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
//初始化（当前用户）预订单数据
function roadOrderList(){
    $.ajax({
        url: "/playerMatching/getThisUserBillMatchInfoList",
        method: "GET",
        dataType: "json",
        data:{
            //获取状态为【待同意】的匹配单信息
            status:1
        },
        success: function(data) {
            // 处理响应数据
            var t_body = $('#orders_tbody');
            //清空表格数据
            t_body.html('');
            //重新生成表格内容
            var list = data.data;
            for (var i=0;i<list.length;i++){
                var matchInfo = list[i];
                var tr=$('<tr></tr>');
                $('<td></td>').text(matchInfo.matchUserName).appendTo(tr);
                $('<td></td>').text(matchInfo.tableCode).appendTo(tr);
                $('<td></td>').text(matchInfo.startDate).appendTo(tr);
                $('<td></td>').text(matchInfo.useTime).appendTo(tr);
                $('<td></td>').text(matchInfo.matchUserNeedPayAmt).appendTo(tr);
                $('<td></td>').text(match_status_mapping[matchInfo.status]).appendTo(tr);
                $('<td></td>').append(
                $('<input type="checkbox" class="btn btn-outline-danger checkbox_order" style="margin-top: 4px"></input>')
                    .text('取消订单').attr('guid',matchInfo.guid).val(matchInfo.guid).addClass("cancel_btn")).appendTo(tr);
                $('<a href="#" class="card-link chat_user" style="margin-left: 10px"></a>').text('聊一聊').attr('userName',matchInfo.billUserName).attr('targetId',matchInfo.billUserId)
                $('<td></td>').append($('<a href="#" class="card-link chat_user" style="margin-left: 10px"></a>').text('聊一聊').attr('userName',matchInfo.matchUserName).attr('targetId',matchInfo.matchUserId)).appendTo(tr);
                t_body.append(tr);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            getToastMsg(errorThrown);
        }
    });
}
//发布匹配按钮点击事件
function create_matching_click_event(){
    $('#create_match_btn').on('click',function () {
        roadOrderList();
        //拉起模态框
        $("#myUpdateModal").modal({
            backdrop:"static",
            keyboard:true
        });

    });
}
//主单用户同意匹配点击事件
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
            url: "/playerMatching/billUserConfirmMatch",
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
//主单用户拒绝匹配点击事件
function  confirm_reject_matching(){
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
        //发送ajax请求，修改预订单状态
        $.ajax({
            url: "/playerMatching/billUserRejectMatch",
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
//拉起模态框
function loadModal(button_name,guid) {
    //清空模态框的数据
    cleanModalData();
    //初始化模态框基础数据 技术等级
    var select = $('#form_update_match_info .level');
    select.html('');
    for (var key in match_level_mapping){
        $('<option></option>').text(match_level_mapping[key]).val(key).appendTo(select);
    }
    //拉起模态框
    $("#myUpdateMatchInfoModal").modal({
        backdrop:"static",
        keyboard:true
    });
    //如果是更新，则回显数据
    if ('update'==button_name){
        //发送请求，回显数据
        $.ajax({
            type: 'get',
            url: "/playerMatching/getThisUserMatchInfo/"+guid,
            // data: guid,
            dataType: "json",
            // contentType: "application/json",
            success: function (data) {
                if (data.success==true){
                    var matchInfo=data.data;
                    var ballTable=matchInfo.ballTable;
                    //图片回显
                    $('.img_ball_table').prop('src',img_url+ballTable.imgPath);
                    //回显数据
                    set_data_to_form(matchInfo,$('#form_update_match_info'));
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
}
//清空模态框的信息
function cleanModalData() {
    //获取对应表单,重置数据
    $('#form_update_match_info')[0].reset();
}
//给编辑按钮添加点击事件
function add_editor_click_event(){
    $('#match_info_view_div').on('click','.editor_btn',function () {
        //获取当前guid
        var guid = $(this).attr('guid');
       loadModal('update',guid);
    });
}
//给删除匹配单按钮添加点击事件
function add_delete_click_event() {
    $('#match_info_view_div').on('click','.delete_btn',function () {
        //获取当前匹配单guid
        var guid = $(this).attr('guid');
        //发送ajax请求删除数据
        $.ajax({
            type: 'DELETE',
            url: "/playerMatching/matchInfo/"+guid,
            // data: JSON.stringify(form_data),
            dataType: "json",
            // contentType: "application/json",
            success: function (data) {
                if (data.success==true){
                    //重新加载数据
                    loadMatchInfoDataList(1);
                    //提示信息
                    getToastMsg('删除成功!');
                }
                else {
                    getToastMsg(data.msg);
                }

            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                getToastMsg(error);
            }
        });
    });
}
//模态框保存按钮事件 (用于更新匹配单的一些备注信息)
function add_save_btn_click_event(){
    $('#myUpdateMatchInfoModal').on('click','.save_btn',function () {
        //获取表单数据
        var form_data = get_data_for_form($('#form_update_match_info'));
        console.log(form_data);
        //发送ajax请求修改数据
        $.ajax({
            type: 'PUT',
            url: "/playerMatching/updateThisUserMatchInfo/",
            data: JSON.stringify(form_data),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (data.success==true){
                    //重新加载数据
                    loadMatchInfoDataList(1);
                    //关闭模态框
                    $('#myUpdateMatchInfoModal').modal('hide');
                    //提示信息
                    getToastMsg('保存成功!');
                }
                else {
                    getToastMsg(data.msg);
                }

            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                getToastMsg(error);
            }
        });
    });
}
//状态改变查询事件 (废弃，已经使用工具栏改变监听)
// function add_status_change_event(){
//     $('#selected_form').on('change','.status_select',function () {
//         //重新加载内容
//         loadMatchInfoDataList(PAGE_NO);
//     });
// }
//加载基础数据，如下拉框的数据
function load_base_data(){
   var status_select = $('#selected_form .status_select');
    status_select.html('');
    //加一个全部(默认选中该)
    $('<option selected="selected"></option>').text('全部').val('').appendTo(status_select);
    //获取映射
    for (var key in match_status_mapping){
        $('<option></option>').text(match_status_mapping[key]).val(key).appendTo(status_select);
    }
}
//增加聊一聊的事件
function add_chat_user_event() {
    //匹配单展示的聊一聊
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
//工具栏查询条件改变事件
function toolbar_change_event(){
    var form=$('#selected_form');
    //监听改变事件
    form.on('change','.condition',function () {
        var form_data = get_data_for_form($('#selected_form'));
        //将条件保存到变量
        params=form_data;
        //重新加载页面数据
        loadMatchInfoDataList(PAGE_NO);
    });
}
$(function () {
    load_base_data();//加载基础数据
    loadMatchInfoDataList(1);//初始化默认加载第一页
    add_change_pageNo_click_event(loadMatchInfoDataList);//绑定切换页码事件
    add_seacher_event();//搜索按钮事件
    create_matching_click_event();//发布匹配信息事件
    confirm_create_matching();//发布匹配确认事件
    add_editor_click_event();//编辑按钮点击事件
    add_save_btn_click_event();
    // add_status_change_event();//状态框选择内容改变事件
    toolbar_change_event();//工具栏查询条件改变事件
    add_chat_user_event();//聊一聊
    confirm_reject_matching();//主单用户拒绝匹配事件
    add_delete_click_event();//删除匹配单事件
});