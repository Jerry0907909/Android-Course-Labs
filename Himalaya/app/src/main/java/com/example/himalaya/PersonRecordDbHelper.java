package com.example.himalaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonRecordDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "person_record.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PERSON = "person_info";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_MARRIED = "married";

    public PersonRecordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PERSON + " ("
                + COLUMN_NAME + " TEXT PRIMARY KEY, "
                + COLUMN_AGE + " TEXT NOT NULL, "
                + COLUMN_HEIGHT + " TEXT NOT NULL, "
                + COLUMN_WEIGHT + " TEXT NOT NULL, "
                + COLUMN_MARRIED + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        onCreate(db);
    }

    public boolean insertPerson(Person person) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toValues(person);
        return db.insert(TABLE_PERSON, null, values) != -1;
    }

    public boolean updatePerson(Person person) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toValues(person);
        return db.update(TABLE_PERSON, values, COLUMN_NAME + "=?", new String[]{person.getName()}) > 0;
    }

    public boolean deletePerson(String name) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PERSON, COLUMN_NAME + "=?", new String[]{name}) > 0;
    }

    public Person queryPerson(String name) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(
                TABLE_PERSON,
                new String[]{COLUMN_NAME, COLUMN_AGE, COLUMN_HEIGHT, COLUMN_WEIGHT, COLUMN_MARRIED},
                COLUMN_NAME + "=?",
                new String[]{name},
                null,
                null,
                null
        )) {
            if (!cursor.moveToFirst()) {
                return null;
            }
            return new Person(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MARRIED)) == 1
            );
        }
    }

    private ContentValues toValues(Person person) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, person.getName());
        values.put(COLUMN_AGE, person.getAge());
        values.put(COLUMN_HEIGHT, person.getHeight());
        values.put(COLUMN_WEIGHT, person.getWeight());
        values.put(COLUMN_MARRIED, person.isMarried() ? 1 : 0);
        return values;
    }

    public static class Person {
        private final String name;
        private final String age;
        private final String height;
        private final String weight;
        private final boolean married;

        public Person(String name, String age, String height, String weight, boolean married) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
            this.married = married;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }

        public String getHeight() {
            return height;
        }

        public String getWeight() {
            return weight;
        }

        public boolean isMarried() {
            return married;
        }
    }
}
