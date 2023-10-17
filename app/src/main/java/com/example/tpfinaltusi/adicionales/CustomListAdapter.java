package com.example.tpfinaltusi.adicionales;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.CodigoQR;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private List<CodigoQR> data; // Reemplaza YourDataModel con tu modelo de datos

    public CustomListAdapter(Context context, List<CodigoQR> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_qr, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.img_qr);
        TextView tvCodigo = convertView.findViewById(R.id.tv_codigo);
        TextView tvPuntos = convertView.findViewById(R.id.tv_puntos);

        CodigoQR item = data.get(position);
        String pureBase64Encoded = item.getImagen().substring(item.getImagen().indexOf(",") + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);
        tvCodigo.setText(item.getCodigo());
        tvPuntos.setText(String.valueOf(item.getPuntos()));

        return convertView;
    }
}
