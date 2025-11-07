package com.application.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.model.*;

public interface MessageRepository extends JpaRepository<Message, Long>{

    @Query("SELECT m FROM Message m WHERE " +
           "((m.senderUser.id = :userId AND m.receiverProfessor.id = :professorId) " +
           "OR (m.senderProfessor.id = :professorId AND m.receiverUser.id = :userId)) " +
           "ORDER BY m.timestamp ASC")
    List<Message> getCourseChatBetweenUserAndProfessor(@Param("userId") String userId,
                                                       @Param("professorId") String professorId);

    
}
