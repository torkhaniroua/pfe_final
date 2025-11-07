package com.application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🔗 Relation avec Course
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "id") // correspond à la colonne dans MySQL
    @JsonBackReference
    private Course course;

    // 🔗 Relation avec Option
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    // 🔹 Constructeurs
    public Question() {
    }

    public Question(String content, LocalDateTime createdAt, Course course) {
        this.content = content;
        this.createdAt = createdAt;
        this.course = course;
    }
    public Question(Long id, String content, LocalDateTime createdAt, Course course, List<Option> options) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.course = course;
        this.options = options;
    }


    // 🔹 Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    // 🔹 Méthodes utilitaires
    public void addOption(Option option) {
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(Option option) {
        options.remove(option);
        option.setQuestion(null);
    }
}
