package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.CanjeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.ListItemCanjeAdapter;
import com.example.tpfinaltusi.entidades.Canje;

import java.util.List;

public class ActivityListCanje extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflar el diseño antes de obtener referencias a las vistas
        setContentView(R.layout.activity_list_canje);

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
        actionBarTitle.setText("Listar Canje");


        listView = findViewById(R.id.lvlistar);

        CanjeNegocio canjeNegocio = new CanjeNegocio();
        SharedPreferences sharedPreferences = getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
        canjeNegocio.traerCanjePorIdUsuario(sharedPreferences.getInt("idUsuario", -1), new CanjeNegocio.CanjesCallback() {
            @Override
            public void onCanjesLoaded(List<Canje> canjes) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListItemCanjeAdapter listItemCanjeAdapter = new ListItemCanjeAdapter(ActivityListCanje.this,ActivityListCanje.this, canjes);
                        listView.setAdapter(listItemCanjeAdapter);
                        if (progressBar != null) {
                            progressBar.setVisibility(View.VISIBLE); // o View.GONE según tus necesidades
                        }

                    }
                });
            }
        });


    }
}