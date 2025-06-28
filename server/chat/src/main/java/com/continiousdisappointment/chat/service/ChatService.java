package com.continiousdisappointment.chat.service;
import java.util.List;
import java.util.UUID;
import com.continiousdisappointment.chat.domain.chat.GenAiMessage;
import org.springframework.stereotype.Service;
import com.continiousdisappointment.chat.domain.chat.Chat;
import com.continiousdisappointment.chat.domain.chat.Message;
import com.continiousdisappointment.chat.domain.chat.Role;
import com.continiousdisappointment.chat.model.ChatModel;
import com.continiousdisappointment.chat.model.MessageModel;
import com.continiousdisappointment.chat.repository.ChatRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {
    public final ChatRepository chatRepository;
    public final GenAiService genAiService;
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
        log.info("New chat created for user {} with title '{}'", userId, title);
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

        var userMessageModel = new MessageModel(
                content,
                role);

        List<GenAiMessage> previousMessages = chatModel.getMessages().stream()
                .map(m -> new GenAiMessage(m.getRole(), m.getContent()))
                .toList();

        String assistantReply = genAiService.generateAssistantReply(userMessageModel.getContent(), previousMessages);

        if (assistantReply.isBlank()) {
            return Message.fromDom(userMessageModel);
        }
        var assistantMessageModel = new MessageModel(
                assistantReply,
                Role.ASSISTANT
        );
        chatModel.getMessages().add(userMessageModel);
        meterRegistry.counter("chats.messageCount", "chatId", chatId).increment();
        chatModel.getMessages().add(assistantMessageModel);
        meterRegistry.counter("chats.messageCount", "chatId", chatId).increment();

        chatRepository.save(chatModel);
        return Message.fromDom(assistantMessageModel);
    }

    private void assertChatBelongsToUser(ChatModel chatModel, int userId) {
        if (chatModel.getUserId() != userId) {
            throw new IllegalArgumentException("Chat does not belong to the user");
        }
    }

}
