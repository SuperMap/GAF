package com.supermap.gaf.boot.event;

import com.supermap.gaf.commontypes.MailSendDetail;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * @author : duke
 * @since 2021/12/9 3:08 PM
 */
public class MailSendEvent extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = -6610173375591321756L;

    private MailSendDetail mailSendDetail;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MailSendEvent(Object source) {
        super(source);
    }


    public MailSendDetail getMailSendDetail() {
        return mailSendDetail;
    }

    public void setMailSendDetail(MailSendDetail mailSendDetail) {
        this.mailSendDetail = mailSendDetail;
    }
}
