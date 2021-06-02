package com.supermap.gaf.authority.util.email;

/**
 * @author yd
 */
public class EmailConstant {

    /**
     * 邮件主题
     */
    public static final String SUBJECT = "GAF 密码";

    public static final String WELCOME_TEXT = "欢迎使用GAF，您的密码为：";
    /**
     * 修改邮箱
     */
    public static final String CAHANGE_EMAIL = "GAF 修改邮箱";

    /**
     * 修改邮箱校验码，邮件内容模板
     */
    public static final String CHECK_CODE_TEXT_TEMPLATE = "亲爱的用户 %s：您好！\n" +
            "\n" +
            "    您收到这封这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了修改邮箱。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。\n" +
            "\n" +
            "    请使用以下验证码完成后续修改邮箱流程\n" +
            "   \n" +
            "    %s\n" +
            "  \n" +
            "    注意:请您在收到邮件%s分钟内(%s前)使用，否则该验证码将会失效。";

}
