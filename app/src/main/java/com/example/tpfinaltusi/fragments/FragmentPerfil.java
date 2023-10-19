package com.example.tpfinaltusi.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.Login;
import com.example.tpfinaltusi.activities.activity_canjear_puntos;
import com.example.tpfinaltusi.entidades.Usuario;

public class FragmentPerfil extends Fragment implements PopupMenu.OnMenuItemClickListener {
    TextView tv_nombre_perfil_usuario;
    Button btnCanjear;
    TextView tv_puntajeActual;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    ImageView imgMedal;

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
/*
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBar.setElevation(0);
        }*/

        // Inflar la vista personalizada
        View customActionBarView = actionBar.getCustomView();

        ImageButton img_config = customActionBarView.findViewById(R.id.menu_overflow);

        img_config.setVisibility(b.VISIBLE);

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
                popupMenu.setOnMenuItemClickListener(FragmentPerfil.this);
                popupMenu.show();
            }
        });

        //////////////////////////////////CONFIGURACION FRAGMENT////////////////////////////////////////////

        tv_nombre_perfil_usuario = b.findViewById(R.id.tv_nombre_perfil_usuario);
        tv_puntajeActual = b.findViewById(R.id.tv_puntajeActual);
        btnCanjear = b.findViewById(R.id.btnCanjear);
        progressBar = b.findViewById(R.id.progressBar);
        relativeLayout = b.findViewById(R.id.layoutOcultar);
        imgMedal = b.findViewById(R.id.imgMedal);

        btnCanjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), activity_canjear_puntos.class);
                startActivity(i);
            }
        });

        cargarDatosUsuario(b);
        return b;
    }

    private void cargarDatosUsuario(View view) {
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        int idUsuario = UsuarioNegocio.obtenerIDUsuario(view.getContext());
        UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
        usuarioNegocio.buscarUsuarioPorId(idUsuario, new UsuarioNegocio.UsuarioCallback() {
            @Override
            public void onSuccess(String mensaje) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onError(String error) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onUsuarioLoaded(Usuario usuario) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        tv_nombre_perfil_usuario.setText("@" + usuario.getAlias().toString());
                        tv_puntajeActual.setText(String.valueOf(usuario.getCantPuntos()));
                        if (usuario.getCantPuntos()>=10000){
                            imgMedal.setForeground(getContext().getDrawable(R.drawable.gold_medall));
                        }else if (usuario.getCantPuntos()<10000 && usuario.getCantPuntos()>=5000){
                            imgMedal.setForeground(getContext().getDrawable(R.drawable.silver_medal));
                        }else {
                            imgMedal.setForeground(getContext().getDrawable(R.drawable.bronze_medal));
                        }
                    }
                });
            }

        });
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

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosUsuario(getView());
    }
}