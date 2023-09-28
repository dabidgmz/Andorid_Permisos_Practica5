package com.example.practica5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    private static final int REQUEST_IMAGE_PICK = 124;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos permiso, solicitarlo
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }

    public void pickImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Si tenemos permiso, abrir la galería
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        } else {
            // Si no tenemos permiso, mostrar un mensaje de advertencia y solicitar permiso nuevamente
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Si el usuario ha denegado el permiso anteriormente, muestra una explicación
            Toast.makeText(this, "Se requiere permiso para acceder a la galería", Toast.LENGTH_SHORT).show();
        }

        // Solicita el permiso
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario concedió el permiso, puedes abrir la galería
                pickImage(null); // Llamar a pickImage de nuevo para abrir la galería
            } else {
                // El usuario denegó el permiso, mostrar un mensaje de advertencia
                Toast.makeText(this, "Se requiere permiso para acceder a la galería", Toast.LENGTH_SHORT).show();
            }
        }

        // Llama al método super para asegurarte de que se manejen correctamente los permisos
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // El usuario seleccionó una imagen de la galería, muestra la imagen en ImageView
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            imageView.setVisibility(View.VISIBLE); // Mostrar ImageView
        }
    }
}

