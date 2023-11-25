package com.example.tpfinaltusi.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.Negocio.Informe_HistorialNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.HomeActivity;
import com.example.tpfinaltusi.activities.HomeActivityAdmin;
import com.example.tpfinaltusi.activities.MapsActivity;
import com.example.tpfinaltusi.entidades.Informe;
import com.example.tpfinaltusi.entidades.Informe_Historial;
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
    private ProgressBar progressBar;
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
        txtUbicacion = view.findViewById(R.id.txt_Ubicacion);
        progressBar = view.findViewById(R.id.progressBar);
        //etUbicacion = view.findViewById(R.id.et_Ubicacion);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UsuarioGuardado", Context.MODE_PRIVATE);
        int idusuario = sharedPreferences.getInt("idUsuario",-1);
        boolean error = false;
        boolean toast = false;
        if(IMG == null || IMG.isEmpty()){
            Toast.makeText(getContext(),"Debe elegir una imagen de prueba",Toast.LENGTH_LONG);
            error = true;
            toast = true;
        }
        if(etTitulo.getText().toString().isEmpty()){
            etTitulo.setError("Debe ingresar un titulo");
            error = true;
        }
        if(etDescripcion.getText().toString().isEmpty()){
            etDescripcion.setError("Debe ingresar una descripcion");
            error = true;
        }
        if(latitud == 0 && longitud == 0){
            error = true;
            txtUbicacion.setError("Debe ingresar una ubicacion");
        } else {
            txtUbicacion.setError(null);
        }
        if(!error){
            Informe informe = new Informe(etTitulo.getText().toString(), etDescripcion.getText().toString(), idusuario, 2, latitud, longitud, IMG);
            progressBar.setVisibility(View.VISIBLE);
            new InformeNegocio().crearInforme(informe, new InformeNegocio.InformeCallback() {
                @Override
                public void onSuccess(String mensaje) {
                    progressBar.setVisibility(View.GONE);
                    Intent i = new Intent(getContext(), HomeActivity.class);
                    startActivity(i);
                }
                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(),error,Toast.LENGTH_LONG);
                }

                @Override
                public void onInformeLoaded(Informe informe) {

                }
            });
        }
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