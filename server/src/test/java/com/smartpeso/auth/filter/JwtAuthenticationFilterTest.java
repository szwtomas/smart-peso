package com.smartpeso.auth.filter;
import com.smartpeso.auth.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    public void doFilterInternal_givenAuthenticationTokenIsNotPresent_shouldNotAuthenticate() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        JwtAuthenticationFilter unit = new JwtAuthenticationFilter(jwtService, userDetailsService);
        unit.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).isValidToken(anyString(), any(UserDetails.class));
        verify(jwtService, never()).extractSubject(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void doFilterInternal_givenTokenIsValid_AuthenticationSuccessful() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        when(jwtService.extractSubject("valid_token")).thenReturn("user@example.com");
        when(jwtService.isValidToken(eq("valid_token"), any(UserDetails.class))).thenReturn(true);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);

        JwtAuthenticationFilter unit = new JwtAuthenticationFilter(jwtService, userDetailsService);
        unit.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, times(1)).isValidToken("valid_token", userDetails);
        verify(jwtService, times(1)).extractSubject("valid_token");
        verify(userDetailsService, times(1)).loadUserByUsername("user@example.com");
    }

    @Test
    public void doFilterInternal_givenInvalidToken_AuthenticationFailed() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(jwtService.extractSubject("invalid_token")).thenReturn(null);

        JwtAuthenticationFilter unit = new JwtAuthenticationFilter(jwtService, userDetailsService);
        unit.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).isValidToken(anyString(), any(UserDetails.class));
        verify(jwtService, times(1)).extractSubject("invalid_token");
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void doFilterInternal_givenNoBearerToken_AuthenticationSkipped() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("invalid_format_token");

        JwtAuthenticationFilter unit = new JwtAuthenticationFilter(jwtService, userDetailsService);
        unit.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).isValidToken(anyString(), any(UserDetails.class));
        verify(jwtService, never()).extractSubject(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void doFilterInternal_givenUserAlreadyAuthenticated_AuthenticationSkipped() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtAuthenticationFilter unit = new JwtAuthenticationFilter(jwtService, userDetailsService);
        unit.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).isValidToken(anyString(), any(UserDetails.class));
        verify(jwtService, never()).extractSubject(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}
