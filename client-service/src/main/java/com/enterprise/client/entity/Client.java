package com.enterprise.client.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "clients",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_client_email",
                columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name", nullable = false, length = 150)
    private String clientName;

    @Column(nullable = false, length = 100)
    private String industry;

    @Column(name = "contact_person", nullable = false, length = 150)
    private String contactPerson;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(nullable = false, length = 100)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClientStatus status;
}
