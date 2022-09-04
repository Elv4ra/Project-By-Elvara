package com.ProjectByElvara.entities.additionalClasses;

public enum Category {
    BAKERY("Хлібобулочні вироби"),
    FRUIT_VEGETABLES("Овочі та фрукти"),
    FROZEN_FOOD("Заморожена продукція"),
    DAIRY_PRODUCTS("Молочна продукція"),
    MEAT("М'ясо"),
    ALCOHOL("Алкоголь");

    private final String label;

    Category(String code) {
        this.label = code;
    }

    public String getLabel() {
        return this.label;
    }

    public static Category valueOfCode(String code) {
        switch (code) {
            case "BAKERY":
                return Category.BAKERY;
            case "FRUIT_VEGETABLES":
                return Category.FRUIT_VEGETABLES;
            case "FROZEN_FOOD":
                return Category.FROZEN_FOOD;
            case "DAIRY_PRODUCTS":
                return Category.DAIRY_PRODUCTS;
            case "MEAT":
                return Category.MEAT;
            case "ALCOHOL":
                return Category.ALCOHOL;
            default:
                throw new IllegalArgumentException("No such category");
        }
    }
}
