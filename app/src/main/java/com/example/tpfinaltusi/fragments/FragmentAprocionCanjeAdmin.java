package com.example.tpfinaltusi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.CanjeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Canje;

public class FragmentAprocionCanjeAdmin extends Fragment {
    private EditText editTextID;
    private Button buttonAprobar;
    private ProgressBar progressBar;

    public FragmentAprocionCanjeAdmin() {
        // Required empty public constructor
    }

    public static FragmentAprocionCanjeAdmin newInstance(String param1, String param2) {
        FragmentAprocionCanjeAdmin fragment = new FragmentAprocionCanjeAdmin();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_aprocion_canje_admin, container, false);

        editTextID = view.findViewById(R.id.et_id);
        buttonAprobar = view.findViewById(R.id.btn_aprobar);
        progressBar = view.findViewById(R.id.cargando);

        buttonAprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                CanjeNegocio canjeNegocio = new CanjeNegocio();
                canjeNegocio.traerCanjePorId(Integer.valueOf(editTextID.getText().toString()), new CanjeNegocio.CanjeCallback() {
                    @Override
                    public void onSuccess(String mensaje) {

                    }

                    @Override
                    public void onError(String error) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"ERROR, no se encontro ID",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onCanjeLoaded(Canje canje) {
                        System.out.println(canje.isEstado());
                        if(canje.isEstado()){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"ERROR, producto ya retirado",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }else{
                            canje.setEstado(true);
                            canjeNegocio.editarCanje(canje, new CanjeNegocio.CanjeCallback() {
                                @Override
                                public void onSuccess(String mensaje) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(),"Retirado con exito!",Toast.LENGTH_LONG).show();
                                            editTextID.setText("");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                @Override
                                public void onError(String error) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(),"ERROR, intente mas tarde :C",Toast.LENGTH_LONG).show();
                                            editTextID.setText("");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                @Override
                                public void onCanjeLoaded(Canje canje) {

                                }
                            });
                        }
                    }
                });
            }
        });

        return view;
    }
}