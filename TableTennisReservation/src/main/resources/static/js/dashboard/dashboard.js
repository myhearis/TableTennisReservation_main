//根据页码加载球桌数据
function loadBallTableDataList(pageNo){
    //发送ajax请求数据
    $.ajax({
        type: 'get',
        url: "/ballTable/getCanReserveBallTableList/"+pageNo,
        // data: ,
        cache: false,
        dataType:'json',
        processData: false,
        contentType: false,
        success: function (data) {
            var img_url=data.imgUrl;
            var base_url=data.baseUrl;
            var list=data.data.dataList;
            //生成展示标签
            var view_ul = $('#ball_view_ul');
            view_ul.html('');
            for (var i=0;i<list.length;i++){
                var ball_table=list[i];
                //生成item
                var li = $('<li class="list-group-item"  ></li>');
                //构造card
                var card = $('<div class="card " style="width: 16rem; overflow: hidden;"></div>');
                var guid_input=$('<input type="hidden" name="guid" >').val(ball_table.guid);
                card.append(guid_input);
                var table_img_container=$('<div class="table_img_container" ></div>')
                    .append($('<img class="card-img-top img_ball_table">').prop('src',img_url+ball_table.imgPath));
                card.append(table_img_container);
                var card_body=$('<div class="card-body"></div>')
                    .append($('<h5 class="card-title" ></h5>').text(ball_table.code+'号桌'))
                    .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">价格(元)：</font>')).append($('<font color="red"></font>').text(''+ball_table.price)))
                    .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">球桌品牌：</font>')).append($('<font></font>').text(''+ball_table.brand)))
                    .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">球桌完好程度：</font>')).append($('<font></font>').text(''+ball_table.qualityRating)))
                    .append($('<p class="card-text " style="margin: 0 0"></p>').append($('<font color="#888888">地面材质：</font>')).append($('<font></font>').text(''+ball_table.floorMaterial)))
                    .append($('<p class="card-text " ></p>').append($('<font color="#888888">描述：</font>')).append($('<font></font>').text(''+ball_table.description)))

                    // .append($('<p class="card-text" style="margin: 0 0"></p>').text('球桌完好程度：    '+ball_table.qualityRating))
                    // .append($('<p class="card-text" style="margin: 0 0"></p>').text('地面材质：    '+ball_table.floorMaterial))
                    // .append($('<p class="card-text" style="margin: 0 0"></p>').text('描述：    '+ball_table.description))
                    .append($('<a href="#" class="btn btn-primary"></a>').text('开始预订').prop("href",base_url+"/reverse/"+ball_table.guid))
                ;
                // card.append(ul);
                card.append(card_body);

                //组装li
                li.append(card);
                li.appendTo(view_ul);
            }
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
//切换页码事件 ,使用通用工具类的内容，不需要再定义了
// function add_change_pageNo_click_event() {
//     $('.page_nav').on('click','.a_page',function () {
//        var pageNo = $(this).attr('pageNo');
//         //重新加载数据
//         loadBallTableDataList(pageNo);
//         return false;
//     })
// }
//根据传入的页码信息，生成一个页码块
function createPageNvl(nowPageNo,pageNo,isPrePage,isNextPage){
    var re=undefined;
    if (isPrePage){
        var pre=$('<li class="page-item"></li>');
        var a_page = $('<a class="page-link pre_page_no a_page" href="#" aria-label="Previous"></a>').attr('pageNo',pageNo);
        a_page.append($('<span aria-hidden="true">&laquo;</span>'));
        pre.append(a_page);
        re=pre;
    }
    else if (isNextPage){
        var next=$('<li class="page-item"></li>');
        var a_page = $('<a class="page-link pre_page_no a_page" href="#" aria-label="Previous"></a>').attr('pageNo',pageNo);
        a_page.append($('<span aria-hidden="true">&raquo;</span>'));
        next.append(a_page);
        re=next;
    }
    //普通页码
    else {
        var next=$('<li class="page-item"></li>');
        var a_page = $('<a class="page-link pre_page_no a_page" href="#" aria-label="Previous"></a>').attr('pageNo',pageNo).text(pageNo);
        //活化页码
        if (pageNo==nowPageNo){
            next.addClass('active');
        }
        next.append(a_page);
        re=next;
    }

    return re;
}

$(function () {
    loadBallTableDataList(1);//初始化默认加载第一页
    add_change_pageNo_click_event(loadBallTableDataList);//绑定切换页码事件
    getViewUserChatNotReadMessageCount();
});