package com.smartpeso.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DateParserTest {
    @Mock
    private JsonParser jsonParserMock;

    @Mock
    private DeserializationContext contextMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testParser_givenDateTimeAsString_shouldParseToCorrectLocalDateTime() throws IOException {
        String dateString = "2023-12-08";
        when(jsonParserMock.getText()).thenReturn(dateString);

        DateParser unit = new DateParser();

        LocalDateTime parsedDateTime = unit.deserialize(jsonParserMock, contextMock);

        assertEquals(2023, parsedDateTime.getYear());
        assertEquals(12, parsedDateTime.getMonth().getValue());
        assertEquals(8, parsedDateTime.getDayOfMonth());
    }
}
