package com.example.korgladyatorler;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class gameActivity extends AppCompatActivity {

    // UI Elemanlarını tanımla
    private ImageButton oyuncu1Kilic, oyuncu1Asa, oyuncu1Kalkan, oyuncu2Kilic, oyuncu2Asa, oyuncu2Kalkan, itemsecButton;
    private TextView oyuncu1Hp, oyuncu2Hp, hpText;
    private ImageView oyuncu1Zar, oyuncu2Zar, oyuncu1Char, oyuncu2Char;

    private ArrayList<String> kiliclar, asalar, kalkanlar;
    private Random random = new Random();

    private boolean oyuncu1SecimYapti = false, oyuncu2SecimYapti = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // UI elemanlarını tanımla
        uiElemanlariTanimla();

        // Tam ekran ve sistem UI gizle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();

        // Item listelerini oluştur
        itemListeleriTanimla();

        // Rastgele item seçme
        rastgeleItemSec();

        // Butonlara tıklama olayları
        itemsecButton.setOnClickListener(v -> rastgeleItemSec());
        setItemClickListeners();

        // Window insets ayarları
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });
    }

    // UI elemanlarını tanımla
    private void uiElemanlariTanimla() {
        oyuncu1Kilic = findViewById(R.id.oyuncu1Kilic);
        oyuncu1Asa = findViewById(R.id.oyuncu1Asa);
        oyuncu1Kalkan = findViewById(R.id.oyuncu1Kalkan);
        oyuncu2Kilic = findViewById(R.id.oyuncu2Kilic);
        oyuncu2Asa = findViewById(R.id.oyuncu2Asa);
        oyuncu2Kalkan = findViewById(R.id.oyuncu2Kalkan);
        oyuncu1Hp = findViewById(R.id.oyuncu1Hp);
        oyuncu2Hp = findViewById(R.id.oyuncu2Hp);
        hpText = findViewById(R.id.hpText);
        itemsecButton = findViewById(R.id.itemsecButton);
        oyuncu1Zar = findViewById(R.id.oyuncu1Zar);
        oyuncu2Zar = findViewById(R.id.oyuncu2Zar);
        oyuncu1Char = findViewById(R.id.oyuncu1Char);
        oyuncu2Char = findViewById(R.id.oyuncu2Char);
    }

    // Item listelerini oluştur
    private void itemListeleriTanimla() {
        kiliclar = new ArrayList<>();
        asalar = new ArrayList<>();
        kalkanlar = new ArrayList<>();

        Collections.addAll(kiliclar, "id01_a_kilic_kritikseven", "id02_b_kilic_dengesizkesen", "id03_b_kilic_kahkahalicelik",
                "id04_c_kilic_paslidovuscu", "id05_c_kilic_topal", "id06_c_kilic_garipyarmac");

        Collections.addAll(asalar, "id07_a_asa_simsekyoldasi", "id08_b_asa_dengesizparlayan", "id09_b_asa_dogaefsunu",
                "id10_c_asa_catlakahsap", "id11_c_asa_titrekalev", "id12_c_asa_garipfisilti");

        Collections.addAll(kalkanlar, "id13_a_kalkan_kaderkoruyucusu", "id14_b_kalkan_yaraligardiyan", "id15_b_kalkan_dengesizdefans",
                "id16_c_kalkan_egribugru", "id17_c_kalkan_delikdesik", "id18_c_kalkan_ahsapharabe");
    }

    // Rastgele item seçme
    private void rastgeleItemSec() {
        Collections.shuffle(kiliclar);
        Collections.shuffle(asalar);
        Collections.shuffle(kalkanlar);

        // Itemları atan ve görünür hale getiren işlemler
        oyuncu1Kilic.setImageResource(getResources().getIdentifier(kiliclar.get(0), "drawable", getPackageName()));
        oyuncu1Asa.setImageResource(getResources().getIdentifier(asalar.get(0), "drawable", getPackageName()));
        oyuncu1Kalkan.setImageResource(getResources().getIdentifier(kalkanlar.get(0), "drawable", getPackageName()));
        oyuncu2Kilic.setImageResource(getResources().getIdentifier(kiliclar.get(1), "drawable", getPackageName()));
        oyuncu2Asa.setImageResource(getResources().getIdentifier(asalar.get(1), "drawable", getPackageName()));
        oyuncu2Kalkan.setImageResource(getResources().getIdentifier(kalkanlar.get(1), "drawable", getPackageName()));

        oyuncu1Kilic.setVisibility(View.VISIBLE);
        oyuncu1Asa.setVisibility(View.VISIBLE);
        oyuncu1Kalkan.setVisibility(View.VISIBLE);
        oyuncu2Kilic.setVisibility(View.VISIBLE);
        oyuncu2Asa.setVisibility(View.VISIBLE);
        oyuncu2Kalkan.setVisibility(View.VISIBLE);
        oyuncu1Hp.setVisibility(View.VISIBLE);
        oyuncu2Hp.setVisibility(View.VISIBLE);
        hpText.setVisibility(View.VISIBLE);
        itemsecButton.setVisibility(View.INVISIBLE);
    }

    // Item butonlarına tıklama olaylarını ayarla
    private void setItemClickListeners() {
        oyuncu1Kilic.setOnClickListener(v -> zarAt(1));
        oyuncu1Asa.setOnClickListener(v -> zarAt(1));
        oyuncu1Kalkan.setOnClickListener(v -> zarAt(1));

        oyuncu2Kilic.setOnClickListener(v -> zarAt(2));
        oyuncu2Asa.setOnClickListener(v -> zarAt(2));
        oyuncu2Kalkan.setOnClickListener(v -> zarAt(2));
    }

    // Zar atma fonksiyonu
    private void zarAt(int oyuncu) {
        int zarDegeri = random.nextInt(6) + 1;
        ImageView zar = oyuncu == 1 ? oyuncu1Zar : oyuncu2Zar;
        ImageButton kilic = oyuncu == 1 ? oyuncu1Kilic : oyuncu2Kilic;
        ImageButton asa = oyuncu == 1 ? oyuncu1Asa : oyuncu2Asa;
        ImageButton kalkan = oyuncu == 1 ? oyuncu1Kalkan : oyuncu2Kalkan;
        ImageView charImage = oyuncu == 1 ? oyuncu1Char : oyuncu2Char;

        zar.setVisibility(View.INVISIBLE);
        zar.setImageResource(getZarResId(zarDegeri));

        // Item butonlarını gizle ve karakter fotoğrafını değiştir
        kilic.setVisibility(View.INVISIBLE);
        asa.setVisibility(View.INVISIBLE);
        kalkan.setVisibility(View.INVISIBLE);
        charImage.setImageResource(R.drawable.char_attack);

        if (oyuncu == 1) {
            oyuncu1SecimYapti = true;
        } else {
            oyuncu2SecimYapti = true;
        }

        // Her iki oyuncu da zar atarsa işlemleri tamamla
        if (oyuncu1SecimYapti && oyuncu2SecimYapti) {
            new Handler().postDelayed(() -> {
                // Karakter fotoğraflarını geri al ve butonları göster
                oyuncu1Char.setImageResource(R.drawable.char_stand);
                oyuncu2Char.setImageResource(R.drawable.char_stand);
                oyuncu1Zar.setVisibility(View.VISIBLE);
                oyuncu2Zar.setVisibility(View.VISIBLE);
                oyuncu1Kilic.setVisibility(View.VISIBLE);
                oyuncu1Asa.setVisibility(View.VISIBLE);
                oyuncu1Kalkan.setVisibility(View.VISIBLE);
                oyuncu2Kilic.setVisibility(View.VISIBLE);
                oyuncu2Asa.setVisibility(View.VISIBLE);
                oyuncu2Kalkan.setVisibility(View.VISIBLE);

                // Seçim durumunu sıfırla
                oyuncu1SecimYapti = false;
                oyuncu2SecimYapti = false;
            }, 1000);
        }
    }

    // Zar görselini döndür
    private int getZarResId(int zarDegeri) {
        switch (zarDegeri) {
            case 1: return R.drawable.zar1;
            case 2: return R.drawable.zar3;
            case 3: return R.drawable.zar6;
            default: return R.drawable.zar1;
        }
    }

    // Sistem arayüzünü gizle
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().hide(WindowInsets.Type.systemBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
