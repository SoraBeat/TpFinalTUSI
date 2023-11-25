package com.example.tpfinaltusi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.Login;
import com.example.tpfinaltusi.adicionales.ReporteAdapter;
import com.example.tpfinaltusi.adicionales.ReporteAdapterAdmin;
import com.example.tpfinaltusi.entidades.Informe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAprobacionReportes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAprobacionReportes extends Fragment implements PopupMenu.OnMenuItemClickListener {

    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spinnerEstados;
    int filtradoEstado = 0;

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
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
/*
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBar.setElevation(0);
        }*/

        // Inflar la vista personalizada
        View customActionBarView = actionBar.getCustomView();

        ImageButton img_config = customActionBarView.findViewById(R.id.menu_overflow);

        img_config.setVisibility(view.VISIBLE);

        img_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireActivity(), img_config);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_overflow, popupMenu.getMenu());
                // Obtén el menú del PopupMenu
                Menu menu = popupMenu.getMenu();

                // Obtiene los elementos de menú y establece un color para el texto
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem menuItem = menu.getItem(i);
                    SpannableString spannableString = new SpannableString(menuItem.getTitle());
                    spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
                    menuItem.setTitle(spannableString);
                }
                popupMenu.setOnMenuItemClickListener(FragmentAprobacionReportes.this);
                popupMenu.show();
            }
        });
        //swipeRefreshLayout.setProgressViewEndTarget(false, 0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                cargarListaDeReportes(view);
            }
        });
        cargarListaDeReportes(view);
        spinnerEstados = view.findViewById(R.id.spinner);
        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View vieww, int position, long id) {
                // Obtener la opción seleccionada en el Spinner
                String opcion = adapterView.getItemAtPosition(position).toString();
                switch (opcion){
                    case "Activos":
                        filtradoEstado = 1;
                        break;
                    case "Pendientes":
                        filtradoEstado = 2;
                        break;
                    case "Revisiones":
                        filtradoEstado = 3;
                        break;
                    case "Terminados":
                        filtradoEstado = 4;
                        break;
                    default:
                        filtradoEstado = 0;
                        break;
                }
                cargarListaDeReportes(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                    switch (filtradoEstado){
                        case 1:
                            informes.removeIf(informe -> informe.getIdEstado() != 1);
                            break;
                        case 2:
                            informes.removeIf(informe -> informe.getIdEstado() != 2);
                            break;
                        case 3:
                            informes.removeIf(informe -> informe.getIdEstado() != 3);
                            break;
                        case 4:
                            informes.removeIf(informe -> informe.getIdEstado() != 4);
                            break;
                    }
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReportes);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    ReporteAdapterAdmin adapter = new ReporteAdapterAdmin(requireContext(), informes);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    spinnerEstados.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.menu_btn_cerrar) {
            // Elimina la variable de SharedPreferences "idUsuario"
            SharedPreferences preferences = getContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("idUsuario");
            editor.apply();

            // Navega de vuelta a la pantalla de inicio de sesión (LoginActivity)
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);

            // Asegúrate de que el fragmento se cierre o se realice alguna acción adicional si es necesario
            // return true; // Dependiendo de tus necesidades
        }

        // Maneja otras acciones de menú si es necesario

        return true;
    }
}