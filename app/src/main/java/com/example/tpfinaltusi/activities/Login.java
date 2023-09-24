package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.db.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements DatabaseConnector.ConnectionListener{
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private ImageButton togglePasswordButton;
    private TextView btnOlvidasteContraseña;
    private TextView btnRegistrarse;
    boolean passwordVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //////////////////////////////////CONFIGURACION ACTION BAR////////////////////////////////////////////
        super.onCreate(savedInstanceState);
        // Habilitar ActionBar y configurar vista personalizada
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        // Desactivar la elevación de la ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        // Inflar la vista personalizada
        View customActionBarView = getSupportActionBar().getCustomView();

        // Configurar el título centrado (opcional)
        TextView actionBarTitle = customActionBarView.findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Login");

        setContentView(R.layout.activity_login);
        /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
        btnLogin = findViewById(R.id.btn_login);
        etEmail = findViewById(R.id.et_mail);
        etPassword = findViewById(R.id.et_password);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        btnRegistrarse = findViewById(R.id.btn_registrarse);
        btnOlvidasteContraseña = findViewById(R.id.btn_olvidasteContraseña);

        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        comportamientoMostrarOcultarContrasenia();
        comportamientoBotonRegistrarse();
        comportamientoBotonOlvidasteContrasenia();
        comportamientoBotonLogin();

        DatabaseConnector.connectInBackground(this);

    }

    private void comportamientoMostrarOcultarContrasenia(){
        //Controlar mostrar ocultar contraseña
        togglePasswordButton.setBackgroundResource(R.drawable.eye_svgrepo_com); // Icono de ojo abierto
        togglePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordVisible) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    togglePasswordButton.setBackgroundResource(R.drawable.eye_close_3); // Icono de ojo cerrado
                    passwordVisible = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordButton.setBackgroundResource(R.drawable.eye_svgrepo_com); // Icono de ojo abierto
                    passwordVisible = true;
                }
                etPassword.setSelection(etPassword.getText().length()); // Mover el cursor al final del texto
            }
        });
    }
    private void comportamientoBotonRegistrarse(){
        //Control boton registrarse
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "FALTA HACER XD", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void comportamientoBotonOlvidasteContrasenia(){
        //Control boton olvidaste contraseña
        btnOlvidasteContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "FALTA HACER XD", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void comportamientoBotonLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern patternMail = Pattern.compile( "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$");

                if(etEmail.getText().toString().equals("")){
                    etEmail.setError("Campo requerido");
                }else if(!patternMail.matcher(etEmail.getText().toString()).matches()){
                    etEmail.setError("Email invalido");
                }else if(etPassword.getText().toString().equals("")){
                    etPassword.setError("Campo requerido");
                }else{
                    ///////ACA PROGRAMAR LOGIN
                }
            }
        });
    }

    @Override
    public void onConnectionSuccess() {
        // La conexión a la base de datos fue exitosa, puedes realizar acciones adicionales aquí
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Actualiza la UI si es necesario
            }
        });
    }

    @Override
    public void onConnectionError(String error) {
        // Hubo un error en la conexión, muestra un mensaje de error o realiza acciones de manejo de errores
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Login.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}