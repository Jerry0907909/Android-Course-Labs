package com.example.himalaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Chapter06ShopActivity extends AppCompatActivity
        implements GoodsAdapter.AddCartListener {

    private HimalayaApplication app;
    private TextView cartCountView;
    private GridView goodsGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter06_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chapter06_shop_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        app = (HimalayaApplication) getApplication();
        cartCountView = findViewById(R.id.tv_cart_count);
        goodsGridView = findViewById(R.id.gv_channel);
        TextView cartEntryView = findViewById(R.id.tv_cart_entry);
        TextView backView = findViewById(R.id.tv_shop_back);

        backView.setOnClickListener(v -> finish());
        cartEntryView.setOnClickListener(v ->
                startActivity(new Intent(this, Chapter06CartActivity.class)));

        showGoods();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    private void showGoods() {
        GoodsAdapter adapter = new GoodsAdapter(this, Chapter06Data.getProducts(), this);
        goodsGridView.setAdapter(adapter);
    }

    @Override
    public void addToCart(ShopProduct product) {
        app.addToCart(product);
        updateCartCount();
        Toast.makeText(this, getString(R.string.chapter06_add_to_cart_success, product.getName()), Toast.LENGTH_SHORT).show();
    }

    private void updateCartCount() {
        int cartCount = app.getCartCount();
        if (cartCount == 0) {
            cartCount = app.getLastSavedCartCount();
        }
        cartCountView.setText(String.valueOf(cartCount));
        cartCountView.setVisibility(cartCount > 0 ? View.VISIBLE : View.GONE);
    }
}
