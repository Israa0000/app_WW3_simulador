package com.example.alertasww3.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.alertasww3.R;
import com.example.alertasww3.controller.AlertaController;
import com.example.alertasww3.model.Alerta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView titulo;
    TextView descripcion;
    TextView fechaHora;
    TextView nivel;
    TextView ultimasAlertas;
    Button verUltimasAlertas;
    Button verDetalles;
    public int contadorClicks = 0;
    final int clicksParaAdmin = 5;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                estadoPermisos();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertaController.asegurarDatos(this);

        titulo = findViewById(R.id.tituloAlerta);
        descripcion = findViewById(R.id.descripcionAlerta);
        fechaHora = findViewById(R.id.fechayHora);
        nivel = findViewById(R.id.nivelAlerta);
        ultimasAlertas = findViewById(R.id.ultimasAlertas);
        verUltimasAlertas = findViewById(R.id.VerUltimasAlertas);
        verDetalles = findViewById(R.id.btnVerDetalles);

        mostrarUltimaAlerta();
        irAVerUltimasAlertas();
        irAVerDetalles();
        solicitarPermisos();
        estadoPermisos();

    }
    private void solicitarPermisos() {
        List<String> permisosNecesarios = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permisosNecesarios.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permisosNecesarios.add(Manifest.permission.CAMERA);
        }

        if (!permisosNecesarios.isEmpty()) {
            requestPermissionLauncher.launch(permisosNecesarios.toArray(new String[0]));
        } else {
            estadoPermisos();
        }
    }

    private void estadoPermisos() {
        // Estado del permiso de Notificaciones
        //buildversion. representa el nicel del la API de android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones: CONCEDIDO.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de notificaciones: DENEGADO.", Toast.LENGTH_LONG).show();
            }
        }

        // Estado del permiso de Linterna
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso de linterna: CONCEDIDO.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permiso de linterna: DENEGADO.", Toast.LENGTH_LONG).show();
        }
    }

    void irAVerDetalles() {
        verDetalles.setOnClickListener(view -> {
            Alerta alerta = AlertaController.getUltimaAlerta();
            if (alerta.getId() != 0) {
                Intent intent = new Intent(this, DetalleAlertaActivity.class);
                intent.putExtra("Alerta_id", alerta.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "No hay detalles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void mostrarUltimaAlerta() {
        Alerta alerta = AlertaController.getUltimaAlerta();
        if (alerta.getId() != 0) {
            Date fecha = new Date(alerta.getFechahora());
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String fechaFormateada = formato.format(fecha);

            titulo.setText(alerta.getTitulo());
            descripcion.setText(alerta.getDescripcion());
            fechaHora.setText(fechaFormateada);
            nivel.setText(alerta.getNivel());
            ultimasAlertas.setText("Ultima alerta");
            verDetalles.setEnabled(true);
        } else {
            titulo.setText("No hay alertas");
            descripcion.setText("Actualmente no hay ninguna alerta para mostrar.");
            fechaHora.setText("");
            nivel.setText("");
            ultimasAlertas.setText("Alertas");
            verDetalles.setEnabled(false);
        }
    }

    void irAVerUltimasAlertas() {
        verUltimasAlertas.setOnClickListener(view -> {
            contadorClicks++;
            Intent intent;
            if (contadorClicks >= clicksParaAdmin) {
                contadorClicks = 0; //reinici contadot
                intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                int clicsRestantes = clicksParaAdmin - contadorClicks;
                if (clicsRestantes < clicksParaAdmin - 1) { // q no se salga al primer clcik
                    String mensaje = "Te faltan " + clicsRestantes + " clicks para ir a administrador";
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                }

                // tiempo de 5 segundos para q se reinicie
                view.postDelayed(() -> {
                    if (contadorClicks > 0 && contadorClicks < clicksParaAdmin) {
                        contadorClicks = 0;
                        Intent i = new Intent(MainActivity.this, UltimasAlertasActivity.class);
                        startActivity(i);
                    }
                }, 2000); // 2 segundos de espera antes de ir a la lista de alertas
            }
        });
    }
}


