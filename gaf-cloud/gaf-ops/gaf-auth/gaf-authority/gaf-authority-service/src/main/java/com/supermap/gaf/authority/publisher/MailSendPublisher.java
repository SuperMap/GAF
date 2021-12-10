package com.supermap.gaf.authority.publisher;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.boot.event.MailSendEvent;
import com.supermap.gaf.commontypes.MailSendDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author : duke
 * @since 2021/12/9 3:42 PM
 */
@Service
public class MailSendPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public void publish(MailSendDetail mailSendDetail){
        MailSendEvent mailSendEvent = new MailSendEvent(this);
        mailSendEvent.setMailSendDetail(mailSendDetail);
        applicationEventPublisher.publishEvent(mailSendEvent);
    }
}
