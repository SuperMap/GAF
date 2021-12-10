package com.supermap.gaf.portal.listener;

import com.supermap.gaf.boot.event.MailSendEvent;
import com.supermap.gaf.cloud.bus.event.RemoteMailSendEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author : duke
 * @since 2021/12/9 2:32 PM
 */
@Service
public class RemoteMailSendListener {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @EventListener(classes = RemoteMailSendEvent.class)
    public void mailSend(RemoteMailSendEvent remoteMailSendEvent){
        MailSendEvent mailSendEvent = new MailSendEvent(this);
        mailSendEvent.setMailSendDetail(remoteMailSendEvent.getMailSendDetail());
        applicationEventPublisher.publishEvent(mailSendEvent);
    }
}
