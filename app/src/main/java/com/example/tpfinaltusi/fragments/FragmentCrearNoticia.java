package com.example.tpfinaltusi.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.tpfinaltusi.Negocio.NoticiaNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.CrearNoticia;
import com.example.tpfinaltusi.adicionales.NoticiaAdapter;
import com.example.tpfinaltusi.adicionales.NoticiaAdapterAdmin;
import com.example.tpfinaltusi.entidades.Noticia;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentCrearNoticia extends Fragment {
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    private Button buttonCrearNoticia;

    public FragmentCrearNoticia() {
        // Required empty public constructor
    }

    public static FragmentCrearNoticia newInstance(String param1, String param2) {
        FragmentCrearNoticia fragment = new FragmentCrearNoticia();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_crear_noticia, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swapRefresh);
        buttonCrearNoticia = view.findViewById(R.id.btnCrearNoticia);
        //swipeRefreshLayout.setProgressViewEndTarget(false, 0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                cargarListaDeNoticias(view);
            }
        });
        buttonCrearNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), CrearNoticia.class);
                startActivity(i);
            }
        });
        cargarListaDeNoticias(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarListaDeNoticias(getView());
    }

    private void cargarListaDeNoticias(View view) {
        progressBar.setVisibility(View.VISIBLE);
        buttonCrearNoticia.setVisibility(View.GONE);
        NoticiaNegocio noticiaNegocio = new NoticiaNegocio();
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
                    NoticiaAdapterAdmin adapter = new NoticiaAdapterAdmin(requireContext(), noticias);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    buttonCrearNoticia.setVisibility(View.VISIBLE);
                } else {
                    // Maneja el caso en el que no se obtuvieron noticias
                    progressBar.setVisibility(View.GONE);
                    buttonCrearNoticia.setVisibility(View.VISIBLE);
                    // Puedes mostrar un mensaje o realizar alguna acción en este caso
                }
            }
            @Override
            public void onError(String error) {
                System.out.println("ERROR AL TRAER NOTICIAS");
            }
        });
    }
}