package com.berk.advertservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class AddAdvert {
    @PositiveOrZero(message = "Price can not be negative")
    @Max(value = Integer.MAX_VALUE)
    private int price;

    @Size(min = 6, max = 60, message = "Title must be between 6 and 60 characters")
    private String title;

    @Size(min = 10, max = 1024, message = "Description must be between 10 and 1024 characters")
    private String description;

    @Size(min = 10, max = 60, message = "Contact details must be between 10 and 60 characters")
    @JsonProperty("contact_details")
    private String contactDetails;

    public AddAdvert(int price, String title, String description, String contactDetails) {
        this.price = price;
        this.title = title;
        this.description = description;
        this.contactDetails = contactDetails;
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

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }
}
