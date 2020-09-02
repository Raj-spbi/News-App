package com.aaratechnologies.lawtrend.models;

public class ModelMenus {
    String menu;
    String object_id;

    public ModelMenus(String menu, String object_id) {
        this.menu = menu;
        this.object_id = object_id;
    }

    public String getMenu() {
        return menu;
    }

    public String getObject_id() {
        return object_id;
    }
}
