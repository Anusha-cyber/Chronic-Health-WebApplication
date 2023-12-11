package com.smqa.smqa.user;

import com.smqa.smqa.JwtTokenService.JwtTokenProvider;
import com.smqa.smqa.datatypes.Role;
import com.smqa.smqa.requests.UserDetailsDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void registerUser() {
        String email = "test@example.com";
        String name = "John Doe";
        String address = "123 Main St";
        String phone = "1234567890";
        String password = "password123";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        Role role = Role.USER;

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");
        userService.registerUser(email, name, address, phone, password, dob, role);
        verify(userRepository).save(Mockito.any(User.class));
    }



    @Test
    void loginUser() {
        // Mock user data
        String email = "test@example.com";
        String name = "John Doe";
        String address = "123 Main St";
        String phone = "1234567890";
        String password = "password123";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        Role role = Role.USER;
        User user = new User(email, name,address,phone,passwordEncoder.encode(password), dob,role);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(email, user.getRole())).thenReturn("mockedJwtToken");


        String jwtToken = userService.loginUser(email, password);
        verify(jwtTokenProvider).generateToken(email, user.getRole());
        assertNotNull(jwtToken);

    }


    @Test
    void getUserDetailsByEmailSuccess() {
        String email = "test@example.com";
        String name = "John Doe";
        String address = "123 Main St";
        String phone = "555-1234";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        Role role = Role.USER;
        User user = new User(email, name, address, phone, "hashedPassword", dob, role);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetailsDto userDetailsDto = userService.getUserDetailsByEmail(email);
        verify(userRepository).findByEmail(email);
        assertEquals(email, userDetailsDto.getEmail());
        assertEquals(name, userDetailsDto.getName());
        assertEquals(address, userDetailsDto.getAddress());
        assertEquals(phone, userDetailsDto.getPhone());
        assertEquals(dob, userDetailsDto.getDob());
        assertEquals(role, userDetailsDto.getRole());
    }

    @Test
    void getUserDetailsByEmailFailure() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserDetailsByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void updateUserDetails_UserDoesNotExist_ShouldThrowRuntimeException() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUserDetails(email, "456 New St", "555-5678", LocalDate.of(1995, 5, 5)));
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_ValidCurrentPassword_NotSave() {
        // Mock user data
        String email = "test@example.com";
        String currentPassword = "newPassword1";
        String newPassword = "newPassword";
        User user = new User(email, "John Doe", "123 Main St", "555-1234", passwordEncoder.encode(currentPassword), LocalDate.of(1990, 1, 1), Role.USER);
        assertThrows(RuntimeException.class, () -> userService.changePassword(email, currentPassword, newPassword));
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any());
    }
    @Test
    void changePassword_InvalidNewPassword() {
        // Mock user data
        String email = "test@example.com";
        String currentPassword = "currentPassword";
        String invalidNewPassword = "weak";
        User user = new User(email, "John Doe", "123 Main St", "555-1234", passwordEncoder.encode(currentPassword), LocalDate.of(1990, 1, 1), Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> userService.changePassword(email, currentPassword, invalidNewPassword));
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any());
    }



    @Test
    void loginUser_InvalidEmail() {
        String invalidEmail = "invalid@example.com";
        String password = "password123";
        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.loginUser(invalidEmail, password));
        verify(userRepository).findByEmail(invalidEmail);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtTokenProvider, never()).generateToken(any(), any());
    }

    @Test
    void loginUser_InvalidPassword() {
        String email = "test@example.com";
        String invalidPassword = "wrongPassword";
        User user = new User(email, "John Doe", "123 Main St", "555-1234", passwordEncoder.encode("correctPassword"), LocalDate.of(1990, 1, 1), Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(invalidPassword, user.getPassword())).thenReturn(false);
        assertThrows(RuntimeException.class, () -> userService.loginUser(email, invalidPassword));
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(invalidPassword, user.getPassword());
        verify(jwtTokenProvider, never()).generateToken(any(), any());
    }

    @Test
    void registerUser_DuplicateEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        assertThrows(RuntimeException.class, () -> userService.registerUser("test@example.com", "John Doe", "123 Main St", "1234567890", "password123", LocalDate.of(1990, 1, 1), Role.USER));
        verify(userRepository).findByEmail("test@example.com");
    }



    @Test
    void registerUser_InvalidDateOfBirth() {
        assertThrows(RuntimeException.class, () -> userService.registerUser("test@example.com", "John Doe", "123 Main St", "1234567890", "password123", LocalDate.of(2100, 1, 1), Role.USER));
    }


}