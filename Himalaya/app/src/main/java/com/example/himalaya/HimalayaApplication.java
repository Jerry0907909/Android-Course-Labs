package com.example.himalaya;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HimalayaApplication extends Application {

    private static final String PREFS_NAME = "chapter06_prefs";
    private static final String KEY_LAST_CART_COUNT = "last_cart_count";
    private static final String KEY_LAST_CHECKOUT_TOTAL = "last_checkout_total";
    private static final String KEY_LAST_RECEIPT_PREVIEW = "last_receipt_preview";

    private final Map<String, CartItem> cartItems = new LinkedHashMap<>();
    private Chapter06CartDbHelper cartDbHelper;
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        cartDbHelper = new Chapter06CartDbHelper(this);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadCartFromDatabase();
    }

    public void addToCart(ShopProduct product) {
        CartItem existingItem = cartItems.get(product.getId());
        if (existingItem == null) {
            existingItem = new CartItem(product, 1);
            cartItems.put(product.getId(), existingItem);
        } else {
            existingItem.increaseQuantity();
        }
        cartDbHelper.upsertCartItem(existingItem);
        saveCartSummaryToPrefs();
    }

    public void clearCart() {
        cartItems.clear();
        cartDbHelper.clearCart();
        saveCartSummaryToPrefs();
    }

    public void removeCartItem(String productId) {
        cartItems.remove(productId);
        cartDbHelper.deleteCartItem(productId);
        saveCartSummaryToPrefs();
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());
    }

    public int getCartCount() {
        int totalCount = 0;
        for (CartItem item : cartItems.values()) {
            totalCount += item.getQuantity();
        }
        return totalCount;
    }

    public int getCartTotalPrice() {
        int totalPrice = 0;
        for (CartItem item : cartItems.values()) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }

    public String checkoutAndExport() {
        int totalPrice = getCartTotalPrice();
        String receiptPreview = Chapter06StorageHelper.writeReceiptAndReadPreview(this, getCartItems(), totalPrice);
        prefs.edit()
                .putInt(KEY_LAST_CHECKOUT_TOTAL, totalPrice)
                .putString(KEY_LAST_RECEIPT_PREVIEW, receiptPreview)
                .apply();
        clearCart();
        return receiptPreview;
    }

    public int getLastCheckoutTotal() {
        return prefs.getInt(KEY_LAST_CHECKOUT_TOTAL, 0);
    }

    public String getLastReceiptPreview() {
        return prefs.getString(KEY_LAST_RECEIPT_PREVIEW, "");
    }

    public int getLastSavedCartCount() {
        return prefs.getInt(KEY_LAST_CART_COUNT, 0);
    }

    private void loadCartFromDatabase() {
        cartItems.clear();
        for (CartItem item : cartDbHelper.loadCartItems()) {
            cartItems.put(item.getProduct().getId(), item);
        }
        saveCartSummaryToPrefs();
    }

    private void saveCartSummaryToPrefs() {
        prefs.edit()
                .putInt(KEY_LAST_CART_COUNT, getCartCount())
                .apply();
    }
}
