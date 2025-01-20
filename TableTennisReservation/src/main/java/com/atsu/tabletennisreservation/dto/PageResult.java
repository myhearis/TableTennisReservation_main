package com.atsu.tabletennisreservation.dto;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -2221294470751667711L;
    private int pageNo;//当前页码
    private int pageSize;//分页大小
    private long totalSize;//总记录数
    private int totalPages; // 页码总数
    private List<T> dataList;//分页数据
    private int prePageNo;//上一页
    private int nextPageNo;//下一页
    List<Integer> pageNoList;//后台分页
    private int viewPageNoCount=5;//要展示的页码数量（奇数）

    public PageResult() {
    }

    //根据分页初始化信息
    public PageResult(PageInfo<T> pageInfo){
        pageNo=pageInfo.getPageNum();
        pageSize=pageInfo.getPageSize();
        totalSize=pageInfo.getTotal();
        totalPages=pageInfo.getPages();
        dataList=pageInfo.getList();
        if (pageNo+1<=totalPages)
            nextPageNo=pageNo+1;
        else
            nextPageNo=pageNo;
        if (pageNo-1>0)
            prePageNo=pageNo-1;
        else
            prePageNo=pageNo;
        //初始化展示的页码信息,默认展示5页
        pageNoList = createPageNoList2(pageNo,viewPageNoCount,totalPages);
    }

    public List<Integer> initPageNoList(int pageNo,int totalPages){
        List<Integer> pageNoList=new ArrayList<>();//后台分页
        //初始化展示的页码信息,默认展示5页
        int temp=5;
        int preReplaceCount=0;
        List<Integer> preList=new ArrayList<>();//用于补充前面页
        for (int i=1;i<=5;i++){
            //如果小于3，直接取后五位
            if (pageNo<3&&i<=totalPages){
                pageNoList.add(i);
            }
            else {
                //生成前两页和当前页
                if (temp/3>=1){
                    pageNoList.add(pageNo-temp%3);
                }
                //生成后两页
                else {
                    int p=pageNo+i%3;
                    //页面存在
                    if (p<=totalPages){
                        pageNoList.add(p);
                    }
                    //页面不存在，使用前置页面代替
                    else {
                        preReplaceCount++;
                        p=pageNoList.get(0)-preReplaceCount;
                        preList.add(p);
                    }

                }
            }
            temp--;
        }
        //合并页码
        List<Integer> list=new ArrayList<>();
        //合并替代前置内容
        for (int i=preList.size()-1;i>=0;i--){
            list.add(preList.get(i));
        }
        //合并后置
        for (int i=0;i<pageNoList.size();i++){
            list.add(pageNoList.get(i));
        }
        return list;
    }
    /**
     * #Description 最新生成页码列表方式（存在问题）
     * @param pageNo: 当前页码
     * @param size: 展示的页码数量
     * @param totalPages: 总页码
     * @return java.util.List<java.lang.Integer>
     * @author sujinbin
     * #Date 2023/12/31
     */
    public List<Integer> createPageNoList(int pageNo,int size,int totalPages){
        int count=size/2;
        List<Integer> leftList=new ArrayList<>();
        List<Integer> rightList=new ArrayList<>();
        List<Integer> preLeftList=new ArrayList<>();
        List<Integer> nextRightList=new ArrayList<>();
        //生成左边页码数量
        for (int i=1;i<=count;i++){
            //计算左侧页码
            int leftNo=pageNo-i;
            //计算右侧页码
            int rightNo=2*pageNo-leftNo;
            //如果都为正数
            if (leftNo>0){
                leftList.add(leftNo);
            }
            if (rightNo<=totalPages){
                rightList.add(rightNo);
            }
        }
        //补全页码信息
        if (leftList.size()<count&&rightList.size()>0){
            int s = count - leftList.size();
            for (int i=0;i<s;i++){
                //使用右边页码代替
                Integer page = rightList.get(rightList.size() - 1)+1;
                if (page<=totalPages)
                    nextRightList.add(page);
            }
        }
        if (rightList.size()>0&&rightList.size()<count){
            int s = count - rightList.size();
            for (int i=0;i<s;i++){
                //使用左边页码代替
                if (leftList.size()>0){
                    Integer page = leftList.get(0)-1;
                    if (page>0)
                        preLeftList.add(page);
                }

            }
        }
        //添加当前页码
        leftList.add(pageNo);
        //整合页码
        preLeftList.addAll(leftList);
        preLeftList.addAll(rightList);
        preLeftList.addAll(nextRightList);
        //排序
        preLeftList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });
        return preLeftList;
    }
    //最新展示页码列表方法
    public List<Integer> createPageNoList2(int pageNo,int size,int totalPages){
        List<Integer> pageNoList=new ArrayList<>();
        //总页码小于导航栏大小的情况  1-totalPages
        if (totalPages<size){
            for (int i=1;i<=totalPages;i++){
                pageNoList.add(i);
            }
        }
        else {
            int count=1;
            while (pageNoList.size()!=size-1){
                //左边
                int leftNo=pageNo-count;
                if (leftNo>0)
                    pageNoList.add(leftNo);
                //右边
                int rightNo=pageNo+count;
                if (rightNo<=totalPages)
                    pageNoList.add(pageNo+count);
                count++;
            }
            //加入当前页码
            pageNoList.add(pageNo);
            //排序
            pageNoList.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1-o2;
                }
            });
        }
        return pageNoList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPrePageNo() {
        return prePageNo;
    }

    public void setPrePageNo(int prePageNo) {
        this.prePageNo = prePageNo;
    }

    public int getNextPageNo() {
        return nextPageNo;
    }

    public void setNextPageNo(int nextPageNo) {
        this.nextPageNo = nextPageNo;
    }

    public List<Integer> getPageNoList() {
        return pageNoList;
    }

    public void setPageNoList(List<Integer> pageNoList) {
        this.pageNoList = pageNoList;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", totalSize=" + totalSize +
                ", totalPages=" + totalPages +
                ", dataList=" + dataList +
                ", prePageNo=" + prePageNo +
                ", nextPageNo=" + nextPageNo +
                '}';
    }

    public static void main(String[] args) {
        PageResult pageResult=new PageResult();
        List pageNoList = pageResult.createPageNoList(5, 4, 7);
        System.out.println(pageNoList);
    }
}
