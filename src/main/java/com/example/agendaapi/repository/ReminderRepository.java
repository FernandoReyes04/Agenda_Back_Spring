package com.example.agendaapi.repository;

import com.example.agendaapi.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    // Método para buscar recordatorios con fecha menor a X
    List<Reminder> findByDateBefore(LocalDate date);
}