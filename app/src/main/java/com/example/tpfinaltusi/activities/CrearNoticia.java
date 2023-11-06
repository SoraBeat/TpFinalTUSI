package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.NoticiaNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Noticia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrearNoticia extends AppCompatActivity {

    private ImageView imgElegida;
    private Button btnElegirArchivo;
    private EditText etTitulo;
    private EditText etDescripcion;
    private Spinner spTipo;
    private EditText etNombreUbicacion;
    private EditText etLatitud;
    private EditText etLongitud;
    private Button btnCrear;
    private boolean eligioImagen=false;
    private ImageView btnBack;
    private static final int PICK_IMAGE = 1;
    private String imagenFinal="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_noticia);
        //////////////////////////////////CONFIGURACION ACTION BAR////////////////////////////////////////////
        // Habilitar ActionBar y configurar vista personalizada
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        // Desactivar la elevación de la ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        // Inflar la vista personalizada
        View customActionBarView = getSupportActionBar().getCustomView();

        // Configurar el título centrado (opcional)
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        btnBack = customActionBarView.findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        actionBarTitle.setText("Crear Noticia/Evento");

        //////////////////////////////////TRAER DATOS////////////////////////////////////////////
        imgElegida = findViewById(R.id.imgElegida);
        btnElegirArchivo = findViewById(R.id.btn_ElegirArchivo);
        etTitulo = findViewById(R.id.et_Titulo);
        etDescripcion = findViewById(R.id.et_Descripcion);
        spTipo = findViewById(R.id.spinnerTipo);
        etNombreUbicacion = findViewById(R.id.et_NombreUbicacion);
        etLatitud = findViewById(R.id.et_Latitud);
        etLongitud = findViewById(R.id.et_Longitud);
        btnCrear = findViewById(R.id.btn_CrearNoticia);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        List<String> elementos = new ArrayList<>();
        elementos.add("Noticia");
        elementos.add("Evento");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elementos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comportamientoCrearNoticia();
            }
        });

        btnElegirArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Solicitar permisos para leer almacenamiento externo si no se han otorgado
                if (ContextCompat.checkSelfPermission(CrearNoticia.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CrearNoticia.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
                } else {
                    openGallery();
                }
            }
        });
    }
    private void comportamientoCrearNoticia(){
        if(!eligioImagen){
            Toast.makeText(this,"Debe elegir una imagen",Toast.LENGTH_SHORT).show();
            return;
        }
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String tipo = spTipo.getSelectedItem().toString();
        String nombreUbicacion = etNombreUbicacion.getText().toString().trim();
        float latitud = Float.valueOf(etLatitud.getText().toString().trim());
        float longitud = Float.valueOf(etLongitud.getText().toString().trim());

        if(etTitulo.getText().toString().trim().equals("")){
            etTitulo.setError("Campo requerido");
        }
        else if(etDescripcion.getText().toString().trim().equals("")){
            etDescripcion.setError("Campo requerido");
        }
        else if(etNombreUbicacion.getText().toString().trim().equals("")){
            etNombreUbicacion.setError("Campo requerido");
        }
        else if(etLatitud.getText().toString().trim().equals("")){
            etLatitud.setError("Campo requerido");
        }
        else if(etLongitud.getText().toString().trim().equals("")){
            etLongitud.setError("Campo requerido");
        }else{

            Noticia noticia = new Noticia(titulo,descripcion,tipo,imagenFinal, Date.valueOf(LocalDate.now().toString()),1,latitud,longitud,nombreUbicacion);
            NoticiaNegocio noticiaNegocio = new NoticiaNegocio();
            noticiaNegocio.crearNoticia(noticia, new NoticiaNegocio.NoticiaCallback() {
                @Override
                public void onSuccess(String mensaje) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Noticia creada",Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Error al crear noticia",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onNoticiaLoaded(Noticia noticia) {

                }
            });
        }

    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permiso denegado, puedes mostrar un mensaje o tomar alguna acción
                Toast.makeText(this, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                String base64String = encodeImageToBase64(bitmap);
                imgElegida.setImageBitmap(bitmap);
                imagenFinal = base64String;
                eligioImagen=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImageToBase64(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}