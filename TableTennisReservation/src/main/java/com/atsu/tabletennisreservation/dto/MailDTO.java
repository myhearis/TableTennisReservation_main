package com.atsu.tabletennisreservation.dto;

import java.io.Serializable;
//邮件dto
public class MailDTO implements Serializable {
    private static final long serialVersionUID = -2261214375711667711L;
    private String targetMail;//接受邮箱账户(不可为空)
    private String originMail;//源邮箱账户（不可为空）
    private String title;//标题
    private String content;//要发送的内容(内容不可为空)
    public String getTargetMail() {
        return targetMail;
    }

    public void setTargetMail(String targetMail) {
        this.targetMail = targetMail;
    }

    public String getOriginMail() {
        return originMail;
    }

    public void setOriginMail(String originMail) {
        this.originMail = originMail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MailDTO(String targetMail, String originMail, String title, String content) {
        this.targetMail = targetMail;
        this.originMail = originMail;
        this.title = title;
        this.content = content;
    }

    public MailDTO() {
    }

    @Override
    public String toString() {
        return "MailDTO{" +
                "targetMail='" + targetMail + '\'' +
                ", originMail='" + originMail + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
