package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.dto.MailDTO;
import com.atsu.tabletennisreservation.service.MailService;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MailServiceImpl implements MailService {
    @Resource
    private MailSender mailSender;

    @Override
    public void send(MailDTO mailDTO) {
        //设置发送方信息
        mailDTO.setOriginMail("sujinbinchn@163.com");
        //简单邮件消息对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 和配置文件中的的username相同，相当于发送方
        message.setFrom(mailDTO.getOriginMail());
        // 收件人邮箱
        message.setTo(mailDTO.getTargetMail());
        // 标题
        message.setSubject(mailDTO.getTitle());
        // 正文
        message.setText(mailDTO.getContent());
        // 发送
        mailSender.send(message);
    }

    @Override
    public void send(String targetId, String title, String text) {
        MailDTO mailDTO=new MailDTO();
        //设置发送方信息
        mailDTO.setOriginMail("sujinbinchn@163.com");
        mailDTO.setTitle(title);
        mailDTO.setTargetMail(targetId);
        mailDTO.setContent(text);
        //发送
        send(mailDTO);
    }
}
