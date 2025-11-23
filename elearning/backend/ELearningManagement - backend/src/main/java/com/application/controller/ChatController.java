package com.application.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.Message;
import com.application.services.ChatService;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/user/send")
    public ResponseEntity<Map<String, String>> sendMessageFromUser(
            @RequestParam String userId,
            @RequestParam String professorId,
            @RequestParam String content) {

        chatService.sendMessageFromUserToProfessor(userId, professorId, content);
        return ResponseEntity.ok(Map.of("message", "Message sent from user to professor."));
    }

    @PostMapping("/professor/send")
    public ResponseEntity<Map<String, String>> sendMessageFromProfessor(
            @RequestParam String professorId,
            @RequestParam String userId,
            @RequestParam String content) {

        chatService.sendMessageFromProfessorToUser(professorId, userId, content);
        return ResponseEntity.ok(Map.of("message", "Message sent from professor to user."));
    }

    @GetMapping
    public ResponseEntity<List<Message>> getCourseChat(
            @RequestParam String userId,
            @RequestParam String professorId) {

        List<Message> messages = chatService.getCourseChat(userId, professorId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/professor/unread")
    public ResponseEntity<Map<String, Long>> getProfessorUnreadCounts(@RequestParam String professorId) {
        return ResponseEntity.ok(chatService.getUnreadCountsForProfessor(professorId));
    }

    @PostMapping("/professor/read")
    public ResponseEntity<Map<String, String>> markProfessorConversationAsRead(
            @RequestParam String professorId,
            @RequestParam String userId) {

        chatService.markConversationAsReadByProfessor(userId, professorId);
        return ResponseEntity.ok(Map.of("message", "Conversation marked as read for professor."));
    }

    @PostMapping("/user/read")
    public ResponseEntity<Map<String, String>> markUserConversationAsRead(
            @RequestParam String userId,
            @RequestParam String professorId) {

        chatService.markConversationAsReadByUser(userId, professorId);
        return ResponseEntity.ok(Map.of("message", "Conversation marked as read for user."));
    }

    @GetMapping("/user/unread")
    public ResponseEntity<Map<String, Long>> getUserUnreadCounts(@RequestParam String userId) {
        return ResponseEntity.ok(chatService.getUnreadCountsForUser(userId));
    }
}
