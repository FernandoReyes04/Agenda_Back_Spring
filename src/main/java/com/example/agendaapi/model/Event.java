package com.example.agendaapi.model;

import jakarta.persistence.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String date;
    private String hour;
    private String description;

    // 🔥 Relación con User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Event() {}

    public Event(String name, String date, String hour, String description) {
        this.name = name;
        this.date = date;
        this.hour = hour;
        this.description = description;
    }

    // ✅ Getters y Setters completos

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

    public void setDescription(String description) {
        this.description = description;
    }

    // ✅ Getter y setter para User
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}