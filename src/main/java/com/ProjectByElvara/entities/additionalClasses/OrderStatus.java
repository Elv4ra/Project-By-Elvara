package com.ProjectByElvara.entities.additionalClasses;

public enum OrderStatus {
    ACCEPTED("Прийнято"),
    REJECTED("Відхилено"),
    CANCELLED("Скасовано"),
    DONE("Виконано");

    private final String label;

    OrderStatus(String code) {
        this.label = code;
    }

    public String getLabel() {
        return label;
    }

    public static OrderStatus valueOfCode(String code) {
        switch (code) {
            case "ACCEPTED":
                return OrderStatus.ACCEPTED;
            case "REJECTED":
                return OrderStatus.REJECTED;
            case "CANCELLED":
                return OrderStatus.CANCELLED;
            case "DONE":
                return OrderStatus.DONE;
            default:
                throw new IllegalArgumentException("No such status");
        }
    }
}
