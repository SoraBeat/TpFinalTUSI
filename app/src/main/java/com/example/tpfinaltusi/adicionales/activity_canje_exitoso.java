package com.example.tpfinaltusi.adicionales;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Usuario;

import color.tpfinaltusi.adicionales.ProductoAdapter;

public class activity_canje_exitoso extends AppCompatActivity {
    TextView tv_puntajeActual;
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

        // Configurar el título centrado (opcional)
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Canjer Puntos");
        setContentView(R.layout.activity_canjear_puntos);

        tv_puntajeActual = findViewById(R.id.tv_puntajeActual);

        // Llenar el ListView con el adaptador ProductoAdapter
        ProductoAdapter productoAdapter = new ProductoAdapter(this);

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