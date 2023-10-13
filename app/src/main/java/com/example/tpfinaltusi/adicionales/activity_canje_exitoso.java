package com.example.tpfinaltusi.adicionales;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Usuario;

public class activity_canje_exitoso extends AppCompatActivity {
    TextView tv_puntajeActual;
    TextView product_title;
    ImageView Imagen_canje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canje_exitoso);

        // CONFIGURACION ACTION BAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        View customActionBarView = getSupportActionBar().getCustomView();
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Canje Exitoso");

        // Obtén las vistas por sus IDs
        tv_puntajeActual = findViewById(R.id.tv_puntajeActual);
        product_title = findViewById(R.id.product_title);
        Imagen_canje = findViewById(R.id.Imagen_canje);

        // Recupera los datos pasados desde Intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");

            // Establece los datos en las vistas correspondientes
            product_title.setText(title);
            // Establece la imagen si la tienes
            // Imagen_canje.setImageResource(R.drawable.tu_imagen);
        }

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