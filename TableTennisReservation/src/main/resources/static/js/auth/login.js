//渲染上一次记住的用户名
function view_remember_user_info(){
    //渲染上一次记录的用户名
    var form=$('#login_form');
    var remember_user_name = getCookie('remember_user_name');
    if (!isNull(remember_user_name)){
        var data={
            userName: remember_user_name
        }
        set_data_to_form(data,form);
        console.log('加载上一次记录的用户:'+remember_user_name);
    }
}
//监听提交按钮事件
function submit_event(){
    //监听按钮提交
    $('#login_btn').on('click',function () {
        //获取表单信息
        var form=$('#login_form');
        //获取数据
        var form_data = get_data_for_form(form);
        //是否需要记住用户名
        if (!isNull(form_data.remember)&&'on'==form_data.remember&&!isNull(form_data.userName)){
            // 设置了过期时间的话，在设置的时间段内 cookie 一直存在有效，到达过期时间点时，cookie被删除而失效
            document.cookie = "remember_user_name" + "=" + form_data.userName + ";expires=" + 3600*24*7;
        }
        return true;
    });
}
$(function () {
    view_remember_user_info();//渲染上一次记住的用户名
    submit_event();////监听提交按钮事件
});