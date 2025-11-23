package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{

    @Query("SELECT m FROM Message m WHERE " +
           "((m.senderUser.email = :userId AND m.receiverProfessor.email = :professorId) " +
           "OR (m.senderProfessor.email = :professorId AND m.receiverUser.email = :userId)) " +
           "ORDER BY m.timestamp ASC")
    List<Message> getCourseChatBetweenUserAndProfessor(@Param("userId") String userId,
                                                       @Param("professorId") String professorId);

    @Query("SELECT m.senderUser.email AS userEmail, COUNT(m) AS unreadCount " +
           "FROM Message m " +
           "WHERE m.receiverProfessor.email = :professorId AND m.readByProfessor = false " +
           "GROUP BY m.senderUser.email")
    List<Object[]> getUnreadCountByUserForProfessor(@Param("professorId") String professorId);

    @Modifying
    @Query("UPDATE Message m SET m.readByProfessor = true " +
           "WHERE m.receiverProfessor.email = :professorId AND m.senderUser.email = :userId " +
           "AND m.readByProfessor = false")
    int markConversationAsReadByProfessor(@Param("userId") String userId,
                                          @Param("professorId") String professorId);

    @Modifying
    @Query("UPDATE Message m SET m.readByUser = true " +
           "WHERE m.receiverUser.email = :userId AND m.senderProfessor.email = :professorId " +
           "AND m.readByUser = false")
    int markConversationAsReadByUser(@Param("userId") String userId,
                                     @Param("professorId") String professorId);

    @Query("SELECT m.senderProfessor.email AS professorEmail, COUNT(m) AS unreadCount " +
           "FROM Message m " +
           "WHERE m.receiverUser.email = :userId AND m.readByUser = false " +
           "GROUP BY m.senderProfessor.email")
    List<Object[]> getUnreadCountByProfessorForUser(@Param("userId") String userId);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.senderUser.email = :email OR m.receiverUser.email = :email")
    int deleteAllUserMessages(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.senderProfessor.email = :email OR m.receiverProfessor.email = :email")
    int deleteAllProfessorMessages(@Param("email") String email);
}
