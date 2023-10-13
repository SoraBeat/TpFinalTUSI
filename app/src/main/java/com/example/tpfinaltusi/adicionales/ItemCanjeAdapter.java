package color.tpfinaltusi.adicionales;

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

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.activity_canje_exitoso;
import com.example.tpfinaltusi.entidades.Premio;

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

        // Asignar los datos del Producto a las vistas
        productTitle.setText(premio.getNombre());
        String pureBase64Encoded = premio.getImagen().substring(premio.getImagen().indexOf(",")  + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        productPts.setText(String.valueOf(premio.getPrecio()));

        // Obtén una referencia al botón dentro del elemento de la lista
        Button boton = convertView.findViewById(R.id.btnCanjear);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertDialog(position);
            }
        });

        return convertView;
    }
    private void mostrarAlertDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustomStyle);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Está seguro de canjear este producto en la posición " + position + "?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acceder a las vistas en el elemento de la lista actual
                View listItemView = getView(position, null, null);
                if (listItemView != null) {
                    TextView productTitle = listItemView.findViewById(R.id.product_title);
                    TextView productPts = listItemView.findViewById(R.id.product_Pts);

                    // Obtener los valores de las vistas
                    String title = productTitle.getText().toString();


                    // Aquí puedes usar los valores y la imagen como desees, por ejemplo, pasarlos a la siguiente actividad
                    Intent intent = new Intent(context, activity_canje_exitoso.class);
                    intent.putExtra("title", title);

                    //Damos de baja la cantidad de puntos canjeados
                    int idUsuario = UsuarioNegocio.obtenerIDUsuario(getContext());
                    UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
                    usuarioNegocio.restarPuntosAUsuarioPorId(idUsuario, Integer.parseInt(productPts.getText().toString()));

                    context.startActivity(intent);
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha cancelado el canje, no es necesario realizar ninguna acción aquí.
            }
        });

        builder.show();
    }

}
