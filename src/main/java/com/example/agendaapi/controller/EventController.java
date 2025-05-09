package com.example.agendaapi.controller;

import com.example.agendaapi.dto.EventDTO;
import com.example.agendaapi.model.Event;
import com.example.agendaapi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Map;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    // GET /api/events
    @GetMapping
    public List<EventDTO> index() {
        return eventRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // POST /api/events
    @PostMapping
    public ResponseEntity<EventDTO> store(@RequestBody @Valid EventDTO dto) {
        try {
            Event event = mapToModel(dto);
            Event saved = eventRepository.save(event);
            return new ResponseEntity<>(mapToDTO(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET /api/events/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> show(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/events/{id}
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody @Valid EventDTO dto) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setName(dto.getName());
                    event.setDate(dto.getDate());
                    event.setHour(dto.getHour());
                    event.setDescription(dto.getDescription());

                    Event saved = eventRepository.save(event);
                    return ResponseEntity.ok(mapToDTO(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/events/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.ok(Map.of("message", "Evento eliminado exitosamente"));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No se encontró el evento con ID: " + id)));
    }

    // Mapeo modelo → DTO
    private Event mapToModel(EventDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDate(dto.getDate());
        event.setHour(dto.getHour());
        event.setDescription(dto.getDescription());
        return event;
    }

    // Mapeo modelo → DTO
    private EventDTO mapToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setHour(event.getHour());
        dto.setDescription(event.getDescription());
        return dto;
    }
}