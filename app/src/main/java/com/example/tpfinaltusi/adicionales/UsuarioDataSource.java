package com.example.tpfinaltusi.adicionales;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.example.tpfinaltusi.entidades.Usuario;

import java.util.List;

public class UsuarioDataSource extends ItemKeyedDataSource<Integer, Usuario> {
    private List<Usuario> usuarios;
    private PagedList.Config config;

    public UsuarioDataSource(List<Usuario> usuarios, PagedList.Config config) {
        this.usuarios = usuarios;
        this.config = config;
    }

    @Override
    public void loadInitial(@NonNull ItemKeyedDataSource.LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Usuario> callback) {
        List<Usuario> sublist = usuarios.subList(0, Math.min(params.requestedLoadSize, usuarios.size()));
        callback.onResult(sublist);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Usuario> callback) {
        int start = params.key + params.requestedLoadSize;
        int end = start + params.requestedLoadSize;

        if (start < usuarios.size()) {
            List<Usuario> sublist = usuarios.subList(start, Math.min(end, usuarios.size()));
            callback.onResult(sublist);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Usuario> callback) {
        int end = params.key;
        int start = Math.max(0, end - params.requestedLoadSize);

        if (start < end) {
            List<Usuario> sublist = usuarios.subList(Math.max(0, start), Math.min(end, usuarios.size()));
            callback.onResult(sublist);
        }
    }

    @NonNull
    @Override
    public Integer getKey(@NonNull Usuario item) {
        // Podrías devolver la posición del usuario en la lista como clave
        return usuarios.indexOf(item);
    }
}
