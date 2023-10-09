package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.ProductoAdapter;
import com.example.tpfinaltusi.entidades.Usuario;

public class activity_canjear_puntos extends AppCompatActivity {

    TextView tv_puntajeActual;
    ListView listView;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        actionBarTitle.setText("Canjer Puntos");
        setContentView(R.layout.activity_canjear_puntos);

        tv_puntajeActual = findViewById(R.id.tv_puntajeActual);
        listView = findViewById(R.id.lvlistar);

        // Llenar el ListView con el adaptador ProductoAdapter
        ProductoAdapter productoAdapter = new ProductoAdapter(this);
        listView.setAdapter(productoAdapter);

        traer_puntaje();
    }

    private void traer_puntaje(){
        int idUsuario = UsuarioNegocio.obtenerIDUsuario(getApplicationContext());
        UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
        usuarioNegocio.buscarUsuarioPorId(idUsuario, new UsuarioNegocio.UsuarioCallback() {
            @Override
            public void onSuccess(String mensaje) {
                // Handle success
            }

            @Override
            public void onError(String error) {
                // Handle error
            }

            @Override
            public void onUsuarioLoaded(Usuario usuario) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_puntajeActual.setText(String.valueOf(usuario.getCantPuntos()));
                    }
                });
            }

        });
    }
}