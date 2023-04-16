package com.example.pcar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class image extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    int id;
    Bitmap bitmap;
    Activity context;
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;

    public image(Activity context, int i) {
        this.context = context;
        this.id = i;
        dbHelper = new imgSql(context);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new imgSql(this);
        db = dbHelper.getWritableDatabase();
    }

//    public Bitmap saveImg(int id) {
//        this.id = id;
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        context.startActivityForResult(intent, 1);
//        return bitmap;
//    }
    public void saveImg() {
        this.id = id;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, 1);

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
//
//            try {
//                InputStream inputStream = getContentResolver().openInputStream(uri);
//                byte[] bytes = getBytes(inputStream);
//               // Assuming that you have a byte array named "byteData" containing the image data
//                 bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                String imageName = String.valueOf(id);
//
//                ContentValues values = new ContentValues();
//                values.put("name", imageName);
//                values.put("image", bytes);
//
//                long newRowId = db.insert("images", null, values);
//                Toast.makeText(this, "Image saved with ID: " + newRowId, Toast.LENGTH_LONG).show();
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    private byte[] getBytes(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//        int len;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//        return byteBuffer.toByteArray();
//    }
}
