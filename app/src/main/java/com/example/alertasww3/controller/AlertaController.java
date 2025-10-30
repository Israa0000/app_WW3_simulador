package com.example.alertasww3.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.alertasww3.model.Alerta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlertaController {

    private static final ArrayList<Alerta> alertasTotales = new ArrayList<>();

    public static void asegurarDatos(Context context) {
        if (alertasTotales.isEmpty()) {
            cargarYProgramarAlertasSimuladas(context);
        }
    }

    public static List<Alerta> getAlertasTotales() {
        return alertasTotales;
    }

    public static Alerta getUltimaAlerta() {
        if (!alertasTotales.isEmpty()) {
            return alertasTotales.get(alertasTotales.size() - 1);
        }
        return Alerta.ALERTA_NULA;
    }

    public static Alerta getAlertaPorId(int id) {
        for (Alerta alerta : alertasTotales) {
            if (alerta.getId() == id) {
                return alerta;
            }
        }
        return Alerta.ALERTA_NULA;
    }

    public static void añadirAlerta(Alerta alerta, Context context) {
        boolean existe = false;
        for (Alerta a : alertasTotales) {
            if (a.getId() == alerta.getId()) {
                existe = true;
            }
        }
        if (!existe && alerta.getId() != 0) {
            alertasTotales.add(alerta);
            Collections.sort(alertasTotales, Comparator.comparingLong(Alerta::getFechahora));
            programarAlerta(context, alerta);
        }
    }

    private static void programarAlerta(Context context, Alerta alerta) {
        Intent intent = new Intent(context, AlertaReceiver.class);
        intent.putExtra("Alerta_id", alerta.getId());
        intent.putExtra("Alerta_titulo", alerta.getTitulo());
        intent.putExtra("Alerta_descripcion", alerta.getDescripcion());

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        flags |= PendingIntent.FLAG_IMMUTABLE;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alerta.getId(), intent, flags);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alerta.getFechahora(), pendingIntent);
    }

    private static void cargarYProgramarAlertasSimuladas(Context context) {
        if (!alertasTotales.isEmpty()) {
            return;
        }
        long ahora = System.currentTimeMillis();// va en milisegundos, por eso utilo long
        Alerta a1 = new Alerta(1, "Explosiones detectadas en Europa", "Detonaciones y anomalías atmosféricas registradas.", ahora,  "CRÍTICO INTERNACIONAL");
        Alerta a2 = new Alerta(2, "Detonaciones nucleares en España", "Múltiples detonaciones en territorio nacional.", ahora + 10000, "CRÍTICO NACIONAL");
        Alerta a3 = new Alerta(3, "Detonación en Vélez de Benaudalla", "Daño severo en los alrededores. Acceso restringido.", ahora + 20000,  "ALERTA ROJA");
        Alerta a4 = new Alerta(4, "Amenaza inminente en Armilla", "Probable detonación sobre instalación militar.", ahora + 30000, "AMENAZA INMINENTE");

        añadirAlerta(a1, context);
        añadirAlerta(a2, context);
        añadirAlerta(a3, context);
        añadirAlerta(a4, context);
    }
}
