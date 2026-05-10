package com.example.himalaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class AddressEditActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_CITY = "extra_city";
    public static final String EXTRA_STREET = "extra_street";
    public static final String EXTRA_HOUSE = "extra_house";
    public static final String EXTRA_POSTCODE = "extra_postcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.address_edit_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView backView = findViewById(R.id.tv_address_back);
        EditText cityInput = findViewById(R.id.et_city);
        EditText streetInput = findViewById(R.id.et_street);
        EditText houseInput = findViewById(R.id.et_house);
        EditText nameInput = findViewById(R.id.et_name);
        EditText phoneInput = findViewById(R.id.et_phone);
        EditText postcodeInput = findViewById(R.id.et_postcode);
        MaterialButton saveButton = findViewById(R.id.btn_save_address);

        backView.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            String street = streetInput.getText().toString().trim();
            String house = houseInput.getText().toString().trim();
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String postcode = postcodeInput.getText().toString().trim();

            if (city.isEmpty() || street.isEmpty() || house.isEmpty()
                    || name.isEmpty() || phone.isEmpty() || postcode.isEmpty()) {
                Toast.makeText(this, R.string.address_empty_hint, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, AddressDisplayActivity.class);
            intent.putExtra(EXTRA_CITY, city);
            intent.putExtra(EXTRA_STREET, street);
            intent.putExtra(EXTRA_HOUSE, house);
            intent.putExtra(EXTRA_NAME, name);
            intent.putExtra(EXTRA_PHONE, phone);
            intent.putExtra(EXTRA_POSTCODE, postcode);
            startActivity(intent);
        });
    }
}
