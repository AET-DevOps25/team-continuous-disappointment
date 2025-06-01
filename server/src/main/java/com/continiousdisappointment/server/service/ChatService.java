package com.continiousdisappointment.server.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.continiousdisappointment.server.domain.chat.Chat;
import com.continiousdisappointment.server.domain.chat.Role;
import com.continiousdisappointment.server.model.ChatModel;
import com.continiousdisappointment.server.model.MessageModel;
import com.continiousdisappointment.server.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    public final ChatRepository chatRepository;

    public List<Chat> getChatsOfUser(int userId) {
        var chats = chatRepository.findByUserId(userId);
        return chats.stream()
                .map(Chat::fromDom)
                .toList();
    }

    public Chat getChatById(int userId, String chatId) {
        var chatModel = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        assertChatBelongsToUser(chatModel, userId);
        return Chat.fromDom(chatModel);
    }

    public Chat createChat(int userId, String title) {
        var chatModel = new ChatModel(
                UUID.randomUUID(),
                userId,
                title);
        chatModel.setMessages(List.of());
        chatRepository.save(chatModel);
        return Chat.fromDom(chatModel);
    }

    public Chat updateChat(int userId, String chatId, String title) {
        var chatModel = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        assertChatBelongsToUser(chatModel, userId);
        chatModel.setTitle(title);
        chatRepository.save(chatModel);
        return Chat.fromDom(chatModel);
    }

    public void deleteChat(int userId, String chatId) {
        var chatModel = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        assertChatBelongsToUser(chatModel, userId);
        chatRepository.delete(chatModel);
    }

    public Chat addMessageToChat(int userId, String chatId, String content, String role) {
        var chatModel = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        assertChatBelongsToUser(chatModel, userId);

        var messageModel = new MessageModel(
                content,
                Role.valueOf(role.toUpperCase()));

        chatModel.getMessages().add(messageModel);
        chatRepository.save(chatModel);
        return Chat.fromDom(chatModel);
    }

    private void assertChatBelongsToUser(ChatModel chatModel, int userId) {
        if (chatModel.getUserId() != userId) {
            throw new IllegalArgumentException("Chat does not belong to the user");
        }
    }

}
