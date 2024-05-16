package ch.simi1892.busrental.config.auth;

import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.repository.UserRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private SecurityFilter securityFilter;

    private UserDbo userDbo;

    @BeforeEach
    void setUp() {
        userDbo = new UserDbo();
        userDbo.setEmail("testUser");
        userDbo.setPassword("password");
        userDbo.setUserRole(UserDbo.UserRole.ADMIN);

        // Clear the security context before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "validToken";
        Mockito.when(tokenProvider.resolveToken(request)).thenReturn(token);
        Mockito.when(tokenProvider.validateToken(token)).thenReturn("testUser");
        Mockito.when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(userDbo));

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        User user = new User(userDbo.getUsername(), userDbo.getPassword(), userDbo.getAuthorities());
        UsernamePasswordAuthenticationToken expectedAuth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

        var actualAuth = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNotNull(actualAuth);
        Assertions.assertEquals(expectedAuth.getName(), actualAuth.getName());
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_NoToken_DoesNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        Mockito.when(tokenProvider.resolveToken(request)).thenReturn(null);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain).doFilter(request, response);
    }
}
