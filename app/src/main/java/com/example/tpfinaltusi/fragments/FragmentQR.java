package com.example.tpfinaltusi.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.example.tpfinaltusi.Negocio.CodigoQRNegocio;
import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.CodigoQR;
import com.example.tpfinaltusi.entidades.Usuario;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class FragmentQR extends Fragment {
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
                integrator.setCameraId(0);
                integrator.setPrompt("Apunta al QR");
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.addExtra(Intents.Scan.ORIENTATION_LOCKED, 0);
                integrator.setOrientationLocked(true);
                // Inicia la actividad de escaneo
                qrCodeScannerLauncher.launch(integrator.createScanIntent());
            }
        });
        traerDatosUsuario(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        btnEscanear.setVisibility(View.GONE);
        tv_puntajeSumar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        layoutOcultar.setVisibility(View.GONE);
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                // Escaneo cancelado
                Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                CodigoQRNegocio codigoQRNegocio = new CodigoQRNegocio();
                codigoQRNegocio.traerTodosLosCodigoQRs(new CodigoQRNegocio.CodigoQRsCallback() {
                    @Override
                    public void onCodigoQRsLoaded(List<CodigoQR> codigoQRs) {
                        String scanResult = result.getContents();
                        int puntaje = 0;
                        for (CodigoQR codigo:codigoQRs
                             ) {
                            if(scanResult.equals(codigo.getCodigo())){
                                puntaje = codigo.getPuntos();
                            }
                        }
                        if (puntaje > 0) {
                            int copiaPuntaje = puntaje;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_puntajeSumar.setText("+" + copiaPuntaje);
                                    //sumar puntaje
                                    int idUsuario = UsuarioNegocio.obtenerIDUsuario(getContext());
                                    UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
                                    int finalPuntaje = copiaPuntaje;
                                    usuarioNegocio.sumarPuntosAUsuarioPorId(idUsuario, copiaPuntaje, new UsuarioNegocio.UsuarioCallback() {
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
                                                    progressBar.setVisibility(View.GONE);
                                                    layoutOcultar.setVisibility(View.VISIBLE);
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
                                                    progressBar.setVisibility(View.GONE);
                                                    layoutOcultar.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onUsuarioLoaded(Usuario usuario) {
                                            // Esta función no se utiliza en este contexto, pero puede manejarla si es necesario
                                        }
                                    });
                                }

                                });


                        } else {
                            Toast.makeText(getContext(), "Codigo invalido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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