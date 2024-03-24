package com.example.tarea23.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

public class Photograph {
    private int id;
    private Bitmap image;
    private String description;

    public Photograph(int id, Bitmap image, String description) {
        this.id = id;
        this.image = image;
        this.description = description;
    }

    public Photograph(Bitmap image, String description) {
        this(-1, image, description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public byte[] getImageBlob() {
        if (image != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        }
        return null;
    }

    public void setImageFromBlob(byte[] imageBlob) {
        if (imageBlob != null) {
            this.image = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
        }
    }

    public static Bitmap convertBlobToBitmap(byte[] imageBlob) {
        if (imageBlob != null && imageBlob.length > 0) {
            return BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
        }
        return null;
    }
}
