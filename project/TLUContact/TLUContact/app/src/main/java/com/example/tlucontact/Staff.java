package com.example.tlucontact;

public class Staff {
    private String id;
    private String name;
    private String position;
    private String department;
    private String phone;
    private String email;
    private String imageBase64;

    // Constructor mặc định cho Firestore
    public Staff() {
    }

    public Staff(String id, String name, String position, String department, String phone, String email, String imageBase64) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.department = department;
        this.phone = phone;
        this.email = email;
        this.imageBase64 = imageBase64;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}

