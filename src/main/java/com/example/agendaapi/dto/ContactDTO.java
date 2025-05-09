package com.example.agendaapi.dto;

public class ContactDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String notes;

    // Constructor vacío (necesario para Jackson)
    public ContactDTO() {}

    // Constructor con todos los campos
    public ContactDTO(Long id, String name, String email, String phone, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.notes = notes;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}