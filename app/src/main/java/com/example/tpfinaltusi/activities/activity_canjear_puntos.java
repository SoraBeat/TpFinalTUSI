package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.NoticiaNegocio;
import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.NoticiaAdapter;
import com.example.tpfinaltusi.entidades.Noticia;
import com.example.tpfinaltusi.entidades.Premio;
import com.example.tpfinaltusi.entidades.Usuario;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class activity_canjear_puntos extends AppCompatActivity {

    TextView tv_puntajeActual;
    ListView listView;
    ProgressBar progressBar;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
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
        Premio premio = new Premio();
        color.tpfinaltusi.adicionales.ItemCanjeAdapter productoAdapter = new color.tpfinaltusi.adicionales.ItemCanjeAdapter(this, Premio premio);
        listView.setAdapter(productoAdapter);
        x
        traer_puntaje();
    }
    private void cargarListaDePremios(View view) {
        NoticiaNegocio noticiaNegocio = new NoticiaNegocio();

        progressBar.setVisibility(View.VISIBLE);
        noticiaNegocio.traerTodasLasNoticias(new NoticiaNegocio.NoticiasCallback() {
            @Override
            public void onNoticiasLoaded(List<Noticia> noticias) {
                if (noticias != null && !noticias.isEmpty()) {
                    // Ordenar la lista de noticias por fecha en orden descendente
                    Collections.sort(noticias, new Comparator<Noticia>() {
                        @Override
                        public int compare(Noticia noticia1, Noticia noticia2) {
                            // Compara las fechas de las noticias en orden descendente
                            return noticia2.getFechaAlta().compareTo(noticia1.getFechaAlta());
                        }
                    });
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNoticias);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    NoticiaAdapter adapter = new NoticiaAdapter(requireContext(), noticias);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    // Maneja el caso en el que no se obtuvieron noticias
                    progressBar.setVisibility(View.GONE);
                    // Puedes mostrar un mensaje o realizar alguna acción en este caso
                }
            }
            @Override
            public void onError(String error) {
                System.out.println("ERROR AL TRAER NOTICIAS");
            }
        });
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