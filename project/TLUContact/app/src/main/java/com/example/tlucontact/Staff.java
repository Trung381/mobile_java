package com.example.tlucontact;

public class Staff {
    private String name;
    private String position;
    private String department;
    private String phone;
    private String email;
    private int imageResource;

    public Staff(String name, String position, String department, String phone, String email, int imageResource) {
        this.name = name;
        this.position = position;
        this.department = department;
        this.phone = phone;
        this.email = email;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getImageResource() {
        return imageResource;
    }
}

