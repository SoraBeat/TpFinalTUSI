package com.example.tpfinaltusi.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tpfinaltusi.Negocio.CodigoQRNegocio;
import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.Login;
import com.example.tpfinaltusi.adicionales.CustomListAdapter;
import com.example.tpfinaltusi.entidades.CodigoQR;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.mail.util.QEncoderStream;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FragmentCrearQR extends Fragment implements PopupMenu.OnMenuItemClickListener{
    View view;
    EditText etPuntos;
    Button btnGenerar;
    ListView listaQR;
    private CodigoQRNegocio codigoQRNegocio;
    private CustomListAdapter codigoQRAdapter;
    ProgressBar progressBar;

    public FragmentCrearQR() {
    }

    public static FragmentCrearQR newInstance(String param1, String param2) {
        FragmentCrearQR fragment = new FragmentCrearQR();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_creacion_qr, container, false);

        etPuntos = view.findViewById(R.id.et_puntos);
        btnGenerar = view.findViewById(R.id.btn_generar);
        listaQR = view.findViewById(R.id.lv_listaqr);
        progressBar = view.findViewById(R.id.cargando);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        // Inflar la vista personalizada
        View customActionBarView = actionBar.getCustomView();

        ImageButton img_config = customActionBarView.findViewById(R.id.menu_overflow);

        img_config.setVisibility(view.VISIBLE);

        img_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(requireActivity(), img_config);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_overflow, popupMenu.getMenu());
                // Obtén el menú del PopupMenu
                Menu menu = popupMenu.getMenu();

                // Obtiene los elementos de menú y establece un color para el texto
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem menuItem = menu.getItem(i);
                    SpannableString spannableString = new SpannableString(menuItem.getTitle());
                    spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
                    menuItem.setTitle(spannableString);
                }
                popupMenu.setOnMenuItemClickListener(FragmentCrearQR.this);
                popupMenu.show();
            }
        });

        traerListaQR();
        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int puntaje = Integer.valueOf(etPuntos.getText().toString().trim());
                    QRCodeWriter writer = new QRCodeWriter();
                    String uniqueCode = UUID.randomUUID().toString();

                    try {
                        BitMatrix bitMatrix = writer.encode(uniqueCode, BarcodeFormat.QR_CODE, 512, 512);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }

                        // Convierte el Bitmap a una cadena base64
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
                        int idUsuario = sharedPreferences.getInt("idUsuario", -1);
                        System.out.println("ID USUARIO: " + idUsuario);
                        // Ahora puedes usar la cadena base64 como desees
                        CodigoQRNegocio codigoQRNegocio = new CodigoQRNegocio();
                        CodigoQR codigoQR = new CodigoQR(0, uniqueCode, "data:image/png;base64," + base64Image, puntaje, false, idUsuario);
                        codigoQRNegocio.crearCodigoQR(codigoQR, new CodigoQRNegocio.CodigoQRCallback() {
                            @Override
                            public void onSuccess(String mensaje) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        etPuntos.setText("");
                                        Toast.makeText(getContext(), "Codigo generado", Toast.LENGTH_SHORT).show();
                                        traerListaQR();
                                    }
                                });
                            }

                            @Override
                            public void onError(String error) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Error al generar codigo", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCodigoQRLoaded(CodigoQR codigoQR) {

                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Puntaje inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void traerListaQR() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        codigoQRNegocio = new CodigoQRNegocio();
        // Llama al método para traer todos los códigos QR
        codigoQRNegocio.traerTodosLosCodigoQRs(new CodigoQRNegocio.CodigoQRsCallback() {
            @Override
            public void onCodigoQRsLoaded(List<CodigoQR> codigoQRs) {
                List<CodigoQR> listaFiltrada = new ArrayList<>();
                for (CodigoQR codigoQR : codigoQRs
                ) {
                    if (!codigoQR.isCanjeado()) {
                        listaFiltrada.add(codigoQR);
                    }
                }
                codigoQRAdapter = new CustomListAdapter(getActivity(), getFragmentManager(),listaFiltrada);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Crea un adaptador personalizado y configura el ListView
                        listaQR.setAdapter(codigoQRAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.menu_btn_cerrar) {
            // Elimina la variable de SharedPreferences "idUsuario"
            SharedPreferences preferences = getContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("idUsuario");
            editor.apply();

            // Navega de vuelta a la pantalla de inicio de sesión (LoginActivity)
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);

            // Asegúrate de que el fragmento se cierre o se realice alguna acción adicional si es necesario
            // return true; // Dependiendo de tus necesidades
        }

        // Maneja otras acciones de menú si es necesario

        return true;
    }
}