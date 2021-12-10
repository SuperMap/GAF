package com.supermap.gaf.cloud.bus.event;

import com.supermap.gaf.commontypes.MailSendDetail;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import java.io.Serializable;

/**
 * 邮件发送事件
 * @author : duke
 * @since 2021/12/9 2:16 PM
 */
public class RemoteMailSendEvent extends RemoteApplicationEvent implements Serializable {
    private static final long serialVersionUID = -5326439319517804418L;

    private MailSendDetail mailSendDetail;

    public RemoteMailSendEvent() {
        super();
    }

    public RemoteMailSendEvent(Object source, String originService, String destinationService) {
        super(source, originService, destinationService);
    }

    public MailSendDetail getMailSendDetail() {
        return mailSendDetail;
    }

    public void setMailSendDetail(MailSendDetail mailSendDetail) {
        this.mailSendDetail = mailSendDetail;
    }
}
