package com.example.agendaapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EventDTO {

    private Long id;

    @JsonProperty("userId")
    private Long userId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La fecha es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "El formato debe ser YYYY-MM-DD")
    private String date;

    @NotBlank(message = "La hora es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "El formato debe ser HH:mm (ej: 10:00)")
    private String hour;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    // Constructor vacío
    public EventDTO() {}

    // Constructor completo
    public EventDTO(Long id, String name, String date, String hour, String description, Long userId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.hour = hour;
        this.description = description;
        setUserId(userId);
    }

    // Getters y Setters

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descripcionNueva) {
        this.description = descripcionNueva;
    }

    public Long getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId es obligatorio y debe ser válido");
        }
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}