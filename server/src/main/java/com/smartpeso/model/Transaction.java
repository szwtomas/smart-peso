package com.smartpeso.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Document
@AllArgsConstructor
public class Transaction {
    @Id
    @Field(name = "_id")
    private String transactionId;

    @Field(name = "userId")
    @JsonIgnore
    private String userId;

    @Field(name = "name")
    private String name;

    @Field(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime date;

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

    @Field(name = "paymentMethod")
    private String paymentMethod;
}
