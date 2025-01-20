//加载菜单树组件
function reloadMenuTree(){
    // //发送ajax请求获取数据
    $.ajax({
        url: "/menu/nodeList",
        method: "GET",
        dataType: "json",
        success: function(data) {
            var nodeList=data.data;
            // 清空旧的树组件
            //重新构造元素
            var tree_view = $('#tree_view');
            tree_view.html('');//清空数据
            var tree = $('<div id="localJson"></div>');
            tree_view.append(tree);
            $("#localJson").AdubyTree({
                data:nodeList,
                dataType:"json",
                checkboxes:true,//是否多选
                // onSelected  : reloadRightMenuInfo,
            });
            $("#localJson").openAll();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 处理错误情况
        }
    });
}

//提交方法
function  submitInfo(){
    //获取表单信息
    var form_data = get_data_for_form($('#select_role_form'));
    //增加选中的node_ids
    var node_ids_str = $("#localJson").getChecked();
    //把node_ids_str转换为列表
    var nodeIds = node_ids_str.split(',');
    form_data.nodeIds = nodeIds;
    console.log(JSON.stringify(form_data));
    $.ajax({
        type: 'POST',
        url: '/role/saveRoleMenu',
        data: JSON.stringify(form_data),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            getToastMsg('操作成功!');
            //重新加载树,可以不用重新加载的
            // reloadMenuTree();
            //加载选中状态

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

//保存按钮
function save_menu_btn_event_add() {
    $('#select_role_form').on('click','.save_btn',function () {
        submitInfo();
    });
}
//根据node_id选中菜单树中的数据,如果数据为空，会把所有的数据都设置为未选中
//treeElement :树组件的jQuery对象
// node_id_set ：要设置选中的node_id的set集合
//原本AdubyTree组件并没有设置选中的方法，这是额外自定义的
function loadCheckedTree(treeElement,node_id_set){
    var checkbox_list = treeElement.find('.checkbox');
    var tree_id=treeElement.prop('id');//获取树元素id
    var split_value=tree_id+"-ckbx-";
    for (var i=0;i<checkbox_list.length;i++){
        var check_div = $(checkbox_list[i]);
        //获取当前节点id
        var node_check_id = check_div.prop('id').substring(split_value.length);
        //如果为选中的菜单node_id
        if (node_id_set.has(node_check_id)){
            //增加选中
            check_div.removeClass('checkBoxUncheck unchecked');//清除未选中状态
            check_div.addClass('checkBoxChecked checked');//增加选中状态
        }
        //如果是未选中，则设置为未选中状态
        else {
            //将两种状态都清除
            check_div.removeClass('checkBoxUncheck unchecked');//清除未选中状态
            check_div.removeClass('checkBoxChecked checked');//清楚选中状态
            //设置为未选中
            check_div.addClass('checkBoxUncheck unchecked');
        }
    }
}
//加载角色信息
function loadRoleInfo(){
    $.ajax({
        type: 'get',
        url: "/role/",
        dataType: "json",
        // contentType: "application/json",
        success: function (data) {
            var roleList=data.data;
            //加载到下拉框中
            var select =  $('#select_role_form .role_select');
            for (var i=0;i<roleList.length;i++){
                var role=roleList[i];
                $(' <option></option>').val(role.guid).text(role.name).appendTo(select);
            }
            //加载完成以后，设置选中状态
            var select_role_id = $('#select_role_form .role_select').val();
            reloadTreeChecked(select_role_id);
        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
}
//添加下拉框改变事件
function select_role_change_event_add() {
    $('#select_role_form').on('change','.role_select',function () {
        //获取选中数据
        var roleId = $(this).val();
        //加载选中状态
        reloadTreeChecked(roleId);
    });

}
//根据角色id，加载选中数据
function reloadTreeChecked(roleId){
    $.ajax({
        type: 'get',
        url: "/role/roleMenuDto/"+roleId,
        dataType: "json",
        // contentType: "application/json",
        success: function (data) {
            var roleMenuDto=data.data;
            var nodeIds = roleMenuDto.nodeIds;
            //清空选中状态
            //构造选中数据(如果数据为空，会把所有的数据都设置为未选中)
            loadCheckedTree($('#localJson'),new Set(nodeIds));
        },
        error: function(xhr, status, error) {
            // 在请求失败时执行的回调方法
            console.log("请求失败！");
            console.log(error);
        }
    });
}
//重新加载选中数据
//清空表单
$(function () {
    loadRoleInfo();//加载角色数据，同时设置选中状态
    save_menu_btn_event_add();//保存权限事件
    select_role_change_event_add();//下拉框角色改变时间
    reloadMenuTree();//加载菜单树
    //加载左侧菜单
    loadMenuView();
});
