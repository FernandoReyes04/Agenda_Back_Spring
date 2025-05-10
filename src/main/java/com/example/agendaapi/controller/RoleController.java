package com.example.agendaapi.controller;

import com.example.agendaapi.dto.RoleDTO;
import com.example.agendaapi.model.Role;
import com.example.agendaapi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    // GET /api/roles
    @GetMapping
    public ResponseEntity<List<RoleDTO>> index() {
        List<RoleDTO> roles = roleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(roles);
    }

    // POST /api/roles
    @PostMapping
    public ResponseEntity<RoleDTO> store(@RequestBody RoleDTO dto) {
        try {
            Role role = mapToModel(dto);
            Role saved = roleRepository.save(role);
            return new ResponseEntity<>(mapToDTO(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET /api/roles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> show(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/roles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable Long id, @RequestBody RoleDTO dto) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setName(dto.getName());
                    Role updated = roleRepository.save(role);
                    return ResponseEntity.ok(mapToDTO(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/roles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(role -> {
                    roleRepository.delete(role);
                    return ResponseEntity.ok("Rol eliminado exitosamente");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el rol"));
    }

    // Métodos auxiliares

    private Role mapToModel(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }

    private RoleDTO mapToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }
}