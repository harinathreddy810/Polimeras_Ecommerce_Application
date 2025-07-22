package com.Polimeras.Service;

import com.Polimeras.Entity.OrderStatus;
import com.Polimeras.Entity.Orders;
import com.Polimeras.Entity.PaymentStatus;
import com.Polimeras.Entity.Users;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    final String setFromEmail = "manapolimeras@gmail.com";
//    public String
    public void emailHandler(String toEmail,String firstname ,String lastname,long id) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setFrom("manapolimeras@gmail.com");
//            helper.setFrom(users.getEmail());
            helper.setTo(toEmail);
            helper.setSubject("Mana Polimeraas welcomes you Mr./Ms. " + firstname + lastname);

            helper.setText(
                    "Id : " + id +
                    "Name : " + firstname + " " + lastname

            );
            helper.addAttachment("Leaf-logo.png", new File("D:\\Harinathreddy\\My Projects\\Ecommerce\\Polimeras-Grocery\\src\\main\\resources\\uploads\\Leaf-logo.png"));
            javaMailSender.send(message);
            System.out.println("Mail sent");

        } catch (Exception e) {
            System.out.println("Mail Not sent");
            System.out.println(e.getMessage());
        }
    }

//    public void newRegister(String firstname, long id, String toEmail) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message,true);
//
//            helper.setFrom("manapolimeras@gmail.com");
////            helper.setFrom(users.getEmail());
//            helper.setTo(toEmail);
//            helper.setSubject("Mana Polimeraas welcomes you Mr./Ms. " + firstname);
//
//            helper.setText(
//                    "Hii " + firstname + " this is yours id : " + id
//
//            );
////            helper.addAttachment("Leaf-logo.png", new File("C:\\Users\\vadla vineeth kumar\\Downloads\\Leaf-logo.png"));
//            javaMailSender.send(message);
//            System.out.println("Mail sent to : " + toEmail);
//
//        } catch (Exception e) {
//            System.out.println("Mail Not sent");
//            System.out.println(e.getMessage());
//        }
//    }


    public void newRegister(String firstname, long id, String toEmail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String customMessage = "This is a message from Mana Polimeras.<br>" +
                    "Your ID is: <strong>" + id + "</strong><br>" +
                    "Thank you for registering.";

            helper.setFrom(setFromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Mana Polimeras welcomes you Mr./Ms. " + firstname);
            Path path = Paths.get("src/main/resources/static/Mail/RegisterMail.html");
            String htmlContent = Files.readString(path, StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("{{name}}", firstname);
            htmlContent = htmlContent.replace("{{message}}", customMessage);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            System.out.println("Mail sent to: " + toEmail);
        } catch (Exception e) {
            System.out.println("Mail not sent");
            e.printStackTrace();
        }
    }








    private void sendEmail(String toEmail, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body,false);

            javaMailSender.send(message);
            System.out.println("Mail sent to : " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private void sendSimpleEmail(String toEmail, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            javaMailSender.send(message);
            System.out.println("Mail sent to : " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


//    public void sendOtpEmail(String toEmail, String otpCode) {
//        String subject = "Your Password Reset Code";
//        String body = "Dear Customer,\n\n"
//                + "Your OTP for resetting your password is: " + otpCode + "\n"
//                + "This code will expire in 5 minutes.\n\n"
//                + "Best regards,\nTeam";
//        System.out.println("Mail sent to : " + toEmail);
//        sendEmail(toEmail, subject, body);
//    }

//    html body

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(setFromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your Password Reset Code");

            Path path = Paths.get("src/main/resources/static/Mail/OtpMail.html");
            String htmlContent = Files.readString(path, StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("{{otp}}", otpCode);

            helper.setText(htmlContent, true);
            javaMailSender.send(message);

            System.out.println("Mail sent to: " + toEmail);
        } catch (Exception e) {
            System.out.println("Mail not sent");
            e.printStackTrace();
        }
    }


//    public void sendResetPasswordConfirmationEmail(String toEmail) {
//        String subject = "Your Password Has Been Successfully Reset";
//        String body = "Hello,\n\n"
//                + "This is a confirmation that your password has been successfully updated.\n\n"
//                + "If you did not perform this action, please contact our support team immediately.\n\n"
//                + "Thank you,\n"
//                + "Mana Polimeraas - Keep Smile...";
//        System.out.println("Mail sent to : " + toEmail);
//        sendSimpleEmail(toEmail, subject, body);
//    }

//    html body

    public void sendResetPasswordConfirmationEmail(String toEmail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(setFromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your Password Has Been Successfully Reset");

            Path path = Paths.get("src/main/resources/static/Mail/ResetPasswordConfirmation.html");
            String htmlContent = Files.readString(path, StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("{{name}}", "Customer");

            helper.setText(htmlContent, true);
            javaMailSender.send(message);

            System.out.println("Mail sent to: " + toEmail);
        } catch (Exception e) {
            System.out.println("Mail not sent");
            e.printStackTrace();
        }
    }


    public void sendBlockNotification(String toEmail, String username) {
        String subject = "Account Disabled";
        String body = "Dear \n" + username + " ," +
                "\n" +
                "We regret to inform you that your account with **MANA POLIMERAAS** has been temporarily restricted.\n" +
                "\n" +
                "If you believe this action was taken in error or would like further clarification, please contact our support team at Support Email/Contact Page.\n" +
                "\n" +
                "We appreciate your understanding and cooperation.\n" +
                "\n" +
                "Best regards,\n" +
                "Mana Polimeraas Support Team" +
                "Thank You.";
        System.out.println("Mail sent to : " + toEmail);
        sendEmail(toEmail,subject,body);
    }

    public void sendApprovalNotification(String toEmail, String username) {
        String subject = "Account Re-Activated";
        String body = "Dear " + username + ",\n\n" +
                "We are pleased to inform you that your account with **MANA POLIMERAAS** has been re-activated and full access has been restored.\n\n" +
                "You may now log in and resume using our services. If you encounter any issues, please contact our support team at [Support Email/Contact Page].\n\n" +
                "Thank you for your patience and cooperation.\n\n" +
                "Best regards,\n" +
                "Mana Polimeraas Support Team\n" +
                "Thank You.";

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom(setFromEmail);
            helper.setTo(toEmail);

            javaMailSender.send(message);
            System.out.println("Mail sent to : " + toEmail );
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public void sendSuccessEmail(Long id , BigDecimal totalAmount , String email,String add) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);
            String subject = "Successfully Order Placed";
            String body = "Your Order Number is : " + id + "/n" +
                    "Total Amount is : " + totalAmount + "/n" +
                    "Shipping Address : " + add + "/n/n/n/n" +
                    "From," +"/n"+
                    "Mana Polimeras.";
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom(setFromEmail);
            helper.setTo(email);

            javaMailSender.send(message);
            System.out.println("Mail sent to : " + email );
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public void orderMailSend(String toEmail, String username, String orderNumber,
                              String paymentMethod, OrderStatus orderStatus,
                                PaymentStatus paymentStatus , double amount) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        String subject = "Successfully Order Placed✅";
        helper.setFrom(setFromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);

        Path path = Paths.get("src/main/resources/static/Mail/OrderMail.html");
        String htmlContent = Files.readString(path, StandardCharsets.UTF_8);

        htmlContent = htmlContent.replace("{{name}}", username);
        htmlContent = htmlContent.replace("{{orderNumber}}", orderNumber);
        htmlContent = htmlContent.replace("{{paymentMethod}}",paymentMethod );
        htmlContent = htmlContent.replace("{{orderStatus}}",orderStatus.toString() );
        htmlContent = htmlContent.replace("{{paymentStatus}}",paymentStatus.toString() );
        htmlContent = htmlContent.replace("{{amount}}",String.valueOf(amount));
        helper.setText(htmlContent, true);

        helper.addAttachment("order.pdf", new File("D:\\Harinathreddy\\My Projects\\Ecommerce\\Polimeras-Grocery\\src\\main\\resources\\uploads\\order.pdf"));
        javaMailSender.send(message);

        System.out.println("Mail sent to: " + toEmail);

    }

}
