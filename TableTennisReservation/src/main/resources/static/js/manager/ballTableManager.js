var  button_name='';//全局按钮名称
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
//清空模态框的信息
function cleanModalData() {
    //清除所选择的文件
    var fileInput = $('#img_file')[0];
    fileInput.value='';
    //清除图片
    $('.img_ball_table').prop('src','');
    $('#form_update_ball_table .code').val('');
    //清空选中状态
    var status_check = $('#form_update_ball_table .status');
    for(var i=0;i<status_check.length;i++){
        status_check[i].checked = false;
    }
    $('#form_update_ball_table .guid').val('');
    $('#form_update_ball_table .address').val('');
    $('#form_update_ball_table .description').val('');
    $('#form_update_ball_table .img_path').val('');
    $('#form_update_ball_table .startTime').val('');
    $('#form_update_ball_table .endTime').val('');
    $('#form_update_ball_table .brand').val('');//
    $('#form_update_ball_table .floorMaterial').val('');
    $('#form_update_ball_table .qualityRating').val('');

}
//监听模态框表单图片的提交事件
function add_form_update_img() {
    // 发送异步请求
    var formData = new FormData();
    // 添加文件字段
    var fileInput = $('#img_file')[0];
    //如果没有选择文件，则不允许提交
    if (fileInput.files[0]==undefined||fileInput.files[0]==''){
        $('#btn_img_submit').popover({
            trigger: 'focus',
            content:'请选择要上传的图片文件'
        });
        $('#btn_img_submit').popover('show');
        return false;
    }
    formData.append('uploadFile', fileInput.files[0]);
    $.ajax({
        type: 'post',
        url: "/public/upload",
        data: formData,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
            //       上传成功以后，更改显示的图片和更改信息表单的图片路径
            $('.img_ball_table').prop('src',data.data.readPath);
            $("#form_update_ball_table [name='imgPath']").val(data.data.relativePath);
        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
    return false;
}
//传入一个日期类型变量，获取时:分:秒.HH:mm:ss
function get_hour_time(date){
    var hour=String(date.getHours()).padStart(2, '0');
    var minutes=String(date.getMinutes()).padStart(2, '0');
    var seconds=String(date.getSeconds()).padStart(2, '0');
    var re=hour+':'+minutes+":"+seconds;
    return re;
}
//给模态框的提交按钮添加保存事件
function ball_table_submit_click() {
    $('.btn_ball_table_submit').on('click',function () {
        //获取表单信息
       var  form = $('#form_update_ball_table');
       var form_data = form.serializeObject();
       var startTime = form_data.startTime;
       var endTime = form_data.endTime;
       //如果选择了时间，则进行校验
       if (startTime!=''&&startTime!=null&&endTime!=null&&endTime!=''){
           //校验开放时间和结束时间
           var startTimeSplit=startTime.split(":");
           var endTimeSplit=endTime.split(":");
           var baseDate = new Date("2000-01-01"); // 创建包含日期的基准日期对象
           var startDate= new Date("2000-01-01");
           var endDate= new Date("2000-01-01");
           //构造开始日期
           startDate.setHours(startTimeSplit[0]);
           startDate.setMinutes(startTimeSplit[1]);
           startDate.setSeconds(0);
           //构造结束日期
           endDate.setHours(endTimeSplit[0]);
           endDate.setMinutes(endTimeSplit[1]);
           endDate.setSeconds(0);
           //校验日期是否规范
           if (startDate.getTime()>endDate.getTime()){
               getToastMsg("开放时间不能大于结束时间!");
               return false;
           }
           //对时间进行格式化,前端只能回传HH:mm
           // 后端希望是HH:mm:ss
           var startTimeFormat=get_hour_time(startDate);
           var endTimeFormat=get_hour_time(endDate);
           //重新更新提交的时间信息
           form_data.startTime=startTimeFormat;
           form_data.endTime=endTimeFormat;
       }
       else {
           form_data.startTime=undefined;
           form_data.endTime=undefined;
       }
       var jsonData = JSON.stringify(form_data);//转为json字符串
       $.ajax({
            type: 'update'==button_name?'put':'post',
            url: "/ballTable/",
            data: jsonData,//序列化数据转化成json对象格式提交
            contentType: "application/json",
            success: function (data) {
                if (data.success==true){
                    //关闭模态框
                    $('#myUpdateModal').modal('hide');
                    getToastMsg('update'==button_name?'修改成功!':'添加成功!');
                    //重新加载表格数据
                    getBallTableList(1);
                }
                else {
                    $('#msg').text(data.msg);
                }


            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                console.log("请求失败！");
                console.log(error);
                $('#msg').text(error.msg);
            }
        });
    })
}
//给添加按钮绑定点击事件
function add_addButton_click() {
    $('#btn_add_ball_table').on('click',function () {
        button_name='add';
        loadModal(button_name,undefined);
    });
}
//给每一个删除按钮添加点击事件
function add_deleteButton_click() {
    //给模态框添加事件
    $("#confirm_delete_modal").on("show.bs.modal", function(event) {
        //展示事件
        var button = $(event.relatedTarget);//获取发起请求的删除按钮组件
        var guid=button.attr('guid');
        //传递guid给确认按钮
        $("#confirm_delete_modal .btn_confirm").attr('guid',guid);
    });
    //给模态框的确认按钮添加点击事件
    $("#confirm_delete_modal .btn_confirm").on('click',function () {
       var guid = $(this).attr('guid');
       // alert(guid);
       //发送删除请求
        $.ajax({
                type: 'delete',
                url: "/ballTable/"+guid,
                dataType:'json', //响应的数据类型
                // contentType: "application/json",
                success: function (data) {
                    //隐藏模态框
                    $("#confirm_delete_modal").modal('hide');
                    getToastMsg('删除成功!');
                    //重新加载数据
                    getBallTableList(1);
                },
                error: function(xhr, status, error) {
                    // 在请求失败时执行的回调方法
                    console.log("请求失败！");
                    console.log(error);
                }
        });
    })
}
//弹出提示框
function getToastMsg(msg) {
    //展示提示框
    $('.toast').toast('show');
    //开启自动隐藏和自动隐藏时间
    $('.toast').toast({
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
    $('.toast .small_text').text(formattedTime);
    //展示信息
    $('.toast .toast-body').text(msg);
}
//给每一个编辑按钮添加点击事件
function add_editorButton_click() {
    $('.ball_table_view').on('click','.update_button' ,function() {
       var guid= $(this).attr('guid');
       button_name='update';
       //加载模态框信息
       loadModal(button_name,guid);
    });
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
                $('#form_update_ball_table .startTime').val(ballTable.startTime);
                $('#form_update_ball_table .endTime').val(ballTable.endTime);
                //新增字段
                $('#form_update_ball_table .brand').val(ballTable.brand);
                $('#form_update_ball_table .floorMaterial').val(ballTable.floorMaterial);
                $('#form_update_ball_table .qualityRating').val(ballTable.qualityRating);
                $('#form_update_ball_table .price').val(ballTable.price);

            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                console.log("请求失败！");
                console.log(error);
            }
        });
    }
}
//发送ajax请求，请求球桌分页信息
function getBallTableList(pageNo){
    $.ajax({
        // url: "/ballTable/ballTableList",
        url: "/ballTable/getAdminBallTableListPage/"+pageNo,
        method: "GET",
        dataType: "json",
        success: function(data) {
            // 处理响应数据
            //重新生成表格内容
            $('#ball_t_body').html('');//刷新
            var ballList=data.data.dataList;
            //重新生成内容
            for (var i=0;i<ballList.length;i++){
                var ballTable=ballList[i];
                var row=$('<tr></tr>');
                //构造td
                //编号
                var code=$('<td></td>').text(ballTable.code);
                var site=$('<td></td>').text(ballTable.address);
                var text=$('<td></td>').text(ballTable.description);
                var status_text='';
                if (ballTable.status==1){
                    status_text='1 启用';
                }
                else {
                    status_text='0 禁用';
                }
                var status=$('<td></td>').text(status_text);
                //按钮
                var editorBtn=$('<button type="button" class="btn btn-primary update_button" style="margin: 10px">编辑</button>').attr('guid',ballTable.guid);
                var deleteBtn=$('<button type="button" class="btn btn-danger delete_button" data-toggle="modal" data-target="#confirm_delete_modal">删除</button>').attr('guid',ballTable.guid);
                //添加到行
                row.append(code);
                row.append(site);
                row.append(text);
                row.append(status);
                row.append(editorBtn);
                row.append(deleteBtn);
                row.appendTo('#ball_t_body');
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
$(function () {
    //绑定编辑事件
    add_editorButton_click();
    add_addButton_click();
    getBallTableList(1);//加载第一页数据
    add_change_pageNo_click_event(getBallTableList);//绑定切换页码加载数据事件
    //给每一个删除按钮添加点击事件
    add_deleteButton_click();
    ball_table_submit_click();
});