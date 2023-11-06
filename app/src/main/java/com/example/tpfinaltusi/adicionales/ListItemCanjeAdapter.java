package com.example.tpfinaltusi.adicionales;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.tpfinaltusi.Negocio.PremioNegocio;
import com.example.tpfinaltusi.Negocio.ProvinciaNegocio;
import com.example.tpfinaltusi.Negocio.PuntoVerdeNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.ActivityListCanje;
import com.example.tpfinaltusi.activities.activity_punto_verde_canje;
import com.example.tpfinaltusi.entidades.Canje;
import com.example.tpfinaltusi.entidades.Localidad;
import com.example.tpfinaltusi.entidades.Premio;
import com.example.tpfinaltusi.entidades.Provincia;
import com.example.tpfinaltusi.entidades.PuntoVerde;

import java.util.List;

public class ListItemCanjeAdapter extends ArrayAdapter<Canje> {
    private Context context;
    private ActivityListCanje activity;

    public ListItemCanjeAdapter(ActivityListCanje activity, Context context, List<Canje> canjes) {
        super(context, 0, canjes);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_list_item_canjes, parent, false);
        }
        // Obtener una instancia del Producto en la posición actual
        Canje canje = getItem(position);

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView productTitle = convertView.findViewById(R.id.product_title);
        TextView canjeEstado = convertView.findViewById(R.id.Estado);
        TextView canjeNumero = convertView.findViewById(R.id.txt_Numero_canje);
        ImageView productImag = convertView.findViewById(R.id.product_image);
        TextView puntoverdeDireccion = convertView.findViewById(R.id.Direccion);

        canjeNumero.setText(String.valueOf(canje.getIdCanje()));


        if(canje.isEstado()){
            canjeEstado.setText("Retirado");
        }
        else{
            canjeEstado.setText("Pediente");
        }


        PremioNegocio premioNegocio = new PremioNegocio();
        premioNegocio.traerPremioPorId(canje.getIdPremio(), new PremioNegocio.PremioCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onPremioLoaded(Premio premio) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Seteamos bitmap para la imagen
                        String pureBase64Encoded = premio.getImagen().substring(premio.getImagen().indexOf(",") + 1);
                        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        // Asignar los datos del Producto a las vistas
                        productTitle.setText(premio.getNombre());
                        productImag.setImageBitmap(bitmap);
                    }
                });

            }
        });

        PuntoVerdeNegocio puntoVerdeNegocio = new PuntoVerdeNegocio();
        puntoVerdeNegocio.traerPuntoVerdePorId(canje.getIdPuntoVerde(), new PuntoVerdeNegocio.PuntoVerdeCallback() {
            @Override
            public void onSuccess(String mensaje) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onPuntoVerdeLoaded(PuntoVerde puntoVerde) {
                LocalidadNegocio localidadNegocio = new LocalidadNegocio();
                localidadNegocio.traerLocalidadPorId(puntoVerde.getIdLocalidad(), new LocalidadNegocio.LocalidadCallback() {
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

                                        puntoverdeDireccion.setText( provincia.getNombre()+", "+localidad.getNombre()+ ", "+ puntoVerde.getCalleAltura());
                                    }
                                });

                            }
                        });


                    }
                });
            }
        });

        return convertView;
    }

}
