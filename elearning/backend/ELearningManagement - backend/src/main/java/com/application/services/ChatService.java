package com.application.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        var user = userRepository.findByEmail(userId);
        var prof = professorRepository.findByEmail(professorId);
        if (user == null || prof == null) {
            throw new IllegalArgumentException("User or professor not found");
        }
        Message msg = new Message();
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());
        msg.setReadByUser(false);
        msg.setReadByProfessor(false);
        msg.setSenderUser(user);
        msg.setReceiverProfessor(prof);
        messageRepository.save(msg);
    }

    public void sendMessageFromProfessorToUser(String professorId, String userId, String content) {
        var prof = professorRepository.findByEmail(professorId);
        var user = userRepository.findByEmail(userId);
        if (user == null || prof == null) {
            throw new IllegalArgumentException("User or professor not found");
        }
        Message msg = new Message();
        msg.setTimestamp(LocalDateTime.now());
        msg.setSenderProfessor(prof);
        msg.setReceiverUser(user);
        msg.setContent(content);
        msg.setReadByUser(false);
        msg.setReadByProfessor(false);
        messageRepository.save(msg);
    }

    public List<Message> getCourseChat(String userId, String professorId) {
        return messageRepository.getCourseChatBetweenUserAndProfessor(userId, professorId);
    }
}
