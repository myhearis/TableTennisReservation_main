package com.atsu.tabletennisreservation.thread;

import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CandidateReserveService;
import com.atsu.tabletennisreservation.utils.UserContext;

import java.util.logging.Logger;

//候补任务线程
public class CandidateReserveThread implements Runnable {
    private CandidateReserveService candidateReserveService;//候补逻辑层
    private Reserve cancelReserve;//当前取消的预订单
    private User user;//当前操作用户

    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Override
    public void run() {
        //绑定当前用户到当前线程中
        UserContext.setThisUser(user);
        logger.info("===========================开始候补线程任务=======================");
        candidateReserveService.processCandidateReserve(cancelReserve);
        logger.info("===========================结束候补线程任务=======================");
    }

    public CandidateReserveService getCandidateReserveService() {
        return candidateReserveService;
    }

    public void setCandidateReserveService(CandidateReserveService candidateReserveService) {
        this.candidateReserveService = candidateReserveService;
    }

    public Reserve getCancelReserve() {
        return cancelReserve;
    }

    public void setCancelReserve(Reserve cancelReserve) {
        this.cancelReserve = cancelReserve;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
