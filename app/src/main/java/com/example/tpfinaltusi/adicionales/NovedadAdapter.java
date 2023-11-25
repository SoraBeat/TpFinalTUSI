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
import com.example.tpfinaltusi.activities.DetalleReporte;
import com.example.tpfinaltusi.entidades.Informe;
import com.example.tpfinaltusi.entidades.Informe_Historial;

import java.text.SimpleDateFormat;
import java.util.List;

public class NovedadAdapter extends RecyclerView.Adapter<NovedadAdapter.NovedadViewHolder>{
    private List<Informe_Historial> informes;
    private Context context;

    public NovedadAdapter(Context context, List<Informe_Historial> informes){
        this.context = context;
        this.informes = informes;
    }

    @NonNull
    @Override
    public NovedadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_novedad, parent, false);
        return new NovedadAdapter.NovedadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NovedadViewHolder holder, int position) {
        Informe_Historial informe = informes.get(position);

        // Configurar los datos de la noticia en la vista personalizada
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(informe.getFecha());
        if(informe.getIdEstado()==0){
            holder.infoTextView.setText(fecha + " - Reporte rechazado");
        }
        if(informe.getIdEstado() == 1){
            if(informe.isResultado()){
                holder.infoTextView.setText(fecha + " - Reporte aprobado");
            } else {
                holder.infoTextView.setText(fecha + " - Reporte rechazado");
            }
        }
        if(informe.getIdEstado() == 2){
            holder.infoTextView.setText(fecha + " - Reporte en revision");
        }
        if(informe.getIdEstado() == 3){
            holder.infoTextView.setText(fecha + " - Reporte en revision para cerrar");
        }
        if(informe.getIdEstado() == 4){
            holder.infoTextView.setText(fecha + " - Reporte cerrado exitosamente");
        }
        holder.titleTextView.setText(informe.getTitulo());
        holder.descriptionTextView.setText(informe.getCuerpo());
        String pureBase64Encoded = informe.getIMG().substring(informe.getIMG().indexOf(",")  + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.image.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuando se hace clic en un elemento, abrir la actividad de detalles de noticia
                Intent intent = new Intent(context, DetalleReporte.class);
                // Puedes pasar datos adicionales a la actividad si es necesario
                intent.putExtra("idinforme_historial", informe.getIdInforme_Historial());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return informes.size();
    }
    public static class NovedadViewHolder extends RecyclerView.ViewHolder {
        TextView infoTextView;
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView image;

        public NovedadViewHolder(@NonNull View itemView) {
            super(itemView);
            infoTextView = itemView.findViewById(R.id.infoTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            image = itemView.findViewById(R.id.newsImageView);
        }
    }
}
