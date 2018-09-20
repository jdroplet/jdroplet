package jdroplet.mail;

import jdroplet.core.SystemConfig;
import jdroplet.exceptions.ApplicationException;

public class MailUtil {

	public static void send(String sendTo, String subject, String body) {
		send(SystemConfig.getProperty("app.mail.smtp"), SystemConfig.getProperty("app.mail.from"), SystemConfig.getProperty("app.mail.pwd"), sendTo, subject, body);
	}
	
	public static void send(String smtp, String sendFrom, String psw, String sendTo, String subject, String body) {
		MailSender mail = new MailSender();
		mail.setSmtpServer(smtp);
		mail.setUsername(sendFrom);
		mail.setPwd(psw);
		// 收件人
		mail.setSendTo(sendTo);
		mail.setSendFrom(sendFrom);
		mail.setSubject(subject);
		mail.setContent(body);

		try {
			mail.send();
		} catch(Exception ex) {
			throw new ApplicationException(ex.getMessage());
		}
	}
}
