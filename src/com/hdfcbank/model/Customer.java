package com.hdfcbank.model;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private LocalDate dob;

    public Customer(String id, String name, String email, String phone, LocalDate dob) {
        this.customerId = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return customerId + " - " + name + " (" + email + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer cust = (Customer) o;
        return Objects.equals(customerId, cust.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
