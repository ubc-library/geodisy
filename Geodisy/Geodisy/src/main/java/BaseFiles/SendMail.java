package BaseFiles;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


public class SendMail {
    GeoLogger logger = new GeoLogger(this.getClass());

    public boolean send(String recipient, String doi){
        String sender = "Geodisy.Info@ubc.ca";
        String host = "localhost";
        String subject = "Please update you dataset if you want map-based searching for it";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        String msgBody = "Geodisy is trying to allow for your dataset to be found via FRDR's map-based research data search, but there is something wrong with the dataset at PERSISTENT_ID: " + doi + ". Please either manually enter a bounding box in the geospatial metadata area or add (country)/(country and state)/(country, state, and city) to the appropriate fields in the geospatial metadata. Thank you, The Geodisy Team";
        Session session = Session.getInstance(props, null);
        try {

            Message message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");
            message.setFrom(new InternetAddress(sender, "NoReply-JD"));
            message.setReplyTo(InternetAddress.parse(sender, false));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient, false));
            message.setSubject(subject);
            message.setText(msgBody);
            message.setSentDate(new Date());

            Transport.send(message);

            System.out.println("Done");
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Something went wrong trying to email " + recipient + " about record " + doi + ".");
            return false;
        }

    }
}
