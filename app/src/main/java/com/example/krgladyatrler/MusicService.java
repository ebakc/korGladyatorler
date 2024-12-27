package com.example.krgladyatrler;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // Arkaplan müziğini başlat
        mediaPlayer = MediaPlayer.create(this, R.raw.korgladyatorlerthememusic); // Arkaplan müziği
        mediaPlayer.setLooping(true);  // Müzik döngüsel olacak
        mediaPlayer.start();  // Müzik başlat
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Bu servis bind edilemez, null döndürülüyor
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Servis durdurulursa müzik durdurulmalı ve kaynak serbest bırakılmalı
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
