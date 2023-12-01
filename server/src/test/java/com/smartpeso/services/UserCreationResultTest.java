package com.smartpeso.services;

import com.smartpeso.model.User;
import com.smartpeso.services.auth.UserCreationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserCreationResultTest {
    @Test
    public void givenResultIsAnUser_UserCreationResultShouldBeSuccessAnReturnTheUser() {
        User user = new User("someId", "john.doe@mail.com", "John", "Doe");
        UserCreationResult creationResult = UserCreationResult.success(user);

        User actual = creationResult.getUser();

        assertTrue(creationResult.isSuccess());
        assertFalse(creationResult.isUserAlreadyExists());
        assertNull(creationResult.getError());
        assertNull(creationResult.getExistingUserEmail());
        assertNotNull(actual);
        assertEquals("john.doe@mail.com", actual.email());
        assertEquals("John", actual.firstName());
        assertEquals("Doe", actual.lastName());
    }

    @Test
    public void givenResultIsAlreadyExistingUser_UserCreationResultShouldReturnThatEmail() {
        UserCreationResult creationResult = UserCreationResult.userAlreadyExists("john.doe@mail.com");

        assertTrue(creationResult.isUserAlreadyExists());
        assertFalse(creationResult.isSuccess());
        assertNull(creationResult.getUser());
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
        assertNull(creationResult.getUser());
        assertNull(creationResult.getExistingUserEmail());
        assertEquals("some error", actualError.getMessage());
    }
}
