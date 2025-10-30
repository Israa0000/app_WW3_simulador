package com.example.alertasww3.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View; // Importar View
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.alertasww3.R;
import com.example.alertasww3.controller.AlertaController;
import com.example.alertasww3.model.Alerta;
import java.text.SimpleDateFormat;
import java.util.Collections; // Importar Collections
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UltimasAlertasActivity extends AppCompatActivity {

    LinearLayout containerAlertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimas_alertas);
        containerAlertas = findViewById(R.id.containerAlertas);
        mostrarAlertas();
    }

    private void mostrarAlertas() {
        AlertaController.asegurarDatos(this);
        List<Alerta> alertas = AlertaController.getAlertasTotales();

        // Ordenamos la lista para mostrar la más reciente arriba
        Collections.reverse(alertas);

        LayoutInflater inflater = LayoutInflater.from(this);
        containerAlertas.removeAllViews();

        for (Alerta alerta : alertas) {
            // Inflamos la vista de item_alerta
            View item = inflater.inflate(R.layout.item_alerta, containerAlertas, false);

            TextView titulo = item.findViewById(R.id.titulo);
            TextView descripcion = item.findViewById(R.id.descripcion);
            Button verDetalles = item.findViewById(R.id.verDetalles);

            // Formateamos la fecha
            Date fecha = new Date(alerta.getFechahora());
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String fechaFormateada = formato.format(fecha);

            // Asignamos los datos a las vistas
            titulo.setText(alerta.getTitulo());
            descripcion.setText(alerta.getDescripcion() + "\n(" + fechaFormateada + ")");

            // Configuramos el click del botón
            verDetalles.setOnClickListener(v -> {
                Intent intent = new Intent(UltimasAlertasActivity.this, DetalleAlertaActivity.class);
                intent.putExtra("Alerta_id", alerta.getId());
                startActivity(intent);
            });

            // Añadimos el item completo al contenedor
            containerAlertas.addView(item);
        }
    }
}
