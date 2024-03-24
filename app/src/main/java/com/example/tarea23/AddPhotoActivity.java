package com.example.tarea23;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.widget.Toast;

import com.example.tarea23.database.DatabaseManager;
import com.example.tarea23.models.Photograph;

import java.io.IOException;

public class AddPhotoActivity extends AppCompatActivity {

    private EditText editTextDescription;
    private ImageView imageViewPhoto;
    private Button buttonSave;
    private DatabaseManager dbManager;
    private Photograph photograph;
    private int photographId = -1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        editTextDescription = findViewById(R.id.editTextDescription);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonSave = findViewById(R.id.buttonSave);
        dbManager = new DatabaseManager(this);

        photographId = getIntent().getIntExtra("PHOTOGRAPH_ID", -1);
        if (photographId != -1) {
            photograph = dbManager.getPhotograph(photographId);
            if (photograph != null) {
                imageViewPhoto.setImageBitmap(photograph.getImage());
                editTextDescription.setText(photograph.getDescription());
            }
        }
        selectImage();
        buttonSave.setOnClickListener(v -> savePhoto());
    }

    private void selectImage() {
        final CharSequence[] options = { "Tomar foto", "Elegir de la galería", "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);
        builder.setTitle("Agregar Foto");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Tomar foto")) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            } else if (options[item].equals("Elegir de la galería")) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent , REQUEST_IMAGE_PICK);
            } else if (options[item].equals("Cancelar")) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } else {
                Toast.makeText(this, "Se necesita permiso de cámara", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void savePhoto() {
        String description = editTextDescription.getText().toString();
        if (photograph != null && photograph.getImage() != null && !description.isEmpty()) {
            photograph.setDescription(description);
            if (photograph.getId() == -1) {
                long id = dbManager.addPhotograph(photograph);
                if (id > -1) {
                    photograph.setId((int) id);
                    Toast.makeText(this, "Fotografía guardada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar la fotografía", Toast.LENGTH_SHORT).show();
                }
            } else {
                int affectedRows = dbManager.updatePhotograph(photograph);
                if (affectedRows > 0) {
                    Toast.makeText(this, "Fotografía actualizada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar la fotografía", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Debe tomar una foto y escribir una descripción", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageViewPhoto.setImageBitmap(imageBitmap);
                if (photograph == null) {
                    photograph = new Photograph(imageBitmap, editTextDescription.getText().toString());
                } else {
                    photograph.setImage(imageBitmap);
                    photograph.setDescription(editTextDescription.getText().toString());
                }
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imageViewPhoto.setImageBitmap(bitmap);
                    if (photograph == null) {
                        photograph = new Photograph(bitmap, editTextDescription.getText().toString());
                    } else {
                        photograph.setImage(bitmap);
                        photograph.setDescription(editTextDescription.getText().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}