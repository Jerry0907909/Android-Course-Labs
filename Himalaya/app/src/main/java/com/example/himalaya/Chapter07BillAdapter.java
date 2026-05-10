package com.example.himalaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Chapter07BillAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Chapter07BillRecord> records = new ArrayList<>();

    public Chapter07BillAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void submitRecords(List<Chapter07BillRecord> list) {
        records.clear();
        records.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_chapter07_bill, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Chapter07BillRecord record = records.get(position);
        holder.dateView.setText(record.getDate());
        holder.remarkView.setText(record.getRemark());
        holder.categoryView.setText(record.getCategory());
        int amountColor = view.getResources().getColor(
                record.isIncome() ? R.color.chapter07_income : R.color.chapter07_expense,
                view.getContext().getTheme()
        );
        holder.amountView.setText(record.isIncome()
                ? view.getContext().getString(R.string.chapter07_amount_positive, record.getAmount())
                : view.getContext().getString(R.string.chapter07_amount_negative, record.getAmount()));
        holder.amountView.setTextColor(amountColor);
        return view;
    }

    private static final class ViewHolder {
        private final TextView dateView;
        private final TextView remarkView;
        private final TextView categoryView;
        private final TextView amountView;

        private ViewHolder(View itemView) {
            dateView = itemView.findViewById(R.id.tv_bill_date);
            remarkView = itemView.findViewById(R.id.tv_bill_remark);
            categoryView = itemView.findViewById(R.id.tv_bill_category);
            amountView = itemView.findViewById(R.id.tv_bill_amount);
        }
    }
}
