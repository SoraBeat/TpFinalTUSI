package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tpfinaltusi.R;

public class Registro extends AppCompatActivity {
    boolean passwordVisible = true;
    boolean repeatPasswordVisible = true;
    private EditText etPassword;
    private ImageButton togglePasswordButton;
    private EditText etRepeatPassword;
    private ImageButton toggleRepeatPasswordButton;


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
        actionBarTitle.setText("Registro");

        setContentView(R.layout.activity_registro);
        /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
        etPassword = findViewById(R.id.et_password);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        etRepeatPassword = findViewById(R.id.et_repeatPassword);
        toggleRepeatPasswordButton = findViewById(R.id.toggleRepeatPasswordButton);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        comportamientoMostrarOcultarContrasenia();
        comportamientoMostrarOcultarContraseniaRepetida();
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
    private void comportamientoMostrarOcultarContraseniaRepetida(){
        //Controlar mostrar ocultar contraseña
        toggleRepeatPasswordButton.setBackgroundResource(R.drawable.eye_svgrepo_com); // Icono de ojo abierto
        toggleRepeatPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatPasswordVisible) {
                    etRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    toggleRepeatPasswordButton.setBackgroundResource(R.drawable.eye_close_3); // Icono de ojo cerrado
                    repeatPasswordVisible = false;
                } else {
                    etRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    toggleRepeatPasswordButton.setBackgroundResource(R.drawable.eye_svgrepo_com); // Icono de ojo abierto
                    repeatPasswordVisible = true;
                }
                etRepeatPassword.setSelection(etRepeatPassword.getText().length()); // Mover el cursor al final del texto
            }
        });
    }
}