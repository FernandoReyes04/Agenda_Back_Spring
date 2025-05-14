package com.example.agendaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

// Para que no aparezcan campos null en JSON de respuesta
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReminderDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    private String hour; // Campo para la hora del recordatorio

    private Long userId; // Relación con usuario logueado

    public ReminderDTO() {}

    public ReminderDTO(Long id, String name, String description, String email, LocalDate date, String hour) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.email = email;
        this.date = date;
        this.hour = hour;
    }

    // Getters y Setters normales

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}