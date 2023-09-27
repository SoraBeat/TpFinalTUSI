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
import com.example.tpfinaltusi.entidades.Noticia;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {

    private List<Noticia> noticias;
    private Context context;

    public NoticiaAdapter(Context context, List<Noticia> noticias) {
        this.context = context;
        this.noticias = noticias;
    }

    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_noticias, parent, false);
        return new NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        Noticia noticia = noticias.get(position);

        // Configurar los datos de la noticia en la vista personalizada
        int mes = noticia.getFechaAlta().getMonth();
        String fecha ="";
        switch (mes) {
            case 0:
                fecha = noticia.getFechaAlta().getDay()+"\nEne.";
                break;
            case 1:
                fecha = noticia.getFechaAlta().getDay()+"\nFeb.";
                break;
            case 2:
                fecha = noticia.getFechaAlta().getDay()+"\nMar.";
                break;
            case 3:
                fecha = noticia.getFechaAlta().getDay()+"\nAbr.";
                break;
            case 4:
                fecha = noticia.getFechaAlta().getDay()+"\nMay.";
                break;
            case 5:
                fecha = noticia.getFechaAlta().getDay()+"\nJun.";
                break;
            case 6:
                fecha = noticia.getFechaAlta().getDay()+"\nJul.";
                break;
            case 7:
                fecha = noticia.getFechaAlta().getDay()+"\nAgo.";
                break;
            case 8:
                fecha = noticia.getFechaAlta().getDay()+"\nSept.";
                break;
            case 9:
                fecha = noticia.getFechaAlta().getDay()+"\nOct.";
                break;
            case 10:
                fecha = noticia.getFechaAlta().getDay()+"\nNov.";
                break;
            case 11:
                fecha = noticia.getFechaAlta().getDay()+"\nDic.";
                break;
            default:
                System.out.println("Mes no válido");
                break;
        }
        holder.dateTextView.setText(fecha);
        holder.titleTextView.setText(noticia.getTitulo());
        holder.descriptionTextView.setText(noticia.getCuerpo().substring(0,130)+"...");
        String pureBase64Encoded = noticia.getImagen().substring(noticia.getImagen().indexOf(",")  + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.image.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuando se hace clic en un elemento, abrir la actividad de detalles de noticia
                Intent intent = new Intent(context, DetalleNoticia.class);
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
