package com.application.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;
import java.util.List;
import com.application.model.*;

public interface MessageRepository extends JpaRepository<Message, Long>{

    @Query("SELECT m FROM Message m WHERE " +
           "((m.senderUser.email = :userEmail AND m.receiverProfessor.email = :profEmail) " +
           "OR (m.senderProfessor.email = :profEmail AND m.receiverUser.email = :userEmail)) " +
           "ORDER BY m.timestamp ASC")
    List<Message> getCourseChatBetweenUserAndProfessor(@Param("userEmail") String userEmail,
                                                       @Param("profEmail") String profEmail);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM message WHERE sender_user_id = :userId OR receiver_user_id = :userId OR sender_professor_id = :userId OR receiver_professor_id = :userId", nativeQuery = true)
    void deleteByUserOrProfessor(@Param("userId") String userId);
}
