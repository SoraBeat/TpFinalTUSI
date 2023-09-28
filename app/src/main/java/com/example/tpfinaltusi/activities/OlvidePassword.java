package com.example.tpfinaltusi.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpfinaltusi.Negocio.UsuarioNegocio;
import com.example.tpfinaltusi.R;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OlvidePassword extends AppCompatActivity {
    EditText et_mail;
    Button btn_recuperar;

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
        actionBarTitle.setText("Recuperar contraseña");

        setContentView(R.layout.activity_olvide_password);
        /////////////////////////////////////OBTENER ELEMENTOS///////////////////////////////////////////////
        btn_recuperar = findViewById(R.id.btn_recuperar);
        et_mail = findViewById(R.id.et_mail);
        /////////////////////////////////////FUNCIONES COMPORTAMIENTO////////////////////////////////////////
        comportamientoRecuperar();
    }

    private void comportamientoRecuperar() {
        btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_mail.getText().toString();
                Pattern patternMail = Pattern.compile("^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$");
                if (email.isEmpty()) {
                    et_mail.setError("Campo requerido");
                } else if (!patternMail.matcher(et_mail.getText().toString()).matches()) {
                    et_mail.setError("Email invalido");
                } else {
                    UsuarioNegocio usuarioNegocio = new UsuarioNegocio();
                    usuarioNegocio.obtenerContraseñaPorEmail(email, new UsuarioNegocio.ContraseñaCallback() {
                        @Override
                        public void onContraseñaObtenida(String contraseña) {
                            //Enviar mail
                            // Enviar correo electrónico
                            enviarCorreo(email, contraseña);
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OlvidePassword.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void enviarCorreo(String recipientEmail, String contraseña) {
        // Configurar propiedades para la sesión de correo electrónico
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP server for Gmail
        props.put("mail.smtp.port", "587"); // Port for Gmail's SMTP server

        // Credenciales del remitente
        final String senderEmail = "ecosostenibleoficial@gmail.com";
        final String senderPassword = "wztvsovghyuedymk";

        // Crear una sesión de correo electrónico
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Crear un mensaje de correo electrónico
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Recuperación de contraseña");
            message.setText("Tu contraseña es: " + contraseña);

            // Enviar el mensaje de correo electrónico
            Transport.send(message);

            // Mensaje de éxito
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OlvidePassword.this, "Correo electrónico enviado con éxito", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MessagingException e) {
            // Manejo de errores
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(OlvidePassword.this, "Error al enviar el correo electrónico", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}