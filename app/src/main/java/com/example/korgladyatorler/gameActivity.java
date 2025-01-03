package com.example.korgladyatorler;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class gameActivity extends AppCompatActivity {

    private void uiElemanlariTanimlama() {
        oyuncu1Kilic = findViewById(R.id.oyuncu1Kilic);
        oyuncu1Asa = findViewById(R.id.oyuncu1Asa);
        oyuncu1Kalkan = findViewById(R.id.oyuncu1Kalkan);

        oyuncu2Kilic = findViewById(R.id.oyuncu2Kilic);
        oyuncu2Asa = findViewById(R.id.oyuncu2Asa);
        oyuncu2Kalkan = findViewById(R.id.oyuncu2Kalkan);

        oyuncu1Hp=findViewById(R.id.oyuncu1Hp);
        oyuncu2Hp=findViewById(R.id.oyuncu2Hp);
        hpText=findViewById(R.id.hpText);

        itemsecButton = findViewById(R.id.itemsecButton);
    }
    private ImageButton oyuncu1Kilic, oyuncu1Asa, oyuncu1Kalkan;
    private ImageButton oyuncu2Kilic, oyuncu2Asa, oyuncu2Kalkan;
    private TextView oyuncu1Hp,oyuncu2Hp,hpText;
    private ImageButton itemsecButton;

    private ArrayList<String> kiliclar;
    private ArrayList<String> asalar;
    private ArrayList<String> kalkanlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Tam ekran moduna geçiş
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Navigasyon çubuğunu gizleme
        hideSystemUI();

        setContentView(R.layout.activity_game);

        uiElemanlariTanimlama();

        itemListeleriTanimlama();

        rastgeleItemSec();

        // Window insets ayarları
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });
    }



    // Item Listelerini Oluşturma
    private void itemListeleriTanimlama() {
        kiliclar = new ArrayList<>();
        kiliclar.add("id01_a_kilic_kritikseven");
        kiliclar.add("id02_b_kilic_dengesizkesen");
        kiliclar.add("id03_b_kilic_kahkahalicelik");
        kiliclar.add("id04_c_kilic_paslidovuscu");
        kiliclar.add("id05_c_kilic_topal");
        kiliclar.add("id06_c_kilic_garipyarmac");

        asalar = new ArrayList<>();
        asalar.add("id07_a_asa_simsekyoldasi");
        asalar.add("id08_b_asa_dengesizparlayan");
        asalar.add("id09_b_asa_dogaefsunu");
        asalar.add("id10_c_asa_catlakahsap");
        asalar.add("id11_c_asa_titrekalev");
        asalar.add("id12_c_asa_garipfisilti");

        kalkanlar = new ArrayList<>();
        kalkanlar.add("id13_a_kalkan_kaderkoruyucusu");
        kalkanlar.add("id14_b_kalkan_yaraligardiyan");
        kalkanlar.add("id15_b_kalkan_dengesizdefans");
        kalkanlar.add("id16_c_kalkan_egribugru");
        kalkanlar.add("id17_c_kalkan_delikdesik");
        kalkanlar.add("id18_c_kalkan_ahsapharabe");
    }

    // Buton Tıklama Olayını Yönetme
    private void rastgeleItemSec() {
        itemsecButton.setOnClickListener(v -> {
            // Listeleri karıştırma
            Collections.shuffle(kiliclar);
            Collections.shuffle(asalar);
            Collections.shuffle(kalkanlar);

            // Itemları rastgele atama
            String kilic1 = kiliclar.get(0);
            String asa1 = asalar.get(0);
            String kalkan1 = kalkanlar.get(0);

            String kilic2 = kiliclar.get(1);
            String asa2 = asalar.get(1);
            String kalkan2 = kalkanlar.get(1);

            // İtemlerin görünmesini sağlama
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

            // Resimleri değiştirme
            oyuncu1Kilic.setImageResource(getResources().getIdentifier(kilic1, "drawable", getPackageName()));
            oyuncu1Asa.setImageResource(getResources().getIdentifier(asa1, "drawable", getPackageName()));
            oyuncu1Kalkan.setImageResource(getResources().getIdentifier(kalkan1, "drawable", getPackageName()));

            oyuncu2Kilic.setImageResource(getResources().getIdentifier(kilic2, "drawable", getPackageName()));
            oyuncu2Asa.setImageResource(getResources().getIdentifier(asa2, "drawable", getPackageName()));
            oyuncu2Kalkan.setImageResource(getResources().getIdentifier(kalkan2, "drawable", getPackageName()));
        });
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