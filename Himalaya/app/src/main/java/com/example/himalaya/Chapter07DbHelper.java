package com.example.himalaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Chapter07DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "chapter07_ledger.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "bill_record";

    public Chapter07DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bill_date TEXT NOT NULL," +
                "bill_month TEXT NOT NULL," +
                "bill_type INTEGER NOT NULL," +
                "bill_category TEXT NOT NULL," +
                "bill_remark TEXT NOT NULL," +
                "bill_amount INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertRecord(String date, String month, boolean income, String category, String remark, int amount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bill_date", date);
        values.put("bill_month", month);
        values.put("bill_type", income ? 1 : 0);
        values.put("bill_category", category);
        values.put("bill_remark", remark);
        values.put("bill_amount", amount);
        return db.insert(TABLE_NAME, null, values);
    }

    public List<String> queryMonths() {
        List<String> months = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT DISTINCT bill_month FROM " + TABLE_NAME + " ORDER BY bill_month DESC", null)) {
            while (cursor.moveToNext()) {
                months.add(cursor.getString(0));
            }
        }
        return months;
    }

    public List<Chapter07BillRecord> queryRecordsByMonth(String month) {
        List<Chapter07BillRecord> records = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(
                TABLE_NAME,
                null,
                "bill_month=?",
                new String[]{month},
                null,
                null,
                "bill_date ASC,_id ASC")) {
            while (cursor.moveToNext()) {
                records.add(new Chapter07BillRecord(
                        cursor.getLong(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("bill_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("bill_month")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("bill_type")) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow("bill_category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("bill_remark")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("bill_amount"))
                ));
            }
        }
        return records;
    }
}
