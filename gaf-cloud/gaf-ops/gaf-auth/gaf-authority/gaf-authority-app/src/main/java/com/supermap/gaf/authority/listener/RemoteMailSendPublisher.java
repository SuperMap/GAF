package com.supermap.gaf.authority.listener;

import com.supermap.gaf.boot.event.MailSendEvent;
import com.supermap.gaf.cloud.bus.event.RemoteMailSendEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author : duke
 * @since 2021/12/9 3:35 PM
 */
@Service
public class RemoteMailSendPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private BusProperties busProperties;

    @EventListener(classes = MailSendEvent.class)
    public void mailSend(MailSendEvent mailSendEvent){
        RemoteMailSendEvent remoteMailSendEvent = new RemoteMailSendEvent(this, busProperties.getId(), "gaf-portal:**");
        remoteMailSendEvent.setMailSendDetail(mailSendEvent.getMailSendDetail());
        applicationEventPublisher.publishEvent(remoteMailSendEvent);
    }
}
