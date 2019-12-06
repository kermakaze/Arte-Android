package com.teamarte.arte.api.data.request;

public class CreateArtRequest {
    private String fullResUrl;
    private double price;
    private String description;


    public CreateArtRequest(String fullResUrl, double price, String description) {
        this.fullResUrl = fullResUrl;
        this.price = price;
        this.description = description;
    }
}
