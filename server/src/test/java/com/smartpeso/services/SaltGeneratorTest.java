package com.smartpeso.services;

import com.smartpeso.services.auth.SaltGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public class SaltGeneratorTest {
    @Test
    public void testGenerateSalt() {
        SecureRandom secureRandom = Mockito.mock(SecureRandom.class);
        byte[] mockSalt = new byte[]{1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            byte[] byteArray = (byte[]) args[0];
            byteArray[0] = 1;
            byteArray[1] = 2;
            byteArray[2] = 3;
            byteArray[3] = 4;
            return null;
        }).when(secureRandom).nextBytes(any());

        SaltGenerator saltGenerator = new SaltGenerator(secureRandom);

        String generatedSalt = saltGenerator.generateSalt();

        String expectedSalt = Base64.getEncoder().encodeToString(mockSalt);
        assertEquals(expectedSalt, generatedSalt);
    }

    @Test
    public void testGenerateUniqueSalts() {
        SaltGenerator saltGenerator = new SaltGenerator(new SecureRandom());

        String salt1 = saltGenerator.generateSalt();
        String salt2 = saltGenerator.generateSalt();

        assertNotEquals(salt1, salt2);
    }
}
