package com.example.alertasww3.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.alertasww3.R;
import com.example.alertasww3.controller.AlertaController;
import com.example.alertasww3.model.Alerta;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetalleAlertaActivity extends AppCompatActivity {
    TextView titulo;
    TextView descripcion;
    TextView fechaHora;
    TextView nivel;
    Button btnVolverAlMenu;
    Alerta alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alerta);

        AlertaController.asegurarDatos(this);

        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        fechaHora = findViewById(R.id.fechayHora);
        nivel = findViewById(R.id.nivel);
        btnVolverAlMenu = findViewById(R.id.btnVolverAlMenu);

        int alertaId = getIntent().getIntExtra("Alerta_id", 0);
        alerta = AlertaController.getAlertaPorId(alertaId);

        if (alerta.getId() == 0) {
            Toast.makeText(this, "Error no hay ninguna alerta " + alertaId, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setInfoAlerta();
        volverAlMenu();
    }

    private void setInfoAlerta() {
        Date fecha = new Date(alerta.getFechahora());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String fechaFormateada = formato.format(fecha);
        titulo.setText(alerta.getTitulo());
        descripcion.setText(alerta.getDescripcion());
        fechaHora.setText(fechaFormateada);
        nivel.setText(alerta.getNivel());
    }

    private void volverAlMenu() {
        btnVolverAlMenu.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleAlertaActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }


}
