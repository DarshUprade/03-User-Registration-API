package in.ashokit.utils;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Component
public class EmailUtils {
	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendMail(String to,String subject, String body) {
		boolean isMailsend=false;
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body,true);  //for html tag we place true if plane text than only body
			mailSender.send(mimeMessage);
			
			isMailsend=true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isMailsend;
	}

}
