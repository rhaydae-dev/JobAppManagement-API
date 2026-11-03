
package com.rhaydae.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendActivationEmail(String toEmail, String token) {

        String activationLink =
                frontendUrl + "/activate-account?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Activation de votre compte recruteur");
        message.setText(
                "Bonjour,\n\n"
                + "Votre compte recruteur a été créé.\n"
                + "Pour choisir votre mot de passe et activer votre compte, "
                + "cliquez sur le lien suivant :\n\n"
                + activationLink + "\n\n"
                + "Ce lien est valable pendant 30 minutes.\n\n"
                + "Si vous n'êtes pas à l'origine de cette demande, "
                + "vous pouvez ignorer cet email."
        );

        mailSender.send(message);
    }
    
    
		
		public void sendApplicationStatusEmail(
		        String toEmail,
		        String jobTitle,
		        String status) {
		
		    SimpleMailMessage message = new SimpleMailMessage();
		
		    message.setFrom(fromEmail);
		    message.setTo(toEmail);
		    message.setSubject("Mise à jour de votre candidature");
		
		    String statusMessage;
		
		    if ("ACCEPTED".equals(status)) {
		        statusMessage = "Bonne nouvelle ! Votre candidature a été acceptée.";
		    } else {
		        statusMessage = "Nous sommes désolés, votre candidature a été refusée.";
		    }
		
		    message.setText(
		            "Bonjour,\n\n"
		            + statusMessage + "\n\n"
		            + "Offre d'emploi : " + jobTitle + "\n"
		            + "Statut : " + status + "\n\n"
		            + "Merci d'avoir utilisé notre plateforme.\n\n"
		            + "Cordialement,\n"
		            + "L'équipe JobAppManagement"
		    );
		
		    mailSender.send(message);
		}

}
