package com.ProjectByElvara.configs;

import com.ProjectByElvara.configs.enums.Pages;

import java.util.ResourceBundle;

public class PageConfig {
    private static PageConfig instance;
    private ResourceBundle resource;
    private static final String BUNDLE_NAME = "pageConfig";

    public static PageConfig getInstance() {
        if (instance == null) {
            instance = new PageConfig();
            instance.resource = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(Pages key) {
        return (String) resource.getObject(key.getCode());
    }
}
