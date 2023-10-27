package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;
import com.example.tpfinaltusi.entidades.Usuario;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    private boolean passwordVisible = true;
    private boolean repeatPasswordVisible = true;
    private EditText etAlias;
    private EditText etDni;
    private EditText etMail;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private ImageButton togglePasswordButton;
    private ImageButton toggleRepeatPasswordButton;
    private CheckBox cbCondiciones;
    private TextView btnCondicionesDelServicio;
    private Button btnRegistrarse;
    private TextView btnLogin;
    private ProgressBar progressBar ;
    private ShapeableImageView shapeableImageView;
    private static final int PICK_IMAGE = 1;
    private String imagenFinal="";
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
        etAlias = findViewById(R.id.et_alias);
        etDni = findViewById(R.id.et_dni);
        etMail = findViewById(R.id.et_mail);
        etPassword = findViewById(R.id.et_password);
        etRepeatPassword = findViewById(R.id.et_repeatPassword);
        cbCondiciones = findViewById(R.id.cb_TerminosCondiciones);
        btnCondicionesDelServicio = findViewById(R.id.btn_condicionesDelServicio);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        toggleRepeatPasswordButton = findViewById(R.id.toggleRepeatPasswordButton);
        btnRegistrarse = findViewById(R.id.btn_registrarse);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        shapeableImageView = findViewById(R.id.btn_seleccionar_imagen);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        shapeableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Solicitar permisos para leer almacenamiento externo si no se han otorgado
                if (ContextCompat.checkSelfPermission(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Registro.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
                } else {
                    openGallery();
                }
            }
        });
        comportamientoMostrarOcultarContrasenia();
        comportamientoMostrarOcultarContraseniaRepetida();
        comportamientoBotonCondicionesDelServicio();
        comportamientoBotonLogin();
        comportamientoBotonRegistro();
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permiso denegado, puedes mostrar un mensaje o tomar alguna acción
                Toast.makeText(this, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                String base64String = encodeImageToBase64(bitmap);
                shapeableImageView.setImageBitmap(bitmap);
                imagenFinal = base64String;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImageToBase64(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void comportamientoBotonLogin() {
        //Control boton registrarse
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    private void comportamientoBotonCondicionesDelServicio() {
        btnCondicionesDelServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un cuadro de diálogo personalizado para mostrar las condiciones del servicio
                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                builder.setTitle("Condiciones del Servicio");
                builder.setMessage(R.string.terminos_condicones);

                // Botón de Aceptar
                builder.setPositiveButton(Html.fromHtml("<font color='#47C68F'>Aceptar</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cerrar el cuadro de diálogo
                        dialog.dismiss();
                    }
                });

                // Mostrar el cuadro de diálogo
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void comportamientoMostrarOcultarContrasenia() {
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

    private void comportamientoMostrarOcultarContraseniaRepetida() {
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

    private void comportamientoBotonRegistro() {
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern patternMail = Pattern.compile("^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$");
                if (etAlias.getText().toString().equals("")) {
                    etAlias.setError("Campo requerido");
                } else if (etAlias.getText().toString().length() < 3) {
                    etAlias.setError("Minimo 3 caracteres");
                } else if (etAlias.getText().toString().length() > 10) {
                    etAlias.setError("Maximo 10 caracteres");
                } else if (etDni.getText().toString().equals("")) {
                    etDni.setError("Campo requerido");
                } else if (etDni.getText().toString().length() > 10) {
                    etDni.setError("Maximo 10 caracteres");
                } else if (etMail.getText().toString().equals("")) {
                    etMail.setError("Campo requerido");
                } else if (!patternMail.matcher(etMail.getText().toString()).matches()) {
                    etMail.setError("Email invalido");
                } else if (etPassword.getText().toString().equals("")) {
                    etPassword.setError("Campo requerido");
                } else if (etPassword.getText().toString().length() < 3) {
                    etPassword.setError("Minimo 3 caracteres");
                } else if (etPassword.getText().toString().length() > 10) {
                    etPassword.setError("Maximo 10 caracteres");
                } else if (!etRepeatPassword.getText().toString().equals(etPassword.getText().toString())) {
                    etRepeatPassword.setError("Las contraseñas no coinciden");
                } else if (!cbCondiciones.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Debe aceptar Terminos y Condiciones", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    btnRegistrarse.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                    btnRegistrarse.setClickable(false);
                    String alias = etAlias.getText().toString();
                    String dni = etDni.getText().toString();
                    String email = etMail.getText().toString();
                    String password = etPassword.getText().toString();

                    Usuario nuevoUsuario = new Usuario(alias, dni, email, password, 0, Date.valueOf(LocalDate.now().toString()), Date.valueOf(LocalDate.now().toString()),0,false,imagenFinal);
                    new Thread(() -> {
                        UsuarioNegocio negocio = new UsuarioNegocio();
                        boolean resultado = negocio.crearUsuario(nuevoUsuario);

                        // Actualizar la interfaz de usuario en el hilo principal
                        runOnUiThread(() -> {
                            if (resultado) {
                                // Éxito: mostrar un mensaje de éxito o realizar alguna acción adicional
                                Toast.makeText(getApplicationContext(), "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Login.class);
                                startActivity(i);
                            } else {
                                // Error: mostrar un mensaje de error o realizar alguna acción adicional
                                Toast.makeText(getApplicationContext(), "Error al crear usuario", Toast.LENGTH_SHORT).show();
                            }
                        });
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegistrarse.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                            btnRegistrarse.setClickable(true);
                        });
                    }).start();

                }
            }
        });
    }
}