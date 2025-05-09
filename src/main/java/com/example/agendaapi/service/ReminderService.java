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
        message.setSubject("Recordatorio: " + reminder.getName());
        message.setText("Tu recordatorio '" + reminder.getName() + "' está programado para " + reminder.getDate() + ".");

        mailSender.send(message);
    }
}