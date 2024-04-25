package mail;
import java.util.Properties;

import javax.mail.*;  
import javax.mail.internet.*;  



public class Mailer {
	
	private static final String sender = "playsphereofficial@gmail.com";
	private static final String password = "znnxncdczbduvbpx";
	private static final String host = "smtp.gmail.com";
	private static final String port = "587";
	//moxiphjaatlajrrh
	
	private static String subject = "PlaySphere mail confirmation";
	
	private String reciever;
	private String registrationConfirmText;
	private String resetPassText;
	
	
	
	public Mailer(String reciever, String username, String confirmationLink) {
		
		this.reciever = reciever;
		this.registrationConfirmText = "Benvenuto "+username+"!\nsiamo felici di averti con noi, per proseguire con la registrazione sul link qui sotto:\n "+confirmationLink;
		this.resetPassText = "Ciao, \nclicca sul link sotto per resettare la password :\n\n"+confirmationLink;
	}
	
	public Mailer(String reciever, String confirmationLink, boolean isResetPass) {
		this.reciever = reciever;
		if(isResetPass) {
			this.registrationConfirmText = this.resetPassText = "Ciao, \nclicca sul link sotto per resettare la password :\n\n"+confirmationLink;
			this.subject = "Playsphere reset Password";
		}
	}
	
	public boolean send() {
		
		Properties props = new Properties();
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
//	    props.put("mail.smtp.socketFactory.port", port);
//	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    
	    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });
	    
	    
	    try {
	    	
	    	Message message = new MimeMessage(session);
	    	message.setFrom(new InternetAddress(Mailer.sender));
	    	message.setRecipient(Message.RecipientType.TO, new InternetAddress(this.reciever));
	    	message.setSubject(subject);
	    	message.setText(registrationConfirmText);
	    	Transport.send(message);
	    	
	    	return true;
	    	
	    }catch(MessagingException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}

}
