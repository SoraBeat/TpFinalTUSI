package color.tpfinaltusi.adicionales;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.tpfinaltusi.activities.OlvidePassword;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.adicionales.activity_canje_exitoso;

public class ProductoAdapter extends ArrayAdapter<String> {
    private Context context;

    public ProductoAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_item_canje, parent, false);
        }

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView productTitle = convertView.findViewById(R.id.product_title);
        TextView productDesc = convertView.findViewById(R.id.product_Desc);
        TextView productPts = convertView.findViewById(R.id.product_Pts);

        // Asignar los datos estáticos del producto a las vistas
        productTitle.setText("Mate");
        productDesc.setText("Disfruta de la tradición argentina con este elegante mate de calabaza y bombilla. Perfecto para compartir y relajarse.");
        productPts.setText("200");

        // Obtén una referencia al botón dentro del elemento de la lista
        Button boton = convertView.findViewById(R.id.btnCanjear);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlertDialog(position);
            }
        });
        // Configura el evento de clic para el botón
        return convertView;
    }
    private void mostrarAlertDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustomStyle);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Está seguro de canjear este producto en la posición " + position + "?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, activity_canje_exitoso.class);
                context.startActivity(intent);
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

    @Override
    public int getCount() {
        // Definir la cantidad de elementos en la lista
        return 3; // Tres elementos estáticos
    }
}
