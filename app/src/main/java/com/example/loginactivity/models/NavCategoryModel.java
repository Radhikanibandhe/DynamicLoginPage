package com.example.loginactivity.models;

public class NavCategoryModel {

    String description;
    String discount;
    String img_url;
    String name;

    public NavCategoryModel() {
    }

    public NavCategoryModel(String description, String discount, String img_url, String name) {
        this.description = description;
        this.discount = discount;
        this.img_url = img_url;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
