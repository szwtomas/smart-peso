package com.smartpeso.services;

import com.smartpeso.services.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtServiceTest {
    @Test
    public void generateToken_givenValidEmail_shouldGenerateToken() {
        String email = "john.doe@mail.com";
        JwtService unit = new JwtService();
        String token = unit.generateAccessToken(email);
        assertNotNull(token);
    }

    @Test
    public void generateToken_givenNullValidEmail_shouldNotGenerateToken() {
        JwtService unit = new JwtService();
        String token = unit.generateAccessToken("");
        assertNull(token);
    }

    @Test
    public void extractClaim_givenSubjectClaim_shouldReturnSubjectEmail() {
        JwtService unit = new JwtService();

        String jwt = unit.generateAccessToken("john.doe@mail.com");
        String actual = unit.extractClaim(jwt, Claims::getSubject);

        assertEquals("john.doe@mail.com", actual);
    }

    @Test
    public void extractSubject_givenValidSubjectClaim_shouldReturnSubjectEmail() {
        JwtService unit = new JwtService();

        String jwt = unit.generateAccessToken("john.doe@mail.com");
        String actual = unit.extractSubject(jwt);

        assertEquals("john.doe@mail.com", actual);
    }

    @Test
    public void isValidToken_givenValidExpirationButUserNotMatching_shouldReturnFalse() {
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsMock.getUsername()).thenReturn("mary.jane@mail.com");

        JwtService unit = new JwtService();
        String jwt = unit.generateAccessToken("john.doe@mail.com");

        boolean isValidToken = unit.isValidToken(jwt, userDetailsMock);
        assertFalse(isValidToken);
    }

    @Test
    public void isValidToken_givenValidExpirationAndUserMatching_shouldReturnFalse() {
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsMock.getUsername()).thenReturn("john.doe@mail.com");

        JwtService unit = new JwtService();
        String jwt = unit.generateAccessToken("john.doe@mail.com");

        boolean isValidToken = unit.isValidToken(jwt, userDetailsMock);
        assertTrue(isValidToken);
    }

    @Test
    public void isValidToken_givenExpirationPassed_shouldThrowException() {
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsMock.getUsername()).thenReturn("john.doe@mail.com");

        JwtService unit = new JwtService();
        String jwt = unit.generateAccessToken("john.doe@mail.com", -1000 * 60);

        assertThrows(ExpiredJwtException.class, () -> unit.isValidToken(jwt, userDetailsMock));
    }
}
