package com.ProjectByElvara.entities.additionalClasses;

public enum Delivery {
    DELIVERY("Доставка"),
    PICKUP("Самовивіз");

    private final String label;

    Delivery(String code) {
        this.label = code;
    }

    public String getLabel() {
        return this.label;
    }

    public static Delivery valueOfCode(String code) {
        switch (code) {
            case "DELIVERY":
                return Delivery.DELIVERY;
            case "PICKUP":
                return Delivery.PICKUP;
            default:
                throw new IllegalArgumentException("No such delivery type");
        }
    }
}
