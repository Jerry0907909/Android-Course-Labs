package com.example.himalaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private static final String DEMO_CODE = "123456";
    private LoginInfoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new LoginInfoDbHelper(this);

        RadioButton passwordModeButton = findViewById(R.id.rb_password_login);
        RadioButton codeModeButton = findViewById(R.id.rb_code_login);
        EditText accountInput = findViewById(R.id.et_phone);
        EditText passwordInput = findViewById(R.id.et_password);
        EditText codeInput = findViewById(R.id.et_login_code);
        MaterialButton getCodeButton = findViewById(R.id.btn_get_login_code);
        Button loginButton = findViewById(R.id.btn_login);
        TextView forgotPasswordView = findViewById(R.id.tv_forgot_password);
        CheckBox rememberPasswordBox = findViewById(R.id.cb_remember_password);
        android.view.View passwordContainer = findViewById(R.id.password_container);
        android.view.View codeContainer = findViewById(R.id.code_container);

        Runnable switchToPasswordMode = () -> {
            passwordModeButton.setChecked(true);
            codeModeButton.setChecked(false);
            passwordContainer.setVisibility(android.view.View.VISIBLE);
            forgotPasswordView.setVisibility(android.view.View.VISIBLE);
            rememberPasswordBox.setVisibility(android.view.View.VISIBLE);
            codeContainer.setVisibility(android.view.View.GONE);
        };

        Runnable switchToCodeMode = () -> {
            passwordModeButton.setChecked(false);
            codeModeButton.setChecked(true);
            passwordContainer.setVisibility(android.view.View.GONE);
            forgotPasswordView.setVisibility(android.view.View.GONE);
            rememberPasswordBox.setVisibility(android.view.View.GONE);
            codeContainer.setVisibility(android.view.View.VISIBLE);
        };

        switchToPasswordMode.run();
        fillSavedLoginInfo(accountInput, passwordInput, rememberPasswordBox);

        passwordModeButton.setOnClickListener(v -> switchToPasswordMode.run());
        codeModeButton.setOnClickListener(v -> switchToCodeMode.run());

        getCodeButton.setOnClickListener(v -> {
            String phone = accountInput.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(this, R.string.phone_empty_hint, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, getString(R.string.code_send_hint, phone, DEMO_CODE), Toast.LENGTH_SHORT).show();
        });

        loginButton.setOnClickListener(v -> {
            String phone = accountInput.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(this, R.string.phone_empty_hint, Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordModeButton.isChecked()) {
                String password = passwordInput.getText().toString().trim();
                if (password.isEmpty()) {
                    Toast.makeText(this, R.string.password_empty_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rememberPasswordBox.isChecked()) {
                    dbHelper.saveLoginInfo(phone, password);
                } else {
                    dbHelper.clearLoginInfo();
                }
                String message = rememberPasswordBox.isChecked()
                        ? getString(R.string.login_success_remember_hint, phone)
                        : getString(R.string.login_success_hint, phone);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                String code = codeInput.getText().toString().trim();
                if (code.isEmpty()) {
                    Toast.makeText(this, R.string.code_empty_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!DEMO_CODE.equals(code)) {
                    Toast.makeText(this, R.string.code_invalid_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, getString(R.string.code_login_success_hint, phone), Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(this, ProfileExperimentActivity.class));
        });

        forgotPasswordView.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void fillSavedLoginInfo(EditText accountInput, EditText passwordInput, CheckBox rememberPasswordBox) {
        LoginInfoDbHelper.SavedLoginInfo savedLoginInfo = dbHelper.getSavedLoginInfo();
        if (savedLoginInfo == null) {
            return;
        }
        accountInput.setText(savedLoginInfo.getPhone());
        passwordInput.setText(savedLoginInfo.getPassword());
        rememberPasswordBox.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
