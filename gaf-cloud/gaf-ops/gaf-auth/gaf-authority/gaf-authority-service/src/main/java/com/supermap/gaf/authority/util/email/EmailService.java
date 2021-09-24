/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.util.email;

import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import java.util.Date;

/**
 * @author yd
 * @date:2021/3/25
 */
@Component
public class EmailService {
    private static final Logger logger = LogUtil.getLocLogger(EmailService.class);

    private final JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username:}")
    private String sender;

    public EmailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendPassword(String receiver, String password) {
        try {
            //true表示支持复杂类型
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            messageHelper.setFrom(new InternetAddress(sender, EmailConstant.NICK_NAME, "UTF-8"));
            messageHelper.setTo(receiver.split(","));
            messageHelper.setSubject(EmailConstant.SUBJECT_TITLE);
            messageHelper.setText(String.format("%s%s", EmailConstant.WELCOME_TEXT, password));
            messageHelper.setSentDate(new Date());
            mailSender.send(messageHelper.getMimeMessage());
            logger.info(String.format("已发送邮件，用户：%s；密码：%s", receiver, password));
        } catch (Exception e) {
            logger.error("发送邮件异常", e);
        }
    }

    @Async
    public void sendText(String receiver, String subject, String context) {
        try {
            //true表示支持复杂类型
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            messageHelper.setFrom(new InternetAddress(sender, EmailConstant.NICK_NAME, "UTF-8"));
            messageHelper.setTo(receiver.split(","));
            messageHelper.setSubject(subject);
            messageHelper.setText(context);
            messageHelper.setSentDate(new Date());
            mailSender.send(messageHelper.getMimeMessage());
            logger.info(String.format("已发送邮件，主题: %s, 内容: %s", subject, context));
        } catch (Exception e) {
            logger.error("发送邮件异常", e);
        }
    }

}
