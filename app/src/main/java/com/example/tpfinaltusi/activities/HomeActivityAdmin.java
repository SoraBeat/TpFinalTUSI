package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.PagerControllerAdmin;
import com.example.tpfinaltusi.entidades.Usuario;
import com.google.android.material.tabs.TabLayout;

public class HomeActivityAdmin extends AppCompatActivity  {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerControllerAdmin pagerController;
    TextView actionBarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Configurar el título centrado (opcional)
        actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Reportes");
        setContentView(R.layout.activity_home_admin);
        /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
        tabLayout = findViewById(R.id.tl_tablayout);
        viewPager = findViewById(R.id.vp_viewpager);
        pagerController = new PagerControllerAdmin(getSupportFragmentManager(),5);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        comportamientoTabLayout();
    }
    private void comportamientoTabLayout(){
        viewPager.setAdapter(pagerController);
        int[] iconos = {R.drawable.baseline_event_24, R.drawable.baseline_qr_code_scanner_24, R.drawable.baseline_people_alt_24,R.drawable.baseline_calendar_month_24,R.drawable.baseline_add_shopping_cart_24};
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View customTabView = LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
                ImageView tabIcon = customTabView.findViewById(R.id.custom_tab_icon);
                tabIcon.setImageResource(iconos[i]); // Reemplaza con tu recurso de icono
                tab.setCustomView(customTabView);
            }
        }
        UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
        usuarioNegocio.buscarUsuarioPorId(UsuarioNegocio.obtenerIDUsuario(getApplicationContext()), new UsuarioNegocio.UsuarioCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onUsuarioLoaded(Usuario usuario) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActionBar actionBar = getSupportActionBar();
                        // Inflar la vista personalizada
                        View customActionBarView = actionBar.getCustomView();

                        ImageButton img_config = customActionBarView.findViewById(R.id.menu_overflow);
                        img_config.setVisibility(View.GONE);
                        ImageView img_perfil = customActionBarView.findViewById(R.id.imagenperfil);
                        String pureBase64Encoded = usuario.getImagen().substring(usuario.getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        // Obtén los parámetros de diseño actuales
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img_perfil.getLayoutParams();
                        // Cambia la propiedad layout_alignParentEnd a true
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
                        // Aplica los nuevos parámetros de diseño
                        img_perfil.setLayoutParams(layoutParams);
                        img_perfil.setVisibility(View.VISIBLE);
                        img_perfil.setImageBitmap(bitmap);
                        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                viewPager.setCurrentItem(tab.getPosition());
                                if(tab.getPosition()==0){
                                    pagerController.notifyDataSetChanged();
                                    actionBarTitle.setText("Reportes");
                                }
                                if(tab.getPosition()==1) {
                                    pagerController.notifyDataSetChanged();
                                    actionBarTitle.setText("Generar QR");
                                }
                                if(tab.getPosition()==2) {
                                    pagerController.notifyDataSetChanged();
                                    actionBarTitle.setText("Usuarios");
                                }
                                if(tab.getPosition()==3) {
                                    pagerController.notifyDataSetChanged();
                                    actionBarTitle.setText("Crear Noticia");
                                }
                                if(tab.getPosition()==4) {
                                    pagerController.notifyDataSetChanged();
                                    actionBarTitle.setText("Retirar canje");
                                }
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    }
                });
            }
        });
    }

}