package ch.simi1892.busrental.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;


import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    private TokenProvider tokenProvider;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider();
        tokenProvider.secret = "mySecretKey";
        tokenProvider.expiration_in_ms = 3600000L; // 1 hour
        tokenProvider.init();
    }

    @Test
    void generateAccessToken_ShouldReturnToken() {
        User user = new User("testUser", "password", new ArrayList<>());
        String token = tokenProvider.generateAccessToken(user);
        Assertions.assertNotNull(token);

        // Decode the token to ensure it contains the correct claims
        String username = JWT.require(Algorithm.HMAC256("mySecretKey"))
                .build()
                .verify(token)
                .getClaim("username")
                .asString();
        Assertions.assertEquals("testUser", username);
    }

    @Test
    void validateToken_ShouldReturnUsername() {
        User user = new User("testUser", "password", new ArrayList<>());
        String token = tokenProvider.generateAccessToken(user);
        String username = tokenProvider.validateToken(token);
        Assertions.assertEquals("testUser", username);
    }

    @Test
    void validateToken_ShouldThrowJWTVerificationException() {
        String invalidToken = "invalidToken";
        Assertions.assertThrows(JWTVerificationException.class, () -> tokenProvider.validateToken(invalidToken));
    }

    @Test
    void resolveToken_ShouldReturnToken() {
        String bearerToken = "Bearer myToken";
        Mockito.when(request.getHeader("Authorization")).thenReturn(bearerToken);
        String token = tokenProvider.resolveToken(request);
        Assertions.assertEquals("myToken", token);
    }

    @Test
    void resolveToken_ShouldReturnNull() {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String token = tokenProvider.resolveToken(request);
        Assertions.assertNull(token);

        Mockito.when(request.getHeader("Authorization")).thenReturn("InvalidToken");
        token = tokenProvider.resolveToken(request);
        Assertions.assertNull(token);
    }
}