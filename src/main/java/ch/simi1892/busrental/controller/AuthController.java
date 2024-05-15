package ch.simi1892.busrental.controller;

import ch.simi1892.busrental.config.auth.TokenProvider;
import ch.simi1892.busrental.dto.JwtDto;
import ch.simi1892.busrental.dto.LoginDto;
import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.tokenService = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRegistrationDto user) {
        UserDto registeredUser = authService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> signIn(@RequestBody LoginDto dto) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            Authentication authUser = authenticationManager.authenticate(usernamePassword);
            UserDbo userDbo = (UserDbo) authUser.getPrincipal();
            String accessToken = tokenService.generateAccessToken(new User(userDbo.getUsername(), userDbo.getPassword(), userDbo.getAuthorities()));
            return ResponseEntity.ok(new JwtDto(accessToken));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtDto("Invalid credentials"));
        }
    }
}
