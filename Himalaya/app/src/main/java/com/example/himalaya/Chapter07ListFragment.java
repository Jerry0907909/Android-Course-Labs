package com.example.himalaya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Chapter07ListFragment extends Fragment {

    private Chapter07DbHelper dbHelper;
    private Spinner monthSpinner;
    private TextView summaryView;
    private TextView emptyView;
    private Chapter07BillAdapter billAdapter;
    private ArrayAdapter<String> monthAdapter;
    private final List<String> months = new ArrayList<>();

    public static Chapter07ListFragment newInstance() {
        return new Chapter07ListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter07_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new Chapter07DbHelper(requireContext());
        monthSpinner = view.findViewById(R.id.sp_bill_month);
        summaryView = view.findViewById(R.id.tv_bill_summary);
        emptyView = view.findViewById(R.id.tv_bill_empty);
        ListView listView = view.findViewById(R.id.lv_bill_list);
        listView.setEmptyView(emptyView);

        billAdapter = new Chapter07BillAdapter(requireContext());
        listView.setAdapter(billAdapter);

        monthAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View itemView, int position, long id) {
                loadRecords(months.get(position));
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        refreshContent(null);
    }

    public void refreshContent(@Nullable String preferredMonth) {
        if (dbHelper == null || monthAdapter == null) {
            return;
        }
        months.clear();
        months.addAll(dbHelper.queryMonths());
        monthAdapter.notifyDataSetChanged();
        if (months.isEmpty()) {
            billAdapter.submitRecords(new ArrayList<>());
            summaryView.setText(getString(R.string.chapter07_summary_format, 0, 0, 0));
            return;
        }
        int targetIndex = 0;
        if (preferredMonth != null) {
            int index = months.indexOf(preferredMonth);
            if (index >= 0) {
                targetIndex = index;
            }
        }
        monthSpinner.setSelection(targetIndex);
        loadRecords(months.get(targetIndex));
    }

    private void loadRecords(String month) {
        List<Chapter07BillRecord> records = dbHelper.queryRecordsByMonth(month);
        int income = 0;
        int expense = 0;
        for (Chapter07BillRecord record : records) {
            if (record.isIncome()) {
                income += record.getAmount();
            } else {
                expense += record.getAmount();
            }
        }
        billAdapter.submitRecords(records);
        summaryView.setText(getString(R.string.chapter07_summary_format, records.size(), income, expense));
    }
}
