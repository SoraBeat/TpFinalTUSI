package com.example.tpfinaltusi.adicionales;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.tpfinaltusi.Negocio.LocalidadNegocio;
import com.example.tpfinaltusi.Negocio.ProvinciaNegocio;
import com.example.tpfinaltusi.Negocio.PuntoVerde_PremioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.activity_punto_verde_canje;
import com.example.tpfinaltusi.entidades.Localidad;
import com.example.tpfinaltusi.entidades.Premio;
import com.example.tpfinaltusi.entidades.Provincia;
import com.example.tpfinaltusi.entidades.PuntoVerde;
import com.example.tpfinaltusi.entidades.PuntoVerde_Premio;

import java.util.ArrayList;
import java.util.List;

public class PuntoVerdeAdapterCanje extends ArrayAdapter<PuntoVerde> {
    private Context context;
    private activity_punto_verde_canje activity;


    public PuntoVerdeAdapterCanje(activity_punto_verde_canje activity, Context context, List<PuntoVerde> PuntoVerde) {
        super(activity, 0, PuntoVerde);;
        this.context = context;
        this.activity = activity;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_punto_verde, parent, false);
        }

        Intent intent = activity.getIntent();
        // Obtener una instancia del Producto en la posición actual
        PuntoVerde PuntoVerde = getItem(position);

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView txtubicacion = convertView.findViewById(R.id.txtUbicacion);
        Button btnElegir = convertView.findViewById(R.id.btnElegir);


        LocalidadNegocio localidadNegocio = new LocalidadNegocio();
        View finalConvertView = convertView;
        localidadNegocio.traerLocalidadPorId(PuntoVerde.getIdLocalidad(), new LocalidadNegocio.LocalidadCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onLocalidadLoaded(Localidad localidad) {
                ProvinciaNegocio provinciaNegocio = new ProvinciaNegocio();
                provinciaNegocio.traerProvinciaPorId(localidad.getIdProvincia(), new ProvinciaNegocio.ProvinciaCallback() {
                    @Override
                    public void onSuccess(String mensaje) {

                    }

                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void onProvinciaLoaded(Provincia provincia) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                txtubicacion.setText( provincia.getNombre()+", "+localidad.getNombre()+ ", "+ PuntoVerde.getCalleAltura());
                            }
                        });

                    }
                });


            }
        });

        PuntoVerde_PremioNegocio puntoVerdePremioNegocio = new PuntoVerde_PremioNegocio();

        puntoVerdePremioNegocio.traerPuntoVerde_PremioPorId(PuntoVerde.getIdPuntoVerde(),intent.getIntExtra("idPremio", -1), new PuntoVerde_PremioNegocio.PuntoVerde_PremioCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onPuntoVerde_PremioLoaded(PuntoVerde_Premio puntoVerde_Premio) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(puntoVerde_Premio.getStock()>0){
                            btnElegir.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Aquí puedes usar los valores y la imagen como desees, por ejemplo, pasarlos a la siguiente actividad
                                    SharedPreferences sharedPreferences = activity.getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE);


                                    Intent intent2 = new Intent(context, activity_canje_exitoso.class);

                                    intent2.putExtra("idUsuario", sharedPreferences.contains("idUsuario"));
                                    intent2.putExtra("idPuntoVerde", PuntoVerde.getIdPuntoVerde());
                                    intent2.putExtra("idPremio", intent.getIntExtra("idPremio", -1));
                                    intent2.putExtra("Precio", intent.getIntExtra("Precio", -1));
                                    intent2.putExtra("Cantidad", 1);


                                    // Agrega la bandera FLAG_ACTIVITY_NEW_TASK
                                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    // Inicia la actividad
                                    context.startActivity(intent2);
                                    btnElegir.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        else{
                            mostrarDialogoSinPuntos();
                        }
                    }
                });

            }
        });



        return convertView;
    }
    private void mostrarDialogoSinPuntos() {

        // Crea un AlertDialog
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustomStyle);
        builder.setTitle("Error puntaje");
        builder.setMessage("No hay Stock en este punto verde, por favor elegir otro");

        // Agregar botones al AlertDialog
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // No se realiza ninguna acción si se selecciona "No".
                dialog.dismiss();
            }
        });

        // Mostrar el AlertDialog
        builder.create().show();
    }
}
