package com.example.tpfinaltusi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinaltusi.R;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtener una referencia a la Action Bar
        ActionBar actionBar = getSupportActionBar();

        // Verificar si la Action Bar no es nula y ocultarla
        if (actionBar != null) {
            actionBar.hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Este código se ejecutará después del tiempo especificado (SPLASH_TIMEOUT)

                // Iniciar la nueva Activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

                // Asegurarse de que la MainActivity no esté en la pila de actividades (opcional)
                finish();
            }
        }, SPLASH_TIMEOUT);
    }

}
