package ch.simi1892.busrental.service;

import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.exception.EmailAlreadyInUseException;
import ch.simi1892.busrental.exception.InvalidEmailException;
import ch.simi1892.busrental.mapper.UserMapper;
import ch.simi1892.busrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
public class AuthService implements UserDetailsService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto registerUser(UserRegistrationDto dto) {
        validateEmail(dto.email());
        validatePassword(dto.password(), dto.passwordConfirmation());
        userRepository.findByEmail(dto.email()).orElseThrow(() -> new EmailAlreadyInUseException("Email already exists"));

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        UserDbo newUser = UserMapper.toDbo(dto);
        newUser.setPassword(encryptedPassword);
        newUser.setActive(true);
        UserDbo createdUser = userRepository.save(newUser);
        return UserMapper.toDto(createdUser);
    }

    private void validateEmail(String email) {
        if (!StringUtils.hasText(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException(email);
        }
    }

    private void validatePassword(String password, String passwordConfirmation) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password must not be empty.");
        }
        if (!password.equals(passwordConfirmation)) {
            throw new IllegalArgumentException("Password and confirmation do not match.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
