package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.exception.SysException;
import com.atsu.tabletennisreservation.mapper.CandidateReserveMapper;
import com.atsu.tabletennisreservation.pojo.*;
import com.atsu.tabletennisreservation.service.*;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Service
public class CandidateReserveServiceImpl implements CandidateReserveService {
    @Resource
    private CandidateReserveMapper candidateReserveMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private ReverseService reverseService;
    @Resource
    private MessageService messageService;
    @Resource
    private UserService userService;
    @Resource
    private MailService mailService;

    @Override
    public List<CandidateReserve> getCandidateReserveList(CandidateReserve candidateReserve) {
        return candidateReserveMapper.getCandidateReserveList(candidateReserve);
    }

    @Override
    public CandidateReserve getCandidateReserveById(String guid) {
        CandidateReserve candidateReserve=new CandidateReserve();
        candidateReserve.setGuid(guid);
        List<CandidateReserve> list = candidateReserveMapper.getCandidateReserveList(candidateReserve);
        if (list!=null&&list.size()==1){
            return list.get(0);
        }
        return null;
    }

    @Override
    public int deleteCandidateReserve(CandidateReserve candidateReserve) {
        return candidateReserveMapper.deleteCandidateReserve(candidateReserve);
    }

    @Override
    public int saveCandidateReserve(CandidateReserve candidateReserve) throws SysException {
        //校验是否允许取消候补
        if (! verifyCandidate(candidateReserve)){
            return 0;
        }
        //补全信息
        User user = commonService.getThisUserId();
        candidateReserve.setUserId(user.getGuid());
        candidateReserve.setStatus(0);//候补状态
        candidateReserve.setGuid(commonService.getId());
        candidateReserve.setCreateDate(commonService.getNowDateStr());
        return candidateReserveMapper.saveCandidateReserve(candidateReserve);
    }

    @Override
    public int updateCandidateReserveById(CandidateReserve candidateReserve) {
        return candidateReserveMapper.updateCandidateReserveById(candidateReserve);
    }

    @Override
    public PageResult<CandidateReserve> getUserCandidateReserveListPage(int pageNo, int pageSize,CandidateReserve candidateReserve) {
        PageHelper.startPage(pageNo,pageSize);//会进行代理分页
        User user = commonService.getThisUserId();
        //构造条件
        candidateReserve.setUserId(user.getGuid());
        List<CandidateReserve> list = candidateReserveMapper.getCandidateReserveList(candidateReserve);
        PageInfo<CandidateReserve> pageInfo=new PageInfo<>(list);
        //构造dto
        PageResult<CandidateReserve> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<CandidateReserve> getCandidateReserveListPage(int pageNo, int pageSize,CandidateReserve candidateReserve) {
        PageHelper.startPage(pageNo,pageSize);//会进行代理分页
        List<CandidateReserve> list = candidateReserveMapper.getCandidateReserveList(candidateReserve);
        PageInfo<CandidateReserve> pageInfo=new PageInfo<>(list);
        //构造dto
        PageResult<CandidateReserve> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }
    //根据取消的订单数据，执行候补生成预定操作,只考虑单一日期,单一球桌（为了减少数据量）
    @Override
    public boolean processCandidateReserve(List<Reserve> reserveList) {
        String key = reserveList.get(0).getReserveDateKey();
        //暂时全量候补遍历
        //获取候补数据
        CandidateReserve candidateReserve=new CandidateReserve();
        candidateReserve.setStatus(0);
        List<CandidateReserve> list = candidateReserveMapper.getCandidateReserveList(candidateReserve);
        //开始遍历候补数据
        for (int i=0;i<list.size();i++){
            CandidateReserve cdr = list.get(i);
            //构造预订单
            Reserve reserve = cdr.parseReserve();
            int site = reverseService.saveReserve(reserve);
            //如果生成订单成功，设置候补状态
            if (site>0){
                cdr.setStatus(1);
                candidateReserveMapper.updateCandidateReserveById(cdr);
            }
        }
        return true;
    }
    //根据取消的订单数据，执行候补生成预定操作,只考虑单一日期,单一球桌（为了减少数据量）
    @Override
    public boolean processCandidateReserve(Reserve cancelReserve) {
        List<CandidateReserve> list=new ArrayList<>();
        //获取日期队列
        String key = cancelReserve.getReserveDateKey();
        Map<String, ReserveDate> reserveDateListMap = commonService.getReserveDateListMap();
        ReserveDate reserveDate = reserveDateListMap.get(key);
        List<Period> periodList = reserveDate.getPeriodList();//期间
        //遍历期间查询
        for (int i=0;i<periodList.size();i++){
            Period period = periodList.get(i);
            //处理特殊情况 start>=end的情况，需要将end设置为24
            //获取候补单
            List<CandidateReserve> useCandidateReserveList = candidateReserveMapper.getUseCandidateReserveList(period);
            //候补单添加
            list.addAll(useCandidateReserveList);
        }
        //开始遍历候补数据
        for (int i=0;i<list.size();i++){
            CandidateReserve cdr = list.get(i);
            //构造预订单
            Reserve reserve = cdr.parseReserve();
            int site = reverseService.saveReserve(reserve);
            //如果生成订单成功，设置候补状态
            if (site>0){
                cdr.setStatus(1);//状态【候补成功】
                candidateReserveMapper.updateCandidateReserveById(cdr);
                //生成候补成功消息
                messageService.saveSystemMessage("候补成功!",cdr.getUserId());
                //推送邮件
                String targetEmail=userService.getUserEmail(cdr.getUserId());
                if (!StringTool.isNull(targetEmail)){
                    mailService.send(targetEmail,"候补成功","您的候补已经完成!");
                }

            }
        }
        return false;
    }
    //校验是否允许候补
    @Override
    public boolean verifyCandidate(CandidateReserve candidateReserve) throws SysException {
        //检查是否已经存在了候补订单
        boolean isExist = verifyUserCandidateReserveIsExist(candidateReserve.getUserId(), candidateReserve.getTableId());
        if (isExist){
            throw new SysException("当前用户在该球桌已经生成过候补订单!");
        }
        return true;
    }


    /**
     * #Description 校验用户对于对应的球桌是否存在进行中的候补订单，如果存在，返回true,不存在返回false
     * @param userId: 用户id
     * @param tableId: 球桌id
     * @return boolean
     * @author sujinbin
     * #Date 2024/2/28
     */
    public boolean verifyUserCandidateReserveIsExist(String userId,String tableId){
        CandidateReserve candidateReserve=new CandidateReserve();
        candidateReserve.setUserId(userId);
        candidateReserve.setTableId(tableId);
        candidateReserve.setStatus(0);
        List<CandidateReserve> list = candidateReserveMapper.getCandidateReserveList(candidateReserve);
        if (list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

}
