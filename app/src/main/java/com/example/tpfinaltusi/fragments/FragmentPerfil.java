package com.example.tpfinaltusi.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.Login;
import com.example.tpfinaltusi.activities.activity_canjear_puntos;
import com.example.tpfinaltusi.entidades.Usuario;

public class FragmentPerfil extends Fragment implements PopupMenu.OnMenuItemClickListener{
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

        //////////////////////////////////CONFIGURACION ACTION BAR////////////////////////////////////////////

        // Habilitar ActionBar y configurar vista personalizada
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBar.setElevation(0);
        }

        // Inflar la vista personalizada
        View customActionBarView = actionBar.getCustomView();

        ImageButton img_config =customActionBarView.findViewById(R.id.menu_overflow);

        img_config.setVisibility(b.VISIBLE);

        img_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireActivity(), img_config);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_overflow, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(FragmentPerfil.this);
                popupMenu.show();
            }
        });

        //////////////////////////////////CONFIGURACION FRAGMENT////////////////////////////////////////////

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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.menu_btn_cerrar) {
            // Elimina la variable de SharedPreferences "idUsuario"
            SharedPreferences preferences = requireContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("idUsuario");
            editor.apply();

            // Navega de vuelta a la pantalla de inicio de sesión (LoginActivity)
            Intent intent = new Intent(requireContext(), Login.class);
            startActivity(intent);

            // Asegúrate de que el fragmento se cierre o se realice alguna acción adicional si es necesario
            // return true; // Dependiendo de tus necesidades
        }

        // Maneja otras acciones de menú si es necesario

        return true;
    }
}