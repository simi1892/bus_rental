package ch.simi1892.busrental.controller;

import ch.simi1892.busrental.dto.JwtDto;
import ch.simi1892.busrental.dto.LoginDto;
import ch.simi1892.busrental.dto.UserDto;
import ch.simi1892.busrental.dto.UserRegistrationDto;
import ch.simi1892.busrental.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRegistrationDto user) {
        UserDto registeredUser = authService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginDto dto) {
        String accessToken = authService.login(dto);
        return ResponseEntity.ok(new JwtDto(accessToken));
    }
}
