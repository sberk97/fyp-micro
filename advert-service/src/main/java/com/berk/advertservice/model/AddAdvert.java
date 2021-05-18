package com.berk.advertservice.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class AddAdvert {
    @PositiveOrZero(message = "Price can not be negative")
    @Max(value = Integer.MAX_VALUE)
    private int price;

    @NotBlank(message = "Title should not be empty.")
    @Size(min = 6, max = 60, message = "Title must be between 6 and 60 characters")
    private String title;

    @NotBlank(message = "Description should not be empty.")
    @Size(min = 10, max = 1024, message = "Description must be between 10 and 1024 characters")
    private String description;

    public AddAdvert(int price, String title, String description) {
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
