package com.example.tpfinaltusi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tpfinaltusi.Negocio.NoticiaNegocio;
import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.NoticiaAdapter;
import com.example.tpfinaltusi.entidades.Noticia;
import com.example.tpfinaltusi.entidades.Usuario;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentNoticia extends Fragment {
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    public FragmentNoticia() {
        // Required empty public constructor
    }

    public static FragmentNoticia newInstance() {
        FragmentNoticia fragment = new FragmentNoticia();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_noticia, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swapRefresh);
        //swipeRefreshLayout.setProgressViewEndTarget(false, 0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                cargarListaDeNoticias();
            }
        });
        cargarListaDeNoticias();
        return  view;
    }
    private void cargarListaDeNoticias() {
        progressBar.setVisibility(View.VISIBLE);
        NoticiaNegocio noticiaNegocio = new NoticiaNegocio();
        noticiaNegocio.traerTodasLasNoticias(new NoticiaNegocio.NoticiasCallback() {
            @Override
            public void onNoticiasLoaded(List<Noticia> noticias) {
                // Ordenar la lista de noticias por fecha en orden descendente
                Collections.sort(noticias, new Comparator<Noticia>() {
                    @Override
                    public int compare(Noticia noticia1, Noticia noticia2) {
                        // Compara las fechas de las noticias en orden descendente
                        return noticia2.getFechaAlta().compareTo(noticia1.getFechaAlta());
                    }
                });
                RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewNoticias);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                NoticiaAdapter adapter = new NoticiaAdapter(requireContext(), noticias);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onError(String error) {
                System.out.println("ERROR AL TRAER NOTICIAS");
            }
        });
    }
}