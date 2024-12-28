package com.example.signin_signup;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    // Store app password securely using PasswordManager
    private static String getAppPassword(Context context) {
        return PasswordManager.getAppPassword(context);  // Retrieve securely stored app password
    }



    public static void sendEmail(Context context, String recipientEmail, String subject, String body) {
        // Allow networking on the main thread (only for testing; avoid this in production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Sender email credentials (use App Password stored securely)
        final String senderEmail = "vleraislami11@gmail.com";
        final String senderPassword = "vbkf bogw zyxg pokl";  // Ensure no extra spaces



        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption

        // Create a mail session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        // Validate the recipient email before proceeding
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            Log.e("EmailSender", "Recipient email is invalid or empty.");
            return;
        }

        // Send the email in background using AsyncTask
        new SendEmailTask(session, recipientEmail, subject, body).execute();
    }


    private static class SendEmailTask extends android.os.AsyncTask<Void, Void, Void> {

        private final Session session;
        private final String recipientEmail;
        private final String subject;
        private final String body;

        public SendEmailTask(Session session, String recipientEmail, String subject, String body) {
            this.session = session;
            this.recipientEmail = recipientEmail;
            this.subject = subject;
            this.body = body;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Validate recipient email
                if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
                    Log.e("EmailSender", "Recipient email is invalid or empty.");
                    return null;
                }

                // Validate email format
                if (!Patterns.EMAIL_ADDRESS.matcher(recipientEmail).matches()) {
                    Log.e("EmailSender", "Invalid email format: " + recipientEmail);
                    return null;
                }

                // Log the recipient email for debugging purposes
                Log.d("EmailSender", "Sending email to: " + recipientEmail);

                if (body != null && !body.isEmpty()) {
                    InternetAddress.parse(body);
                } else {
                    // Handle the case when the email is null or empty
                }

                // Create the email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(session.getProperty("mail.smtp.user"))); // Sender email
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Recipient email
                message.setSubject(subject); // Email subject
                message.setText(body); // Email body

                // Send the email
                Transport.send(message);
                Log.d("EmailSender", "Email sent successfully to " + recipientEmail);

            } catch (MessagingException e) {
                Log.e("EmailSender", "Error sending email", e);
            }
            return null;
        }
    }


}
