package com.example.agendaapi.controller;

import com.example.agendaapi.dto.ContactDTO;
import com.example.agendaapi.model.Contact;
import com.example.agendaapi.repository.ContactRepository;
import com.example.agendaapi.repository.UserRepository;

import jakarta.validation.Valid;
import com.example.agendaapi.model.User; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contact") 
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    // GET /api/contacts
    @GetMapping
    public List<ContactDTO> index() {
        return contactRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // POST /api/contacts
    @PostMapping
public ResponseEntity<Contact> store(@RequestBody ContactDTO dto) {
    Contact contact;

    if (dto.getId() != null && contactRepository.existsById(dto.getId())) {
        // Si viene un ID y ya existe → editar
        contact = contactRepository.getReferenceById(dto.getId());
    } else {
        // Si no hay ID → crear uno nuevo
        contact = new Contact();
    }

    // Asigna los campos comunes
    contact.setFirst_name(dto.getFirst_name());
    contact.setLast_name(dto.getLast_name());
    contact.setEmail(dto.getEmail());
    contact.setPhone_number(dto.getPhone_number());
    contact.setNotes(dto.getNotes());

    // Relación con usuario
    User user = userRepository.findById(dto.getUserId()).orElseThrow();
    contact.setUser(user);

    // Guarda en la BD
    Contact saved = contactRepository.save(contact);
    return ResponseEntity.status(201).body(saved);
}
    // GET /api/contacts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> show(@PathVariable Long id) {
        return contactRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/contacts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody @Valid ContactDTO dto) {
        return contactRepository.findById(id)
                .map(contact -> {
                    contact.setFirst_name(dto.getFirst_name());
                    contact.setLast_name(dto.getLast_name());
                    contact.setEmail(dto.getEmail());
                    contact.setPhone_number(dto.getPhone_number());
                    contact.setNotes(dto.getNotes());

                    Contact saved = contactRepository.save(contact);
                    return ResponseEntity.ok(mapToDTO(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/contacts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return contactRepository.findById(id)
                .map(contact -> {
                    contactRepository.delete(contact);
                    return ResponseEntity.ok("Contacto eliminado exitosamente");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el contacto"));
    }

    // Métodos auxiliares
    private ContactDTO mapToDTO(Contact contact) {
        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setFirst_name(contact.getFirst_name());
        dto.setLast_name(contact.getLast_name());
        dto.setEmail(contact.getEmail());
        dto.setPhone_number(contact.getPhone_number());
        dto.setNotes(contact.getNotes());
        return dto;
    }
}