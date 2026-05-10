package com.example.himalaya;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String DEMO_CODE = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton closeButton = findViewById(R.id.btn_close);
        EditText newPasswordInput = findViewById(R.id.et_new_password);
        EditText confirmPasswordInput = findViewById(R.id.et_confirm_password);
        EditText codeInput = findViewById(R.id.et_reset_code);
        MaterialButton getCodeButton = findViewById(R.id.btn_get_reset_code);
        MaterialButton submitButton = findViewById(R.id.btn_submit);
        TextView titleView = findViewById(R.id.tv_forgot_title);

        closeButton.setOnClickListener(v -> finish());
        titleView.setText(R.string.forgot_password_title);

        getCodeButton.setOnClickListener(v ->
                Toast.makeText(this, getString(R.string.reset_code_send_hint, DEMO_CODE), Toast.LENGTH_SHORT).show());

        submitButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();
            String code = codeInput.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty() || code.isEmpty()) {
                Toast.makeText(this, R.string.reset_empty_hint, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, R.string.password_mismatch_hint, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!DEMO_CODE.equals(code)) {
                Toast.makeText(this, R.string.code_invalid_hint, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, R.string.password_reset_success_hint, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
