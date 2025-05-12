package com.example.agendaapi.dto;

public class ContactDTO {

    private Long id; // ✅ Campo faltante
    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private String notes;
    private Long userId;

    // Constructor vacío
    public ContactDTO() {}

    // Constructor con todos los campos
    public ContactDTO(Long id, String first_name, String last_name, String email, String phone_number, String notes, Long userId) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.notes = notes;
        this.userId = userId;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}