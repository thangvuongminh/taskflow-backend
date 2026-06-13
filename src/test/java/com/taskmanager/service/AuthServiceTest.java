package com.taskmanager.service;

import com.taskmanager.dto.request.RegisterRequest;
import com.taskmanager.entity.User;
import com.taskmanager.exception.ConflictException;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenProvider jwtTokenProvider;
    @InjectMocks AuthService authService;

    @Test
    void register_duplicateEmail_throwsConflict() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);
        RegisterRequest req = new RegisterRequest("user", "test@test.com", "password");
        assertThatThrownBy(() -> authService.register(req))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("Email");
    }

    @Test
    void register_success_returnsAuthResponse() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        User saved = User.builder().id(1L).username("user").email("test@test.com").password("hashed").build();
        when(userRepository.save(any())).thenReturn(saved);
        when(jwtTokenProvider.generateToken(1L, "test@test.com")).thenReturn("token123");

        RegisterRequest req = new RegisterRequest("user", "test@test.com", "password");
        var response = authService.register(req);

        assertThat(response.token()).isEqualTo("token123");
        assertThat(response.user().email()).isEqualTo("test@test.com");
    }
}
