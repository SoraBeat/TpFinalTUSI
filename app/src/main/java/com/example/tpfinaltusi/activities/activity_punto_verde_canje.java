package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.PremioNegocio;
import com.example.tpfinaltusi.Negocio.PuntoVerdeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.ItemCanjeAdapter;
import com.example.tpfinaltusi.adicionales.PuntoVerdeAdapterCanje;
import com.example.tpfinaltusi.entidades.Premio;
import com.example.tpfinaltusi.entidades.PuntoVerde;

import java.util.List;

public class activity_punto_verde_canje extends AppCompatActivity {
    TextView tv_puntajeActual;
    ListView listView;
    ProgressBar progressBar;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflar el diseño antes de obtener referencias a las vistas
        setContentView(R.layout.activity_punto_verde_canje);

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

        btnBack = customActionBarView.findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);

        progressBar = findViewById(R.id.progressBar);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Configurar el título centrado (opcional)
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Elegir Punto Verde");

        listView = findViewById(R.id.lvlistar);


        // Llenar el ListView con el adaptador ProductoAdapter
        PuntoVerdeNegocio PuntoVerdeNegocio = new PuntoVerdeNegocio();

        PuntoVerdeNegocio.traerTodosLosPuntosVerdes(new PuntoVerdeNegocio.PuntoVerdesCallback() {
            @Override
            public void onPuntoVerdesLoaded(List<PuntoVerde> puntosVerdes) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Crear una instancia del adaptador y establecer la lista de productos
                            PuntoVerdeAdapterCanje PuntoVerdeAdapter = new PuntoVerdeAdapterCanje(activity_punto_verde_canje.this,activity_punto_verde_canje.this, puntosVerdes);
                            System.out.println(puntosVerdes.toString());
                            listView.setAdapter(PuntoVerdeAdapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
            }
        });
    }
}