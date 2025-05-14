package com.example.agendaapi.service;

import com.example.agendaapi.model.Reminder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class ReminderService {

    private final JavaMailSender mailSender;

    public ReminderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(Reminder reminder) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(reminder.getEmail());
    message.setSubject("Recordatorio importante: " + reminder.getName());
    message.setText("Hola,\n\n"
        + "Este es un recordatorio cordial para informarte que tienes programado: \"" + reminder.getName() + "\" "
        + "para el día " + reminder.getDate() + ".\n\n"
        + "Te recomendamos tenerlo presente y tomar las previsiones necesarias.\n\n"
        + "Gracias por usar nuestra agenda.\n"
        + "¡Que tengas un excelente día!\n\n"
        + "Saludos cordiales,\n"
        + "El equipo de AgendaApp");

    mailSender.send(message);
}

}