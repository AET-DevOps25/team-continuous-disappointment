package com.continiousdisappointment.server.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.continiousdisappointment.server.service.ChatService;
import com.continiousdisappointment.server.domain.chat.Chat;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> getChats(@RequestParam("userId") int userId) {
        return ResponseEntity.ok(chatService.getChatsOfUser(userId));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChat(@RequestParam("userId") int userId, @PathVariable String chatId) {
        return ResponseEntity.ok(chatService.getChatById(userId, chatId));
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestParam("userId") int userId, @RequestParam String title) {
        return ResponseEntity.ok(chatService.createChat(userId, title));
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<Chat> updateChat(
            @RequestParam("userId") int userId,
            @PathVariable("chatId") String chatId,
            @RequestParam String title) {
        return ResponseEntity.ok(chatService.updateChat(userId, chatId, title));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@RequestParam("userId") int userId, @PathVariable("chatId") String chatId) {
        chatService.deleteChat(userId, chatId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Chat> addMessage(
            @RequestParam("userId") int userId,
            @PathVariable("chatId") String chatId,
            @RequestParam String content,
            @RequestParam String role) {
        return ResponseEntity.ok(chatService.addMessageToChat(userId, chatId, content, role));
    }
}
