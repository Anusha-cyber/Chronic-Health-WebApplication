package com.smqa.smqa.user;

import com.smqa.smqa.JwtTokenService.JwtTokenProvider;
import com.smqa.smqa.datatypes.Role;
import com.smqa.smqa.requests.UserDetailsDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private  final JwtTokenProvider jwtTokenProvider;
    // Inject UserRepository and PasswordEncoder through constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void registerUser(String email, String name, String address, String phone, String password, LocalDate dob, Role role) {
        try {
            if (email == null || name == null || address == null || phone == null || password == null || dob == null || role == null) {
                throw new IllegalArgumentException("Parameters cannot be null");
            }
            if (userRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("User with this email already exists");
            }
            if (password.length() < 8) {
                throw new RuntimeException("Weak password. Password must be at least 8 characters long");
            }
            if (dob.isAfter(LocalDate.now())) {
                throw new RuntimeException("Invalid date of birth");
            }

            String encodedPassword = passwordEncoder.encode(password);
            User newUser = new User(email, name, address, phone, encodedPassword, dob, role);

            userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Error during user registration" + e.getMessage());
        }
    }


    public String loginUser(String email, String password) {
        if (email == null || password == null || password.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {

                return jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
            } else {
                throw new RuntimeException("Incorrect password");
            }
        } else {
            // User not found
            throw new RuntimeException("User not found for email");
        }
    }

    public UserDetailsDto getUserDetailsByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDetailsDto(user.getEmail(), user.getName(), user.getAddress(), user.getPhone(), user.getDob(), user.getRole());
        } else {
            // Handle the case where the user is not found
            throw new RuntimeException("User not found for email: " + email);
        }
    }

    public List<UserDetailsDto> getAllUserDetails() {
        List<User> userList = userRepository.findAll();

        // Convert the list of users to a list of UserDetailsDto
        return userList.stream()
                .map(user -> new UserDetailsDto(
                        user.getEmail(),
                        user.getName(),
                        user.getAddress(),
                        user.getPhone(),
                        user.getDob(),
                        user.getRole()))
                .collect(Collectors.toList());
    }

    @Transactional
    public String updateUserDetails(String email, String address, String phone, LocalDate dob) {
        System.out.println(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        // Update user details
        if(address!=null) {
            user.setAddress(address);
        }
        if(phone!=null) {
            user.setPhone(phone);
        }
        if(dob!=null) {
            user.setDob(dob);
        }
        userRepository.save(user);
        return "User updated successfully";
    }

    public String changePassword(String email, String currentPassword, String newPassword) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        // Validate the current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Invalid current password");
        }

        // Update the password to the new password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Save the updated user
        userRepository.save(user);
        return "password reset successfully";
    }

}
