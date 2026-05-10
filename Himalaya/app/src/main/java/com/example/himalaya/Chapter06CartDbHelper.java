package com.example.himalaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Chapter06CartDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chapter06_cart.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CART = "cart_items";
    private static final String COLUMN_ID = "product_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SUBTITLE = "subtitle";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE_RES = "image_res";
    private static final String COLUMN_QUANTITY = "quantity";

    public Chapter06CartDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CART + " ("
                + COLUMN_ID + " TEXT PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_SUBTITLE + " TEXT NOT NULL, "
                + COLUMN_PRICE + " INTEGER NOT NULL, "
                + COLUMN_IMAGE_RES + " INTEGER NOT NULL, "
                + COLUMN_QUANTITY + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public List<CartItem> loadCartItems() {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(
                TABLE_CART,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_SUBTITLE, COLUMN_PRICE, COLUMN_IMAGE_RES, COLUMN_QUANTITY},
                null,
                null,
                null,
                null,
                null
        )) {
            while (cursor.moveToNext()) {
                ShopProduct product = new ShopProduct(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RES))
                );
                items.add(new CartItem(
                        product,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
                ));
            }
        }
        return items;
    }

    public void upsertCartItem(CartItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, item.getProduct().getId());
        values.put(COLUMN_NAME, item.getProduct().getName());
        values.put(COLUMN_SUBTITLE, item.getProduct().getSubtitle());
        values.put(COLUMN_PRICE, item.getProduct().getPrice());
        values.put(COLUMN_IMAGE_RES, item.getProduct().getImageResId());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        db.replace(TABLE_CART, null, values);
    }

    public void clearCart() {
        getWritableDatabase().delete(TABLE_CART, null, null);
    }

    public void deleteCartItem(String productId) {
        getWritableDatabase().delete(TABLE_CART, COLUMN_ID + "=?", new String[]{productId});
    }
}
