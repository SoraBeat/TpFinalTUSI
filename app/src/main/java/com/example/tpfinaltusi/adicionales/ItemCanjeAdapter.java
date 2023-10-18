package com.example.tpfinaltusi.adicionales;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import com.example.tpfinaltusi.entidades.Usuario;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ItemCanjeAdapter extends ArrayAdapter<Premio> {
    private Context context;
    private int puntos;

    public ItemCanjeAdapter(Context context, List<Premio> Premio, int puntos) {
        super(context, 0, Premio);
        this.context = context;
        this.puntos = puntos;
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

        int precioPremio = premio.getPrecio(); // Suponiendo que premio.getPrecio() devuelve un entero

        System.out.println("Valor de puntos: " + puntos);
        System.out.println("Precio del premio: " + precioPremio);


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validamos si el usuario tiene suficientes puntos para canjear el premio
                if(puntos >= premio.getPrecio()){
                    mostrarDialogoConfirmacion(premio);
                }
                else{
                    mostrarDialogoSinPuntos(premio);
                }

            }
        });

        return convertView;
    }

    private void mostrarDialogoConfirmacion(final Premio premio) {
        // Realiza una acción cuando se hace clic en el botón de acción
        // Acceder a los valores del premio directamente
        String title = premio.getNombre();

        // Crea un AlertDialog
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustomStyle);
        builder.setTitle("Confirmar canje");
        builder.setMessage("¿Estás seguro de que deseas canjear el premio " + title + "?");

        // Agregar botones al AlertDialog
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Realiza una acción cuando se hace clic en el botón de acción
                // Acceder a los valores del premio directamente
                String title = premio.getNombre();

                // Aquí puedes usar los valores y la imagen como desees, por ejemplo, pasarlos a la siguiente actividad
                Intent intent = new Intent(context, activity_canje_exitoso.class);
                intent.putExtra("title", title);
                intent.putExtra("idPremio", premio.getIdPremio());
                intent.putExtra("Precio", premio.getPrecio());

                // Agrega la bandera FLAG_ACTIVITY_NEW_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Inicia la actividad
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // No se realiza ninguna acción si se selecciona "No".
                dialog.dismiss();
            }
        });

        // Mostrar el AlertDialog
        builder.create().show();
    }
    private void mostrarDialogoSinPuntos(final Premio premio) {
        // Realiza una acción cuando se hace clic en el botón de acción
        // Acceder a los valores del premio directamente
        String title = premio.getNombre();

        // Crea un AlertDialog
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustomStyle);
        builder.setTitle("Error puntaje");
        builder.setMessage("No es posible canjear el premio--------------- " + title + ", puntos no suficientes");

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
