package com.example.korgladyatorler;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    private ImageButton baslaButton, nasilOynanirButton;
    private LinearLayout nasilOynanirPanel;
    private TextView kapatButton;
    private MusicService musicService;
    private boolean serviceBound = false;
    private ImageButton sesButton;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            serviceBound = true;
            musicService.playMusic(R.raw.theme_music);
            updateMusicButton();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

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

        // UI elemanlarını tanımla
        baslaButton = findViewById(R.id.baslaButton);
        nasilOynanirButton = findViewById(R.id.nasilOynanirButton);
        nasilOynanirPanel = findViewById(R.id.nasilOynanirPanel);
        kapatButton = findViewById(R.id.kapatButton);

        // Panel arkaplanını ayarla
        nasilOynanirPanel.setBackgroundResource(R.drawable.rounded_panel);
        nasilOynanirPanel.setElevation(5);

        // Panel boyutunu ayarla
        ViewGroup.LayoutParams layoutParams = nasilOynanirPanel.getLayoutParams();
        layoutParams.width = dpToPx(450);  // 500'den 450'ye düşürdük
        nasilOynanirPanel.setLayoutParams(layoutParams);

        // Panel margin ve padding ayarları
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) nasilOynanirPanel.getLayoutParams();
        params.setMargins(
            dpToPx(20),  // left
            dpToPx(140), // top - daha yukarı çektik (180'den 140'a)
            dpToPx(20),  // right
            dpToPx(20)   // bottom
        );
        nasilOynanirPanel.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        // Kapatma butonu ayarları
        kapatButton.setTextSize(18);
        kapatButton.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        kapatButton.setTypeface(ResourcesCompat.getFont(this, R.font.alagard));

        // Bilgi metni ayarları - sol kısım
        TextView bilgiTextSol = findViewById(R.id.bilgiTextSol);
        TextView bilgiTextOrta = findViewById(R.id.bilgiTextOrta);
        TextView bilgiTextSag = findViewById(R.id.bilgiTextSag);

        bilgiTextSol.setTextSize(12);
        bilgiTextOrta.setTextSize(12);
        bilgiTextSag.setTextSize(12);

        // Sol kısım metni
        String bilgiMetniSol = "⚔️ ITEMLER\n\n" +
                "🗡️ KILIC\n" +
                "Rakibe dogrudan\n" +
                "hasar verir\n\n" +
                "🔮 ASA\n" +
                "Buyu gucu ile hasar\n" +
                "verir ve can yeniler\n\n" +
                "🛡️ KALKAN\n" +
                "Rakibin saldirisini\n" +
                "engeller";

        // Orta kısım metni
        String bilgiMetniOrta = "🎮 OYUN HAKKINDA\n\n" +
                "⚡ Her turda iki oyuncu\n" +
                "da item secer ve\n" +
                "secilen item sonraki\n" +
                "tur kullanilamaz.\n\n" +
                "🎲 Secimler sonrasi\n" +
                "zar atilir ve\n" +
                "hasarlar hesaplanir.\n\n" +
                "✨ Sans faktorunu ve\n" +
                "🧠 tahmin yeteneklerini\n" +
                "kullanarak rakibin\n" +
                "hamlesini tahmin et,\n" +
                "dogru secimi yap ve\n" +
                "oyunu kazan!🏆";

        // Sağ kısım metni
        String bilgiMetniSag = "📊 SEVIYELER\n\n" +
                "🟡 A SEVIYE\n" +
                "Sari renk ile\n" +
                "gosterilir ve en\n" +
                "yuksek etkiye sahip\n\n" +
                "🔵 B SEVIYE\n" +
                "Mavi renk ile\n" +
                "gosterilir ve orta\n" +
                "duzeyde etkili\n\n" +
                "⚪ C SEVIYE\n" +
                "Gri renk ile\n" +
                "gosterilir ve en\n" +
                "dusuk etkiye sahip";

        bilgiTextSol.setText(bilgiMetniSol);
        bilgiTextOrta.setText(bilgiMetniOrta);
        bilgiTextSag.setText(bilgiMetniSag);

        // Kolonlar arası boşlukları azalt
        LinearLayout solKisim = findViewById(R.id.solKisim);
        LinearLayout ortaKisim = findViewById(R.id.ortaKisim);
        ((LinearLayout.LayoutParams) solKisim.getLayoutParams()).setMarginEnd(dpToPx(20));  // 24'ten 20'ye
        ((LinearLayout.LayoutParams) ortaKisim.getLayoutParams()).setMarginEnd(dpToPx(20));  // 24'ten 20'ye

        // Nasıl Oynanır butonu için listener
        nasilOynanirButton.setOnClickListener(v -> {
            baslaButton.setVisibility(View.INVISIBLE);
            nasilOynanirButton.setVisibility(View.INVISIBLE);
            nasilOynanirPanel.setVisibility(View.VISIBLE);
        });

        // Kapatma butonu için listener
        kapatButton.setOnClickListener(v -> {
            nasilOynanirPanel.setVisibility(View.GONE);
            baslaButton.setVisibility(View.VISIBLE);
            nasilOynanirButton.setVisibility(View.VISIBLE);
        });

        // "Başla" butonunun tıklama işlemi
        baslaButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, itemSec.class);
            startActivity(intent);
        });

        // Müzik servisini başlat
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        sesButton = findViewById(R.id.menusesButton);
        sesButton.setOnClickListener(v -> {
            if (serviceBound) {
                musicService.toggleMusic();
                updateMusicButton();
            }
        });
    }

    private void updateMusicButton() {
        if (serviceBound) {
            sesButton.setImageResource(musicService.isMusicEnabled() ? 
                R.drawable.muzik_acik : R.drawable.muzik_kapali);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
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

    // dp'yi px'e çeviren yardımcı metod
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}