package com.continiousdisappointment.chat.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.continiousdisappointment.chat.service.ChatService;
import com.continiousdisappointment.chat.service.UserService;
import com.continiousdisappointment.chat.domain.chat.Chat;
import com.continiousdisappointment.chat.domain.chat.Message;
import com.continiousdisappointment.chat.domain.chat.Role;
import com.continiousdisappointment.chat.dto.AddMessageToChatDto;
import com.continiousdisappointment.chat.dto.CreateChatDto;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Chat>> getChats() {
        var user = userService.getUserInfo();
        return ResponseEntity.ok(chatService.getChatsOfUser(user.id()));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChat(@PathVariable String chatId) {
        var user = userService.getUserInfo();
        return ResponseEntity.ok(chatService.getChatById(user.id(), chatId));
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody CreateChatDto dto) {
        var user = userService.getUserInfo();
        return ResponseEntity.ok(chatService.createChat(user.id(), dto.title()));
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<Chat> updateChat(
            @PathVariable("chatId") String chatId,
            @RequestParam String title) {
        var user = userService.getUserInfo();
        return ResponseEntity.ok(chatService.updateChat(user.id(), chatId, title));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable("chatId") String chatId) {
        var user = userService.getUserInfo();
        chatService.deleteChat(user.id(), chatId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Message> addMessage(@PathVariable("chatId") String chatId,
            @RequestBody AddMessageToChatDto dto) {
        var user = userService.getUserInfo();
        return ResponseEntity.ok(chatService.addMessageToChat(user.id(), chatId, dto.content(), Role.USER));
    }
}
