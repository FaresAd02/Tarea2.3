package com.example.tarea23.database;
import com.example.tarea23.models.Photograph;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME = "Photographs.db";
    public  static final int DATABASE_VERSION = 1;
    public  static final String TABLE_NAME = "photographs";
    public  static final String COLUMN_ID = "_id";
    public  static final String COLUMN_IMAGE = "image";
    public  static final String COLUMN_DESCRIPTION = "description";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_IMAGE + " BLOB,"
                    + COLUMN_DESCRIPTION + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Photograph> getAllPhotographs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Photograph> photographs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));

                Photograph photograph = new Photograph(image, description);
                photographs.add(photograph);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return photographs;
    }

    public long addPhotograph(Photograph photograph) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, photograph.getImageBlob());
        values.put(COLUMN_DESCRIPTION, photograph.getDescription());

        db.insert(TABLE_NAME, null, values);

        return 0;
    }

    public int updatePhotograph(Photograph photograph) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, photograph.getImageBlob());
        values.put(COLUMN_DESCRIPTION, photograph.getDescription());

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(photograph.getId())});

        return 0;
    }

    public void deletePhotograph(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

    }


    public Photograph getPhotograph(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_IMAGE, COLUMN_DESCRIPTION},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            Photograph photograph = new Photograph(image, description);
            cursor.close();
            return photograph;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
}