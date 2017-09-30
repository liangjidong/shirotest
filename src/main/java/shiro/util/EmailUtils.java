package shiro.util;

import shiro.entity.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by author on 2017/9/16.
 */
public class EmailUtils {
    public static void main(String[] args) {
    }

    private static final String FROM = "XXX@xx.com";
    private static final String PWD = "xxxxxxxxxxxxxxxxx";

    /**
     * 发送账号激活邮件，需要传入对应用户的随机码
     * @param user
     * @param randomCode
     */
    public static void sendAccountActivateEmail(User user, String randomCode) {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        try {
            message.setSubject("帐户激活邮件");
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(FROM));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setContent("<a href='" + GenerateLinkUtils.generateActiveLink(user,randomCode) + "'>点击激活帐户</a>", "text/html;charset=utf-8");
            // 发送邮件
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送忘记密码修改邮件，需要传入对应用户的随机码
     * @param user
     * @param randomCode
     */
    public static void sendForgotPwdEmail(User user, String randomCode) {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        try {
            message.setSubject("密码修改邮件");
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(FROM));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setContent("<a href='" + GenerateLinkUtils.generateResetPwdLink(user,randomCode) + "'>点击修改密码</a>", "text/html;charset=utf-8");
            // 发送邮件
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Session getSession() {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(FROM, PWD);
            }
        });
        return session;
    }
}
