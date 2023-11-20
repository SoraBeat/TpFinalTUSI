package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.Calendar;
import java.util.List;

public class ActivityModificarNoticia extends AppCompatActivity {

    private ImageView imgElegida;
    private Button btnElegirArchivo;
    private EditText etTitulo;
    private EditText etDescripcion;
    private Spinner spTipo;
    private EditText etNombreUbicacion;
    private EditText etLatitud;
    private EditText etLongitud;
    private Button btnModificar;
    private boolean eligioImagen=false;
    private ImageView btnBack;
    private static final int PICK_IMAGE = 1;
    private String imagenFinal="";
    private int idFinal;
    private LinearLayout contenedor;
    private ProgressBar progressBar;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_noticia);
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
        actionBarTitle.setText("Modificar Noticia/Evento");

        //////////////////////////////////TRAER DATOS////////////////////////////////////////////
        imgElegida = findViewById(R.id.imgElegida);
        btnElegirArchivo = findViewById(R.id.btn_ElegirArchivo);
        etTitulo = findViewById(R.id.et_Titulo);
        etDescripcion = findViewById(R.id.et_Descripcion);
        spTipo = findViewById(R.id.spinnerTipo);
        etNombreUbicacion = findViewById(R.id.et_NombreUbicacion);
        etLatitud = findViewById(R.id.et_Latitud);
        etLongitud = findViewById(R.id.et_Longitud);
        btnModificar = findViewById(R.id.btn_ModificarNoticia);
        contenedor = findViewById(R.id.contenedor);
        progressBar = findViewById(R.id.progressBar);
        datePicker = findViewById(R.id.datePicker);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        List<String> elementos = new ArrayList<>();
        elementos.add("Noticia");
        elementos.add("Evento");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elementos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);

        comportamientoTraerDatos();
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comportamientoModificarNoticia();
            }
        });
        btnElegirArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Solicitar permisos para leer almacenamiento externo si no se han otorgado
                if (ContextCompat.checkSelfPermission(ActivityModificarNoticia.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ActivityModificarNoticia.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
                } else {
                    openGallery();
                }
            }
        });
    }
    private void comportamientoModificarNoticia(){
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
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date selectedDate = new Date(calendar.getTimeInMillis());

            Noticia noticia = new Noticia(idFinal,titulo,descripcion,tipo,imagenFinal,selectedDate ,1,latitud,longitud,nombreUbicacion);
            NoticiaNegocio noticiaNegocio = new NoticiaNegocio();
            noticiaNegocio.editarNoticia(noticia, new NoticiaNegocio.NoticiaCallback() {
                @Override
                public void onSuccess(String mensaje) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Noticia modificada",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    finish();
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Error al modificar noticia",Toast.LENGTH_SHORT).show();
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
    private void comportamientoTraerDatos(){
        contenedor.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String idNoticia = intent.getStringExtra("idNoticia");
        idFinal = Integer.parseInt(idNoticia);
        NoticiaNegocio noticiaNegocio = new NoticiaNegocio();
        noticiaNegocio.traerNoticiaPorId(Integer.parseInt(idNoticia), new NoticiaNegocio.NoticiaCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onNoticiaLoaded(Noticia noticia) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etTitulo.setText(noticia.getTitulo());
                        etDescripcion.setText(noticia.getCuerpo());
                        if(noticia.getCategoria().equals("Noticia")){
                            spTipo.setSelection(0);
                        }else{
                            spTipo.setSelection(1);
                        }
                        etNombreUbicacion.setText(noticia.getTagMaps());
                        etLatitud.setText(String.valueOf(noticia.getLatitud()));
                        etLongitud.setText(String.valueOf(noticia.getLongitud()));
                        imagenFinal = noticia.getImagen();
                        String pureBase64Encoded = noticia.getImagen().substring(noticia.getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imgElegida.setImageBitmap(bitmap);
                        // Obtener la fecha del objeto Date
                        Date fechaNoticia = (Date) noticia.getFechaAlta();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fechaNoticia);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        datePicker.init(year, month, day, null);
                        contenedor.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}