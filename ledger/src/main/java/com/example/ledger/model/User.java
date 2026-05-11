package com.example.ledger.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String email;
    private String hashedPswd;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    // JPA compatibility
    protected User() {
    }

    public User(
        String name,
        String email) {
        this.name = name;
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPswd() {
        return hashedPswd;
    }

    public void setHashedPswd(String hashedPswd) {
        this.hashedPswd = hashedPswd;
    }
}
