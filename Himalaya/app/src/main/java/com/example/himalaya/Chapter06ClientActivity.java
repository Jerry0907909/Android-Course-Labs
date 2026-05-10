package com.example.himalaya;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Chapter06ClientActivity extends AppCompatActivity {

    private static final int ACTION_NONE = 0;
    private static final int ACTION_ADD = 1;
    private static final int ACTION_QUERY = 2;

    private EditText nameInput;
    private EditText phoneInput;
    private EditText emailInput;
    private int pendingAction = ACTION_NONE;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean granted = hasPermissionsForAction(pendingAction);
                int action = pendingAction;
                pendingAction = ACTION_NONE;
                if (!granted) {
                    Toast.makeText(this, R.string.chapter06_client_permission_denied, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (action == ACTION_ADD) {
                    addContact();
                } else if (action == ACTION_QUERY) {
                    queryContact();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter06_client);
        setTitle(R.string.chapter06_client_title);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chapter06_client_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.et_contact_name);
        phoneInput = findViewById(R.id.et_contact_phone);
        emailInput = findViewById(R.id.et_contact_email);

        Button addButton = findViewById(R.id.btn_add_contact);
        Button queryButton = findViewById(R.id.btn_query_contact);
        addButton.setOnClickListener(v -> ensurePermissionsAndRun(ACTION_ADD));
        queryButton.setOnClickListener(v -> ensurePermissionsAndRun(ACTION_QUERY));
    }

    private void ensurePermissionsAndRun(int action) {
        if (action == ACTION_ADD && !hasCompleteInput()) {
            return;
        }
        if (action == ACTION_QUERY && !hasNameInput()) {
            return;
        }
        pendingAction = action;
        if (hasPermissionsForAction(action)) {
            pendingAction = ACTION_NONE;
            if (action == ACTION_ADD) {
                addContact();
            } else {
                queryContact();
            }
            return;
        }
        permissionLauncher.launch(getPermissionsForAction(action));
    }

    private boolean hasCompleteInput() {
        if (readName().isEmpty() || readPhone().isEmpty() || readEmail().isEmpty()) {
            Toast.makeText(this, R.string.chapter06_client_input_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean hasNameInput() {
        if (readName().isEmpty()) {
            Toast.makeText(this, R.string.chapter06_client_name_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addContact() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, readName())
                .build());
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, readPhone())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, readEmail())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
            Toast.makeText(this, R.string.chapter06_client_add_success, Toast.LENGTH_SHORT).show();
            clearInputs();
        } catch (Exception e) {
            Toast.makeText(this, R.string.chapter06_client_add_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void queryContact() {
        String name = readName();
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {name};
        try (Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            if (cursor == null || !cursor.moveToFirst()) {
                phoneInput.setText("");
                emailInput.setText("");
                Toast.makeText(this, R.string.chapter06_client_query_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneInput.setText(phone);
            emailInput.setText(queryEmail(contactId));
            Toast.makeText(this, R.string.chapter06_client_query_success, Toast.LENGTH_SHORT).show();
        }
    }

    private String queryEmail(long contactId) {
        String[] projection = {ContactsContract.CommonDataKinds.Email.DATA};
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(contactId)};
        try (Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            if (cursor == null || !cursor.moveToFirst()) {
                return getString(R.string.chapter06_client_email_none);
            }
            return cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Email.DATA));
        }
    }

    private boolean hasPermissionsForAction(int action) {
        for (String permission : getPermissionsForAction(action)) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private @NonNull String[] getPermissionsForAction(int action) {
        if (action == ACTION_ADD) {
            return new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
            };
        }
        return new String[]{Manifest.permission.READ_CONTACTS};
    }

    private String readName() {
        return nameInput.getText().toString().trim();
    }

    private String readPhone() {
        return phoneInput.getText().toString().trim();
    }

    private String readEmail() {
        return emailInput.getText().toString().trim();
    }

    private void clearInputs() {
        nameInput.setText("");
        phoneInput.setText("");
        emailInput.setText("");
    }
}
