package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.CanjeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.ListItemCanjeAdapter;
import com.example.tpfinaltusi.entidades.Canje;

import java.util.ArrayList;
import java.util.List;

public class ActivityListCanje extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    ImageView btnBack;
    Spinner spinnerFiltro;
    private List<Canje> canjesFiltrados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflar el diseño antes de obtener referencias a las vistas
        setContentView(R.layout.activity_list_canje);

        // Obtener referencias a las vistas
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.lvlistar);
        spinnerFiltro = findViewById(R.id.spinnerFiltro);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Configurar el título centrado (opcional)
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Listar Canje");


        //////////////////////////////////CONFIGURACION FILTRO////////////////////////////////////////////
        // Configurar el ArrayAdapter para el Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.opciones_filtro, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(spinnerAdapter);
        spinnerFiltro.setVisibility(View.VISIBLE);

        // Configurar el Listener para el cambio de selección en el Spinner
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Obtener la opción seleccionada en el Spinner
                String opcion = adapterView.getItemAtPosition(position).toString();

                // Convertir la opción a un valor numérico (1, 2, o 3)
                int valorNumerico = 1;
                if (opcion.equals("Todo")) {
                    valorNumerico = 1;
                } else if (opcion.equals("Pendiente")) {
                    valorNumerico = 2;
                } else if (opcion.equals("Retirado")) {
                    valorNumerico = 3;
                }

                //////////////////////////////////CONFIGURACION NORMAL VISTA////////////////////////////////////////////
                CanjeNegocio canjeNegocio = new CanjeNegocio();
                SharedPreferences sharedPreferences = getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
                int finalValorNumerico = valorNumerico;
                canjeNegocio.traerCanjePorIdUsuario(sharedPreferences.getInt("idUsuario", -1), new CanjeNegocio.CanjesCallback() {
                    @Override
                    public void onCanjesLoaded(List<Canje> canjes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ListItemCanjeAdapter listItemCanjeAdapter = new ListItemCanjeAdapter(ActivityListCanje.this,ActivityListCanje.this,  filtrarPorEstado(canjes, finalValorNumerico), finalValorNumerico);
                                listView.setAdapter(listItemCanjeAdapter);
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.VISIBLE); // o View.GONE según tus necesidades
                                }

                            }
                        });
                    }
                });
                // Hacer algo con el valor numérico, por ejemplo, filtrar la lista
                // utilizando el valor en tu adaptador (no proporcionado en este código).
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // No se necesita implementación aquí
            }
        });

    }
    public List<Canje> filtrarPorEstado(List<Canje> canjes, int filtro) {
        if (canjesFiltrados == null) {
            canjesFiltrados = new ArrayList<>();
        } else {
            canjesFiltrados.clear();
        }


        for (Canje canje : canjes) {

            if(filtro == 1){
                canjesFiltrados.add(canje);
            }
            else if(filtro == 2 && canje.isEstado() == false){
                canjesFiltrados.add(canje);

            }
            else if(filtro == 3 && canje.isEstado() == true){
                canjesFiltrados.add(canje);
            }

        }
        return canjesFiltrados;
    }
}