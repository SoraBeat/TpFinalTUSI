package com.example.tpfinaltusi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.NoticiaNegocio;
import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Noticia;
import com.example.tpfinaltusi.entidades.Usuario;

import java.util.Date;
import java.util.List;

public class DetalleNoticia extends AppCompatActivity {
    TextView tvTitulo;
    TextView tvDescripcion;
    ImageView ivImagen;
    TextView tvFecha;
    ProgressBar progressBar;
    LinearLayout layoutInvisible;
    ImageView btnBack;
    LinearLayout btnGoogleMaps;

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
        actionBarTitle.setText("Noticia");
        setContentView(R.layout.activity_detalle_noticia);
        //////////////////////////////////TRAER DATOS////////////////////////////////////////////
        Intent intent = getIntent();
        if (intent != null) {
            /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
            tvTitulo = findViewById(R.id.tv_titulo);
            tvDescripcion = findViewById(R.id.tv_cuerpo);
            ivImagen = findViewById(R.id.iv_imagen);
            tvFecha = findViewById(R.id.dateTextView);
            progressBar = findViewById(R.id.progressBar);
            layoutInvisible = findViewById(R.id.layoutInvisible);
            btnGoogleMaps = findViewById(R.id.btn_verMaps);
            /////////////////////////////////////OBTENER DATOS///////////////////////////////////////////////
            int id = intent.getIntExtra("id_noticia", -1);
            cargarNoticia(id);
        }
    }

    private void cargarNoticia(int id) {
        NoticiaNegocio noticiaNegocio = new NoticiaNegocio();
        progressBar.setVisibility(View.VISIBLE);
        noticiaNegocio.traerNoticiaPorId(id, new NoticiaNegocio.NoticiaCallback() {
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
                        layoutInvisible.setVisibility(View.VISIBLE);
                        tvTitulo.setText(noticia.getTitulo());
                        tvDescripcion.setText(noticia.getCuerpo());
                        int mes = noticia.getFechaAlta().getMonth();
                        String fecha = "";
                        switch (mes) {
                            case 0:
                                fecha = noticia.getFechaAlta().getDate() + "\nEne.";
                                break;
                            case 1:
                                fecha = noticia.getFechaAlta().getDate() + "\nFeb.";
                                break;
                            case 2:
                                fecha = noticia.getFechaAlta().getDate() + "\nMar.";
                                break;
                            case 3:
                                fecha = noticia.getFechaAlta().getDate() + "\nAbr.";
                                break;
                            case 4:
                                fecha = noticia.getFechaAlta().getDate() + "\nMay.";
                                break;
                            case 5:
                                fecha = noticia.getFechaAlta().getDate() + "\nJun.";
                                break;
                            case 6:
                                fecha = noticia.getFechaAlta().getDate() + "\nJul.";
                                break;
                            case 7:
                                fecha = noticia.getFechaAlta().getDate() + "\nAgo.";
                                break;
                            case 8:
                                fecha = noticia.getFechaAlta().getDate() + "\nSept.";
                                break;
                            case 9:
                                fecha = noticia.getFechaAlta().getDate() + "\nOct.";
                                break;
                            case 10:
                                fecha = noticia.getFechaAlta().getDate() + "\nNov.";
                                break;
                            case 11:
                                fecha = noticia.getFechaAlta().getDate() + "\nDic.";
                                break;
                            default:
                                System.out.println("Mes no válido");
                                break;
                        }
                        tvFecha.setText(fecha);
                        String pureBase64Encoded = noticia.getImagen().substring(noticia.getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        ivImagen.setImageBitmap(bitmap);
                        progressBar.setVisibility(View.GONE);
                        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                    i.putExtra("latitud",noticia.getLatitud());
                                    i.putExtra("longitud",noticia.getLongitud());
                                    i.putExtra("tagmaps",noticia.getTagMaps());
                                    startActivity(i);
                                }catch (Exception e){
                                    System.out.println(e);
                                }
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