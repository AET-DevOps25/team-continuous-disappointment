package com.continiousdisappointment.chat;

import com.continiousdisappointment.chat.domain.DietaryPreference;
import com.continiousdisappointment.chat.domain.User;
import com.continiousdisappointment.chat.domain.chat.Chat;
import com.continiousdisappointment.chat.domain.chat.GenAiMessage;
import com.continiousdisappointment.chat.domain.chat.Message;
import com.continiousdisappointment.chat.domain.chat.Role;
import com.continiousdisappointment.chat.model.ChatModel;
import com.continiousdisappointment.chat.model.MessageModel;
import com.continiousdisappointment.chat.repository.ChatRepository;
import com.continiousdisappointment.chat.service.ChatService;
import com.continiousdisappointment.chat.service.GenAiService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private GenAiService genAiService;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @InjectMocks
    private ChatService chatService;

    private final UUID chatId = UUID.randomUUID();
    private final int userId = 123;
    private final int otherUserId = 456;
    private final String chatTitle = "Test Chat";
    private final String messageContent = "Hello, world!";
    private ChatModel testChatModel;
    private User testUser;

    @BeforeEach
    void setUp() {
        testChatModel = new ChatModel(chatId, userId, chatTitle);
        testChatModel.setCreatedAt(Instant.now());
        testChatModel.setUpdatedAt(Instant.now());
        testChatModel.setMessages(new ArrayList<>());

        testUser = new User(userId, "testuser", Set.of(DietaryPreference.VEGETARIAN, DietaryPreference.GLUTEN_FREE));
    }

    @Test
    void getChatsOfUser_WhenChatsExist_ReturnsListOfChats() {
        // Given
        List<ChatModel> chatModels = Arrays.asList(testChatModel);
        when(chatRepository.findByUserId(userId)).thenReturn(chatModels);

        // When
        List<Chat> result = chatService.getChatsOfUser(userId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(chatId);
        assertThat(result.get(0).userId()).isEqualTo(userId);
        assertThat(result.get(0).title()).isEqualTo(chatTitle);
        verify(chatRepository).findByUserId(userId);
    }

    @Test
    void getChatsOfUser_WhenNoChatsExist_ReturnsEmptyList() {
        // Given
        when(chatRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<Chat> result = chatService.getChatsOfUser(userId);

        // Then
        assertThat(result).isEmpty();
        verify(chatRepository).findByUserId(userId);
    }

    @Test
    void getChatById_WhenChatExistsAndBelongsToUser_ReturnsChat() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));

        // When
        Chat result = chatService.getChatById(userId, chatId.toString());

        // Then
        assertThat(result.id()).isEqualTo(chatId);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.title()).isEqualTo(chatTitle);
        verify(chatRepository).findById(chatId);
    }

    @Test
    void getChatById_WhenChatNotFound_ThrowsIllegalArgumentException() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> chatService.getChatById(userId, chatId.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat not found");
        verify(chatRepository).findById(chatId);
    }

    @Test
    void getChatById_WhenChatDoesNotBelongToUser_ThrowsIllegalArgumentException() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));

        // When & Then
        assertThatThrownBy(() -> chatService.getChatById(otherUserId, chatId.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat does not belong to the user");
        verify(chatRepository).findById(chatId);
    }

    @Test
    void createChat_WhenValidInput_CreatesAndReturnsChat() {
        // Given
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        ArgumentCaptor<ChatModel> chatModelCaptor = ArgumentCaptor.forClass(ChatModel.class);

        // When
        Chat result = chatService.createChat(userId, chatTitle);

        // Then
        verify(chatRepository).save(chatModelCaptor.capture());
        verify(counter).increment();

        ChatModel savedChatModel = chatModelCaptor.getValue();
        assertThat(savedChatModel.getUserId()).isEqualTo(userId);
        assertThat(savedChatModel.getTitle()).isEqualTo(chatTitle);
        assertThat(savedChatModel.getId()).isNotNull();
        assertThat(savedChatModel.getMessages()).isEmpty();

        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.title()).isEqualTo(chatTitle);
        assertThat(result.id()).isNotNull();
    }

    @Test
    void updateChat_WhenChatExistsAndBelongsToUser_UpdatesAndReturnsChat() {
        // Given
        String newTitle = "Updated Title";
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));

        // When
        Chat result = chatService.updateChat(userId, chatId.toString(), newTitle);

        // Then
        verify(chatRepository).save(testChatModel);
        assertThat(testChatModel.getTitle()).isEqualTo(newTitle);
        assertThat(result.title()).isEqualTo(newTitle);
    }

    @Test
    void updateChat_WhenChatNotFound_ThrowsIllegalArgumentException() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> chatService.updateChat(userId, chatId.toString(), "New Title"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat not found");
    }

    @Test
    void updateChat_WhenChatDoesNotBelongToUser_ThrowsIllegalArgumentException() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));

        // When & Then
        assertThatThrownBy(() -> chatService.updateChat(otherUserId, chatId.toString(), "New Title"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat does not belong to the user");
    }

    @Test
    void deleteChat_WhenChatExistsAndBelongsToUser_DeletesChat() {
        // Given
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));

        // When
        chatService.deleteChat(userId, chatId.toString());

        // Then
        verify(chatRepository).delete(testChatModel);
        verify(counter).increment();
    }

    @Test
    void deleteChat_WhenChatNotFound_ThrowsIllegalArgumentException() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> chatService.deleteChat(userId, chatId.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat not found");
    }

    @Test
    void deleteChat_WhenChatDoesNotBelongToUser_ThrowsIllegalArgumentException() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));

        // When & Then
        assertThatThrownBy(() -> chatService.deleteChat(otherUserId, chatId.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat does not belong to the user");
    }

    @Test
    void addMessageToChat_WhenValidInputAndAiResponds_AddsUserAndAssistantMessages() {
        // Given
        when(meterRegistry.counter(anyString(), anyString(), anyString())).thenReturn(counter);
        String aiResponse = "AI generated response";
        MessageModel existingMessage = new MessageModel("Previous message", Role.USER);
        testChatModel.getMessages().add(existingMessage);

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));
        when(genAiService.generateAssistantReply(anyString(), anyList(), anyInt())).thenReturn(aiResponse);

        // When
        Message result = chatService.addMessageToChat(testUser, chatId.toString(), messageContent);

        // Then
        verify(chatRepository).save(testChatModel);
        verify(counter, times(2)).increment(); // Two messages added

        assertThat(testChatModel.getMessages()).hasSize(3); // Previous + user + assistant
        assertThat(testChatModel.getMessages().get(1).getContent()).isEqualTo(messageContent);
        assertThat(testChatModel.getMessages().get(1).getRole()).isEqualTo(Role.USER);
        assertThat(testChatModel.getMessages().get(2).getContent()).isEqualTo(aiResponse);
        assertThat(testChatModel.getMessages().get(2).getRole()).isEqualTo(Role.ASSISTANT);

        assertThat(result.content()).isEqualTo(aiResponse);
        assertThat(result.role()).isEqualTo(Role.ASSISTANT);

        // Verify AI service was called with dietary preferences appended
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<GenAiMessage>> messagesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(genAiService).generateAssistantReply(queryCaptor.capture(), messagesCaptor.capture(), idCaptor.capture());

        String capturedQuery = queryCaptor.getValue();
        assertThat(capturedQuery).contains(messageContent);
        assertThat(capturedQuery).contains("My dietary preferences are:");
        assertThat(capturedQuery).contains(testUser.dietaryPreferences().toString());

        List<GenAiMessage> capturedMessages = messagesCaptor.getValue();
        assertThat(capturedMessages).hasSize(1);
        assertThat(capturedMessages.get(0).content()).isEqualTo("Previous message");
    }

    @Test
    void addMessageToChat_WhenAiResponseIsBlank_AddsOnlyUserMessage() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));
        when(genAiService.generateAssistantReply(anyString(), anyList(), anyInt())).thenReturn("");

        // When
        Message result = chatService.addMessageToChat(testUser, chatId.toString(), messageContent);

        // Then
        verify(chatRepository, never()).save(any());
        assertThat(result.content()).isEqualTo(messageContent);
        assertThat(result.role()).isEqualTo(Role.USER);
    }

    @Test
    void addMessageToChat_WhenChatNotFound_ThrowsIllegalArgumentException() {
        // Given
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> chatService.addMessageToChat(testUser, chatId.toString(), messageContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat not found");
    }

    @Test
    void addMessageToChat_WhenChatDoesNotBelongToUser_ThrowsIllegalArgumentException() {
        // Given
        User otherUser = new User(otherUserId, "otheruser", Set.of(DietaryPreference.VEGAN));
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));

        // When & Then
        assertThatThrownBy(() -> chatService.addMessageToChat(otherUser, chatId.toString(), messageContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat does not belong to the user");
    }

    @Test
    void addMessageToChat_WhenChatHasNoExistingMessages_WorksCorrectly() {
        // Given
        String aiResponse = "First AI response";
        when(meterRegistry.counter(anyString(), anyString(), anyString())).thenReturn(counter);
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));
        when(genAiService.generateAssistantReply(anyString(), anyList(), anyInt())).thenReturn(aiResponse);

        // When
        Message result = chatService.addMessageToChat(testUser, chatId.toString(), messageContent);

        // Then
        ArgumentCaptor<List<GenAiMessage>> messagesCaptor = ArgumentCaptor.forClass(List.class);
        verify(genAiService).generateAssistantReply(anyString(), messagesCaptor.capture(), anyInt());

        List<GenAiMessage> capturedMessages = messagesCaptor.getValue();
        assertThat(capturedMessages).isEmpty();

        assertThat(testChatModel.getMessages()).hasSize(2);
        assertThat(result.content()).isEqualTo(aiResponse);
        assertThat(result.role()).isEqualTo(Role.ASSISTANT);
    }

    @Test
    void addMessageToChat_WhenUserHasNoDietaryPreferences_AppendsEmptySet() {
        // Given
        User userWithNoPreferences = new User(userId, "testuser", Set.of());
        String aiResponse = "AI response";
        when(meterRegistry.counter(anyString(), anyString(), anyString())).thenReturn(counter);
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(testChatModel));
        when(genAiService.generateAssistantReply(anyString(), anyList(), anyInt())).thenReturn(aiResponse);

        // When
        chatService.addMessageToChat(userWithNoPreferences, chatId.toString(), messageContent);

        // Then
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(genAiService).generateAssistantReply(queryCaptor.capture(), anyList(), anyInt());

        String capturedQuery = queryCaptor.getValue();
        assertThat(capturedQuery).contains("My dietary preferences are: []");
    }
}