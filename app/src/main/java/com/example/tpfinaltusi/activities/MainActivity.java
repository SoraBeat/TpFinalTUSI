package com.example.tpfinaltusi.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.db.IntegrationDB;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Realizar la conexión a la base de datos en un hilo secundario
        new ConnectToDatabaseTask().execute();
    }

    private class ConnectToDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // Llamar a la función testConnection de IntegrationDB en segundo plano
            return IntegrationDB.testConnection();
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (isConnected) {
                // La conexión a la base de datos es exitosa, puedes continuar con otras acciones
                // ...

                // Mostrar un comentario de conexión exitosa en el logcat
                System.out.println("Conexión exitosa a la base de datos.");
            } else {
                // La conexión a la base de datos ha fallado, puedes manejarlo aquí
                Toast.makeText(getApplicationContext(), "Error de conexión a la base de datos", Toast.LENGTH_SHORT).show();

                // Mostrar un comentario de conexión fallida en el logcat
                System.out.println("Error de conexión a la base de datos.");
            }
        }
    }
}
