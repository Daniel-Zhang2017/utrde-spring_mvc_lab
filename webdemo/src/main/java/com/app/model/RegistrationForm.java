package com.app.webdemo.model;

import jakarta.validation.constraints.*;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class RegistrationForm {

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Country is required")
    private String country;

    @Past(message = "Date of Birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Specify the date format
    private Date dob;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // New field for annual income
    @NotNull(message = "Annual income is required")
    @Min(value = 0, message = "Annual income must be positive")
    @Max(value = 10000000, message = "Annual income cannot exceed 10,000,000")
    private Double annualIncome;

    // Constructors
    public RegistrationForm() {
    }

    public RegistrationForm(String firstName, String lastName, String country, 
                          Date dob, String email, Double annualIncome) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.dob = dob;
        this.email = email;
        this.annualIncome = annualIncome;
    }

    // Getters and Setters
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }
}