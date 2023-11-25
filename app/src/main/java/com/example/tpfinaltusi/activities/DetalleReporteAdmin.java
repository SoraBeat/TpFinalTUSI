package com.example.tpfinaltusi.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.tpfinaltusi.Negocio.InformeImagenNegocio;
import com.example.tpfinaltusi.Negocio.InformeNegocio;
import com.example.tpfinaltusi.Negocio.Informe_HistorialNegocio;
import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Informe;
import com.example.tpfinaltusi.entidades.Informe_Historial;
import com.example.tpfinaltusi.entidades.Informe_Imagen;
import com.example.tpfinaltusi.entidades.Usuario;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class DetalleReporteAdmin extends AppCompatActivity {
    TextView tvTitulo;
    TextView tvDescripcion;
    TextView tvInfo;
    ImageView ivImagen;
    DialogViewImage dialogViewImage;
    Bitmap bitmap = null;
    Bitmap prueba = null;
    ProgressBar progressBar;
    LinearLayout layoutInvisible;
    ImageView btnBack;
    LinearLayout btnGoogleMaps;
    Button btnAprobarInforme;
    Button btnCancelarInforme;
    Button btnVerPrueba;
    EditText etPuntos;
    TextView tvPuntos;
    Informe inf;
    int idInformeImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //////////////////////////////////CONFIGURACION ACTION BAR////////////////////////////////////////////
        super.onCreate(savedInstanceState);
        // Habilitar ActionBar y configurar vista personalizada
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        // Desactivar la elevación de la ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        // Inflar la vista personalizada
        View customActionBarView = getSupportActionBar().getCustomView();

        // Configurar el título centrado (opcional)
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        btnBack = customActionBarView.findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        actionBarTitle.setText("Reporte");
        setContentView(R.layout.activity_aprobacion_detalle_reporte);
        //////////////////////////////////TRAER DATOS////////////////////////////////////////////
        Intent intent = getIntent();
        if (intent != null) {
            /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
            tvTitulo = findViewById(R.id.tv_titulo);
            tvDescripcion = findViewById(R.id.tv_cuerpo);
            ivImagen = findViewById(R.id.iv_imagen);
            ivImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialogViewImage == null || dialogViewImage.getDialog() == null || !dialogViewImage.getDialog().isShowing()){
                        FragmentManager fm = getSupportFragmentManager();
                        Bundle arguments = new Bundle();

                        arguments.putParcelable("PICTURE_SELECTED", bitmap);
                        dialogViewImage = DialogViewImage.newInstance(arguments);
                        dialogViewImage.show(fm,"DialogViewImage");
                    }
                }
            });

            tvInfo = findViewById(R.id.infoTextView);
            tvPuntos = findViewById(R.id.tvPuntos);
            progressBar = findViewById(R.id.progressBar);
            layoutInvisible = findViewById(R.id.layoutInvisible);
            btnGoogleMaps = findViewById(R.id.btn_verMaps);
            etPuntos = findViewById(R.id.editTextPuntos);
            btnAprobarInforme = findViewById(R.id.btn_aprobarInforme);
            btnCancelarInforme = findViewById(R.id.btn_cancelarInforme);
            btnVerPrueba = findViewById(R.id.btn_verPrueba);
            /////////////////////////////////////OBTENER DATOS///////////////////////////////////////////////
            int id = intent.getIntExtra("id_informe", -1);
            cargarReporte(id);
        }
    }

    private void cargarReporteRevision(){
        new InformeImagenNegocio().traerTodasLasInformeImagenes(new InformeImagenNegocio.InformeImagenesCallback() {
            @Override
            public void onInformeImagenesLoaded(List<Informe_Imagen> informeImagenes) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Informe_Imagen imagen = informeImagenes.stream().filter(img -> img.getIdInforme() == getIntent().getIntExtra("id_informe", -1)).findFirst().get();
                            String pureBase64Encoded = imagen.getImagen().substring(informeImagenes.get(0).getImagen().indexOf(",") + 1);
                            byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                            prueba = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            idInformeImagen = imagen.getIdInformeImagen();
                        layoutInvisible.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        btnAprobarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etPuntos.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnAprobarInforme.setEnabled(false);
                    btnCancelarInforme.setEnabled(false);
                    Informe informe = new Informe();
                    informe.setIdInforme(getIntent().getIntExtra("id_informe", -1));
                    informe.setPuntosRecompensa(Integer.parseInt(etPuntos.getText().toString()));
                    new InformeNegocio().CerrarInforme(informe, new InformeNegocio.InformeCallback() {
                        @Override
                        public void onSuccess(String mensaje) {
                            Informe_Historial informe_historial = new Informe_Historial();
                            informe_historial.setIdInforme(informe.getIdInforme());
                            informe_historial.setIMG(inf.getImagen());
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            prueba.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream .toByteArray();
                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            informe_historial.setIMG_Prueba(encoded);
                            informe_historial.setIdEstado(4);
                            informe_historial.setTitulo(inf.getTitulo());
                            informe_historial.setCuerpo(inf.getCuerpo());
                            informe_historial.setOcultar(false);
                            new Informe_HistorialNegocio().crearInforme_Historial(informe_historial, new Informe_HistorialNegocio.Informe_HistorialCallback() {
                                @Override
                                public void onSuccess(String mensaje) {
                                    Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                                    startActivity(i);
                                }

                                @Override
                                public void onError(String error) {

                                }

                                @Override
                                public void onInformeHistorialLoaded(Informe_Historial informeHistorial) {

                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                            startActivity(i);
                        }

                        @Override
                        public void onInformeLoaded(Informe informe) {

                        }
                    });
                } else {
                    etPuntos.setError("Debe ingresar una recompensa");
                }
            }
        });
        btnVerPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogViewImage == null || dialogViewImage.getDialog() == null || !dialogViewImage.getDialog().isShowing()){
                    FragmentManager fm = getSupportFragmentManager();
                    Bundle arguments = new Bundle();

                    arguments.putParcelable("PICTURE_SELECTED", prueba);
                    dialogViewImage = DialogViewImage.newInstance(arguments);
                    dialogViewImage.show(fm,"DialogViewImage");
                }
            }
        });
        btnCancelarInforme.setText("Rechazar");

        btnCancelarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inf.setFechaBaja(null);
                inf.setUsuarioBaja(-1);
                inf.setIdEstado(1);
                new InformeNegocio().editarInforme(inf, new InformeNegocio.InformeCallback() {
                    @Override
                    public void onSuccess(String mensaje) {
                        new InformeImagenNegocio().borrarInformeImagen(idInformeImagen, new InformeImagenNegocio.InformeImagenCallback() {
                            @Override
                            public void onSuccess(String mensaje) {
                                Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                                startActivity(i);
                            }

                            @Override
                            public void onError(String error) {
                                Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                                startActivity(i);
                            }

                            @Override
                            public void onInformeImagenLoaded(Informe_Imagen informeImagen) {

                            }
                        });

                    }

                    @Override
                    public void onError(String error) {
                        Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                        startActivity(i);
                    }

                    @Override
                    public void onInformeLoaded(Informe informe) {

                    }
                });

            }
        });
        tvPuntos.setText("Defina puntos de recompensa (Recomendados " + inf.getPuntosRecompensa() + "):");
    }
    private void cargarReportePendiente(){
        btnVerPrueba.setVisibility(View.GONE);
        btnAprobarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etPuntos.getText().toString().isEmpty()){

                progressBar.setVisibility(View.VISIBLE);
                btnAprobarInforme.setEnabled(false);
                btnCancelarInforme.setEnabled(false);
                inf.setPuntosRecompensa(Integer.parseInt(etPuntos.getText().toString()));
                inf.setIdEstado(1);
                if(inf.getPuntosRecompensa()<50)
                    inf.setIdNivel(1);
                if(inf.getPuntosRecompensa()>=50 && inf.getPuntosRecompensa() <150)
                    inf.setIdNivel(2);
                if(inf.getPuntosRecompensa()>=150)
                    inf.setIdNivel(3);
                new InformeNegocio().editarInforme(inf, new InformeNegocio.InformeCallback() {
                    @Override
                    public void onSuccess(String mensaje) {
                        Informe_Historial informe_historial = new Informe_Historial();
                        informe_historial.setIdInforme(inf.getIdInforme());
                        informe_historial.setIMG(inf.getImagen());
                        informe_historial.setIdEstado(4);
                        informe_historial.setTitulo(inf.getTitulo());
                        informe_historial.setCuerpo(inf.getCuerpo());
                        informe_historial.setOcultar(false);
                        new Informe_HistorialNegocio().crearInforme_Historial(informe_historial, new Informe_HistorialNegocio.Informe_HistorialCallback() {
                            @Override
                            public void onSuccess(String mensaje) {
                                Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                                startActivity(i);
                            }

                            @Override
                            public void onError(String error) {

                            }

                            @Override
                            public void onInformeHistorialLoaded(Informe_Historial informeHistorial) {

                            }
                        });
                    }
                    @Override
                    public void onError(String error) {
                        Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                        startActivity(i);
                    }
                    @Override
                    public void onInformeLoaded(Informe informe) {

                    }
                });

                } else {
                    etPuntos.setError("Debe ingresar una recompensa");
                }
            }
        });
        btnCancelarInforme.setText("Rechazar");
        btnCancelarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAprobarInforme.setEnabled(false);
                btnCancelarInforme.setEnabled(false);
                new InformeNegocio().borrarInforme(inf.getIdInforme(), new InformeNegocio.InformeCallback() {
                    @Override
                    public void onSuccess(String mensaje) {
                        Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(String error) {
                        Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                        startActivity(i);
                    }

                    @Override
                    public void onInformeLoaded(Informe informe) {

                    }
                });
            }
        });
        layoutInvisible.setVisibility(View.VISIBLE);
    }
    private void cargarReporteActivo(){
        btnVerPrueba.setVisibility(View.GONE);
        btnAprobarInforme.setVisibility(View.GONE);
        btnCancelarInforme.setVisibility(View.GONE);
        tvPuntos.setVisibility(View.GONE);
        etPuntos.setVisibility(View.GONE);
        layoutInvisible.setVisibility(View.VISIBLE);
    }
    private void cargarReporteTerminado(){
        btnAprobarInforme.setVisibility(View.GONE);
        btnCancelarInforme.setVisibility(View.GONE);
        tvPuntos.setText("Se otorgaron " + inf.getPuntosRecompensa() + " puntos al usuario " + inf.getUsuarioBaja());
        etPuntos.setVisibility(View.GONE);
        new InformeImagenNegocio().traerTodasLasInformeImagenes(new InformeImagenNegocio.InformeImagenesCallback() {
            @Override
            public void onInformeImagenesLoaded(List<Informe_Imagen> informeImagenes) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Informe_Imagen imagen = informeImagenes.stream().filter(img -> img.getIdInforme() == getIntent().getIntExtra("id_informe", -1)).findFirst().get();
                        String pureBase64Encoded = imagen.getImagen().substring(informeImagenes.get(0).getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        prueba = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        idInformeImagen = imagen.getIdInformeImagen();
                        layoutInvisible.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        btnVerPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogViewImage == null || dialogViewImage.getDialog() == null || !dialogViewImage.getDialog().isShowing()){
                    FragmentManager fm = getSupportFragmentManager();
                    Bundle arguments = new Bundle();

                    arguments.putParcelable("PICTURE_SELECTED", prueba);
                    dialogViewImage = DialogViewImage.newInstance(arguments);
                    dialogViewImage.show(fm,"DialogViewImage");
                }
            }
        });
    }
    private void cargarReporte(int id) {
        InformeNegocio informeNegocio = new InformeNegocio();
        progressBar.setVisibility(View.VISIBLE);
        informeNegocio.traerInformePorId(id, new InformeNegocio.InformeCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onInformeLoaded(Informe informe) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTitulo.setText(informe.getTitulo());
                        tvDescripcion.setText(informe.getCuerpo());
                        String descripcionEstado = "";
                        switch (informe.getIdEstado()){
                            case 1:
                                descripcionEstado = "Activo";
                                break;
                            case 2:
                                descripcionEstado = "Pendiente";
                                break;
                            case 3:
                                descripcionEstado = "Revisión";
                                break;
                            case 4:
                                descripcionEstado = "Terminado";
                                break;
                        }
                        tvInfo.setText(new SimpleDateFormat("dd/MM/yyyy").format(informe.getFechaAlta()) + " - " + descripcionEstado);
                        String pureBase64Encoded = informe.getImagen().substring(informe.getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        ivImagen.setImageBitmap(bitmap);

                        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                    i.putExtra("latitud",informe.getLatitud());
                                    i.putExtra("longitud",informe.getLongitud());
                                    i.putExtra("tagmaps",informe.getTitulo());
                                    startActivity(i);
                                }catch (Exception e){
                                    System.out.println(e);
                                }
                            }
                        });
                        btnCancelarInforme.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), HomeActivityAdmin.class);
                                startActivity(i);
                            }
                        });
                        inf = informe;
                        switch (informe.getIdEstado()){
                            case 1:
                                cargarReporteActivo();
                                break;
                            case 2:
                                cargarReportePendiente();
                                break;
                            case 3:
                                cargarReporteRevision();
                                break;
                            case 4:
                                cargarReporteTerminado();
                                break;
                            default:
                                finish();
                                break;
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
