package com.artplan.pets.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Gender {
    MAIL("MAIL"), 
    FEMAIL("FEMAIL");

    @Getter
    private final String value;

    public static Gender of(String value) {
        for (Gender e : values()) {
            if (value.equals(e.value)) {
                return e;
            }
        }
        return null;
    }
}
