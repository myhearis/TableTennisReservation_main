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
//判断传入的小时数是否在球桌开放时段以内
function isInOpen(hour) {
    //补全时间
    var baseDateStr='1970-01-01';//基准日期
    var time = new Date(baseDateStr);
    time.setHours(parseInt(hour));
    if (time.getTime() < startTimeObj.getTime() || time > endTimeObj.getTime()){
        return false;
    }
    return true;
}
//提交信息方法
function submit(){
    //1、校验当前所选时间是否已经被预定，或者与已预订的信息冲突
    //2、发送预定请求
    var form = $('#reverse_form');
    var form_data = form.serializeObject();
    //校验信息
    if (isNull( form_data.useDate)){
        getToastMsg('请先选择使用时间！');
        return ;
    }
    if (isNull(form_data.countHour)){
        getToastMsg('请先选择使用时长!');
        return ;
    }
    //校验预订时间是否超过了该球桌的开放时间
    if (!isNull( form_data.useDate)&&!isNull(form_data.countHour)){
        //补全时间
        // var baseDateStr='1970-01-01';//基准日期
        // var useDateStartTimeObj = new Date(baseDateStr);
        // useDateStartTimeObj.setHours(parseInt(form_data.useDate));
        // var useDateEndTimeObj = new Date(baseDateStr);
        // useDateEndTimeObj.setHours(parseInt(form_data.useDate)+parseInt(form_data.countHour));
        // //球桌开放时间
        // var sp1 = startTime.split(':');
        // var startTimeObj = new Date(baseDateStr);
        // startTimeObj.setHours(parseInt(sp1[0]));
        // startTimeObj.setMinutes(parseInt(sp1[1]));
        // //球桌结束开放时间
        // var sp2 = endTime.split(':');
        // var endTimeObj = new Date(baseDateStr);
        // endTimeObj.setHours(parseInt(sp2[0]));
        // endTimeObj.setMinutes(parseInt(sp2[1]));
        //开始比较  开放时间<使用时间<关闭时间 = 使用时间<开放时间 || 使用时间>关闭时间
        if (!isInOpen(parseInt(form_data.useDate))){
            getToastMsg('开始时间不能在球桌开放时段之外！球桌开放时段：['+startTime+'-'+endTime+']');
            return ;
        }
        if (!isInOpen(parseInt(form_data.useDate)+parseInt(form_data.countHour))){
            getToastMsg('预订时间不能在球桌开放时段之外！球桌开放时段：['+startTime+'-'+endTime+']');
            return ;
        }
    }
    var jsonData = JSON.stringify(form_data);//转为json字符串
    //发送ajax请求
    $.ajax({
        type: 'post',
        url: "/reverse/",
        dataType:'json',
        contentType: "application/json",
        data: jsonData,
        success: function (data) {
            if (data.success==true){
                //保存成功后的操作
                getToastMsg("预订成功!");
                //初始化付款界面内容
                $('#confirm_modal .confirm_pay_btn').attr('redis_key',data.data);
                //拉起模态框
                $('#confirm_modal').modal({
                    backdrop:"static",
                    keyboard:true
                });
            }
            else {
                //询问用户是否候补
                if ('1'==data.data){
                    create_confirm(confirm_modal_success,'houbu','提示','当前选择时间已经被预定，是否候补?');
                }
                //校验失败
                else if ('3'==data.data){
                    getToastMsg(data.msg);
                }
                //其他异常
                else if ('5'==data.data){
                    getToastMsg(data.msg);
                }
                else {
                    getToastMsg(data.msg);
                }
            }


        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("保存失败！");
            console.log(error);
        }
    });
    return false;
}
//付款按钮点击事件
function add_pay_btn_event() {
    $('#confirm_modal').on('click','.confirm_pay_btn',function () {
        //获取redis_key
        var redis_key = $(this).attr('redis_key');
        //发送付款请求
        // 将当前页面重定向到指定的 URL
        window.location.href = base_url+"/pay/processPayReserve/"+redis_key;
    });
}
//给使用时间增加内容改变事件，实时更新可以使用的时长信息
function add_change_use_date_event(){
    $('#use_date').on('change',function () {
        //获取当前选择的内容
        var useDate = $(this).val();
        //获取使用时长下拉框组件
        var use_time_select =  $('#use_time');
        //清空内容
        use_time_select.html('');
        var len=closeTime-useDate;
        //新增内容
        for (var i=1;i<=len;i++){
            var op=$('<option>1</option>').prop("value",i).text(i+'');
            use_time_select.append(op);
        }
        reLoadPayAmt();
    });
}
//重新计算总价
function reLoadPayAmt() {
    //获取表单
    var pay_input = $('#reverse_form .payAmt');
    var one_price= pay_input.attr('one_price');
    //时长
    var form_data = get_data_for_form($('#reverse_form'));
    var countHour = form_data.countHour;
    if (!isNull(countHour)&&!isNull(one_price)){
        pay_input.val(one_price*countHour);
    }
    console.log('小时：'+countHour);
    console.log('单价：'+one_price);
}
//确认模态框的所有确认映射方法
var confirm_modal_success = function () {
    //获取模态框的映射
  var mapping =  $('#common_confirm_modal').attr('mapping');
  //确认候补
  if ('houbu'==mapping){
      //发送ajax请求生成候补单
      var form = $('#reverse_form');
      var form_data = form.serializeObject();
      var jsonData = JSON.stringify(form_data);//转为json字符串
      $.ajax({
          type: 'post',
          url: "/candidateReserve/",
          dataType:'json',
          contentType: "application/json",
          data: jsonData,
          success: function (data) {
              if (data.success==true){
                  //获取redisKey
                  var redisKey=data.data;
                  //重定向页面到支付页
                  document.location.href='/candidateReserve/processCreateCandidateReservePay/'+redisKey;
              }
              else {
                  getToastMsg(data.msg);
              }
              //关闭模态框
              $('#common_confirm_modal').modal('hide');
          },
          error: function(xhr, status, error) {
              // 在请求失败时执行的回调方法
              console.log("保存失败！");
              console.log(error);
              alert(data.msg);
              //关闭模态框
              $('#common_confirm_modal').modal('hide');
          }
      });
  }
    //重置映射(否则会重复发送请求，暂时不知道为什么,可能是其他页面存在引用，同时会进行调用的问题)
    $('#common_confirm_modal').attr('mapping','');
}
//请求当前日期的对应球桌的可预订期间,并展示在界面
//当前日期:年-月-日
//当前球桌编号
function viewDateCanReverse(dateStr,tableCode){
    $.ajax({
        type: 'get',
        url: "/reverse/reserveDate/"+tableCode+'/'+dateStr,
        dataType:'json',
        // contentType: "application/json",
        success: function (data) {
            if (data.success==true){
                //根据响应的内容加载表格
                var t_body =  $('#reverse_status_t_body');
                t_body.html('');//清空表格
                var dateList=data.data.dateList;
                //遍历生成表格元素,从9时开始，到21时结束
                for (var i=9;i<21;i++){
                    //构造元素
                   var tr =  $('<tr></tr>');
                   var time_str=formatTime(i)+'-'+formatTime(i+1);
                   $(' <th scope="row"></th>').text(time_str).appendTo(tr);
                   var status_text='';
                   if (dateList[i]==0){
                       status_text='可预订';
                       tr.addClass('table-success');
                   }
                   else {
                       status_text='不可预订';
                       tr.addClass('table-dark');
                   }
                   //判断是否在球桌预订时段内
                    if (!isInOpen(i)){
                        status_text='未在球桌开放时段';
                        tr.addClass('table-dark');
                    }
                   $(' <th></th>').text(status_text).appendTo(tr);
                   t_body.append(tr);
                }
            }
            else {
               getToastMsg(data.msg);
            }

        },
        error: function(xhr, status, error) {
            getToastMsg(error);
        }
    });
}
//给切换开始时间的下拉框添加内容改变时间，更新预订表格
function add_change_update_start_date_event(){
    $('#reverse_form').on('change','.startDate',function (){
        //获取选择的内容
        //获取选择的日期
        var form_data = get_data_for_form($('#reverse_form'));
        var process_date = form_data.startDate;
        var table_code=form_data.code;
        viewDateCanReverse(process_date,table_code);
        reLoadPayAmt();
    });
    //监听小时改变事件
    $('#reverse_form').on('change','.countHour',function (){
        reLoadPayAmt();
    });
}
function formatTime(hour) {
    var leadingZero = hour < 10 ? "0" : ""; // 判断是否需要补零
    var timeString = leadingZero + hour + ":00"; // 将小时数和固定的分钟数拼接成字符串
    return timeString;
}
//监听时间选择变化，更新时间信息（也可以不写，如果冲突了就弹出提示框提示用户即可）
$(function () {
    //初始化一遍预订表格 start
    var form_data = get_data_for_form($('#reverse_form'));
    var process_date = form_data.startDate;
    viewDateCanReverse(process_date,table_code);
    //初始化一遍预订表格 end
    add_change_update_start_date_event();//预订日期改变事件
    //给提交按钮绑定事件
    $('#reverse_submit_btn').on('click',function () {
        submit();
        return false;
    })
    add_change_use_date_event();
    add_pay_btn_event();
    reLoadPayAmt();//加载后计算一次总价
});