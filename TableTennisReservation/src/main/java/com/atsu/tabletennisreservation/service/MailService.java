package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.MailDTO;

//推送邮件的业务接口
public interface MailService {
    void send(MailDTO mailDTO);
    void send(String targetId,String title,String text);
}
