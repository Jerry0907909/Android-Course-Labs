package com.example.himalaya;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Chapter07EditorFragment extends Fragment {

    private static final String[] INCOME_CATEGORIES = {"工资", "红包", "兼职", "理财", "退款", "其他"};
    private static final String[] EXPENSE_CATEGORIES = {"餐饮", "出行", "购物", "充值", "日用", "其他"};

    private final Calendar selectedCalendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

    private Chapter07DbHelper dbHelper;
    private TextView dateValueView;
    private Chapter07CategoryAdapter categoryAdapter;
    private RadioGroup typeGroup;
    private EditText remarkInput;
    private EditText amountInput;

    public interface OnBillSavedListener {
        void onBillSaved(String month);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter07_editor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new Chapter07DbHelper(requireContext());
        dateValueView = view.findViewById(R.id.tv_bill_date_value);
        typeGroup = view.findViewById(R.id.rg_bill_type);
        GridView categoryGridView = view.findViewById(R.id.gv_bill_category);
        remarkInput = view.findViewById(R.id.et_bill_remark);
        amountInput = view.findViewById(R.id.et_bill_amount);

        categoryAdapter = new Chapter07CategoryAdapter(requireContext());
        categoryGridView.setAdapter(categoryAdapter);
        categoryAdapter.submitCategories(INCOME_CATEGORIES);
        categoryGridView.setOnItemClickListener((parent, itemView, position, id) ->
                categoryAdapter.setSelectedPosition(position));

        view.findViewById(R.id.layout_bill_date).setOnClickListener(v -> showDatePicker());
        view.findViewById(R.id.btn_bill_save).setOnClickListener(v -> saveBill());
        typeGroup.setOnCheckedChangeListener((group, checkedId) -> categoryAdapter.submitCategories(
                checkedId == R.id.rb_bill_income ? INCOME_CATEGORIES : EXPENSE_CATEGORIES
        ));

        updateDateText();
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(), (picker, year, month, dayOfMonth) -> {
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, month);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateText();
        }, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateText() {
        dateValueView.setText(dateFormat.format(selectedCalendar.getTime()));
    }

    private void saveBill() {
        String remark = remarkInput.getText().toString().trim();
        String amountText = amountInput.getText().toString().trim();
        String category = categoryAdapter.getSelectedCategory();
        if (TextUtils.isEmpty(remark) || TextUtils.isEmpty(amountText) || TextUtils.isEmpty(category)) {
            Toast.makeText(requireContext(), R.string.chapter07_input_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(amountText);
        } catch (NumberFormatException exception) {
            amount = 0;
        }
        if (amount <= 0) {
            Toast.makeText(requireContext(), R.string.chapter07_amount_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean income = typeGroup.getCheckedRadioButtonId() == R.id.rb_bill_income;
        String date = dateFormat.format(selectedCalendar.getTime());
        String month = monthFormat.format(selectedCalendar.getTime());
        dbHelper.insertRecord(date, month, income, category, remark, amount);
        Toast.makeText(requireContext(), R.string.chapter07_save_success, Toast.LENGTH_SHORT).show();
        remarkInput.setText("");
        amountInput.setText("");

        if (getActivity() instanceof OnBillSavedListener) {
            ((OnBillSavedListener) getActivity()).onBillSaved(month);
        }
    }
}
