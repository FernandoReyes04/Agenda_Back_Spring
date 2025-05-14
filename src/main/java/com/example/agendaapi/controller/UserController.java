package com.example.agendaapi.controller;

import com.example.agendaapi.dto.LoginDTO;
import com.example.agendaapi.dto.UserDTO;
import com.example.agendaapi.model.User;
import com.example.agendaapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;



    // GET /api/users – Obtener todos los usuarios (solo para pruebas)
    @GetMapping
    public List<User> index() {
        return userRepository.findAll();
    }

    // POST /api/users/register – Registrar nuevo usuario con contraseña encriptada
   @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody @Valid UserDTO dto) {
    System.out.println("Datos recibidos: " + dto.getEmail());
    System.out.println("Contraseña: " + dto.getPassword()); // Si es null → problema aquí

    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        return ResponseEntity.status(400).body("El correo ya está registrado");
    }

    if (dto.getUsername() == null || dto.getPassword() == null) {
        return ResponseEntity.status(400).body("Faltan campos obligatorios");
    }

    if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
        return ResponseEntity.status(400).body("El nombre de usuario ya existe");
    }

    User user = new User();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());

    userRepository.save(user);
    return ResponseEntity.status(201).body(user);
}

    // POST /api/users/login – Iniciar sesión
    @PostMapping("/login")
public ResponseEntity<User> login(@RequestBody LoginDTO dto) {
    try {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Comparamos contraseñas en texto plano
        if (!dto.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return ResponseEntity.ok(user);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

    // POST /api/users – Crear un nuevo usuario (opcional)
    @PostMapping
public ResponseEntity<User> store(@RequestBody User user) {
    // Guardamos contraseña en texto plano
    User saved = userRepository.save(user);
    return ResponseEntity.status(201).body(saved);
}
}