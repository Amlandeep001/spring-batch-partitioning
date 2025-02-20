package com.spring.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "CUSTOMERS_INFO")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Customer
{
    @Id
    @Column(name = "CUSTOMER_ID")
    int id;

    String firstName;
    String lastName;
    String email;
    String gender;

    @Column(name = "CONTACT")
    String contactNo;

    String country;
    String dob;
}
