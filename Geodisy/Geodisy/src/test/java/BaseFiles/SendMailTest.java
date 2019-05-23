package BaseFiles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendMailTest {

    @Test
    void send() {
        SendMail sm = new SendMail();
        boolean sent = sm.send("Paul.dante@ubc.ca","This is where the doi would go.");
        System.out.println("The email got sent? " + sent);
    }
}