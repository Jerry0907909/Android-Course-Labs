package com.example.himalaya;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public final class Chapter06StorageHelper {

    private static final String DIRECTORY_NAME = "chapter06";
    private static final String RECEIPT_FILE_NAME = "latest_receipt.txt";
    private static final String IMAGE_FILE_NAME = "latest_cover.png";

    private Chapter06StorageHelper() {
    }

    public static String writeReceiptAndReadPreview(Context context, List<CartItem> items, int totalPrice) {
        File baseDir = getBaseDir(context);
        if (!baseDir.exists() && !baseDir.mkdirs()) {
            return "";
        }
        File receiptFile = new File(baseDir, RECEIPT_FILE_NAME);
        File imageFile = new File(baseDir, IMAGE_FILE_NAME);

        StringBuilder builder = new StringBuilder();
        builder.append("购物车结算清单").append('\n');
        for (CartItem item : items) {
            builder.append(item.getProduct().getName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" = ")
                    .append(item.getTotalPrice())
                    .append("元")
                    .append('\n');
        }
        builder.append("总金额: ").append(totalPrice).append("元");

        try {
            writeReceiptFile(receiptFile, builder.toString());
            writeImage(context, items, imageFile);
            return readReceiptPreview(receiptFile);
        } catch (IOException e) {
            return "";
        }
    }

    private static void writeImage(Context context, List<CartItem> items, File imageFile) throws IOException {
        if (items.isEmpty()) {
            return;
        }
        try (InputStream inputStream = context.getResources().openRawResource(items.get(0).getProduct().getImageResId());
             FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            }
        }
    }

    private static String readReceiptPreview(File receiptFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(receiptFile)))) {
            String line = reader.readLine();
            return line == null ? "" : line;
        }
    }

    private static void writeReceiptFile(File receiptFile, String content) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(receiptFile))) {
            writer.write(content);
            writer.flush();
        }
    }

    private static File getBaseDir(Context context) {
        File externalRoot = context.getExternalFilesDir(null);
        if (externalRoot == null) {
            return new File(context.getFilesDir(), DIRECTORY_NAME);
        }
        return new File(externalRoot, DIRECTORY_NAME);
    }
}
