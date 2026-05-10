package com.example.himalaya;

public class Chapter07BillRecord {

    private final long id;
    private final String date;
    private final String month;
    private final boolean income;
    private final String category;
    private final String remark;
    private final int amount;

    public Chapter07BillRecord(long id, String date, String month, boolean income, String category, String remark, int amount) {
        this.id = id;
        this.date = date;
        this.month = month;
        this.income = income;
        this.category = category;
        this.remark = remark;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public boolean isIncome() {
        return income;
    }

    public String getCategory() {
        return category;
    }

    public String getRemark() {
        return remark;
    }

    public int getAmount() {
        return amount;
    }
}
