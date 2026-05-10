package com.example.himalaya;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Chapter05DatabaseActivity extends AppCompatActivity {

    private PersonRecordDbHelper dbHelper;
    private EditText nameInput;
    private EditText ageInput;
    private EditText heightInput;
    private EditText weightInput;
    private CheckBox marriedCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter05_database);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chapter05_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new PersonRecordDbHelper(this);
        bindViews();
        bindActions();
    }

    private void bindViews() {
        nameInput = findViewById(R.id.et_name);
        ageInput = findViewById(R.id.et_age);
        heightInput = findViewById(R.id.et_height);
        weightInput = findViewById(R.id.et_weight);
        marriedCheckBox = findViewById(R.id.cb_married);
    }

    private void bindActions() {
        Button addButton = findViewById(R.id.btn_add);
        Button deleteButton = findViewById(R.id.btn_delete);
        Button updateButton = findViewById(R.id.btn_update);
        Button queryButton = findViewById(R.id.btn_query);

        addButton.setOnClickListener(v -> addPerson());
        deleteButton.setOnClickListener(v -> deletePerson());
        updateButton.setOnClickListener(v -> updatePerson());
        queryButton.setOnClickListener(v -> queryPerson());
    }

    private void addPerson() {
        PersonRecordDbHelper.Person person = readCompletePerson();
        if (person == null) {
            return;
        }
        if (!dbHelper.insertPerson(person)) {
            Toast.makeText(this, R.string.chapter05_add_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, R.string.chapter05_add_success, Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    private void deletePerson() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.chapter05_name_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!dbHelper.deletePerson(name)) {
            Toast.makeText(this, R.string.chapter05_delete_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, R.string.chapter05_delete_success, Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    private void updatePerson() {
        PersonRecordDbHelper.Person person = readCompletePerson();
        if (person == null) {
            return;
        }
        if (!dbHelper.updatePerson(person)) {
            Toast.makeText(this, R.string.chapter05_update_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, R.string.chapter05_update_success, Toast.LENGTH_SHORT).show();
    }

    private void queryPerson() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.chapter05_name_required, Toast.LENGTH_SHORT).show();
            return;
        }
        PersonRecordDbHelper.Person person = dbHelper.queryPerson(name);
        if (person == null) {
            Toast.makeText(this, R.string.chapter05_query_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        ageInput.setText(person.getAge());
        heightInput.setText(person.getHeight());
        weightInput.setText(person.getWeight());
        marriedCheckBox.setChecked(person.isMarried());
        Toast.makeText(this, R.string.chapter05_query_success, Toast.LENGTH_SHORT).show();
    }

    private PersonRecordDbHelper.Person readCompletePerson() {
        String name = nameInput.getText().toString().trim();
        String age = ageInput.getText().toString().trim();
        String height = heightInput.getText().toString().trim();
        String weight = weightInput.getText().toString().trim();
        if (name.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty()) {
            Toast.makeText(this, R.string.chapter05_input_required, Toast.LENGTH_SHORT).show();
            return null;
        }
        return new PersonRecordDbHelper.Person(name, age, height, weight, marriedCheckBox.isChecked());
    }

    private void clearInputs() {
        nameInput.setText("");
        ageInput.setText("");
        heightInput.setText("");
        weightInput.setText("");
        marriedCheckBox.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
