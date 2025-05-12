package com.example.agendaapi.controller;

import com.example.agendaapi.dto.ReminderDTO;
import com.example.agendaapi.model.Reminder;
import com.example.agendaapi.model.User;
import com.example.agendaapi.repository.ReminderRepository;
import com.example.agendaapi.repository.UserRepository;

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
@RequestMapping("/api/reminder")
public class ReminderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private JavaMailSender mailSender;

    // POST /api/reminder - Crear nuevo recordatorio
    @PostMapping
public ResponseEntity<?> store(@RequestBody @Valid ReminderDTO dto) {
    try {
        // ✅ Validaciones claras
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre es obligatorio");
        }

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo es obligatorio");
        }

        if (dto.getDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La fecha es obligatoria");
        }

        // ✅ Busca al usuario por userId
        User user = userRepository.findById(dto.getUserId()).orElseThrow(
            () -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUserId())
        );

        Reminder reminder = mapToModel(dto);
        reminder.setUser(user);

        Reminder saved = reminderRepository.save(reminder);
        sendEmail(saved);

        return ResponseEntity.status(201).body(mapToDTO(saved));

    } catch (Exception e) {
        System.err.println("Error al guardar recordatorio: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

    // GET /api/reminders - Obtener todos los recordatorios
    @GetMapping
    public List<ReminderDTO> index() {
        return reminderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
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
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No se encontró el recordatorio")));
    }

    // Métodos auxiliares

    private Reminder mapToModel(ReminderDTO dto) {
    Reminder reminder = new Reminder();
    reminder.setName(dto.getName());
    reminder.setDescription(dto.getDescription());
    reminder.setEmail(dto.getEmail());
    reminder.setDate(dto.getDate());

    if (dto.getHour() != null && !dto.getHour().isEmpty()) {
        reminder.setHour(dto.getHour());
    }

    // ✅ Asigna usuario solo si tenemos userId
    if (dto.getUserId() != null && dto.getUserId() > 0) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(
            () -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUserId())
        );
        reminder.setUser(user); // ✅ Relación correcta
    }

    return reminder;
}

    private ReminderDTO mapToDTO(Reminder reminder) {
    ReminderDTO dto = new ReminderDTO();
    dto.setId(reminder.getId());
    dto.setName(reminder.getName());
    dto.setDescription(reminder.getDescription());
    dto.setEmail(reminder.getEmail());
    dto.setDate(reminder.getDate());

    // ✅ Asignamos userId solo si existe en el recordatorio
    if (reminder.getUser() != null && reminder.getUser().getId() != null) {
        dto.setUserId(reminder.getUser().getId()); // Ahora sí funciona
    }

    return dto;
}

    // Método para enviar correo electrónico de recordatorio
    private void sendEmail(Reminder reminder) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reminder.getEmail());
        message.setSubject("Nuevo recordatorio: " + reminder.getName());
        message.setText("Tu recordatorio '" + reminder.getName() + "' ha sido creado.\nFecha: " + reminder.getDate() + "\nDescripción: " + reminder.getDescription());
        
        mailSender.send(message);
    }
}