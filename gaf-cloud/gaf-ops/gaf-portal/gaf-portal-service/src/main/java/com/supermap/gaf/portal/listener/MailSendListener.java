package com.supermap.gaf.portal.listener;

import com.supermap.gaf.boot.event.MailSendEvent;

import com.supermap.gaf.commontypes.MailSendDetail;
import com.supermap.gaf.portal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author : duke
 * @since 2021/12/9 3:16 PM
 */
@Service
public class MailSendListener {
    @Autowired
    private EmailService emailService;

    @EventListener(classes = MailSendEvent.class)
    public void mailSend(MailSendEvent mailSendEvent){
        MailSendDetail mailSendDetail = mailSendEvent.getMailSendDetail();
        emailService.sendMail(mailSendDetail);
    }
}
