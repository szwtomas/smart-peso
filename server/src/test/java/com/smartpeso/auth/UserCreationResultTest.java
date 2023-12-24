package com.smartpeso.auth;

import com.smartpeso.auth.model.dto.AuthenticationResponse;
import com.smartpeso.auth.model.UserCreationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserCreationResultTest {
    @Test
    public void givenResultIsSuccess_UserCreationResultShouldBeSuccessAnReturnTheAuthResponse() {
        String accessToken = "soooomeeeeacccesssstokeeeenn";
        AuthenticationResponse response = new AuthenticationResponse(accessToken);
        UserCreationResult creationResult = UserCreationResult.success(response);

        AuthenticationResponse actual = creationResult.getAuthenticationResponse();

        assertTrue(creationResult.isSuccess());
        assertFalse(creationResult.isUserAlreadyExists());
        assertNull(creationResult.getError());
        assertNull(creationResult.getExistingUserEmail());
        assertNotNull(actual);
        assertEquals(accessToken, actual.accessToken());
    }

    @Test
    public void givenResultIsAlreadyExistingUser_UserCreationResultShouldReturnThatEmail() {
        UserCreationResult creationResult = UserCreationResult.userAlreadyExists("john.doe@mail.com");

        assertTrue(creationResult.isUserAlreadyExists());
        assertFalse(creationResult.isSuccess());
        assertNull(creationResult.getAuthenticationResponse());
        assertNull(creationResult.getError());
        assertEquals("john.doe@mail.com", creationResult.getExistingUserEmail());
    }

    @Test
    public void givenResultIsAnError_UserCreationResultShouldOnlyReturnThatError() {
        Throwable error = new RuntimeException("some error");
        UserCreationResult creationResult = UserCreationResult.failure(error);
        Throwable actualError = creationResult.getError();

        assertFalse(creationResult.isSuccess());
        assertFalse(creationResult.isUserAlreadyExists());
        assertNull(creationResult.getAuthenticationResponse());
        assertNull(creationResult.getExistingUserEmail());
        assertEquals("some error", actualError.getMessage());
    }
}
