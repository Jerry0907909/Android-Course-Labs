package com.example.himalaya;

public class CartItem {

    private final ShopProduct product;
    private int quantity;

    public CartItem(ShopProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ShopProduct getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public int getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
