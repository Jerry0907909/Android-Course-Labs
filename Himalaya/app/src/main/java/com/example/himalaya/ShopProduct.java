package com.example.himalaya;

public class ShopProduct {

    private final String id;
    private final String name;
    private final String subtitle;
    private final int price;
    private final int imageResId;

    public ShopProduct(String id, String name, String subtitle, int price, int imageResId) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}
