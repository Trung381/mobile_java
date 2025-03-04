package com.example.tlucontact;

public class Department {
    private String name;
    private String phone;
    private String address;
    private String email;
    private int imageResource;

    public Department(String name, String phone, String address, String email, int imageResource) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getImageResource() {
        return imageResource;
    }
}

