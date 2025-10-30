package com.example.alertasww3.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.alertasww3.R;
import com.example.alertasww3.controller.AlertaController;
import com.example.alertasww3.model.Alerta;
import java.util.Calendar;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {

    EditText tituloAdmin;
    EditText descripcionAdmin;
    EditText nivelAdmin;
    Button btnCrearAlerta;
    Button btnElegirFecha;
    Button btnElegirHora;
    TextView tvFechaHora;
    int año;
    int mes;
    int dia;
    int hora;
    int minuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        AlertaController.asegurarDatos(this);

        tituloAdmin = findViewById(R.id.tituloAdmin);
        descripcionAdmin = findViewById(R.id.descripcionAdmin);
        nivelAdmin = findViewById(R.id.nivelAdmin);
        btnCrearAlerta = findViewById(R.id.btnCrearAlerta);
        btnElegirFecha = findViewById(R.id.btnElegirFecha);
        btnElegirHora = findViewById(R.id.btnElegirHora);
        tvFechaHora = findViewById(R.id.tvFechaHora);

        final Calendar calendario = Calendar.getInstance();
        año = calendario.get(Calendar.YEAR);
        mes = calendario.get(Calendar.MONTH);
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minuto = calendario.get(Calendar.MINUTE);

        elegirHora();
        elegirFecha();
        crearAlerta();
        actualizarFechaHora();
    }


    void crearAlerta() {
        btnCrearAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //si estan vacios se lo dice al usuario
                if (tituloAdmin.getText().toString().isEmpty() ||
                        descripcionAdmin.getText().toString().isEmpty() ||
                        nivelAdmin.getText().toString().isEmpty()) {
                    Toast.makeText(AdminActivity.this, "rellena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                int nuevoId = 1;
                if (!AlertaController.getAlertasTotales().isEmpty()) {
                    nuevoId = AlertaController.getUltimaAlerta().getId() + 1;
                }

                Alerta alerta = new Alerta(
                        nuevoId,
                        tituloAdmin.getText().toString(),
                        descripcionAdmin.getText().toString(),
                        0,
                        nivelAdmin.getText().toString()
                );

                Calendar calendario = Calendar.getInstance();
                calendario.set(año, mes, dia, hora, minuto, 0);
                alerta.setFechahora(calendario.getTimeInMillis());
                AlertaController.añadirAlerta(alerta, AdminActivity.this);
                Toast.makeText(AdminActivity.this, "Alerta creada y programada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    void elegirHora() {
        btnElegirHora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();
                new TimePickerDialog(
                        AdminActivity.this,
                        (timePicker, h, min) -> {
                            hora = h;
                            minuto = min;
                            actualizarFechaHora();
                        },
                        calendario.get(Calendar.HOUR_OF_DAY),
                        calendario.get(Calendar.MINUTE),
                        true
                ).show();
            }
        });
    }


    void elegirFecha() {
        btnElegirFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                new DatePickerDialog(
                        AdminActivity.this,
                        (datePicker, a, m, d) -> {
                            año = a;
                            mes = m;
                            dia = d;
                            actualizarFechaHora();
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }
    private void actualizarFechaHora() {
        tvFechaHora.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d %02d:%02d", dia, mes + 1, año, hora, minuto));
        // lo de  "%02d/%02d/%04d %02d:%02d" simplemente le da formato a la fecha y hora para que siempre tengan dos digitos
        //menos el año
    }
}
