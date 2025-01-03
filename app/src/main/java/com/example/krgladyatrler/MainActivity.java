package com.example.krgladyatrler;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageButton sesButon;
    private boolean isMusicMuted = false; // Arkaplan müziği durdurma durumu
    private boolean isAllMuted = false;  // Tüm sesleri kapatma durumu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Tam ekran moduna geçiş
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Navigasyon çubuğunu gizleme
        hideSystemUI();

        setContentView(R.layout.activity_main);

        // Window insets ayarları
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        // "Kör Gladyatörler" başlığına yanıp sönme efekti ekleme
        TextView baslikText = findViewById(R.id.baslikText);
        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        baslikText.startAnimation(blinkAnimation);

        // "Başla" butonunun tıklama işlemi
        ImageButton baslaButton = findViewById(R.id.baslaButton);
        baslaButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, gameActivity.class);
            startActivity(intent);
        });

        // Ses butonu tanımlama
        sesButon = findViewById(R.id.sesbutton);

        // Arkaplan müziğini başlatma
        Intent musicServiceIntent = new Intent(this, MusicService.class);
        startService(musicServiceIntent);

        // Ses butonuna tıklama işlemleri
        sesButon.setOnClickListener(v -> handleSesButonu(musicServiceIntent));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Müzik servisini durdurma (activity arka planda iken)
        Intent musicServiceIntent = new Intent(this, MusicService.class);
        stopService(musicServiceIntent);
    }

    // Ses butonu işlemleri
    private void handleSesButonu(Intent musicServiceIntent) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        if (!isMusicMuted && !isAllMuted) {
            // Arkaplan müziğini durdur
            stopService(musicServiceIntent);
            isMusicMuted = true;
            sesButon.setImageResource(R.drawable.sesyok);
        } else if (isMusicMuted && !isAllMuted) {
            // Tüm sesleri kapat
            if (audioManager != null) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
            isAllMuted = true;
            sesButon.setImageResource(R.drawable.seskapali);
        } else {
            // Tüm sesleri aç
            startService(musicServiceIntent);
            if (audioManager != null) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
            isMusicMuted = false;
            isAllMuted = false;
            sesButon.setImageResource(R.drawable.ses);
        }
    }

    // Sistem çubuklarını gizleme ve tam ekran modu etkinleştirme
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.systemBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
    }
}
