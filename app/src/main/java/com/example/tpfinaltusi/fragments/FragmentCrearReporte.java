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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.MapsActivity;
import com.example.tpfinaltusi.entidades.Informe;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FragmentCrearReporte extends Fragment {
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
    public FragmentCrearReporte() {
    }
    public static FragmentCrearReporte newInstance(String param1, String param2) {
        FragmentCrearReporte fragment = new FragmentCrearReporte();
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

        view = inflater.inflate(R.layout.fragment_crear_reporte, container, false);
        btnElegirArchivo = view.findViewById(R.id.btn_ElegirArchivo);
        btnCrear = view.findViewById(R.id.btn_CrearReporte);
        txtUbicacion =view.findViewById(R.id.txt_Ubicacion);
        btnElegirArchivo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                // Inicia la actividad de selección de archivos
                startActivityForResult(intent, 1);
            }
        });
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCrear_Click();
            }
        });

        txtUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerUbicacionYAbrirMapa();
            }
        });

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // La actividad se cerró correctamente
            Uri uri = data.getData();
            String path = uri.getPath();
            File img = new File(path);
            img.canRead();
            InputStream inputStream = null;
            try {
                ContentResolver resolver = getActivity().getContentResolver();
                inputStream = resolver.openInputStream(uri);

                // Read the image into a byte array
                byte[] bytes = new byte[(int) inputStream.available()];
                inputStream.read(bytes);

                // Convert the byte array to Base64
                IMG = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
    }


    public void btnCrear_Click(){
        etTitulo = view.findViewById(R.id.et_Titulo);
        etDescripcion = view.findViewById(R.id.et_Descripcion);
        //etUbicacion = view.findViewById(R.id.et_Ubicacion);

        ///TODO: TIENE QUE OBTENER EL USUARIO LOGUEADO
        int idusuario = 1;

        Informe informe = new Informe(etTitulo.getText().toString(), etDescripcion.getText().toString(), idusuario, 2, latitud, longitud, IMG);
        InformeNegocio informeNegocio = new InformeNegocio();
        informeNegocio.crearInforme(informe);
    }


    private void obtenerUbicacionYAbrirMapa() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tienen permisos, solicitarlos al usuario
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Ya se tienen los permisos, obtener la ubicación
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                                // Crear un intent y enviar la ubicación a MapsActivity
                                Intent i = new Intent(requireContext(), MapsActivity.class);
                                i.putExtra("latitud", latitud);
                                i.putExtra("longitud", longitud);
                                i.putExtra("tagmaps", "Ubicación actual");
                                startActivity(i);
                            }
                        }
                    });
        }
    }

}