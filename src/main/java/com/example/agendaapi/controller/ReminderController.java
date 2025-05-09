package com.example.agendaapi.controller;

import com.example.agendaapi.dto.ReminderDTO;
import com.example.agendaapi.model.Reminder;
import com.example.agendaapi.repository.ReminderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private JavaMailSender mailSender;

    // GET /api/reminders
    @GetMapping
    public List<ReminderDTO> index() {
        return reminderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // POST /api/reminders
    @PostMapping
    public ResponseEntity<?> store(@RequestBody @Valid ReminderDTO dto) {
        try {
            Reminder reminder = mapToModel(dto);
            Reminder saved = reminderRepository.save(reminder);

            sendEmail(saved);

            return ResponseEntity.status(201).body(mapToDTO(saved));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("No se pudo crear el recordatorio");
        }
    }

    // GET /api/reminders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReminderDTO> show(@PathVariable Long id) {
    return reminderRepository.findById(id)
            .map(this::mapToDTO)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}

    // DELETE /api/reminders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return reminderRepository.findById(id)
                .map(reminder -> {
                    reminderRepository.delete(reminder);
                    return ResponseEntity.ok(Map.of("message", "Recordatorio eliminado exitosamente"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No se encontró el recordatorio")));
    }

    // Métodos auxiliares

    private Reminder mapToModel(ReminderDTO dto) {
        Reminder reminder = new Reminder();
        reminder.setName(dto.getName());
        reminder.setDescription(dto.getDescription());
        reminder.setEmail(dto.getEmail());
        reminder.setDate(dto.getDate());
        return reminder;
    }

    private ReminderDTO mapToDTO(Reminder reminder) {
        ReminderDTO dto = new ReminderDTO();
        dto.setId(reminder.getId());
        dto.setName(reminder.getName());
        dto.setDescription(reminder.getDescription());
        dto.setEmail(reminder.getEmail());
        dto.setDate(reminder.getDate());
        return dto;
    }

    private void sendEmail(Reminder reminder) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reminder.getEmail());
        message.setSubject("Recordatorio: " + reminder.getName());
        message.setText("Tu recordatorio '" + reminder.getName() + "' está programado para " + reminder.getDate() + ".");
        
        mailSender.send(message);
    }
}