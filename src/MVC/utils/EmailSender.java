/*
Name:           EmailSender.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class provides methods for the email functionality of the app.
                The class makes use of the JavaMail class.
                Methods in the class are adapted from:
                https://mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/

*/
package MVC.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;

public class EmailSender
{

    /**
     * Simple test method to ensure email authentication is working
     * @param recipient the email address to send the test email to
     * @throws Exception
     */
    private static void sendTestEmail(String recipient) throws Exception
    {

        ArrayList<String> loginDetails = getLogin("password_uea.txt");

        final String username = loginDetails.get(0);
        final String password = loginDetails.get(1);

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.office365.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });

        session.setDebug(true);

        try
        {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );
            message.setSubject("Automated Email testing");
            message.setText("This is a test email");

            Transport.send(message);

        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Send an email to the desired recipient
     * @param recipient The email address of the user to send to (can be
     *                  multiple emails, separated by a comma)
     * @param subject The subject of the email
     * @param messageContents The message to send (body of the email)
     * @throws Exception If an error occurs
     */
    public static void sendEmail(String recipient, String subject, String messageContents) throws Exception
    {
        ArrayList<String> loginDetails = getLogin("src\\MVC\\config\\email_credentials.txt");

        final String username = loginDetails.get(0);
        final String password = loginDetails.get(1);

        Properties prop = new Properties();

        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });

        //session.setDebug(true);

        try
        {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );
            message.setSubject(subject);
            message.setText(messageContents);

            Transport.send(message);

        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Private helper method to get login credentials
     * @param filepath The file path and name of file containing login details
     * @return An arraylist containing the login credentials
     * @throws Exception If an IO error occurs
     */
    private static ArrayList<String> getLogin(String filepath) throws Exception
    {
        ArrayList<String> details = new ArrayList<>();

        File file = new File(filepath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
            details.add(st);

        return details;

    }


}
