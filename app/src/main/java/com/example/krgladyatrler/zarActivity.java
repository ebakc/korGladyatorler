package com.example.krgladyatrler;

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

public class zarActivity extends AppCompatActivity {

    private ImageView oyuncu1Zar, oyuncu2Zar;
    private ImageButton zarButton;
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

        // Kazanan TextView'i tanımla
        kazananText = findViewById(R.id.kazananText);

        // Zar butonuna tıklama işlemi
        zarButton.setOnClickListener(v -> {
            if (!isZarAtildi) {
                // Zar atma animasyonunu başlat
                isZarAtildi = true;
                zarButton.setEnabled(false);  // Butonu inaktif yap
                zarButton.setVisibility(View.INVISIBLE);  // Butonu invisible yap
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
                    zarButton.setEnabled(true);  // Butonu tekrar aktif yap
                    zarButton.setVisibility(View.VISIBLE);  // Butonu görünür yap
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
