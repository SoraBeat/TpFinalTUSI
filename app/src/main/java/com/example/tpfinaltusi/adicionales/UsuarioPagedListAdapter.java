package com.example.tpfinaltusi.adicionales;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Usuario;

public class UsuarioPagedListAdapter extends PagedListAdapter<Usuario, UsuarioPagedListAdapter.UsuarioViewHolder> {

    public UsuarioPagedListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = getItem(position);
        if (usuario != null) {
            if(usuario.getImagen() != null && !usuario.getImagen().equals("") && usuario.getImagen().length()>0){
                String pureBase64Encoded = usuario.getImagen().substring(usuario.getImagen().indexOf(",") + 1);
                byte[] imageBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.imageViewProfile.setImageBitmap(bitmap);
            }
            // Mostrar los datos de usuario
            holder.textViewNombreApellido.setText(usuario.getAlias());
            holder.textViewDNI.setText(usuario.getDni());
            holder.textViewEmail.setText(usuario.getEmail());
        }
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewNombreApellido;
        TextView textViewDNI;
        TextView textViewEmail;

        UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewNombreApellido = itemView.findViewById(R.id.textViewNombreApellido);
            textViewDNI = itemView.findViewById(R.id.textViewDNI);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }

    private static final DiffUtil.ItemCallback<Usuario> DIFF_CALLBACK = new DiffUtil.ItemCallback<Usuario>() {
        @Override
        public boolean areItemsTheSame(@NonNull Usuario oldItem, @NonNull Usuario newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Usuario oldItem, @NonNull Usuario newItem) {
            return oldItem.equals(newItem);
        }
    };
}