package com.example.auctionapplication;

public class User {
    private int id;
    //private String country;
    private String firstName;
    private String lastName;
//    private String address;
//    private String city;
//    private double zipCode;
//    private double phoneNumber;
    private String email;
    private String password;

    public User() {
    }

    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public double getZipCode() {
//        return zipCode;
//    }
//
//    public void setZipCode(double zipCode) {
//        this.zipCode = zipCode;
//    }
//
//    public double getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(double phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
