package com.example.tarea23;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea23.adapters.PhotographAdapter;
import com.example.tarea23.database.DatabaseManager;
import com.example.tarea23.models.Photograph;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private PhotographAdapter adapter;
    private DatabaseManager dbManager;
    private FloatingActionButton addPhotoFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view_photographs);
        addPhotoFab = findViewById(R.id.add_photo_fab);
        dbManager = new DatabaseManager(this);

        ArrayList<Photograph> photographs = dbManager.getAllPhotographs();
        adapter = new PhotographAdapter(this, photographs, dbManager);
        listView.setAdapter(adapter);

        addPhotoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Photograph> updatedPhotographs = dbManager.getAllPhotographs();
        adapter.clear();
        adapter.addAll(updatedPhotographs);
        adapter.notifyDataSetChanged();
    }
}