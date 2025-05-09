// src/main/java/com/example/agendaapi/controller/ContactController.java

package com.example.agendaapi.controller;

import com.example.agendaapi.dto.ContactDTO;
import com.example.agendaapi.model.Contact;
import com.example.agendaapi.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    // GET /api/contacts
    @GetMapping
    public List<ContactDTO> index() {
        return contactRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
}

    // POST /api/contacts
    @PostMapping
    public ResponseEntity<ContactDTO> store(@RequestBody Contact contact) {
    try {
        Contact saved = contactRepository.save(contact);
        ContactDTO dto = mapToDTO(saved);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    // GET /api/contacts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> show(@PathVariable Long id) {
    return contactRepository.findById(id)
        .map(contact -> mapToDTO(contact))
        .map(dto -> ResponseEntity.ok(dto))
        .orElseGet(() -> ResponseEntity.notFound().build());
}

    // PUT /api/contacts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable Long id, @RequestBody Contact updatedContact) {
    return contactRepository.findById(id)
        .map(contact -> {
            contact.setName(updatedContact.getName());
            contact.setEmail(updatedContact.getEmail());
            contact.setPhone(updatedContact.getPhone());
            contact.setNotes(updatedContact.getNotes());

            Contact saved = contactRepository.save(contact);
            ContactDTO dto = mapToDTO(saved);
            return ResponseEntity.ok(dto);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
}
    // DELETE /api/contacts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return contactRepository.findById(id)
            .map(contact -> {
                contactRepository.delete(contact);
                return new ResponseEntity<>("Contacto eliminado exitosamente", HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>("No se encontró el contacto", HttpStatus.NOT_FOUND));
    }

    private ContactDTO mapToDTO(Contact contact) {
        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setPhone(contact.getPhone());
        dto.setNotes(contact.getNotes());
        return dto;
    }
}
