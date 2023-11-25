package com.example.tpfinaltusi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.Negocio.Informe_HistorialNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.GraficoContaminacion;
import com.example.tpfinaltusi.activities.HomeActivity;
import com.example.tpfinaltusi.adicionales.NovedadAdapter;
import com.example.tpfinaltusi.adicionales.ReporteAdapter;
import com.example.tpfinaltusi.entidades.Informe;
import com.example.tpfinaltusi.entidades.Informe_Historial;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentReporte#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentReporte extends Fragment {

    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    Button ivGraficoContaminacion;
    Spinner spinner;
    int estadoElegido = 0;

    public FragmentReporte() {
        // Required empty public constructor
    }

    public static FragmentReporte newInstance() {
        FragmentReporte fragment = new FragmentReporte();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_reporte, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swapRefresh);
        //swipeRefreshLayout.setProgress ViewEndTarget(false, 0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                carga(view);
            }
        });

        ivGraficoContaminacion = view.findViewById(R.id.ivGraficoContaminacion);
        ivGraficoContaminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = requireContext();
                Intent i = new Intent(context, GraficoContaminacion.class);
                context.startActivity(i);
            }
        });
        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View vieww, int i, long l) {
                // Obtener la opción seleccionada en el Spinner
                String opcion = adapterView.getItemAtPosition(i).toString();
                switch (opcion){
                    case "Novedades":
                        estadoElegido = -1;
                        break;
                    case "Activos":
                        estadoElegido = 1;
                        break;
                    case "Pendientes":
                        estadoElegido = 2;
                        break;
                    case "Revisiones":
                        estadoElegido = 3;
                        break;
                    case "Terminados":
                        estadoElegido = 4;
                        break;
                    default:
                        estadoElegido = 0;
                        break;
                }
                carga(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cargarListaDeReportes(view);


        return  view;
    }
    private void carga(View view){
        if(estadoElegido == -1){
            RecyclerView recyclerViewNovedades = view.findViewById(R.id.recyclerViewNovedades);
            recyclerViewNovedades.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReportes);
            recyclerView.setVisibility(View.GONE);
            cargarListaDeNovedades(view);
        } else {
            RecyclerView recyclerViewNovedades = view.findViewById(R.id.recyclerViewNovedades);
            recyclerViewNovedades.setVisibility(View.GONE);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReportes);
            recyclerView.setVisibility(View.VISIBLE);
            cargarListaDeReportes(view);
        }
    }
    private void cargarListaDeReportes(View view) {
        progressBar.setVisibility(View.VISIBLE);
        InformeNegocio informeNegocio = new InformeNegocio();
        informeNegocio.traerTodosLosInformes(new InformeNegocio.InformesCallback() {
            @Override
            public void onInformesLoaded(List<Informe> informes) {
                if (isAdded()) {
                    if (informes != null && !informes.isEmpty()) {
                        // Ordenar la lista de noticias por fecha en orden descendente
                        Collections.sort(informes, new Comparator<Informe>() {
                            @Override
                            public int compare(Informe informe1, Informe informe2) {
                                // Compara las fechas de las noticias en orden descendente
                                return informe2.getFechaAlta().compareTo(informe1.getFechaAlta());
                            }
                        });
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UsuarioGuardado", Context.MODE_PRIVATE);
                        int idUsuario = sharedPreferences.getInt("idUsuario",-1);
                        if(estadoElegido != 0){
                            informes.removeIf(inf -> inf.getIdEstado() != estadoElegido);
                            informes.removeIf(inf -> estadoElegido == 2 && inf.getUsuarioAlta()!=idUsuario);
                            informes.removeIf(inf -> estadoElegido == 3 && inf.getUsuarioBaja()!=idUsuario);
                            informes.removeIf(inf -> estadoElegido == 4 && inf.getUsuarioBaja()!=idUsuario);
                        } else {
                            informes.removeIf(inf -> inf.getIdEstado() == 2 && inf.getUsuarioAlta()!=idUsuario);
                            informes.removeIf(inf -> inf.getIdEstado() == 3 && inf.getUsuarioBaja()!=idUsuario);
                            informes.removeIf(inf -> inf.getIdEstado() == 4 && inf.getUsuarioBaja()!=idUsuario);
                        }

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
            }
            @Override
            public void onError(String error) {
                System.out.println("ERROR AL TRAER NOTICIAS");
            }
        });
    }
    private void cargarListaDeNovedades(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Informe_HistorialNegocio informeNegocio = new Informe_HistorialNegocio();
        informeNegocio.traerTodosLosInformesHistoriales(new Informe_HistorialNegocio.Informe_HistorialesCallback() {
            @Override
            public void onInformeHistorialesLoaded(List<Informe_Historial> informes) {
                if (isAdded()) {
                    if (informes != null && !informes.isEmpty()) {
                        // Ordenar la lista de noticias por fecha en orden descendente
                        Collections.sort(informes, new Comparator<Informe_Historial>() {
                            @Override
                            public int compare(Informe_Historial informe1, Informe_Historial informe2) {
                                // Compara las fechas de las noticias en orden descendente
                                return informe2.getFecha().compareTo(informe1.getFecha());
                            }
                        });
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UsuarioGuardado", Context.MODE_PRIVATE);
                        int idUsuario = sharedPreferences.getInt("idUsuario",-1);
                        informes.removeIf(informeHistorial -> informeHistorial.isOcultar() || informeHistorial.getIdUsuario() != idUsuario);
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNovedades);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        NovedadAdapter adapter = new NovedadAdapter(requireContext(), informes);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // Maneja el caso en el que no se obtuvieron noticias
                        progressBar.setVisibility(View.GONE);
                        // Puedes mostrar un mensaje o realizar alguna acción en este caso
                    }
                }
            }
        });
    }
}