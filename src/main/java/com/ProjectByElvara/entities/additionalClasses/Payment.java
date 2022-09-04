package com.ProjectByElvara.entities.additionalClasses;

public enum Payment {
    CARD("Картка"),
    CASH("Готівка");

    private final String label;

    Payment(String code) {
        this.label = code;
    }

    public String getLabel() {
        return this.label;
    }

    public static Payment valueOfCode(String code) {
        switch (code) {
            case "CARD":
                return Payment.CARD;
            case "CASH":
                return Payment.CASH;
            default:
                throw new IllegalArgumentException("No such payment method");
        }
    }
}
