
$(function () {
   //注册按钮监听事件
   $('#submit').on('click',function () {
      var form = $('#register_form');
      //预校验输入的注册信息
      var form_data = get_data_for_form(form);
      //判断密码和确认密码
      var password =form_data.password;
      var surePassword = form_data.surePassword;
      //初步校验
      if (password!=surePassword){
         $('#msg').text('输入密码和确认密码不一致!');
         return false;
      }
      //预处理校验
      $.ajax({
         url: "/user/verifyCanRegister/",
         method: "POST",
         dataType: "json",
         data:JSON.stringify(form_data),
         contentType: "application/json",
         success: function(data) {
            if (data.success==true){
               //提交信息
               form.submit();
            }
            else {
               // 处理错误情况
               getToastMsg(data.msg);
               $('#msg').text(data.msg);
               return false;
            }

         },
         error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
            // getToastMsg(errorThrown);
            $('#msg').text(errorThrown.msg);
            return false;
         }
      });
   });
});