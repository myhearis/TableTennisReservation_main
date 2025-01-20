var is_editor=false;//是否编辑,默认为非编辑状态，即只读
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
            $("#form_update_user [name='imgPath']").val(data.data.relativePath);
        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
    return false;
}
//设置信息是否只读
function isOnlyReadInfo(isOnlyRead){
    $('#form_update_user .user_info').prop('readonly',isOnlyRead);
    $('#form_update_user .user_info').prop('disabled',isOnlyRead);
    $('#form_update_user .btn_save').prop('disabled',isOnlyRead);
}
//加载用户信息(后端直接返回session中的用户信息)
function loadUserInfo(){
    //发送ajax请求获取当前登录用户信息
    $.ajax({
        type: 'get',
        url: "/user/info/",
        dataType:'json',
        success: function (data) {
            var user=data.data;
            //回显信息
            $('#form_update_user .img_ball_table').prop('src',data.imgUrl+user.imgPath);//图片展示路径
            $("#form_update_user [name='imgPath']").val(user.imgPath);
            // $("#form_update_user [name='email']").val(user.email);
            $("#form_update_user [name='userName']").val(user.userName);
            $("#form_update_user [name='guid']").val(user.guid);
            $("#form_update_user [name='age']").val(user.age);
            $("#form_update_user [name='gender']").val(user.gender);
            $("#form_update_user [name='email']").val(user.email);
            $("#form_update_user [name='mobilePhone']").val(user.mobilePhone);

        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
}
//给提交按钮绑定信息
function add_btn_save_click_event() {
    $('.btn_save').on('click',function () {
        submitInfo();
        return false;
    })
}
//提交表单信息
function submitInfo(){
    //获取表单信息
    //获取表单信息
    var  form = $('#form_update_user');
    var form_data = form.serializeObject();
    var jsonData = JSON.stringify(form_data);//转为json字符串
    //发送ajax请求
    $.ajax({
        type: 'put',
        url: "/user/info/",
        dataType:'json',
        contentType: "application/json",
        data: jsonData,
        success: function (data) {
                //保存成功后的操作
                getToastMsg("修改成功!如果上传了头像，需要重新登陆以后生效");
                //重新设置不可编辑状态
                 is_editor=false;
                 isOnlyReadInfo(!is_editor);
        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
}
$(function () {
    //加载用户信息
    loadUserInfo();
    //设置初始编辑状态
    isOnlyReadInfo(!is_editor);
    //编辑按钮添加绑定事件
    $('#form_update_user .btn_editor').on('click',function () {
        is_editor=!is_editor;
        //设置信息是否只读
        isOnlyReadInfo(!is_editor);
    });
    //绑定提交事件
    add_btn_save_click_event();
});