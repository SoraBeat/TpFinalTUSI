package com.example.tpfinaltusi.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.tpfinaltusi.Negocio.CodigoQRNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.CustomListAdapter;
import com.example.tpfinaltusi.entidades.CodigoQR;

import java.util.List;

public class FragmentCrearQR extends Fragment {
    View view;
    EditText etPuntos;
    Button btnGenerar;
    ListView listaQR;
    private CodigoQRNegocio codigoQRNegocio;
    private CustomListAdapter codigoQRAdapter;
    public FragmentCrearQR() {
    }
    public static FragmentCrearQR newInstance(String param1, String param2) {
        FragmentCrearQR fragment = new FragmentCrearQR();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_creacion_qr, container, false);

        etPuntos = view.findViewById(R.id.et_puntos);
        btnGenerar = view.findViewById(R.id.btn_generar);
        listaQR = view.findViewById(R.id.lv_listaqr);

        codigoQRNegocio = new CodigoQRNegocio();
        // Llama al método para traer todos los códigos QR
        codigoQRNegocio.traerTodosLosCodigoQRs(new CodigoQRNegocio.CodigoQRsCallback() {
            @Override
            public void onCodigoQRsLoaded(List<CodigoQR> codigoQRs) {
                getActivity().runOnUiThread(() -> {
                    // Crea un adaptador personalizado y configura el ListView
                    codigoQRAdapter = new CustomListAdapter(getActivity(), codigoQRs);
                    listaQR.setAdapter(codigoQRAdapter);
                });
            }
        });

        return  view;
    }

}