package com.smartpeso.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Document
public class Transaction {
    @Id
    @Field(name = "_id")
    private String transactionId;

    @Field(name = "user")
    private User user;

    @Field(name = "name")
    private String name;

    @Field(name = "date")
    private Date date;

    @Field(name = "type")
    private String type;

    @Field(name = "currency")
    private String currency;

    @Field(name = "value")
    private double value;

    @Field(name = "category")
    private String category;

    @Field(name = "description")
    private String description;
}
