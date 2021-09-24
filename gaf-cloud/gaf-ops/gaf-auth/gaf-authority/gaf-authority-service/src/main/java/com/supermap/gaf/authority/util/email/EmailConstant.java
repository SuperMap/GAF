package com.supermap.gaf.authority.util.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author yd
 */
@Component
public class EmailConstant {

    /**
     * 邮件主题
     */
    public static String SUBJECT_TITLE = "GAF 密码";

    public static String WELCOME_TEXT = "欢迎使用GAF，您的密码为：";
    /**
     * 修改邮箱
     */
    public static String CAHANGE_EMAIL = "GAF 修改邮箱";

    /**
     * 修改邮箱校验码，邮件内容模板
     */
    public static String CHECK_CODE_TEXT_TEMPLATE = "亲爱的用户 %s：您好！\n" +
            "\n" +
            "    您收到这封这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了修改邮箱。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。\n" +
            "\n" +
            "    请使用以下验证码完成后续修改邮箱流程\n" +
            "   \n" +
            "    %s\n" +
            "  \n" +
            "    注意:请您在收到邮件%s分钟内(%s前)使用，否则该验证码将会失效。";


    @Value("${gaf.mail.subjectTitle:}")
    public static void setSubjectTitle(String subjectTitle) {
        if (!StringUtils.isEmpty(subjectTitle)) {
            EmailConstant.SUBJECT_TITLE = subjectTitle;
        }
    }
    @Value("${gaf.mail.welcomeText:}")
    public static void setWelcomeText(String welcomeText) {
        if (!StringUtils.isEmpty(welcomeText)) {
            EmailConstant.WELCOME_TEXT = welcomeText;
        }
    }
    @Value("${gaf.mail.cahangeEmail:}")
    public static void setCahangeEmail(String cahangeEmail) {
        if (!StringUtils.isEmpty(cahangeEmail)) {
            EmailConstant.CAHANGE_EMAIL = cahangeEmail;
        }
    }
    @Value("${gaf.mail.checkCodeTextTemplate:}")
    public static void setCheckCodeTextTemplate(String checkCodeTextTemplate) {
        if (!StringUtils.isEmpty(checkCodeTextTemplate)) {
            EmailConstant.CHECK_CODE_TEXT_TEMPLATE = checkCodeTextTemplate;
        }
    }
}
