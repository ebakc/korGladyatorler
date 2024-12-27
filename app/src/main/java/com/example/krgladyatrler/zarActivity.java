package com.example.krgladyatrler;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

import android.media.AudioManager;

public class zarActivity extends AppCompatActivity {

    private ImageButton sesButon;
    private boolean isMusicMuted = false; // Arkaplan müziği durdurma durumu
    private boolean isAllMuted = false;  // Tüm sesleri kapatma durumu

    private ImageView oyuncu1Zar, oyuncu2Zar;
    private ImageButton zarButton, devamEtButton;
    private TextView kazananText;  // Kazanan yazısını gösterecek TextView
    private Random random = new Random();
    private Handler handler = new Handler();
    private int[] zarResimleri = {
            R.drawable.zar1, R.drawable.zar2, R.drawable.zar3,
            R.drawable.zar4, R.drawable.zar5, R.drawable.zar6
    };

    private boolean isZarAtildi = false; // Zar atılıp atılmadığını kontrol etmek için flag
    private Runnable animasyonRunnable; // Zar animasyonu için Runnable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Tam ekrana geçiş. (setContentView'dan önce yazılmalı.)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Navigasyon çubuğunu gizleme fonksiyonunu aktif ediyoruz.
        hideSystemUI();
        setContentView(R.layout.activity_zar);

        // ImageView'ları tanımla
        oyuncu1Zar = findViewById(R.id.oyuncu1Zar);
        oyuncu2Zar = findViewById(R.id.oyuncu2Zar);

        // Zar butonunu tanımla
        zarButton = findViewById(R.id.zarButton);

        // Devam et butonunu tanımla
        devamEtButton = findViewById(R.id.devamEtButton);

        // Kazanan TextView'i tanımla
        kazananText = findViewById(R.id.kazananText);

        // Devam et butonunu başlangıçta gizle
        devamEtButton.setVisibility(View.GONE);

        // Zar butonuna tıklama işlemi
        zarButton.setOnClickListener(v -> {
            if (!isZarAtildi) {
                // Zar atma animasyonunu başlat
                isZarAtildi = true;
                zarButton.setEnabled(false);  // Butonu inaktif yap
                zarButton.setVisibility(View.INVISIBLE);  // Zar butonunu gizle
                kazananText.setText("");  // Kazanan yazısını temizle
                zarAtmaAnimasyonu();
            }
        });

        // Window insets ayarları (UI elemanları için padding ekler)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // sesButon tanımlama
        sesButon = findViewById(R.id.sesbutton2);

        // Arkaplan müziğini başlat
        Intent musicServiceIntent = new Intent(this, MusicService.class);
        startService(musicServiceIntent);

        // sesButon için tıklama işlemi
        sesButon.setOnClickListener(v -> {
            if (!isMusicMuted && !isAllMuted) {
                // İlk tıklama: Arkaplan müziğini kapat
                stopService(musicServiceIntent);
                isMusicMuted = true;
                sesButon.setImageResource(R.drawable.sesyok); // Yeni resim
            } else if (isMusicMuted && !isAllMuted) {
                // İkinci tıklama: Tüm sesleri kapat
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true); // Tüm sesleri kapat
                }
                isAllMuted = true;
                sesButon.setImageResource(R.drawable.seskapali); // Yeni resim
            } else {
                // Üçüncü tıklama: Tüm sesleri aç
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

    private void zarAtmaAnimasyonu() {
        // Runnable animasyon oluşturuluyor
        animasyonRunnable = new Runnable() {
            @Override
            public void run() {
                int oyuncu1ZarIndex = random.nextInt(6);
                int oyuncu2ZarIndex = random.nextInt(6);

                // Random olarak zarları göster
                oyuncu1Zar.setImageResource(zarResimleri[oyuncu1ZarIndex]);
                oyuncu2Zar.setImageResource(zarResimleri[oyuncu2ZarIndex]);

                // 300ms aralıklarla zarları döndür
                handler.postDelayed(this, 300);
            }
        };

        // Zarları döndürmeye başladık
        handler.post(animasyonRunnable);

        // 3 saniye sonra animasyonu durduruyor ve zarları sabitliyoruz
        handler.postDelayed(() -> {
            // Zarları sabitle
            int finalOyuncu1Zar = random.nextInt(6);
            int finalOyuncu2Zar = random.nextInt(6);

            // Eğer zarlar eşitse, hemen yeni zar seçilecek
            if (finalOyuncu1Zar == finalOyuncu2Zar) {
                // Zarlar eşitse yeni animasyon başlat
                zarAtmaAnimasyonu();
            } else {
                oyuncu1Zar.setImageResource(zarResimleri[finalOyuncu1Zar]);
                oyuncu2Zar.setImageResource(zarResimleri[finalOyuncu2Zar]);

                // Kazananı belirle
                if (finalOyuncu1Zar > finalOyuncu2Zar) {
                    kazananText.setText("Oyuncu 1 Kazandı!");  // Kazananı yaz
                } else if (finalOyuncu1Zar < finalOyuncu2Zar) {
                    kazananText.setText("Oyuncu 2 Kazandı!");  // Kazananı yaz
                }

                // 3 saniye sonra butonu tekrar aktif yap
                handler.postDelayed(() -> {
                    isZarAtildi = false;
                    zarButton.setEnabled(true);  // Zar butonunu tekrar aktif yap
                    zarButton.setVisibility(View.INVISIBLE);  // Zar butonunu gizle
                    devamEtButton.setVisibility(View.VISIBLE);  // Devam Et butonunu görünür yap
                }, 1000);
            }

            // Animasyon bitince handler'ı temizleyelim
            handler.removeCallbacks(animasyonRunnable);

        }, 3000);  // 3 saniye sonra zarlar sabitlenir
    }

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
