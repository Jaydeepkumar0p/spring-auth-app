package com.example.SpringAuth.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    // ── Generic HTML sender ───────────────────────────────────────────────
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = isHtml
            mailSender.send(message);
            log.info("Email sent to {}: {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    // ── Todo Created ──────────────────────────────────────────────────────
    public void sendTodoCreated(String to, String userName, String todoTitle, String description) {
        String subject = "Todo created: " + todoTitle;
        String body = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:#378ADD">New Todo Created</h2>
              <p>Hi <strong>%s</strong>,</p>
              <p>Your new todo has been created successfully.</p>
              <table style="width:100%%;border-collapse:collapse;margin:16px 0">
                <tr><td style="padding:8px;background:#f5f5f5;font-weight:bold;width:30%%">Title</td>
                    <td style="padding:8px;border-bottom:1px solid #eee">%s</td></tr>
                <tr><td style="padding:8px;background:#f5f5f5;font-weight:bold">Description</td>
                    <td style="padding:8px;border-bottom:1px solid #eee">%s</td></tr>
              </table>
              <p style="color:#888;font-size:13px">Good luck completing it!</p>
            </div>
            """.formatted(userName, todoTitle, description != null ? description : "—");
        sendHtmlEmail(to, subject, body);
    }

    // ── Todo Completed ────────────────────────────────────────────────────
    public void sendTodoCompleted(String to, String userName, String todoTitle) {
        String subject = "Completed: " + todoTitle;
        String body = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:#27500A">Todo Completed!</h2>
              <p>Hi <strong>%s</strong>,</p>
              <p>Congratulations! You completed: <strong>%s</strong></p>
              <p style="font-size:32px;text-align:center">✅</p>
              <p style="color:#888;font-size:13px">Keep up the great work!</p>
            </div>
            """.formatted(userName, todoTitle);
        sendHtmlEmail(to, subject, body);
    }

    // ── Todo Updated ──────────────────────────────────────────────────────
    public void sendTodoUpdated(String to, String userName, String todoTitle) {
        String subject = "Todo updated: " + todoTitle;
        String body = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:#BA7517">Todo Updated</h2>
              <p>Hi <strong>%s</strong>,</p>
              <p>Your todo <strong>%s</strong> has been updated.</p>
              <p style="color:#888;font-size:13px">Log in to see the changes.</p>
            </div>
            """.formatted(userName, todoTitle);
        sendHtmlEmail(to, subject, body);
    }

    // ── Todo Deleted ──────────────────────────────────────────────────────
    public void sendTodoDeleted(String to, String userName, String todoTitle) {
        String subject = "Todo deleted: " + todoTitle;
        String body = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:24px;border:1px solid #e0e0e0;border-radius:8px">
              <h2 style="color:#A32D2D">Todo Deleted</h2>
              <p>Hi <strong>%s</strong>,</p>
              <p>Your todo <strong>%s</strong> has been permanently deleted.</p>
              <p style="color:#888;font-size:13px">If this was a mistake, please recreate it.</p>
            </div>
            """.formatted(userName, todoTitle);
        sendHtmlEmail(to, subject, body);
    }
}