package com.continiousdisappointment.chat.controller;

import com.continiousdisappointment.chat.domain.User;
import com.continiousdisappointment.chat.domain.chat.Chat;
import com.continiousdisappointment.chat.domain.chat.Message;
import com.continiousdisappointment.chat.domain.chat.Role;
import com.continiousdisappointment.chat.dto.AddMessageToChatDto;
import com.continiousdisappointment.chat.dto.CreateChatDto;
import com.continiousdisappointment.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "Chat Controller", description = "Manages chat operations, requires bearer authorization")
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> getChats(@RequestAttribute("user") User user) {
        return ResponseEntity.ok(chatService.getChatsOfUser(user.id()));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChat(@PathVariable String chatId, @RequestAttribute("user") User user) {
        return ResponseEntity.ok(chatService.getChatById(user.id(), chatId));
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody CreateChatDto dto, @RequestAttribute("user") User user) {
        return ResponseEntity.ok(chatService.createChat(user.id(), dto.title()));
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<Chat> updateChat(
            @PathVariable("chatId") String chatId,
            @RequestParam String title,
            @RequestAttribute("user") User user) {
        return ResponseEntity.ok(chatService.updateChat(user.id(), chatId, title));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable("chatId") String chatId, @RequestAttribute("user") User user) {
        chatService.deleteChat(user.id(), chatId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Message> addMessage(
            @PathVariable("chatId") String chatId,
            @RequestBody AddMessageToChatDto dto,
            @RequestAttribute("user") User user) {
        return ResponseEntity.ok(chatService.addMessageToChat(user.id(), chatId, dto.content(), Role.USER));
    }
}
