package com.example.agendaapi.controller;

import com.example.agendaapi.dto.EventDTO;
import com.example.agendaapi.model.Event;
import com.example.agendaapi.repository.EventRepository;
import com.example.agendaapi.model.User;
import com.example.agendaapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // GET /api/event - Obtener todos los eventos
    @GetMapping
    public List<EventDTO> index() {
        return eventRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // POST /api/event - Crear nuevo evento
    @PostMapping
public ResponseEntity<EventDTO> store(@RequestBody @Valid EventDTO dto) {
    //  Log para ver todo el objeto recibido
    System.out.println("Evento DTO completo: " + dto.toString());
    System.out.println("Nombre: " + dto.getName());
    System.out.println("Fecha: " + dto.getDate());
    System.out.println("userId: " + dto.getUserId());


    //  Verificar si userId es null o <= 0
    if (dto.getUserId() == null || dto.getUserId() <= 0) {
        System.out.println("userId es nulo o inválido: " + dto.getUserId());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    try {
        // Buscar usuario por ID
        User user = userRepository.findById(dto.getUserId()).orElseThrow(
            () -> new IllegalArgumentException("Usuario no encontrado con ID: " + dto.getUserId())
        );

        // Asignar datos del evento
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setDate(dto.getDate());
        event.setHour(dto.getHour());
        event.setUser(user); // Relación con usuario

        Event saved = eventRepository.save(event);
        return ResponseEntity.status(201).body(mapToDTO(saved));

    } catch (IllegalArgumentException e) {
        // Manejo de errores personalizados
        System.err.println("Error de validación: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    } catch (Exception e) {
        // Otros errores internos
        System.err.println("Error interno al guardar evento: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    // GET /api/event/{id} - Ver evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> show(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/event/{id} - Actualizar evento
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody @Valid EventDTO dto) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setName(dto.getName());
                    event.setDescription(dto.getDescription());
                    event.setDate(dto.getDate());
                    event.setHour(dto.getHour());

                    Event updated = eventRepository.save(event);
                    return ResponseEntity.ok(mapToDTO(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/event/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.ok(Map.of("message", "Evento eliminado exitosamente"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No se encontró el evento con ID: " + id)));
    }

    private EventDTO mapToDTO(Event event) {
    EventDTO dto = new EventDTO();
    dto.setId(event.getId());
    dto.setName(event.getName());
    dto.setDate(event.getDate());
    dto.setHour(event.getHour());
    dto.setDescription(event.getDescription());

    // ✅ Evitamos NullPointerException
    if (event.getUser() != null && event.getUser().getId() != null) {
        dto.setUserId(event.getUser().getId());
    } else {
        System.out.println("⚠️ Advertencia: Evento no tiene usuario asignado");
        dto.setUserId(null); // O evita lanzar excepción
    }

    return dto;
    }
}