package com.example.alertasww3.controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.example.alertasww3.R;
import com.example.alertasww3.view.DetalleAlertaActivity;

public class AlertaReceiver extends BroadcastReceiver {
    private static final String TAG = "AlertaReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int alertaId = intent.getIntExtra("Alerta_id", 0);
        String alertaTitulo = intent.getStringExtra("Alerta_titulo");

        Intent detalleIntent = new Intent(context, DetalleAlertaActivity.class);
        detalleIntent.putExtra("Alerta_id", alertaId);

        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(detalleIntent)
                .getPendingIntent(alertaId, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "canal_alertas";
        String channelName = "Alertas_ww3";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Canal para notificaciones de alertas críticas");

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 3000, 200, 500});

           // la notificaion se salta las reglas de no molestar
            channel.setBypassDnd(true); //Dnd es Do not disturb
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificacionBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle(alertaTitulo)
                .setContentText("Nueva alerta recibida. Pulsa para ver detalles.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND);

        //se muestra la notificación
        notificationManager.notify(alertaId, notificacionBuilder.build());
        Log.d(TAG, "Notificación mostrada con ID: " + alertaId);

        // linterna
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Log.d(TAG, "Intentando activar la linterna después de la notificación...");
            linterna(context, true); // Encender

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                linterna(context, false); // Apagar
            }, 3000);
        }
    }

    private void linterna(Context context, boolean encender) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager == null) {
            Log.e(TAG, "Error: CameraManager es nulo.");
            return;
        }
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            Log.d(TAG, "Intentando cambiar modo linterna a: " + encender);
            cameraManager.setTorchMode(cameraId, encender);
            Log.d(TAG, "Modo linterna cambiado a: " + encender);
        } catch (Exception e) {
            Log.e(TAG, "error al intentar cambiar la linterna a " + encender, e);
        }
    }
}
