package com.smartpeso.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public record User(
        @Id
        @Field(name = "_id")
        String userId,
        @Field(name = "email")
        String email,
        @Field(name = "firstName")
        String firstName,
        @Field(name = "lastName")
        String lastName) {
}
