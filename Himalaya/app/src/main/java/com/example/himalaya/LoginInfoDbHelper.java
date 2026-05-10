package com.example.himalaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginInfoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "login_info.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_LOGIN_INFO = "login_info";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final int SINGLE_ROW_ID = 1;

    public LoginInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_LOGIN_INFO + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_PHONE + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_INFO);
        onCreate(db);
    }

    public void saveLoginInfo(String phone, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, SINGLE_ROW_ID);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        db.replace(TABLE_LOGIN_INFO, null, values);
    }

    public SavedLoginInfo getSavedLoginInfo() {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(
                TABLE_LOGIN_INFO,
                new String[]{COLUMN_PHONE, COLUMN_PASSWORD},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(SINGLE_ROW_ID)},
                null,
                null,
                null
        )) {
            if (!cursor.moveToFirst()) {
                return null;
            }
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            return new SavedLoginInfo(phone, password);
        }
    }

    public void clearLoginInfo() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LOGIN_INFO, COLUMN_ID + "=?", new String[]{String.valueOf(SINGLE_ROW_ID)});
    }

    public static class SavedLoginInfo {
        private final String phone;
        private final String password;

        public SavedLoginInfo(String phone, String password) {
            this.phone = phone;
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public String getPassword() {
            return password;
        }
    }
}
