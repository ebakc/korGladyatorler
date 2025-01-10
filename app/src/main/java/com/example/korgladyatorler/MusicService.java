package com.example.korgladyatorler;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();
    private boolean isMusicEnabled = true;

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void playMusic(int resourceId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, resourceId);
        mediaPlayer.setLooping(true);
        if (isMusicEnabled) {
            mediaPlayer.start();
        }
    }

    public void toggleMusic() {
        isMusicEnabled = !isMusicEnabled;
        if (mediaPlayer != null) {
            if (isMusicEnabled) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        }
    }

    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
} 