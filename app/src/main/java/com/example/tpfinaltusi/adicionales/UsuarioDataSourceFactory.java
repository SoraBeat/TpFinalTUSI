package com.example.tpfinaltusi.adicionales;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import com.example.tpfinaltusi.entidades.Usuario;

import java.util.List;

public class UsuarioDataSourceFactory extends DataSource.Factory<Integer, Usuario> {
    private List<Usuario> usuarios;
    private PagedList.Config config;

    public UsuarioDataSourceFactory(List<Usuario> usuarios, PagedList.Config config) {
        this.usuarios = usuarios;
        this.config = config;
    }

    @NonNull
    @Override
    public DataSource<Integer, Usuario> create() {
        return new UsuarioDataSource(usuarios, config);
    }
}
