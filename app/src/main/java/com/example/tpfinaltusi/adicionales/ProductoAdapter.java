package com.example.tpfinaltusi.adicionales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tpfinaltusi.R;

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
            convertView = inflater.inflate(R.layout.fragment_item_canje, parent, false);
        }

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView productTitle = convertView.findViewById(R.id.product_title);
        TextView productDesc = convertView.findViewById(R.id.product_Desc);
        TextView productPts = convertView.findViewById(R.id.product_Pts);

        // Asignar los datos estáticos del producto a las vistas
        productTitle.setText("Mate");
        productDesc.setText("Disfruta de la tradición argentina con este elegante mate de calabaza y bombilla. Perfecto para compartir y relajarse.");
        productPts.setText("200 Pts");

        return convertView;
    }

    @Override
    public int getCount() {
        // Definir la cantidad de elementos en la lista
        return 3; // Tres elementos estáticos
    }
}
