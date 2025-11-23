package com.application.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.model.Message;
import com.application.repository.MessageRepository;
import com.application.repository.ProfessorRepository;
import com.application.repository.UserRepository;

@Service
public class ChatService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessorRepository professorRepository;


    public void sendMessageFromUserToProfessor(String userId, String professorId, String content) {
        Message msg = new Message();
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());
        msg.setSenderUser(userRepository.findByEmail(userId));
        msg.setReceiverProfessor(professorRepository.findByEmail(professorId));
        msg.setReadByProfessor(false);
        msg.setReadByUser(true);
        messageRepository.save(msg);
    }

    public void sendMessageFromProfessorToUser(String professorId, String userId, String content) {
        Message msg = new Message();
        msg.setTimestamp(LocalDateTime.now());
        msg.setSenderProfessor(professorRepository.findByEmail(professorId));
        msg.setReceiverUser(userRepository.findByEmail(userId));
        msg.setContent(content);
        msg.setReadByProfessor(true);
        msg.setReadByUser(false);
        messageRepository.save(msg);
    }

    public List<Message> getCourseChat(String userId, String professorId) {
        return messageRepository.getCourseChatBetweenUserAndProfessor(userId, professorId);
    }

    public Map<String, Long> getUnreadCountsForProfessor(String professorId) {
        List<Object[]> rawCounts = messageRepository.getUnreadCountByUserForProfessor(professorId);
        Map<String, Long> counts = new HashMap<>();
        for (Object[] row : rawCounts) {
            String userEmail = (String) row[0];
            Long unread = row[1] instanceof Long ? (Long) row[1] : ((Number) row[1]).longValue();
            counts.put(userEmail, unread);
        }
        return counts;
    }

    @Transactional
    public void markConversationAsReadByProfessor(String userId, String professorId) {
        messageRepository.markConversationAsReadByProfessor(userId, professorId);
    }

    @Transactional
    public void markConversationAsReadByUser(String userId, String professorId) {
        messageRepository.markConversationAsReadByUser(userId, professorId);
    }

    public Map<String, Long> getUnreadCountsForUser(String userId) {
        List<Object[]> rawCounts = messageRepository.getUnreadCountByProfessorForUser(userId);
        Map<String, Long> counts = new HashMap<>();
        for (Object[] row : rawCounts) {
            String professorEmail = (String) row[0];
            Long unread = row[1] instanceof Long ? (Long) row[1] : ((Number) row[1]).longValue();
            counts.put(professorEmail, unread);
        }
        return counts;
    }
}
