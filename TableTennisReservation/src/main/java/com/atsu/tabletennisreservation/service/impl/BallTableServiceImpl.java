package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.mapper.BallTableMapper;
import com.atsu.tabletennisreservation.pojo.BallTable;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.BallTableService;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.atsu.tabletennisreservation.utils.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BallTableServiceImpl implements BallTableService {
    @Resource
    private CommonService commonService;
    @Resource
    private BallTableMapper ballTableMapper;

    @Override
    public List<BallTable> getBallTableList(Integer status) {
//        if (status!=null){
//            return ballTableMapper.getBallTableList(status);
//        }
//        else {
//            return ballTableMapper.getAllBallTableList();
//        }
        BallTable ballTable=new BallTable();
        ballTable.setStatus(status);
        List<BallTable> list = ballTableMapper.getBallTableListCondition(ballTable);
        return list;
    }

    @Override
    public int saveBallTable(BallTable ballTable) {
        return ballTableMapper.saveBallTable(ballTable);
    }

    @Override
    public int updateBallTableById(BallTable ballTable) throws Exception {
        //校验是否允许保存
        verifyCanSaveBallTable(ballTable);
        //补充信息
        //获取当前用户
        User user = commonService.getThisUserId();
        User thisUser = UserContext.getThisUser();
        String nowDateStr = commonService.getNowDateStr();
        ballTable.setUpdateDate(nowDateStr);
        ballTable.setUpdateUser(user.getGuid());
        return ballTableMapper.updateBallTableById(ballTable);
    }

    @Override
    public PageResult<BallTable> getBallTableListPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);//会进行代理分页
        List<BallTable> ballTableList = ballTableMapper.getBallTableList(1);
        PageInfo<BallTable> pageInfo=new PageInfo<>(ballTableList);
        //构造dto
        PageResult<BallTable> pageResult = new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public BallTable getBallTableById(String guid) {
        return ballTableMapper.getBallTableById(guid);
    }

    @Override
    public int addBallTable(BallTable ballTable) throws Exception {
        //校验球桌数据
        verifyCanSaveBallTable(ballTable);
        //获取当前用户
        User user = commonService.getThisUserId();
        String nowDateStr = commonService.getNowDateStr();
        //如果未上传图片，使用默认图片路径
        if (ballTable.getImgPath()==null||ballTable.getImgPath().length()==0){
            ballTable.setImgPath("pingpong.jpg");
        }
        //补充字段
        ballTable.setGuid(commonService.getId());
        ballTable.setCreateUser(user.getGuid());
        ballTable.setUpdateUser(user.getGuid());
        ballTable.setCreateDate(nowDateStr);
        ballTable.setUpdateDate(nowDateStr);
        return ballTableMapper.addBallTable(ballTable);
    }
    //校验是否允许保存球桌信息
    public boolean verifyCanSaveBallTable(BallTable ballTable) throws Exception {
        //空数据校验
        if (StringTool.isNull(ballTable.getCode())
                ||StringTool.isNull(ballTable.getStartTime())
                ||StringTool.isNull(ballTable.getEndTime()
        )
        )
        {
            throw new Exception("球桌编号、开放开始时间、开放结束时间不允许为空！");
        }
        //基础数据校验
        if (ballTable.getPrice()==null){
            throw new Exception("价格不允许为空!");
        }
        if (ballTable.getStatus()==null){
            throw new Exception("球桌启用状态不能为空!");
        }
        //校验编码数据，只能为不超过10位的数字
        String codeRegex="^\\d{1,10}$";
        Pattern pattern=Pattern.compile(codeRegex);
        Matcher matcher = pattern.matcher(ballTable.getCode());
        if (!matcher.matches()){
            throw new Exception("编码只允许数字1-10位数字字符组成!");
        }
        //校验编码是否重复
        BallTable condition=new BallTable();
        condition.setCode(ballTable.getCode());
        List<BallTable> list = ballTableMapper.getBallTableListCondition(condition);
        if (list!=null&&list.size()>0){
            BallTable table = list.get(0);
            //除当前球桌以外的重复编码数据，才抛异常，为了应对修改时的情况
            if (!table.getGuid().equals(ballTable.getGuid())){
                throw new Exception("该编码的球桌已经存在!");
            }

        }
        return true;
    }

    @Override
    public int deleteBallTableById(String guid) {
        return ballTableMapper.deleteBallTableById(guid);
    }

    @Override
    public void initReverseData(ServletContext servletContext) {
        //初始化7天的预订内容,二维列表
        //key:球桌id  value:球桌7天的预订队列
        Map<String,List<List>> ballReverseData=new HashMap<>();
        //加入球桌数据
        List<BallTable> ballTableList = getBallTableList(1);
        for (int i=0;i<ballTableList.size();i++){
            BallTable ballTable = ballTableList.get(i);
            List<List> listList=new ArrayList<>();
            for (int j=0;j<7;j++){
                //一天只预订12小时的
                ArrayList dayList = new ArrayList(12);
                listList.add(dayList);
            }
            ballReverseData.put(ballTable.getGuid(),listList);
        }
        //存入Context域中
        servletContext.setAttribute("ballReverseData",ballReverseData);
    }

    @Override
    public List<BallTable> getCanReserveBallTableList() {
        return ballTableMapper.getCanReserveBallTableList();
    }

    @Override
    public PageResult<BallTable> getCanReserveBallTableListPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);//会进行代理分页
        List<BallTable> ballTableList = ballTableMapper.getCanReserveBallTableList();
        PageInfo<BallTable> pageInfo=new PageInfo<>(ballTableList);
        //构造dto
        PageResult<BallTable> pageResult = new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<BallTable> getAllBallTableListPage(BallTable condition,int pageNo, int pageSize) {
        //开启分页
        PageHelper.startPage(pageNo,pageSize);
        List<BallTable> list = ballTableMapper.getBallTableListCondition(new BallTable());//获取所有数据
        PageInfo<BallTable> pageInfo=new PageInfo<>(list);
        PageResult<BallTable> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

}
