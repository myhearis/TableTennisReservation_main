//加载菜单树组件
function reloadMenuTree(){
    // //发送ajax请求获取数据
    $.ajax({
        url: "/menu/nodeList",
        method: "GET",
        dataType: "json",
        success: function(data) {
            var nodeList=data.data;
            //构造一个根节点
            var root={
                id:"000",
                data:"菜单信息",
                children:nodeList
            }
            // 调用 loadData 方法重新加载数据
            // 清空旧的树组件
            //重新构造元素
            var tree_view = $('#tree_view');
            tree_view.html('');//清空数据
            var tree = $('<div id="localJson"></div>');
            tree_view.append(tree);
            $("#localJson").AdubyTree({
                data:nodeList,
                dataType:"json",
                checkboxes:false,//是否多选
                onSelected  : reloadRightMenuInfo,
            });
            $("#localJson").openAll();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
//根据所选菜单，加载右侧详细信息
function  reloadRightMenuInfo(node){
    var guid=node.id;
    selected_node_id=guid;//设置当前选中的节点id
    reloadRightMenuInfoByGuid(guid);
}
function reloadRightMenuInfoByGuid(guid){
    //发送ajax请求获取数据
    $.ajax({
        url: "/menu/extend/"+guid,
        method: "GET",
        dataType: "json",
        success: function(data) {
            var menuList=data.data;
            console.log(menuList);
            //获取表单信息
            // var form =  $('#menu_form');
            // 将 JSON 数据设置到表单中
            // $.each(menu, function(key, value) {
            //     form.find('[name="' + key + '"]').val(value);
            // });
            //构造表格内容

            var t_body = $('#menu_t_body');
            //清空表格
            t_body.html('');
            for (var i=0;i<menuList.length;i++){
                var menu=menuList[i];
                var tr = $('<tr></tr>')
                    .append($('<th scope="row"></th>').text(i+1))
                    .append($('<td></td>').text(menu.guid))
                    .append($('<td></td>').text(menu.name))
                    .append($('<td></td>').text(menu.url))
                    .append($('<td></td>').text(menu.isLeaf))
                    .append($('<td></td>').text(menu.parentId))
                    .append(
                        $('<button type="button" class="btn btn-info menu_update_btn" style="margin-left: 10px">编辑</button>').attr('guid',menu.guid)
                    )
                    .append($('<button type="button" class="btn btn-danger menu_delete_btn" style="margin-left: 10px">删除</button>').attr('guid',menu.guid))
                    .appendTo(t_body)
                ;

            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}
function getopenid(node){
    alert(node.id);
}
//按钮点击事件
function btn_click(){
    $('#menu_form .add').bind('click',function (event) {
        button_name='add';
        //清空表单
        $('#menu_form')[0].reset(); // 重置表单
        //将保存按钮改为确认新增
        $('#menu_form .save').text('确认新增');
    });
    $('#menu_form .editor').bind('click',function (event) {
        button_name='editor';
        $('#menu_form .save').text('保存');
    });
    $('#menu_form .save').bind('click',function (event) {
       submitInfo();
       return false;
    });
}
//提交方法
function  submitInfo(guid){
    var url='';
    var method='';
    //获取表单信息
    var  form = $('#menu_form');
    var form_data = form.serializeObject();
    //更新或新增
    if ('update'==button_name){
        url='/menu/simple';
        method='PUT';
    }
    else if ('add'==button_name){
        url='/menu/simple';
        method='POST';
        //将guid设置为code
        form_data.guid=form_data.code;
    }
    else  if ('delete'==button_name){
        url='/menu/simple';
        method='DELETE';
        form_data={
            'guid':guid
        }
    }
    $.ajax({
        type: method,
        url: url,
        data: JSON.stringify(form_data),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            getToastMsg('操作成功!');
            //关闭模态框
            //关闭模态框
            $('#myUpdateModal').modal('hide');
            //重新加载树
            reloadMenuTree();
            //如果的新增的数据，右侧表格展示新增数据
            selected_node_id=form_data.guid;
            reloadRightMenuInfoByGuid(selected_node_id);
        },
        error: function(xhr, status, error) {
            //关闭模态框
            $('#myUpdateModal').modal('hide');
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
}
//拉起模态框
function loadModal(button_name,guid) {
    cleanModalData();
    //如果是更新，则回显数据
    if ('update'==button_name){
        //发送请求，回显数据
        $.ajax({
            type: 'get',
            url: "/menu/simple/"+guid,
            // data: guid,
            dataType: "json",
            // contentType: "application/json",
            success: function (data) {
                //回显信息
                $('#menu_form')[0].reset();//清空表单信息
                set_data_to_form(data.data,$('#menu_form'));//回显数据
            },
            error: function(xhr, status, error) {
                // 在请求失败时执行的回调方法
                console.log("请求失败！");
                console.log(error);
            }
        });
    }
    //如果是新增
    else if ('add'==button_name){
        //回显当前所选父节点数据
        if ('-1'!=selected_node_id)
        var form_data={
            'code':selected_node_id,
            'parentId':selected_node_id
        }
        set_data_to_form(form_data,$('#menu_form'));
    }
    //拉起模态框
    $("#myUpdateModal").modal({
        backdrop:"static",
        keyboard:true
    });
}
//编辑按钮
function update_btn_event_add() {
    $('#menu_table').on('click','.menu_update_btn',function () {
       var guid = $(this).attr('guid');
       button_name='update';
       loadModal('update',guid);
    });
}
//删除按钮
function delete_btn_event_add() {
    $('#menu_table').on('click','.menu_delete_btn',function () {
        var guid = $(this).attr('guid');
        button_name='delete';
        submitInfo(guid);
    });
}
//新增按钮
function add_menu_btn_event_add() {
    $('#add_menu_btn').on('click',function () {
        button_name='add';
        loadModal(button_name,undefined);
    });
}
//保存按钮
function save_menu_btn_event_add() {
    $('#myUpdateModal').on('click','#btn_update_submit',function () {
        submitInfo();
    });
}
function cleanModalData(){
    //获取表单
    //清空表单
    $('#menu_form')[0].reset(); // 重置表单
}
//清空表单
$(function () {
    //先根据默认的id加载一次右侧表格
    reloadRightMenuInfoByGuid(selected_node_id);
    delete_btn_event_add();//所有删除按钮事件
    update_btn_event_add();//所有修改按钮事件
    add_menu_btn_event_add();//新增按钮事件
    save_menu_btn_event_add();
    reloadMenuTree();
});
