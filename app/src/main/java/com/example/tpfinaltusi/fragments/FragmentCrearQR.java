package com.example.tpfinaltusi.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.MapsActivity;
import com.example.tpfinaltusi.entidades.Informe;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FragmentCrearQR extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    View view;
    private EditText etTitulo;
    private EditText etDescripcion;
    private TextView txtUbicacion;
    private Button btnElegirArchivo;
    private Button btnCrear;
    private String IMG;
    private double latitud;
    private double longitud;
    private FusedLocationProviderClient fusedLocationClient;
    public FragmentCrearQR() {
    }
    public static FragmentCrearQR newInstance(String param1, String param2) {
        FragmentCrearQR fragment = new FragmentCrearQR();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();

        try {
            if (activity != null) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            }
        }catch (Exception e) {
                System.out.println(e);
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_creacion_qr, container, false);

        return  view;
    }

}