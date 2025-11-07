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
        Message msg = new Message();
        msg.setContent(content);
        msg.setTimestamp(LocalDateTime.now());
        msg.setSenderUser(userRepository.findByEmail(userId));
        msg.setReceiverProfessor(professorRepository.findByEmail(professorId));
        messageRepository.save(msg);
    }

    public void sendMessageFromProfessorToUser(String professorId, String userId, String content) {
        Message msg = new Message();
        msg.setTimestamp(LocalDateTime.now());
        msg.setSenderProfessor(professorRepository.findByEmail(professorId));
        msg.setReceiverUser(userRepository.findByEmail(userId));
        msg.setContent(content);
        messageRepository.save(msg);
    }

    public List<Message> getCourseChat(String userId, String professorId) {
        return messageRepository.getCourseChatBetweenUserAndProfessor(userId, professorId);
    }
}
