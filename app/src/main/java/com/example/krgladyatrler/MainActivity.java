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

        // Navigasyon çubuğunu gizleme fonksiyonu
        hideSystemUI();

        setContentView(R.layout.activity_main);

        // Window insets ayarları
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Kör gladyatörler yazısının yanıp sönme efekti
            TextView baslikText = findViewById(R.id.baslikText);
            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
            baslikText.startAnimation(blinkAnimation);

            return insets;
        });

        // Başla butonuna tıklama işlemi
        ImageButton baslaButton = findViewById(R.id.baslaButton);
        baslaButton.setOnClickListener(v -> {
            // Zar activity'ye geçiş yapmak için Intent kullandık
            Intent intent = new Intent(MainActivity.this, zarActivity.class);
            startActivity(intent);
        });

        // Ses butonunu tanımla
        sesButon = findViewById(R.id.sesbutton);

        // Arkaplan müziğini başlat
        Intent musicServiceIntent = new Intent(this, MusicService.class);
        startService(musicServiceIntent);

        // Ses butonuna tıklama işlemi
        sesButon.setOnClickListener(v -> {
            if (!isMusicMuted && !isAllMuted) {
                // Arkaplan müziğini kapat
                stopService(musicServiceIntent);
                isMusicMuted = true;
                sesButon.setImageResource(R.drawable.sesyok); // Yeni resim
            } else if (isMusicMuted && !isAllMuted) {
                // Tüm sesleri kapat
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true); // Tüm sesleri kapat
                }
                isAllMuted = true;
                sesButon.setImageResource(R.drawable.seskapali); // Yeni resim
            } else {
                // Tüm sesleri aç
                Intent restartMusicService = new Intent(this, MusicService.class);
                startService(restartMusicService);

                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false); // Tüm sesleri aç
                }

                isMusicMuted = false;
                isAllMuted = false;
                sesButon.setImageResource(R.drawable.ses); // İlk resim
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Müzik servisini durdur
        Intent musicServiceIntent = new Intent(this, MusicService.class);
        stopService(musicServiceIntent);  // Activity geçişi sırasında müzik durdurulacak
    }

    // Navigasyon bar gizleme fonksiyonu.
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getWindow().setDecorFitsSystemWindows(false);
            }
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
