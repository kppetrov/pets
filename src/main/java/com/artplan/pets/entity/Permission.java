package com.artplan.pets.entity;

public enum Permission {

    PET_READ("pet:read"), 
    PET_WRITE("pet:write"),
    TYPE_READ("type:read"),
    TYPE_WRITE("type:write");

    private final String value;

    private Permission(String value) {
        this.value = value;
    }

    public String getPermission() {
        return value;
    }

}
