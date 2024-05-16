package ch.simi1892.busrental.service;

import ch.simi1892.busrental.config.auth.TokenProvider;
import ch.simi1892.busrental.dto.LoginDto;
import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.exception.EmailAlreadyInUseException;
import ch.simi1892.busrental.exception.InvalidEmailException;
import ch.simi1892.busrental.mapper.UserMapper;
import ch.simi1892.busrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
public class AuthService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private final UserRepository userRepository;
    private final TokenProvider tokenService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenService = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional(readOnly = true)
    public String login(LoginDto dto) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            Authentication authUser = authenticationManager.authenticate(usernamePassword);
            UserDbo user = (UserDbo) authUser.getPrincipal();
            return tokenService.generateAccessToken(new User(user.getUsername(), user.getPassword(), user.getAuthorities()));
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    @Transactional
    public UserDto registerUser(UserRegistrationDto dto) {
        validateEmail(dto.email());
        validatePassword(dto.password(), dto.passwordConfirmation());
        userRepository.findByEmail(dto.email()).ifPresent(existingUser -> {
            throw new EmailAlreadyInUseException("Email already exists");
        });

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
}
