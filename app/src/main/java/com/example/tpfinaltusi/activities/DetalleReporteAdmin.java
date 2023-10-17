package com.example.tpfinaltusi.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Informe;

import java.text.SimpleDateFormat;

public class DetalleReporteAdmin extends AppCompatActivity {
    TextView tvTitulo;
    TextView tvDescripcion;
    TextView tvInfo;
    ImageView ivImagen;
    ProgressBar progressBar;
    LinearLayout layoutInvisible;
    ImageView btnBack;
    LinearLayout btnGoogleMaps;
    Button btnAprobarInforme;
    Button btnCancelarInforme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //////////////////////////////////CONFIGURACION ACTION BAR////////////////////////////////////////////
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
        btnBack = customActionBarView.findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        actionBarTitle.setText("Reporte");
        setContentView(R.layout.activity_aprobacion_detalle_reporte);
        //////////////////////////////////TRAER DATOS////////////////////////////////////////////
        Intent intent = getIntent();
        if (intent != null) {
            /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
            tvTitulo = findViewById(R.id.tv_titulo);
            tvDescripcion = findViewById(R.id.tv_cuerpo);
            ivImagen = findViewById(R.id.iv_imagen);
            tvInfo = findViewById(R.id.infoTextView);
            progressBar = findViewById(R.id.progressBar);
            layoutInvisible = findViewById(R.id.layoutInvisible);
            btnGoogleMaps = findViewById(R.id.btn_verMaps);
            btnAprobarInforme = findViewById(R.id.btn_aprobarInforme);
            btnCancelarInforme = findViewById(R.id.btn_cancelarInforme);
            /////////////////////////////////////OBTENER DATOS///////////////////////////////////////////////
            int id = intent.getIntExtra("id_informe", -1);
            cargarReporte(id);
        }
    }

    private void cargarReporte(int id) {
        InformeNegocio informeNegocio = new InformeNegocio();
        progressBar.setVisibility(View.VISIBLE);
        informeNegocio.traerInformePorId(id, new InformeNegocio.InformeCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onInformeLoaded(Informe informe) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layoutInvisible.setVisibility(View.VISIBLE);
                        tvTitulo.setText(informe.getTitulo());
                        tvDescripcion.setText(informe.getCuerpo());
                        tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informe.getFechaAlta()));
                        String pureBase64Encoded = informe.getImagen().substring(informe.getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        ivImagen.setImageBitmap(bitmap);
                        progressBar.setVisibility(View.GONE);
                        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                    i.putExtra("latitud",informe.getLatitud());
                                    i.putExtra("longitud",informe.getLongitud());
                                    i.putExtra("tagmaps",informe.getTitulo());
                                    startActivity(i);
                                }catch (Exception e){
                                    System.out.println(e);
                                }
                            }
                        });
                        btnAprobarInforme.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                        btnCancelarInforme.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
