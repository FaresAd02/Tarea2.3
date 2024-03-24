package com.example.tarea23.database;

import android.content.Context;
import com.example.tarea23.models.Photograph;
import java.util.ArrayList;

public class DatabaseManager {
    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<Photograph> getAllPhotographs() {
        return dbHelper.getAllPhotographs();
    }

    public long addPhotograph(Photograph photograph) {
        return dbHelper.addPhotograph(photograph);
    }

    public int updatePhotograph(Photograph photograph) {
        return dbHelper.updatePhotograph(photograph);
    }

    public void deletePhotograph(int photographId) {
        dbHelper.deletePhotograph(photographId);
    }

    public Photograph getPhotograph(int id) {
        return dbHelper.getPhotograph(id);
    }
}
