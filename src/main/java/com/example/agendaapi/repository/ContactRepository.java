// src/main/java/com/example/agendaapi/repository/ContactRepository.java

package com.example.agendaapi.repository;

import com.example.agendaapi.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
