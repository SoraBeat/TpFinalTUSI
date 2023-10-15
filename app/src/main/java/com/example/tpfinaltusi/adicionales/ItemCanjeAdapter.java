package com.example.tpfinaltusi.adicionales;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.activity_canje_exitoso;
import com.example.tpfinaltusi.entidades.Premio;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ItemCanjeAdapter extends ArrayAdapter<Premio> {
    private Context context;

    public ItemCanjeAdapter(Context context, List<Premio> Premio) {
        super(context, 0, Premio);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_item_canje, parent, false);
        }
        // Obtener una instancia del Producto en la posición actual
        Premio premio = getItem(position);

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView productTitle = convertView.findViewById(R.id.product_title);
        TextView productDesc = convertView.findViewById(R.id.product_Desc);
        ImageView productImag = convertView.findViewById(R.id.product_image);
        TextView productPts = convertView.findViewById(R.id.product_Pts);
        Button boton = convertView.findViewById(R.id.btnCanjear);

        //Seteamos bitmap para la imagen
        String pureBase64Encoded = premio.getImagen().substring(premio.getImagen().indexOf(",") + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        // Asignar los datos del Producto a las vistas
        productTitle.setText(premio.getNombre());
        productDesc.setText(premio.getDescripcion());
        productPts.setText(String.valueOf(premio.getPrecio()));
        productImag.setImageBitmap(bitmap);

        View finalConvertView = convertView;
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertDialog(finalConvertView, premio);
            }
        });

        return convertView;
    }

    private void mostrarAlertDialog(View view, final Premio premio) {

        Snackbar snackbar = Snackbar.make(view,"", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(ContextCompat.getColor(context, R.color.black));
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.black));
        TextView tv2 = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv2.setVisibility(View.GONE);
        TextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextSize(20);
        tv.setAllCaps(false);
        snackbar.setAction("Apreta aqui para canjear", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Realiza una acción cuando se hace clic en el botón de acción
                // Acceder a los valores del premio directamente
                String title = premio.getNombre();

                // Aquí puedes usar los valores y la imagen como desees, por ejemplo, pasarlos a la siguiente actividad
                Intent intent = new Intent(context, activity_canje_exitoso.class);
                intent.putExtra("title", title);

                //Damos de baja la cantidad de puntos canjeados
                int idUsuario = UsuarioNegocio.obtenerIDUsuario(context);
                UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
                usuarioNegocio.restarPuntosAUsuarioPorId(idUsuario, premio.getPrecio());

                snackbar.getContext().startActivity(intent);
            }
        });
        snackbar.show();
    }
}
