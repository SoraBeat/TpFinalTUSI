package com.example.tpfinaltusi.adicionales;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.DialogViewImage;
import com.example.tpfinaltusi.entidades.CodigoQR;

import java.util.List;

public class CustomListAdapterQRListComplete extends BaseAdapter {
    private Context context;
    private FragmentManager fragmentManager;
    private List<CodigoQR> data;
    private DialogViewImage dialogViewImage;

    public CustomListAdapterQRListComplete(Context context, FragmentManager fragmentManager, List<CodigoQR> data) {
        this.context = context;
        this.fragmentManager = fragmentManager;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_qr_complete, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.img_qr);
        TextView tvCodigo = convertView.findViewById(R.id.tv_codigo);
        TextView tvPuntos = convertView.findViewById(R.id.tv_puntos);
        TextView tvAdmin = convertView.findViewById(R.id.tv_admin);
        TextView tvUsuario = convertView.findViewById(R.id.tv_usuario);

        CodigoQR item = data.get(position);
        String pureBase64Encoded = item.getImagen().substring(item.getImagen().indexOf(",") + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);
        tvCodigo.setText(item.getCodigo());
        tvPuntos.setText(String.valueOf(item.getPuntos()));
        tvAdmin.setText("ID Admin: "+item.getUsuarioQueLoCreo());
        tvUsuario.setText("ID Usuario: "+item.getIdusuario());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogViewImage == null || dialogViewImage.getDialog() == null || !dialogViewImage.getDialog().isShowing()) {
                    // Utiliza el fragmentManager que recibiste en el constructor
                    Bundle arguments = new Bundle();
                    arguments.putParcelable("PICTURE_SELECTED", bitmap);
                    dialogViewImage = DialogViewImage.newInstance(arguments);
                    dialogViewImage.show(fragmentManager, "DialogViewImage");
                }
            }
        });

        return convertView;
    }
}
