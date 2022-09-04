package com.ProjectByElvara.entities.additionalClasses;

public enum ProductStatus {
    ACTIVE,
    ARCHIVED;

    public static ProductStatus valueOfCode(String code) {
        switch (code) {
            case "ACTIVE":
                return ProductStatus.ACTIVE;
            case "ARCHIVED":
                return ProductStatus.ARCHIVED;
            default:
                throw new IllegalArgumentException("No such status");
        }
    }
}
