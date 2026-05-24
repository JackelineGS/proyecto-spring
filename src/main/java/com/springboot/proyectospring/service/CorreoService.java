package com.springboot.proyectospring.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class CorreoService {

    private static final Logger log = LoggerFactory.getLogger(CorreoService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    public CorreoService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarComprobante(String destinatario, Map<String, Object> variables) {
        if (destinatario == null || destinatario.isBlank()) return;
        try {
            Context ctx = new Context();
            ctx.setVariables(variables);
            String html = templateEngine.process("email/comprobante-email", ctx);

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(from, "Digital Health");
            helper.setTo(destinatario);
            helper.setSubject("Tu comprobante de pago - Digital Health");
            helper.setText(html, true);
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("No se pudo enviar el comprobante a {}: {}", destinatario, e.getMessage());
        }
    }
}
