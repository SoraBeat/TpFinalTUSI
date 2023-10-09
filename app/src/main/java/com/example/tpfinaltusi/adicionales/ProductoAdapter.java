package com.example.tpfinaltusi.adicionales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.tpfinaltusi.entidades.Premio;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Premio> {
    private Context context;
    private List<Premio> Premios;

    public ProductoAdapter(Context context, List<Premio> Premios) {
        super(context, R.layout.list_item_producto, Canje);
        this.context = context;
        this.Premios = Premios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_producto, parent, false);
        }

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView productId = convertView.findViewById(R.id.product_id);
        TextView productName = convertView.findViewById(R.id.product_name);
        // Obtener el producto actual
        Premio producto = Premios.get(position);

        // Asignar los datos del producto a las vistas
        productId.setText(producto.getNombre());
        productName.setText(producto.getNombre());

        return convertView;
    }
}

