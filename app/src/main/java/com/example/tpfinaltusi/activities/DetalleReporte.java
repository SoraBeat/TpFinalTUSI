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
import com.example.tpfinaltusi.Negocio.Informe_HistorialNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Informe;
import com.example.tpfinaltusi.entidades.Informe_Historial;

import java.text.SimpleDateFormat;

public class DetalleReporte extends AppCompatActivity {
    TextView tvTitulo;
    TextView tvDescripcion;
    TextView tvInfo;
    ImageView ivImagen;
    ProgressBar progressBar;
    LinearLayout layoutInvisible;
    ImageView btnBack;
    LinearLayout btnGoogleMaps;
    Button btnCerrarInforme;
    TextView tvPuntos;
    TextView tvPuntosInfo;

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
        setContentView(R.layout.activity_detalle_reporte);
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
            btnCerrarInforme = findViewById(R.id.btn_cerrarInforme);
            tvPuntos = findViewById(R.id.tvPuntos);
            tvPuntosInfo = findViewById(R.id.tvPuntosInfo);
            /////////////////////////////////////OBTENER DATOS///////////////////////////////////////////////
            int id = intent.getIntExtra("id_informe", -1);
            int idInformeHistorial = intent.getIntExtra("idinforme_historial", -1);
            if(id != -1) cargarReporte(id);
            if(idInformeHistorial != -1) cargarHistorial(idInformeHistorial);
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
                        switch (informe.getIdEstado()){
                            case 1:
                                tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informe.getFechaAlta())+ " - Activo");
                                break;
                            case 2:
                                tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informe.getFechaAlta())+ " - Pendiente");
                                break;
                            case 3:
                                tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informe.getFechaAlta())+ " - En Revision");
                                break;
                            case 4:
                                tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informe.getFechaAlta())+ " - Terminado");
                                break;
                        }
                        String pureBase64Encoded = informe.getImagen().substring(informe.getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        ivImagen.setImageBitmap(bitmap);
                        progressBar.setVisibility(View.GONE);
                        layoutInvisible.setVisibility(View.VISIBLE);
                        tvPuntos.setText(informe.getPuntosRecompensa());
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
                        if(informe.getIdEstado()==1){
                            btnCerrarInforme.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View view) {
                                    try{
                                        Intent i = new Intent(getApplicationContext(), CerrarInforme.class);
                                        i.putExtra("id_informe",getIntent().getIntExtra("id_informe", -1));
                                        startActivity(i);
                                    } catch(Exception e){
                                        System.out.println(e);
                                    }
                                }
                            });
                        } else {
                            btnCerrarInforme.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
    private void cargarHistorial(int id){
        progressBar.setVisibility(View.VISIBLE);
        new Informe_HistorialNegocio().traerInformeHistorialPorId(id, new Informe_HistorialNegocio.Informe_HistorialCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onInformeHistorialLoaded(Informe_Historial informeHistorial) {
                tvTitulo.setText(informeHistorial.getTitulo());
                String pureBase64Encoded = informeHistorial.getIMG().substring(informeHistorial.getIMG().indexOf(",") + 1);
                byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivImagen.setImageBitmap(bitmap);
                switch (informeHistorial.getIdEstado()){
                    case 0:
                        tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informeHistorial.getFecha())+ " - El reporte fue rechazado");
                        break;
                    case 1:
                        if(informeHistorial.isResultado()){
                            tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informeHistorial.getFecha())+ " - El reporte fue aprobado");
                        } else {
                            tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informeHistorial.getFecha())+ " - El reporte fue rechazado");
                        }
                        break;
                    case 2:
                        tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informeHistorial.getFecha())+ " - El reporte será revisado");
                        break;
                    case 3:
                        tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informeHistorial.getFecha())+ " - El reporte será revisado");
                        break;
                    case 4:
                        tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informeHistorial.getFecha())+ " - El reporte se cerró exitosamente");
                        break;
                }
                tvDescripcion.setText(informeHistorial.getCuerpo());
                btnGoogleMaps.setVisibility(View.GONE);
                tvPuntos.setVisibility(View.GONE);
                tvPuntosInfo.setVisibility(View.GONE);
                btnCerrarInforme.setText("Cerrar Novedad");
                btnCerrarInforme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Informe_HistorialNegocio().ocultarInforme_Historial(informeHistorial.getIdInforme_Historial(), new Informe_HistorialNegocio.Informe_HistorialCallback() {
                            @Override
                            public void onSuccess(String mensaje) {
                                finish();
                            }

                            @Override
                            public void onError(String error) {

                            }

                            @Override
                            public void onInformeHistorialLoaded(Informe_Historial informeHistorial) {

                            }
                        });
                    }
                });
                progressBar.setVisibility(View.GONE);
                layoutInvisible.setVisibility(View.VISIBLE);
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
