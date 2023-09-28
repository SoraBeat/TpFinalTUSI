package com.example.tpfinaltusi.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Usuario;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class FragmentQR extends Fragment {
    private final String codigoQR25 = "A5fGv9wK3lM8pR7";
    private final String codigoQR50 = "N1yTzPq2Rm8Lk6J";
    private final String codigoQR100 = "X4sHtVp7Zi9KlW3";
    private final String codigoQR200 = "D2pLk5Y9RwNvQ6Z";
    private final String codigoQR300 = "M7oTn2Lw9XpRfVz";
    TextView tv_puntajeActual;
    TextView tv_puntajeSumar;
    Button btnEscanear;
    ProgressBar progressBar;
    RelativeLayout layoutOcultar;
    private ActivityResultLauncher<Intent> qrCodeScannerLauncher;

    public FragmentQR() {
        // Required empty public constructor
    }

    public static FragmentQR newInstance(String param1, String param2) {
        FragmentQR fragment = new FragmentQR();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);
        tv_puntajeActual = view.findViewById(R.id.tv_puntajeActual);
        tv_puntajeSumar = view.findViewById(R.id.tv_puntajeSumar);
        btnEscanear = view.findViewById(R.id.btnEscanear);
        progressBar = view.findViewById(R.id.progressBar);
        layoutOcultar = view.findViewById(R.id.layoutOcultar);
        // Inicializa el ActivityResultLauncher
        qrCodeScannerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        onActivityResult(IntentIntegrator.REQUEST_CODE, RESULT_OK, data);
                    }
                }
        );
        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Apunta al QR");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);

                // Inicia la actividad de escaneo
                qrCodeScannerLauncher.launch(integrator.createScanIntent());
            }
        });
        traerDatosUsuario(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                // Escaneo cancelado
                Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                // Escaneo exitoso
                String scanResult = result.getContents();
                int puntaje = 0;
                switch (scanResult) {
                    case codigoQR25:
                        puntaje = 25;
                        break;
                    case codigoQR50:
                        puntaje = 50;
                        break;
                    case codigoQR100:
                        puntaje = 100;
                        break;
                    case codigoQR200:
                        puntaje = 200;
                        break;
                    case codigoQR300:
                        puntaje = 300;
                        break;
                    default:
                        puntaje = 0;
                        break;
                }
                if (puntaje > 0) {
                    btnEscanear.setVisibility(View.GONE);
                    tv_puntajeSumar.setVisibility(View.VISIBLE);
                    tv_puntajeSumar.setText("+" + puntaje);
                    //sumar puntaje
                    int idUsuario = UsuarioNegocio.obtenerIDUsuario(getContext());
                    UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
                    int finalPuntaje = puntaje;
                    usuarioNegocio.sumarPuntosAUsuarioPorId(idUsuario, puntaje, new UsuarioNegocio.UsuarioCallback() {
                        @Override
                        public void onSuccess(String mensaje) {
                            // Manejar el éxito, por ejemplo, mostrar un mensaje al usuario
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Puntaje sumado", Toast.LENGTH_SHORT).show();
                                    int puntosActuales = (Integer.parseInt(tv_puntajeActual.getText().toString()))+finalPuntaje;
                                    tv_puntajeActual.setText(String.valueOf(puntosActuales));
                                    btnEscanear.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            // Manejar el error, por ejemplo, mostrar un mensaje de error al usuario
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Error al sumar puntaje", Toast.LENGTH_SHORT).show();
                                    tv_puntajeSumar.setVisibility(View.GONE);
                                    btnEscanear.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                        @Override
                        public void onUsuarioLoaded(Usuario usuario) {
                            // Esta función no se utiliza en este contexto, pero puede manejarla si es necesario
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Codigo invalido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void traerDatosUsuario(View view) {
        int idUsuario = UsuarioNegocio.obtenerIDUsuario(view.getContext());
        UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
        progressBar.setVisibility(View.VISIBLE);
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
                        progressBar.setVisibility(View.GONE);
                        layoutOcultar.setVisibility(View.VISIBLE);
                        tv_puntajeActual.setText(String.valueOf(usuario.getCantPuntos())); // Debes convertir el valor int a String
                    }
                });
            }

        });
    }
}