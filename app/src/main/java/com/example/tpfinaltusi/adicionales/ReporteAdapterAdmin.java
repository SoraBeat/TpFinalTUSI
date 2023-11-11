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
import com.example.tpfinaltusi.activities.DetalleReporteAdmin;
import com.example.tpfinaltusi.entidades.Informe;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReporteAdapterAdmin extends RecyclerView.Adapter<ReporteAdapterAdmin.ReporteAdminViewHolder> {
    private List<Informe> informes;
    private Context context;

    public ReporteAdapterAdmin(Context context, List<Informe> informes) {
        this.context = context;
        this.informes = informes;
    }

    @NonNull
    @Override
    public ReporteAdapterAdmin.ReporteAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view_reporte, parent, false);
        return new ReporteAdapterAdmin.ReporteAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteAdapterAdmin.ReporteAdminViewHolder holder, int position) {
        Informe informe = informes.get(position);

        // Configurar los datos de la noticia en la vista personalizada
        int mes = informe.getFechaAlta().getMonth();
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(informe.getFechaAlta());
        String descripcionEstado = "";
        switch (informe.getIdEstado()){
            case 1:
                descripcionEstado = "Activo";
                break;
            case 2:
                descripcionEstado = "Pendiente";
                break;
            case 3:
                descripcionEstado = "Revisión";
                break;
            case 4:
                descripcionEstado = "Terminado";
                break;
        }
        fecha += " - " + descripcionEstado;
        holder.infoTextView.setText(fecha);
        holder.titleTextView.setText(informe.getTitulo());
        holder.descriptionTextView.setText(informe.getCuerpo());
        String pureBase64Encoded = informe.getImagen().substring(informe.getImagen().indexOf(",")  + 1);
        byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.image.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuando se hace clic en un elemento, abrir la actividad de detalles de noticia
                Intent intent = new Intent(context, DetalleReporteAdmin.class);
                // Puedes pasar datos adicionales a la actividad si es necesario
                intent.putExtra("id_informe", informe.getIdInforme());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return informes.size();
    }

    public static class ReporteAdminViewHolder extends RecyclerView.ViewHolder {
        TextView infoTextView;
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView image;

        public ReporteAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            infoTextView = itemView.findViewById(R.id.infoTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            image = itemView.findViewById(R.id.newsImageView);
        }
    }
}
