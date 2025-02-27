package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void user를_정상적으로_조회한다() {
        // Given
        long userId = 1L;
        String email = "test@test.com";
        User user = new User(email,"password", UserRole.USER);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        UserResponse result = userService.getUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void user_조회_중_user를_찾지_못해_에러가_발생한다() {
        // Given
        long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidRequestException.class, () -> userService.getUser(userId));
    }



    @Test
    public void password를_정상적으로_변경한다(){
        // Given
        long userId = 1L;
        User user = new User("test@test.com", "encodedOldPassword123", UserRole.USER);
        UserChangePasswordRequest request = new UserChangePasswordRequest(
                "oldPassword123",
                "newPassword123"
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(request.getNewPassword())).willReturn("encodedNewPassword123");

        // When
        userService.changePassword(userId, request);

        // Then
        verify(userRepository, times(1)).findById(userId);
        assertEquals("encodedNewPassword123", user.getPassword());
    }

    @Test
    public void password_변경_중_기존_비밀번호와_같아_에러가_발생한다() {
        // Given
        long userId = 1L;
        User user = new User("test@test.com", "encodedOldPassword123", UserRole.USER);
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword123", "oldPassword123");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(true);

        // When & Then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, request));
    }

    @Test
    public void password_변경_중_현재_비밀번호가_일치하지_않아_에러가_발생한다() {
        // Given
        long userId = 1L;
        User user = new User("test@test.com", "encodedOldPassword123", UserRole.USER);
        UserChangePasswordRequest request = new UserChangePasswordRequest("wrongOldPassword123", "newPassword123");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(false);

        // When & Then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, request));
    }
}