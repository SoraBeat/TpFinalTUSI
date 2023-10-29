package com.example.tpfinaltusi.adicionales;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.DetalleNoticia;
import com.example.tpfinaltusi.activities.DetalleNoticiaAdmin;
import com.example.tpfinaltusi.entidades.Noticia;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoticiaAdapterAdmin extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {

    private List<Noticia> noticias;
    private Context context;

    public NoticiaAdapterAdmin(Context context, List<Noticia> noticias) {
        this.context = context;
        this.noticias = noticias;
    }


    @NonNull
    @Override
    public NoticiaAdapter.NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_noticias_admin, parent, false);
        return new NoticiaAdapter.NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaAdapter.NoticiaViewHolder holder, int position) {
        Noticia noticia = noticias.get(position);

        // Configurar los datos de la noticia en la vista personalizada
        int mes = noticia.getFechaAlta().getMonth();
        String fecha ="";
        switch (mes) {
            case 0:
                fecha = noticia.getFechaAlta().getDate()+"\nEne.";
                break;
            case 1:
                fecha = noticia.getFechaAlta().getDate()+"\nFeb.";
                break;
            case 2:
                fecha = noticia.getFechaAlta().getDate()+"\nMar.";
                break;
            case 3:
                fecha = noticia.getFechaAlta().getDate()+"\nAbr.";
                break;
            case 4:
                fecha = noticia.getFechaAlta().getDate()+"\nMay.";
                break;
            case 5:
                fecha = noticia.getFechaAlta().getDate()+"\nJun.";
                break;
            case 6:
                fecha = noticia.getFechaAlta().getDate()+"\nJul.";
                break;
            case 7:
                fecha = noticia.getFechaAlta().getDate()+"\nAgo.";
                break;
            case 8:
                fecha = noticia.getFechaAlta().getDate()+"\nSept.";
                break;
            case 9:
                fecha = noticia.getFechaAlta().getDate()+"\nOct.";
                break;
            case 10:
                fecha = noticia.getFechaAlta().getDate()+"\nNov.";
                break;
            case 11:
                fecha = noticia.getFechaAlta().getDate()+"\nDic.";
                break;
            default:
                System.out.println("Mes no válido");
                break;
        }
        holder.dateTextView.setText(fecha);
        holder.titleTextView.setText(noticia.getTitulo());
        if(noticia.getCuerpo().length()>=130){
            holder.descriptionTextView.setText(noticia.getCuerpo().substring(0,130)+"...");
        }else{
            holder.descriptionTextView.setText(noticia.getCuerpo());
        }
        String pureBase64Encoded = noticia.getImagen().substring(noticia.getImagen().indexOf(",")  + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.image.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuando se hace clic en un elemento, abrir la actividad de detalles de noticia
                Intent intent = new Intent(context, DetalleNoticiaAdmin.class);
                // Puedes pasar datos adicionales a la actividad si es necesario
                intent.putExtra("id_noticia", noticia.getIdNoticia());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView image;

        public NoticiaViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            image = itemView.findViewById(R.id.newsImageView);
        }
    }
}