package com.example.bluegreen.Domain;
public class User {
    private String Id;
    private String Email;
    private String Password;
    private String Name;
    private String Address;
    private String Phone;

    private boolean IsAdmin;

    // Constructors
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email, String password, String name, String address, String phone, boolean isAdmin) {
        this.Id = id;
        this.Email = email;
        this.Password = password;
        this.Name = name;
        this.Address = address;
        this.Phone = phone;
        this.IsAdmin = isAdmin;
    }

    // Getters and setters
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }
}
