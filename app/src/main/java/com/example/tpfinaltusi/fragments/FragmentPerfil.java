package com.example.tpfinaltusi.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.activity_canjear_puntos;
import com.example.tpfinaltusi.entidades.Usuario;

public class FragmentPerfil extends Fragment {
    TextView tv_nombre_perfil_usuario;
    Button btnCanjear;
    TextView tv_puntajeActual;
    public FragmentPerfil() {
        // Required empty public constructor
    }

    public static FragmentPerfil newInstance(String param1, String param2) {
        FragmentPerfil fragment = new FragmentPerfil();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View b = inflater.inflate(R.layout.fragment_perfil, container, false);

        tv_nombre_perfil_usuario = b.findViewById(R.id.tv_nombre_perfil_usuario);
        tv_puntajeActual = b.findViewById(R.id.tv_puntajeActual);
        btnCanjear = b.findViewById(R.id.btnCanjear);

        btnCanjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), activity_canjear_puntos.class);
                startActivity(i);
            }
        });

        int idUsuario = UsuarioNegocio.obtenerIDUsuario(b.getContext());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_nombre_perfil_usuario.setText("@"+usuario.getAlias().toString());
                        tv_puntajeActual.setText(String.valueOf(usuario.getCantPuntos()));
                    }
                });
            }

        });
        return b;
    }
}