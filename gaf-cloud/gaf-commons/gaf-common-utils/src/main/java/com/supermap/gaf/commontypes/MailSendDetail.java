package com.supermap.gaf.commontypes;


import java.io.Serializable;

/**
 * @author : duke
 * @since 2021/12/9 2:19 PM
 */
public class MailSendDetail implements Serializable {
    private static final long serialVersionUID = -5392220525687521886L;

    public MailSendDetail() {
    }

    /**
     * 非必填，默认使用mail-properties中的username
     */
    private String from;
    /**
     * 邮箱地址 xxxx@xx.com
     */
    private String send;
    /**
     * 主标题
     */
    private String subject;
    /**
     * 文本内容
     */
    private String content;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
