package com.example.tpfinaltusi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.UsuarioDataSourceFactory;
import com.example.tpfinaltusi.adicionales.UsuarioPagedListAdapter;
import com.example.tpfinaltusi.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FragmentUsuariosAdmin extends Fragment {
    private RecyclerView recyclerView;
    private UsuarioPagedListAdapter adapter;
    private Button buttonBuscar;

    private Spinner spinnerFiltro;
    private EditText editTextFiltro;


    public FragmentUsuariosAdmin() {
        // Required empty public constructor
    }

    public static FragmentUsuariosAdmin newInstance(String param1, String param2) {
        FragmentUsuariosAdmin fragment = new FragmentUsuariosAdmin();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuarios_admin, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        buttonBuscar = view.findViewById(R.id.btnBuscar);
        editTextFiltro = view.findViewById(R.id.etFiltro);
        spinnerFiltro = view.findViewById(R.id.spinnerFiltro);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new UsuarioPagedListAdapter();
        recyclerView.setAdapter(adapter);

        cargarUsuarios();

        List<String> elementos = new ArrayList<>();
        elementos.add("Alias");
        elementos.add("Email");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, elementos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapter);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtroSeleccionado = spinnerFiltro.getSelectedItem().toString();
                cargarUsuariosConFiltro(filtroSeleccionado,editTextFiltro.getText().toString());
            }
        });

        return view;
    }
    private void cargarUsuariosConFiltro(String filtro, String valor) {
        if(filtro.equals("Alias")){
            UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
            usuarioNegocio.traerUsuarioPorAliasFiltro(valor, new UsuarioNegocio.UsuariosCallback() {
                @Override
                public void onUsuariosLoaded(List<Usuario> usuarios) {
                    PagedList.Config config = new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .build();

                    UsuarioDataSourceFactory factory = new UsuarioDataSourceFactory(usuarios, config);

                    LiveData<PagedList<Usuario>> pagedListLiveData = new LivePagedListBuilder<>(factory, config).build();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pagedListLiveData.observe(getViewLifecycleOwner(), pagedList -> {
                                // Actualizar tu adaptador con la nueva lista paginada
                                adapter.submitList(pagedList);
                            });
                        }
                    });
                }
            });
        } else if (filtro.equals("Email")) {
            UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
            usuarioNegocio.traerUsuarioPorEmailFiltro(valor, new UsuarioNegocio.UsuariosCallback() {
                @Override
                public void onUsuariosLoaded(List<Usuario> usuarios) {
                    PagedList.Config config = new PagedList.Config.Builder()
                            .setEnablePlaceholders(false)
                            .setPageSize(10)
                            .build();

                    UsuarioDataSourceFactory factory = new UsuarioDataSourceFactory(usuarios, config);

                    LiveData<PagedList<Usuario>> pagedListLiveData = new LivePagedListBuilder<>(factory, config).build();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pagedListLiveData.observe(getViewLifecycleOwner(), pagedList -> {
                                // Actualizar tu adaptador con la nueva lista paginada
                                adapter.submitList(pagedList);
                            });
                        }
                    });
                }
            });
        }

    }
    private void cargarUsuarios() {
        UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
        usuarioNegocio.traerTodosLosUsuarios(new UsuarioNegocio.UsuariosCallback() {
            @Override
            public void onUsuariosLoaded(List<Usuario> usuarios) {
                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .build();

                UsuarioDataSourceFactory factory = new UsuarioDataSourceFactory(usuarios, config);

                LiveData<PagedList<Usuario>> pagedListLiveData = new LivePagedListBuilder<>(factory, config).build();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pagedListLiveData.observe(getViewLifecycleOwner(), pagedList -> {
                                // Actualizar tu adaptador con la nueva lista paginada
                                adapter.submitList(pagedList);
                            });
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                });

            }
        });
    }
}