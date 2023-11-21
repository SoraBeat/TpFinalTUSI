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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

public class ListItemCanjeAdapter extends ArrayAdapter<Canje> {
    private Context context;
    private ActivityListCanje activity;
    private int filtro;
    private List<Canje> canjes;
    private List<Canje> canjesFiltrados; // Lista filtrada

    public ListItemCanjeAdapter(ActivityListCanje activity, Context context, List<Canje> canjes, int filtro) {
        super(context, 0, canjes);
        this.context = context;
        this.activity = activity;
        this.filtro = filtro;
        this.canjes = canjes;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_list_item_canjes, parent, false);
        }

        filtrarPorEstado();


        // Obtener una instancia del Producto en la posición actual
        Canje canje = getItemFromFiltrados(position);

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView productTitle = convertView.findViewById(R.id.product_title);
        TextView canjeEstado = convertView.findViewById(R.id.Estado);
        TextView canjeNumero = convertView.findViewById(R.id.txt_Numero_canje);
        ImageView productImag = convertView.findViewById(R.id.product_image);
        TextView puntoverdeDireccion = convertView.findViewById(R.id.Direccion);
        ProgressBar progressBar = convertView.findViewById(R.id.cargando);
        LinearLayout contenedor = convertView.findViewById(R.id.contenedor);

        contenedor.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

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
                                        contenedor.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
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

    public List<Canje> filtrarPorEstado() {
        if (canjesFiltrados == null) {
            canjesFiltrados = new ArrayList<>();
        } else {
            canjesFiltrados.clear();
        }


        for (Canje canje : canjes) {

            if(filtro == 1){
                canjesFiltrados.add(canje);
            }
            else if(filtro == 2 && canje.isEstado() == true){
                canjesFiltrados.add(canje);

            }
            else if(filtro == 3 && canje.isEstado() == false){
                canjesFiltrados.add(canje);
            }

        }

        notifyDataSetChanged();
        return canjesFiltrados;
    }
    private Canje getItemFromFiltrados(int position) {
        return canjesFiltrados.get(position);
    }
}
