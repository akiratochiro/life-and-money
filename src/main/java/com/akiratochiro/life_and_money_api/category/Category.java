package com.akiratochiro.life_and_money_api.category;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table (name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String name;
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    private boolean archived;
    private Instant createdAt;


    protected Category(){}

    public Category(Long userId, String name, CategoryType type) {
        this.userId = userId;
        this.name = normalizeName(name);
        this.type = type;
        this.archived = false;
        this.createdAt = Instant.now();
    }

    public boolean isArchived() {
        return archived;
    }

    public Long getId(){
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName(){
        return name;
    }

    public CategoryType getType(){
        return type;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

    public void rename(String newName) {
        this.name = normalizeName(newName);
    }

    public void archive(){
        archived = true;
    }

    static String normalizeName(String name) {
        return name.strip();
    }
}
