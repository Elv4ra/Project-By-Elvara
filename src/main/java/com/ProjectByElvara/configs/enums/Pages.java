package com.ProjectByElvara.configs.enums;

public enum Pages {
    LOGIN("LOGIN"),
    SIGN_UP("SIGN_UP"),
    USERS("USERS"),
    USER("USER"),
    ADD_PRODUCT("ADD_PRODUCT"),
    PRODUCTS("PRODUCTS"),
    PRODUCT("PRODUCT"),
    UPDATE_PRODUCT("UPDATE_PRODUCT"),
    CART("CART"),
    ORDERS("ORDERS"),
    ORDER("ORDER");

    private final String code;

    private Pages(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
