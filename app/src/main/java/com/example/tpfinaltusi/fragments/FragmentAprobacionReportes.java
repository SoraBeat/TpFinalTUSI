package com.example.tpfinaltusi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.ReporteAdapter;
import com.example.tpfinaltusi.entidades.Informe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAprobacionReportes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAprobacionReportes extends Fragment {

    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    public FragmentAprobacionReportes() {
        // Required empty public constructor
    }

    public static FragmentAprobacionReportes newInstance() {
        FragmentAprobacionReportes fragment = new FragmentAprobacionReportes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_aprobacion_reportes, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swapRefresh);
        //swipeRefreshLayout.setProgressViewEndTarget(false, 0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                cargarListaDeReportes(view);
            }
        });
        cargarListaDeReportes(view);
        return  view;
    }

    private void cargarListaDeReportes(View view) {
        progressBar.setVisibility(View.VISIBLE);
        InformeNegocio informeNegocio = new InformeNegocio();
        informeNegocio.traerTodosLosInformes(new InformeNegocio.InformesCallback() {
            @Override
            public void onInformesLoaded(List<Informe> informes) {
                if (informes != null && !informes.isEmpty()) {
                    // Ordenar la lista de noticias por fecha en orden descendente
                    Collections.sort(informes, new Comparator<Informe>() {
                        @Override
                        public int compare(Informe informe1, Informe informe2) {
                            // Compara las fechas de las noticias en orden descendente
                            return informe2.getFechaAlta().compareTo(informe1.getFechaAlta());
                        }
                    });
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReportes);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    ReporteAdapter adapter = new ReporteAdapter(requireContext(), informes);
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
}