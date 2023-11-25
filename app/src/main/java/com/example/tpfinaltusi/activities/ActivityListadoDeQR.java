package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.CodigoQRNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.CustomListAdapter;
import com.example.tpfinaltusi.adicionales.CustomListAdapterQRListComplete;
import com.example.tpfinaltusi.entidades.CodigoQR;

import java.util.ArrayList;
import java.util.List;

public class ActivityListadoDeQR extends AppCompatActivity {
    private ImageView btnBack;
    private ListView lista;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_de_qr);
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
        actionBarTitle.setText("Listado de QRs");

        //////////////////////////////////TRAER ELEMENTOS////////////////////////////////////////////
        lista = findViewById(R.id.listaQR);
        progressBar = findViewById(R.id.progressBar);

        traerListaQR();
    }
    private void traerListaQR() {
        lista.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        CodigoQRNegocio codigoQRNegocio = new CodigoQRNegocio();
        // Llama al método para traer todos los códigos QR
        codigoQRNegocio.traerTodosLosCodigoQRs(new CodigoQRNegocio.CodigoQRsCallback() {
            @Override
            public void onCodigoQRsLoaded(List<CodigoQR> codigoQRs) {
                List<CodigoQR> listaFiltrada = new ArrayList<>();
                for (CodigoQR codigoQR : codigoQRs
                ) {
                    if (codigoQR.isCanjeado()) {
                        listaFiltrada.add(codigoQR);
                    }
                }
                CustomListAdapterQRListComplete codigoQRAdapter = new CustomListAdapterQRListComplete(getBaseContext(), getSupportFragmentManager(),listaFiltrada);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Crea un adaptador personalizado y configura el ListView
                        lista.setAdapter(codigoQRAdapter);
                        progressBar.setVisibility(View.GONE);
                        lista.setVisibility(View.VISIBLE);
                    }
                });


            }
        });
    }
}