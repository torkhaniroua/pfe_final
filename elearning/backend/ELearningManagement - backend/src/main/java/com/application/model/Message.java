package com.application.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "sender_professor_id")
    private Professor senderProfessor;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private User receiverUser;

    @ManyToOne
    @JoinColumn(name = "receiver_professor_id")
    private Professor receiverProfessor;

    @Column(nullable = false)
    private boolean readByProfessor = false;

    @Column(nullable = false)
    private boolean readByUser = false;
}
