package com.example.tpfinaltusi.adicionales;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.PremioNegocio;
import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.activity_canjear_puntos;
import com.example.tpfinaltusi.entidades.Premio;
import com.example.tpfinaltusi.entidades.Usuario;
import com.example.tpfinaltusi.fragments.FragmentPerfil;
import com.j256.ormlite.stmt.query.In;

public class activity_canje_exitoso extends AppCompatActivity {
    TextView tv_puntajeActual;
    TextView product_title;
    ImageView Imagen_canje;
    Button btCerrar;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canje_exitoso);

        // CONFIGURACION ACTION BAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        View customActionBarView = getSupportActionBar().getCustomView();
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Canje Exitoso");

        // Obtén las vistas por sus IDs
        tv_puntajeActual = findViewById(R.id.tv_puntajeActual);
        product_title = findViewById(R.id.product_title);
        Imagen_canje = findViewById(R.id.Imagen_canje);
        btCerrar = findViewById(R.id.btnCerrar);
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.layoutOcultar);

        mostrarDatos();

    }

    private void mostrarDatos() {
        relativeLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        // Recupera los datos pasados desde Intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            PremioNegocio PremioNegocio = new PremioNegocio();
            PremioNegocio.traerPremioPorId(intent.getIntExtra("idPremio", -1), new PremioNegocio.PremioCallback() {
                @Override
                public void onSuccess(String mensaje) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onPremioLoaded(Premio premio) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Seteamos bitmap para la imagen
                            String pureBase64Encoded = premio.getImagen().substring(premio.getImagen().indexOf(",") + 1);
                            byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            // Establece los datos en las vistas correspondientes
                            product_title.setText(title);
                            Imagen_canje.setImageBitmap(bitmap);

                            //Damos de baja la cantidad de puntos canjeados
                            int idUsuario = UsuarioNegocio.obtenerIDUsuario(getApplicationContext());
                            UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
                            usuarioNegocio.restarPuntosAUsuarioPorId(idUsuario, premio.getPrecio(), new UsuarioNegocio.UsuarioCallback() {
                                @Override
                                public void onSuccess(String mensaje) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int puntosActuales = (Integer.parseInt(tv_puntajeActual.getText().toString()));
                                            tv_puntajeActual.setText(String.valueOf(puntosActuales));
                                            relativeLayout.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                }

                                @Override
                                public void onError(String error) {
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onUsuarioLoaded(Usuario usuario) {
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    });


                }
            });
            btCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Coloca aquí el código que deseas ejecutar cuando se hace clic en el botón "Cerrar"
                    // Por ejemplo, puedes cerrar la actividad actual
                    finish();
                    //Intent intent = new Intent(getApplicationContext(), FragmentPerfil.class);
                    //startActivity(intent);
                }
            });
        }
        traer_puntaje();
    }

    private void traer_puntaje() {
        int idUsuario = UsuarioNegocio.obtenerIDUsuario(getApplicationContext());
        UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_puntajeActual.setText(String.valueOf(usuario.getCantPuntos() - getIntent().getIntExtra("Precio", -1)));
                    }
                });
            }
        });
    }
}