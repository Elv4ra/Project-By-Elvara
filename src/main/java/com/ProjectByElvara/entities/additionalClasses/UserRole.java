package com.ProjectByElvara.entities.additionalClasses;

public enum UserRole {
    ROLE_CLIENT("Клієнт"),
    ROLE_ADMIN("Адмін");

    private final String label;

    UserRole(String code) {
        this.label = code;
    }

    public String getLabel() {
        return this.label;
    }

    public static UserRole valueOfCode(String code) {
        switch (code) {
            case "ROLE_CLIENT":
                return UserRole.ROLE_CLIENT;
            case "ROLE_ADMIN":
                return UserRole.ROLE_ADMIN;
            default:
                throw new IllegalArgumentException("No such user type");
        }
    }
}
