package com.example.agendaapi.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
    this.user = user;
}

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone_number() { return phone_number; }
    public void setPhone_number(String phone_number) { this.phone_number = phone_number; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}