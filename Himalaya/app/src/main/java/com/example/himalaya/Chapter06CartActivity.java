package com.example.himalaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Chapter06CartActivity extends AppCompatActivity
        implements CartAdapter.OnCartItemActionListener {

    private HimalayaApplication app;
    private final List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private ListView cartListView;
    private LinearLayout emptyLayout;
    private TextView totalPriceView;
    private TextView cartCountView;
    private TextView cartStorageStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter06_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chapter06_cart_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        app = (HimalayaApplication) getApplication();
        cartListView = findViewById(R.id.lv_cart);
        emptyLayout = findViewById(R.id.ll_cart_empty);
        totalPriceView = findViewById(R.id.tv_cart_total_price);
        cartCountView = findViewById(R.id.tv_cart_page_count);
        cartStorageStatusView = findViewById(R.id.tv_cart_storage_status);

        TextView backView = findViewById(R.id.tv_cart_back);
        Button clearButton = findViewById(R.id.btn_cart_clear);
        Button checkoutButton = findViewById(R.id.btn_cart_checkout);
        Button shoppingButton = findViewById(R.id.btn_shopping_channel);

        cartAdapter = new CartAdapter(this, cartItems, this);
        cartListView.setAdapter(cartAdapter);

        backView.setOnClickListener(v -> finish());
        shoppingButton.setOnClickListener(v -> {
            startActivity(new Intent(this, Chapter06ShopActivity.class));
            finish();
        });
        clearButton.setOnClickListener(v -> {
            app.clearCart();
            showCart();
            Toast.makeText(this, R.string.chapter06_cart_cleared, Toast.LENGTH_SHORT).show();
        });
        checkoutButton.setOnClickListener(v -> checkout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCart();
    }

    private void showCart() {
        cartItems.clear();
        cartItems.addAll(app.getCartItems());
        cartAdapter.notifyDataSetChanged();

        boolean isEmpty = cartItems.isEmpty();
        emptyLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        cartListView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        cartCountView.setText(String.valueOf(app.getCartCount()));
        totalPriceView.setText(getString(R.string.chapter06_price_value, app.getCartTotalPrice()));
        updateStorageStatus();
    }

    private void checkout() {
        if (app.getCartCount() == 0) {
            Toast.makeText(this, R.string.chapter06_cart_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        int totalPrice = app.getCartTotalPrice();
        String receiptPreview = app.checkoutAndExport();
        showCart();
        Toast.makeText(
                this,
                getString(R.string.chapter06_checkout_success, totalPrice, receiptPreview),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void updateStorageStatus() {
        int lastTotal = app.getLastCheckoutTotal();
        String receiptPreview = app.getLastReceiptPreview();
        if (lastTotal <= 0 || receiptPreview == null || receiptPreview.isEmpty()) {
            cartStorageStatusView.setText(R.string.chapter06_storage_status_empty);
            return;
        }
        cartStorageStatusView.setText(getString(R.string.chapter06_storage_status_value, lastTotal, receiptPreview));
    }

    @Override
    public void onCartItemClick(CartItem item) {
        Toast.makeText(
                this,
                getString(R.string.chapter06_cart_item_click_tip, item.getProduct().getName(), item.getTotalPrice()),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onCartItemLongClick(CartItem item) {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.chapter06_cart_delete_confirm, item.getProduct().getName()))
                .setPositiveButton(R.string.chapter06_cart_delete_yes, (dialog, which) -> {
                    app.removeCartItem(item.getProduct().getId());
                    showCart();
                })
                .setNegativeButton(R.string.chapter06_cart_delete_no, null)
                .show();
    }
}
