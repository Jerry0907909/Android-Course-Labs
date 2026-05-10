package com.example.himalaya;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddressDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.address_display_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView backView = findViewById(R.id.tv_display_back);
        TextView contactView = findViewById(R.id.tv_contact);
        TextView addressView = findViewById(R.id.tv_address);
        TextView postcodeView = findViewById(R.id.tv_postcode);

        String name = getIntent().getStringExtra(AddressEditActivity.EXTRA_NAME);
        String phone = getIntent().getStringExtra(AddressEditActivity.EXTRA_PHONE);
        String city = getIntent().getStringExtra(AddressEditActivity.EXTRA_CITY);
        String street = getIntent().getStringExtra(AddressEditActivity.EXTRA_STREET);
        String house = getIntent().getStringExtra(AddressEditActivity.EXTRA_HOUSE);
        String postcode = getIntent().getStringExtra(AddressEditActivity.EXTRA_POSTCODE);

        contactView.setText(String.format("%s    %s", safeValue(name), safeValue(phone)));
        addressView.setText(String.format("%s%s%s", safeValue(city), safeValue(street), safeValue(house)));
        postcodeView.setText(getString(R.string.postcode_prefix, safeValue(postcode)));
        backView.setOnClickListener(v -> finish());
    }

    private String safeValue(String value) {
        return value == null ? "" : value;
    }
}
