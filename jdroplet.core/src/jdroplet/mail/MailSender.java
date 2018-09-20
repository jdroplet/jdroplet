package jdroplet.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import jdroplet.exceptions.ApplicationException;


public class MailSender {
	// 收件人地址
	private String sendTo;
	// 发送人地址
	private String sendFrom;
	// SMTP服务
	private String smtpServer;
	// 登录SMTP用户
	private String userName;
	// 密码
	private String pwd;
	// 邮件主题
	private String subject;
	// 邮件内容
	private String content;
	// 附件
	List<String> attachments = new ArrayList<String>();

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MailSender(String sendTo, String sendFrom, String smtpServer,
			String userName, String pwd, String subject, String content,
			List<String> attachments) {
		this.sendTo = sendTo;
		this.sendFrom = sendFrom;
		this.smtpServer = smtpServer;
		this.userName = userName;
		this.pwd = pwd;
		this.subject = subject;
		this.content = content;
		this.attachments = attachments;
	}

	public MailSender() {
	}

	/**
	 * 转换中文格式
	 * 
	 * @param strText
	 * @return
	 */
	public String transferChinese(String strText) {
		try {
			strText = MimeUtility.encodeText(strText, "gb2312", "B");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strText;
	}

	public void attachfile(String name) {
		attachments.add(name);
	}

	// 发送邮件
	public boolean send() {
		// 创建邮件Session所需要的properties
		Properties prop = new Properties();
		prop.put("mail.smtp.host", smtpServer);
		prop.put("mail.smtp.auth", "true");
		// 以匿名内部类的形式创建登录服务器的认证对象
		Session sess = Session.getDefaultInstance(prop, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, pwd);
			}
		});
		// 构造MimeMessage崩设置相关属性
		MimeMessage mmsg = new MimeMessage(sess);
		try {
			// 设置发送人
			mmsg.setFrom(new InternetAddress(sendFrom));
			// 设置收信人
			InternetAddress[] addresses = { new InternetAddress(sendTo) };
			mmsg.setRecipients(Message.RecipientType.TO, addresses);
			// 设置主题
			subject = transferChinese(subject);
			//mmsg.setSubject(subject);
			mmsg.setSubject(subject, "GB2312"); // ！！！注意设置编码
			// 构造Multipart
			Multipart mp = new MimeMultipart();
			// 向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setText(content);
			// 将BodyPart添加到Multipart中
			mp.addBodyPart(mbpContent);
			// 向Multipart中添加附件
			// 遍历集合
			for (String efile : attachments) {
				MimeBodyPart mbp = new MimeBodyPart();
				// 以文件创建FileDataSource对象
				FileDataSource fds = new FileDataSource(efile);
				mbp.setDataHandler(new DataHandler(fds));
				mbp.setFileName(fds.getName());
				mp.addBodyPart(mbp);
			}
			attachments.clear();// 清空附件表
			mmsg.setContent(mp);
			mmsg.setSentDate(new Date());// 设置发送日期
			Transport.send(mmsg);
		} catch (AddressException e) {
			throw new ApplicationException(e.getMessage());
		} catch (MessagingException e) {
			throw new ApplicationException(e.getMessage());
		}
		return true;
	}
	
//	public static void main(String[] args) throws Exception {
//		MailSender mail = new MailSender();
//		mail.setSmtpServer("smtp.hainan.net");
//		mail.setUsername("messager@hainan.net");
//		mail.setPwd("a123456");
//		// 收件人
//		mail.setSendTo("84290742@qq.com");
//		mail.setSendFrom("messager@hainan.net");
//		mail.setSubject("测试邮件");
//		mail.setContent("这是一个测试邮件，请勿回信，谢谢");
//		//mail.attachments.add("src/email/SendMail.java");
//		//mail.attachments.add("src/email/Test.java");
//		System.out.println("正在发送。。。。。");
//		if (mail.send()) {
//			System.out.println("～～～发送成功～～～");
//		} else {
//			System.out.println("发送失败");
//		}
//	}
}
