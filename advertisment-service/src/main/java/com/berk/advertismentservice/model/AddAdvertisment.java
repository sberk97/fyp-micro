package com.berk.advertismentservice.model;

public class AddAdvertisment {
    private int price;
    private String title;
    private String description;

    public AddAdvertisment(int price, String title, String description) {
        this.price = price;
        this.title = title;
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
