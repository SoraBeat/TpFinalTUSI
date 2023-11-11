package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class CerrarInforme extends AppCompatActivity {
    ImageView btnBack;
    Button btnElegirArchivo;
    Button btnCerrarInforme;
    Button btnCancelar;
    private String IMG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        actionBarTitle.setText("Cerrar Informe");
        setContentView(R.layout.activity_cerrar_informe);
        btnBack = customActionBarView.findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();
            }
        });

        btnElegirArchivo = findViewById(R.id.btn_ElegirArchivo);
        btnElegirArchivo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                // Solicitar permisos para leer almacenamiento externo si no se han otorgado
                startActivityForResult(intent,1);
            }
        });
        btnCerrarInforme = findViewById(R.id.btn_CerrarInforme);
        btnCerrarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idInforme = getIntent().getIntExtra("id_informe", -1);
                int idUsuario = getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE).getInt("idUsuario",-1);
                if(idInforme!=-1 && idUsuario!=-1){
                    new InformeNegocio().revisionInforme(idInforme,idUsuario,IMG);
                    Toast.makeText(getApplicationContext(),"El informe pasó a estado de revisión.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnCancelar = findViewById(R.id.btn_Cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // La actividad se cerró correctamente
            Uri uri = data.getData();
            String path = uri.getPath();
            File img = new File(path);
            img.canRead();
            InputStream inputStream = null;
            try {
                ContentResolver resolver = getContentResolver();
                inputStream = resolver.openInputStream(uri);

                // Read the image into a byte array
                byte[] bytes = new byte[(int) inputStream.available()];
                inputStream.read(bytes);

                // Convert the byte array to Base64
                IMG = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {

                    }
                }
            }
            btnElegirArchivo.setVisibility(View.GONE);
            btnCerrarInforme.setVisibility(View.VISIBLE);
            btnCancelar.setVisibility(View.VISIBLE);
        }
    }

}