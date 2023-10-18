package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.PagerController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerController pagerController;
    TextView actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //////////////////////////////////CONFIGURACION ACTION BAR////////////////////////////////////////////
        super.onCreate(savedInstanceState);
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
        actionBarTitle.setText("Noticias");
        setContentView(R.layout.activity_home);
        /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
        tabLayout = findViewById(R.id.tl_tablayout);
        viewPager = findViewById(R.id.vp_viewpager);
        pagerController = new PagerController(getSupportFragmentManager(),5);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        comportamientoTabLayout();
    }
    private void comportamientoTabLayout(){
        viewPager.setAdapter(pagerController);
        int[] iconos = {R.drawable.baseline_home_24, R.drawable.baseline_event_24, R.drawable.baseline_qr_code_scanner_24, R.drawable.baseline_add_alert_24,R.drawable.baseline_person_outline_24};
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View customTabView = LayoutInflater.from(this).inflate(R.layout.custom_tab_item, null);
                ImageView tabIcon = customTabView.findViewById(R.id.custom_tab_icon);
                tabIcon.setImageResource(iconos[i]); // Reemplaza con tu recurso de icono
                tab.setCustomView(customTabView);
            }
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    pagerController.notifyDataSetChanged();
                    actionBarTitle.setText("Noticias");
                }
                if(tab.getPosition()==1) {
                    pagerController.notifyDataSetChanged();
                    actionBarTitle.setText("Reportes");
                }
                if(tab.getPosition()==2){
                    pagerController.notifyDataSetChanged();
                    actionBarTitle.setText("QR");
                }
                if(tab.getPosition()==3){
                    pagerController.notifyDataSetChanged();
                    actionBarTitle.setText("Crear reporte");
                }
                if(tab.getPosition()==4){
                    pagerController.notifyDataSetChanged();
                    actionBarTitle.setText("Perfil");
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
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustomStyle);
        builder.setMessage("¿Desea salir de la aplicación?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish(); // Cierra la aplicación
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}