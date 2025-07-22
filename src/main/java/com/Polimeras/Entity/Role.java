package com.Polimeras.Entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN,
    VENDOR,
    CUSTOMER;
//     private final String value;
//
//    Role(String value) {
//        this.value = value;
//    }
//    @JsonValue
//    public String getValue(){
//        return value;
//    }
}