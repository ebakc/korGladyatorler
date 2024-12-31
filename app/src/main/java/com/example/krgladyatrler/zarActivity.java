package com.example.krgladyatrler;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
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
    private boolean isMusicMuted = false; // Arkaplan müziğinin durdurulup durdurulmadığını kontrol eder
    private boolean isAllMuted = false;  // Tüm seslerin kapalı olup olmadığını kontrol eder

    public int kazananOyuncu; // Kazanan oyuncunun bilgisini tutar

    private ImageView oyuncu1Zar, oyuncu2Zar;
    private ImageButton zarButton, devamEtButton;
    private TextView kazananText; // Kazananı göstermek için TextView
    private Random random = new Random();
    private Handler handler = new Handler();
    private int[] zarResimleri = {
            R.drawable.zar1, R.drawable.zar2, R.drawable.zar3,
            R.drawable.zar4, R.drawable.zar5, R.drawable.zar6
    };

    private boolean isZarAtildi = false; // Zarın atılıp atılmadığını kontrol etmek için bayrak
    private Runnable animasyonRunnable; // Zar animasyonu için Runnable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Tam ekran modu ayarı
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();
        setContentView(R.layout.activity_zar);

        // Bileşenlerin tanımlanması
        oyuncu1Zar = findViewById(R.id.oyuncu1Zar);
        oyuncu2Zar = findViewById(R.id.oyuncu2Zar);
        zarButton = findViewById(R.id.zarButton);
        devamEtButton = findViewById(R.id.devamEtButton);
        kazananText = findViewById(R.id.kazananText);
        sesButon = findViewById(R.id.sesbutton2);

        // Başlangıç ayarları
        devamEtButton.setVisibility(View.GONE); // "Devam Et" butonunu başlangıçta gizle

        zarButton.setOnClickListener(v -> {
            if (!isZarAtildi) {
                isZarAtildi = true; // Zarın atıldığını işaretle
                zarButton.setEnabled(false); // Zar butonunu devre dışı bırak
                zarButton.setVisibility(View.INVISIBLE); // Zar butonunu gizle
                kazananText.setText(""); // Kazanan yazısını temizle
                zarAtmaAnimasyonu(); // Zar atma animasyonunu başlat
            }
        });

        sesButon.setOnClickListener(v -> handleSesButonu()); // Ses butonuna tıklama işlemi

        devamEtButton.setOnClickListener(v -> {
            Intent intent = new Intent(zarActivity.this, itemActivity.class);
            startActivity(intent); // "Devam Et" butonuna tıklayınca yeni aktiviteye geç
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Zar animasyonunu başlatır
    private void zarAtmaAnimasyonu() {
        animasyonRunnable = new Runnable() {
            @Override
            public void run() {
                // Zar animasyon görsellerini güncelle
                int oyuncu1ZarIndex = random.nextInt(6);
                int oyuncu2ZarIndex = random.nextInt(6);

                oyuncu1Zar.setImageResource(zarResimleri[oyuncu1ZarIndex]);
                oyuncu2Zar.setImageResource(zarResimleri[oyuncu2ZarIndex]);

                handler.postDelayed(this, 300); // 300ms aralıklarla çalıştır
            }
        };

        handler.post(animasyonRunnable); // Animasyonu başlat

        handler.postDelayed(() -> {
            handler.removeCallbacks(animasyonRunnable); // Animasyonu durdur

            // Final zar değerlerini belirle
            int finalOyuncu1Zar = random.nextInt(6);
            int finalOyuncu2Zar = random.nextInt(6);

            oyuncu1Zar.setImageResource(zarResimleri[finalOyuncu1Zar]);
            oyuncu2Zar.setImageResource(zarResimleri[finalOyuncu2Zar]);

            // Kazananı belirle
            if (finalOyuncu1Zar > finalOyuncu2Zar) {
                kazananText.setText("Oyuncu 1 Kazandı!");
                kazananOyuncu = 1;
            } else if (finalOyuncu1Zar < finalOyuncu2Zar) {
                kazananText.setText("Oyuncu 2 Kazandı!");
                kazananOyuncu = 2;
            } else {
                kazananText.setText("Berabere, tekrar zar atılıyor...");
                zarAtmaAnimasyonu(); // Beraberlik durumunda tekrar başlat
                return;
            }

            devamEtButton.setVisibility(View.VISIBLE); // "Devam Et" butonunu göster

        }, 2000); // Animasyon süresi 2 saniye
    }

    // Ses butonuna tıklama işlemini yönetir
    private void handleSesButonu() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (!isMusicMuted && !isAllMuted) {
            stopService(new Intent(this, MusicService.class));
            isMusicMuted = true;
            sesButon.setImageResource(R.drawable.sesyok);
        } else if (isMusicMuted && !isAllMuted) {
            if (audioManager != null) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
            isAllMuted = true;
            sesButon.setImageResource(R.drawable.seskapali);
        } else {
            startService(new Intent(this, MusicService.class));
            if (audioManager != null) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
            isMusicMuted = false;
            isAllMuted = false;
            sesButon.setImageResource(R.drawable.ses);
        }
    }

    // Sistem çubuklarını gizler ve tam ekran modu etkinleştirir
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