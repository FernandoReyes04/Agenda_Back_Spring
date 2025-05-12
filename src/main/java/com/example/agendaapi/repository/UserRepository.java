package com.example.agendaapi.repository;

import com.example.agendaapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     // ✅ Método para buscar usuario por correo electrónico
    Optional<User> findByEmail(String email);

    // Si también quieres buscar por username:
    Optional<User> findByUsername(String username);
}