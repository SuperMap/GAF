package com.supermap.gaf.portal.service;

import com.supermap.gaf.commontypes.MailSendDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : duke
 * @since 2021/12/9 9:59 AM
 */
@Service
public class EmailService {
    @Resource
    private JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username:}")
    private String from;


    /**
     * 发送纯文本邮件.
     *
     * @param to      目标email 地址
     * @param subject 邮件主题
     * @param text    纯文本内容
     */
    public void sendMail(String from,String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }


    public void sendMail(MailSendDetail detail) {
        String detailFrom = detail.getFrom();
        if (StringUtils.isBlank(detailFrom)){
            detailFrom = from;
        }
        sendMail(detailFrom, detail.getSend(), detail.getSubject(), detail.getContent());
    }
}
