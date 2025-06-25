package com.continiousdisappointment.chat.service;

import com.continiousdisappointment.chat.domain.chat.Chat;
import com.continiousdisappointment.chat.domain.chat.Message;
import com.continiousdisappointment.chat.domain.chat.Role;
import com.continiousdisappointment.chat.model.ChatModel;
import com.continiousdisappointment.chat.model.MessageModel;
import com.continiousdisappointment.chat.repository.ChatRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MeterRegistry meterRegistry;

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
        meterRegistry.counter("chats.created").increment();
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
        meterRegistry.counter("chats.deleted").increment();
    }

    public Message addMessageToChat(int userId, String chatId, String content, Role role) {
        var chatModel = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        assertChatBelongsToUser(chatModel, userId);

        var messageModel = new MessageModel(
                content,
                role);

        chatModel.getMessages().add(messageModel);
        chatRepository.save(chatModel);
        meterRegistry.counter("chats.messageCount", "chatId", chatId).increment();
        return Message.fromDom(messageModel);
    }

    private void assertChatBelongsToUser(ChatModel chatModel, int userId) {
        if (chatModel.getUserId() != userId) {
            throw new IllegalArgumentException("Chat does not belong to the user");
        }
    }

}
