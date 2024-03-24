package com.example.tarea23.adapters;

import com.example.tarea23.R;
import com.example.tarea23.models.Photograph;
import com.example.tarea23.database.DatabaseManager;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class PhotographAdapter extends ArrayAdapter<Photograph> {
    private DatabaseManager dbManager;
    public PhotographAdapter(Context context, ArrayList<Photograph> photographs, DatabaseManager dbManager) {
        super(context, 0, photographs);
        this.dbManager = dbManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.photograph_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);
        Button deleteButton = convertView.findViewById(R.id.button_delete);

        Photograph photograph = getItem(position);

        imageView.setImageBitmap(photograph.getImage());
        textView.setText(photograph.getDescription());

        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(photograph, position));

        return convertView;
    }

    private void showDeleteConfirmationDialog(Photograph photograph, int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this photograph?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    deletePhotograph(photograph, position);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deletePhotograph(Photograph photograph, int position) {
        dbManager.deletePhotograph(photograph.getId());
        remove(photograph);
        notifyDataSetChanged();
    }


}
