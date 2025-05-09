package com.example.agendaapi.scheduled;

import com.example.agendaapi.model.Reminder;
import com.example.agendaapi.repository.ReminderRepository;
import com.example.agendaapi.service.ReminderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class UpcomingReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final ReminderService reminderService;

    public UpcomingReminderScheduler(
            ReminderRepository reminderRepository,
            ReminderService reminderService) {
        this.reminderRepository = reminderRepository;
        this.reminderService = reminderService;
    }

    // Se ejecuta todos los días a las 8:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void checkUpcomingReminders() {
        List<Reminder> reminders = reminderRepository.findByDateBefore(LocalDate.now().plusDays(1));

        for (Reminder reminder : reminders) {
            reminderService.sendEmail(reminder);
        }

        System.out.println("Recordatorios enviados: " + reminders.size());
    }
}