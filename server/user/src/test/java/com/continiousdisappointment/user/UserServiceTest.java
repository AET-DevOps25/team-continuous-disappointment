package com.continiousdisappointment.user;

import com.continiousdisappointment.user.domain.DietaryPreference;
import com.continiousdisappointment.user.domain.OAuthUser;
import com.continiousdisappointment.user.domain.User;
import com.continiousdisappointment.user.model.UserPreferencesModel;
import com.continiousdisappointment.user.repository.UserPreferencesRepository;
import com.continiousdisappointment.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserPreferencesRepository userPreferencesRepository;

    @InjectMocks
    private UserService userService;

    private static final String VALID_AUTHORIZATION = "Bearer valid-token";
    private static final String GITLAB_API_URL = "https://gitlab.lrz.de/api/v4/user";
    private static final Integer USER_ID = 123;
    private static final String USERNAME = "testuser";

    private OAuthUser mockOAuthUser;
    private UserPreferencesModel mockUserPreferences;
    private Set<DietaryPreference> testDietaryPreferences;

    @BeforeEach
    void setUp() {
        // Inject the mock RestTemplate into UserService
        ReflectionTestUtils.setField(userService, "restTemplate", restTemplate);

        mockOAuthUser = new OAuthUser(USERNAME, USER_ID);

        testDietaryPreferences = Set.of(
                DietaryPreference.VEGETARIAN,
                DietaryPreference.GLUTEN_FREE);

        mockUserPreferences = new UserPreferencesModel();
        mockUserPreferences.setId(UUID.randomUUID());
        mockUserPreferences.setUserId(USER_ID);
        mockUserPreferences.setDietaryPreferences(testDietaryPreferences);
    }

    @Test
    void getUserInfo_WhenValidAuthorization_ReturnsUserWithPreferences() {
        // Arrange
        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenReturn(new ResponseEntity<>(mockOAuthUser, HttpStatus.OK));

        when(userPreferencesRepository.findByUserId(USER_ID))
                .thenReturn(List.of(mockUserPreferences));

        // Act
        User result = userService.getUserInfo(VALID_AUTHORIZATION);

        // Assert
        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        assertEquals(USER_ID, result.getId());
        assertEquals(testDietaryPreferences, result.getDietaryPreferences());

        verify(userPreferencesRepository).findByUserId(USER_ID);
    }

    @Test
    void getUserInfo_WhenUserHasNoPreferences_ReturnsUserWithEmptyPreferences() {
        // Arrange
        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenReturn(new ResponseEntity<>(mockOAuthUser, HttpStatus.OK));

        when(userPreferencesRepository.findByUserId(USER_ID))
                .thenReturn(Collections.emptyList());

        // Act
        User result = userService.getUserInfo(VALID_AUTHORIZATION);

        // Assert
        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        assertEquals(USER_ID, result.getId());
        assertTrue(result.getDietaryPreferences().isEmpty());
    }

    @Test
    void getUserInfo_WhenNullAuthorization_ThrowsIllegalStateException() {
        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.getUserInfo(null));

        assertEquals("No access token found in security context", exception.getMessage());

        verifyNoInteractions(restTemplate);
        verifyNoInteractions(userPreferencesRepository);
    }

    @Test
    void getUserInfo_WhenRestTemplateThrowsException_PropagatesException() {
        // Arrange
        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenThrow(new RestClientException("API call failed"));

        // Act & Assert
        assertThrows(
                RestClientException.class,
                () -> userService.getUserInfo(VALID_AUTHORIZATION));

        verifyNoInteractions(userPreferencesRepository);
    }

    @Test
    void saveUserPreferences_WhenUserHasNoExistingPreferences_CreatesNewPreferences() {
        // Arrange
        Set<DietaryPreference> newPreferences = Set.of(DietaryPreference.VEGAN, DietaryPreference.NUT_FREE);

        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenReturn(new ResponseEntity<>(mockOAuthUser, HttpStatus.OK));

        when(userPreferencesRepository.findByUserId(USER_ID))
                .thenReturn(Collections.emptyList());

        // Act
        userService.saveUserPreferences(VALID_AUTHORIZATION, newPreferences);

        // Assert
        ArgumentCaptor<UserPreferencesModel> captor = ArgumentCaptor.forClass(UserPreferencesModel.class);
        verify(userPreferencesRepository).save(captor.capture());

        UserPreferencesModel savedModel = captor.getValue();
        assertEquals(USER_ID, savedModel.getUserId());
        assertEquals(newPreferences, savedModel.getDietaryPreferences());
        assertNotNull(savedModel.getId());
    }

    @Test
    void saveUserPreferences_WhenUserHasExistingPreferences_UpdatesExistingPreferences() {
        // Arrange
        Set<DietaryPreference> newPreferences = Set.of(DietaryPreference.VEGAN, DietaryPreference.DAIRY_FREE);

        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenReturn(new ResponseEntity<>(mockOAuthUser, HttpStatus.OK));

        when(userPreferencesRepository.findByUserId(USER_ID))
                .thenReturn(List.of(mockUserPreferences));

        // Act
        userService.saveUserPreferences(VALID_AUTHORIZATION, newPreferences);

        // Assert
        ArgumentCaptor<UserPreferencesModel> captor = ArgumentCaptor.forClass(UserPreferencesModel.class);
        verify(userPreferencesRepository).save(captor.capture());

        UserPreferencesModel savedModel = captor.getValue();
        assertEquals(mockUserPreferences.getId(), savedModel.getId());
        assertEquals(USER_ID, savedModel.getUserId());
        assertEquals(newPreferences, savedModel.getDietaryPreferences());
    }

    @Test
    void saveUserPreferences_WhenNullAuthorization_ThrowsIllegalStateException() {
        // Arrange
        Set<DietaryPreference> preferences = Set.of(DietaryPreference.VEGETARIAN);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.saveUserPreferences(null, preferences));

        assertEquals("No access token found in security context", exception.getMessage());

        verifyNoInteractions(userPreferencesRepository);
    }

    @Test
    void saveUserPreferences_WhenEmptyPreferences_SavesEmptySet() {
        // Arrange
        Set<DietaryPreference> emptyPreferences = Set.of();

        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenReturn(new ResponseEntity<>(mockOAuthUser, HttpStatus.OK));

        when(userPreferencesRepository.findByUserId(USER_ID))
                .thenReturn(Collections.emptyList());

        // Act
        userService.saveUserPreferences(VALID_AUTHORIZATION, emptyPreferences);

        // Assert
        ArgumentCaptor<UserPreferencesModel> captor = ArgumentCaptor.forClass(UserPreferencesModel.class);
        verify(userPreferencesRepository).save(captor.capture());

        UserPreferencesModel savedModel = captor.getValue();
        assertTrue(savedModel.getDietaryPreferences().isEmpty());
    }

    @Test
    void saveUserPreferences_WhenMultipleExistingPreferences_UpdatesFirstOne() {
        // Arrange
        Set<DietaryPreference> newPreferences = Set.of(DietaryPreference.SPICY_FOOD);

        UserPreferencesModel secondPreferences = new UserPreferencesModel();
        secondPreferences.setId(UUID.randomUUID());
        secondPreferences.setUserId(USER_ID);
        secondPreferences.setDietaryPreferences(Set.of(DietaryPreference.DAIRY_FREE));

        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenReturn(new ResponseEntity<>(mockOAuthUser, HttpStatus.OK));

        when(userPreferencesRepository.findByUserId(USER_ID))
                .thenReturn(List.of(mockUserPreferences, secondPreferences));

        // Act
        userService.saveUserPreferences(VALID_AUTHORIZATION, newPreferences);

        // Assert
        ArgumentCaptor<UserPreferencesModel> captor = ArgumentCaptor.forClass(UserPreferencesModel.class);
        verify(userPreferencesRepository).save(captor.capture());

        UserPreferencesModel savedModel = captor.getValue();
        assertEquals(mockUserPreferences.getId(), savedModel.getId()); // Should update the first one
        assertEquals(newPreferences, savedModel.getDietaryPreferences());
    }

    @Test
    void getUserPreferences_WhenRepositoryReturnsMultipleResults_ReturnsFirstResult() {
        // Arrange
        UserPreferencesModel firstPrefs = new UserPreferencesModel();
        firstPrefs.setDietaryPreferences(Set.of(DietaryPreference.VEGETARIAN));

        UserPreferencesModel secondPrefs = new UserPreferencesModel();
        secondPrefs.setDietaryPreferences(Set.of(DietaryPreference.VEGAN));

        when(restTemplate.exchange(
                eq(GITLAB_API_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(OAuthUser.class))).thenReturn(new ResponseEntity<>(mockOAuthUser, HttpStatus.OK));

        when(userPreferencesRepository.findByUserId(USER_ID))
                .thenReturn(List.of(firstPrefs, secondPrefs));

        // Act
        User result = userService.getUserInfo(VALID_AUTHORIZATION);

        // Assert
        assertEquals(Set.of(DietaryPreference.VEGETARIAN), result.getDietaryPreferences());
    }
}