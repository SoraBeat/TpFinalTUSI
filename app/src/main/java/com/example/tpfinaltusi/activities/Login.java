package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.DAO.UsuarioDAO;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Usuario;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private ImageButton togglePasswordButton;
    private TextView btnOlvidasteContraseña;
    private TextView btnRegistrarse;
    boolean passwordVisible = true;
    private ProgressBar progressBar ;

    private CountDownLatch connectionLatch = new CountDownLatch(1);

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
        progressBar = findViewById(R.id.progressBar);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        comportamientoMostrarOcultarContrasenia();
        comportamientoBotonRegistrarse();
        comportamientoBotonOlvidasteContrasenia();
        comportamientoBotonLogin();


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
                Intent intent = new Intent(getApplicationContext(),Registro.class);
                startActivity(intent);
            }
        });
    }
    private void comportamientoBotonOlvidasteContrasenia(){
        //Control boton olvidaste contraseña
        btnOlvidasteContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OlvidePassword.class);
                startActivity(i);
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
                    progressBar.setVisibility(View.VISIBLE);
                    btnLogin.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                    btnLogin.setClickable(false);
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    // Realizar el inicio de sesión en un hilo o AsyncTask
                    new Thread(() -> {
                        boolean loginExitoso = UsuarioNegocio.login(email, password);

                        // Mostrar el resultado en el hilo principal
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                            btnLogin.setClickable(true);
                            if (loginExitoso) {
                                // Inicio de sesión exitoso

                                guardarCredenciales(email,password);
                            } else {
                                // Inicio de sesión fallido
                                Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }).start();

                }
            }
        });
    }

    private void guardarCredenciales(String email,String password){
        UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
        usuarioNegocio.buscarUsuarioPorCredenciales(this, email, password, new UsuarioNegocio.UsuarioCallback() {
            @Override
            public void onSuccess(String mensaje) {
                // Handle success
            }

            @Override
            public void onError(String error) {
                // Handle error
            }

            @Override
            public void onUsuarioLoaded(Usuario usuario) {
                UsuarioNegocio.guardarIDUsuario(getApplicationContext(),usuario.getId());
                // Handle loaded user
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }

}