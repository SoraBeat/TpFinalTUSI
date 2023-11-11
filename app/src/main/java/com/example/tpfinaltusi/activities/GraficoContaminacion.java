package com.example.tpfinaltusi.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Informe;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GraficoContaminacion extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //////////////////////////////////CONFIGURACION ACTION BAR////////////////////////////////////////////
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        // Desactivar la elevación de la ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        // Inflar la vista personalizada
        View customActionBarView = getSupportActionBar().getCustomView();
        setContentView(R.layout.activity_grafico_contaminacion);

        // Configurar el título centrado (opcional)
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Grafico de Contaminacion");
        ImageView btnBack = customActionBarView.findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ProgressBar progressBar = findViewById(R.id.progressBar);
        new InformeNegocio().traerTodosLosInformes(new InformeNegocio.InformesCallback() {
            @Override
            public void onInformesLoaded(List<Informe> informes) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long total = informes.stream()
                                .mapToLong(informe -> informe.getPuntosRecompensa()).sum();
                        long Reciclado = informes.stream()
                                .filter(informe -> informe.getIdEstado() == 4)
                                .mapToLong(informe->informe.getPuntosRecompensa()).sum();
                        long sinReciclar = total - Reciclado;

                        PieChart pieChart = (PieChart) findViewById(R.id.pieChartContaminacion);
                        ArrayList<PieEntry> entiers = new ArrayList<>();
                        entiers.add(new PieEntry(Reciclado,"Reciclado"));
                        entiers.add(new PieEntry(sinReciclar,"Sin reciclar"));

                        PieDataSet pieDataSet = new PieDataSet(entiers,"");
                        pieDataSet.setColors(Color.rgb(71, 198, 143),Color.rgb(233, 144, 144));

                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
                        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                        pieChart.getLegend().setFormSize(20f);
                        pieChart.setEntryLabelTextSize(20f);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.animateY(1000);
                        pieChart.invalidate();
                        pieChart.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onError(String error) {

            }
        });


    }

}
