package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.activities.Login;
import com.example.tpfinaltusi.db.IntegrationDB;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Realizar una conexión de prueba a la base de datos MySQL
        Connection connection = IntegrationDB.connect();

        if (connection != null) {
            // La conexión se ha establecido correctamente. Puedes mostrar un mensaje o registrar el éxito.
            // En este ejemplo, simplemente imprimiremos un mensaje en la consola.
            System.out.println("Conexión exitosa a la base de datos MySQL en Railway");
            IntegrationDB.disconnect(connection);
        } else {
            // No se pudo conectar a la base de datos. Puedes mostrar un mensaje de error si lo deseas.
            System.err.println("No se pudo conectar a la base de datos MySQL en Railway");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
