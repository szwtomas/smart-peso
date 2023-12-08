package com.smartpeso.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateParser extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        String date = jsonParser.getText();
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        ZonedDateTime zonedDateTime = localDate.atTime(12, 0).atZone(ZoneId.of("America/Argentina/Buenos_Aires"));

        return zonedDateTime.toLocalDateTime();
    }
}
