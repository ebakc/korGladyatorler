package com.example.korgladyatorler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import android.view.Gravity;
import android.os.Build;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

public class itemSec extends AppCompatActivity {
    private ImageView oyuncu1Zar, oyuncu2Zar;
    private ImageButton zarAtButton, devamButton, otomatikSecButton;
    private GridLayout itemGrid;
    private LinearLayout oyuncu1Items, oyuncu2Items;
    private ArrayList<String> tumItemler, secilenItemler;
    private boolean oyuncu1Sirasi = true;
    private int oyuncu1ZarDegeri, oyuncu2ZarDegeri;
    private Random random;
    private TextView uyariText;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sec);
        
        // Tam ekran modu ve sistem çubuklarını gizleme
        hideSystemUI();

        // UI elemanlarını tanımla
        uiElemanlariniTanimla();
        
        // Item listesini oluştur
        itemListesiniOlustur();
        
        // Manuel seçim butonu için listener ekle
        zarAtButton.setOnClickListener(v -> {
            // Manuel seçim ve otomatik seçim butonlarını gizle
            zarAtButton.setVisibility(View.INVISIBLE);
            otomatikSecButton.setVisibility(View.INVISIBLE);
            
            // Zar atma işlemini başlat
            zarAt();
        });

        // Otomatik seçim butonu için listener
        otomatikSecButton.setOnClickListener(v -> otomatikItemSec());
    }

    private void uiElemanlariniTanimla() {
        oyuncu1Zar = findViewById(R.id.oyuncu1Zar);
        oyuncu2Zar = findViewById(R.id.oyuncu2Zar);
        zarAtButton = findViewById(R.id.manuelSecButton);
        otomatikSecButton = findViewById(R.id.otomatikSecButton);
        devamButton = findViewById(R.id.devamButton);
        itemGrid = findViewById(R.id.itemGrid);
        oyuncu1Items = findViewById(R.id.oyuncu1Items);
        oyuncu2Items = findViewById(R.id.oyuncu2Items);
        random = new Random();
        uyariText = findViewById(R.id.uyariText);
        
        // secilenItemler listesini 6 elemanlı olarak başlat
        secilenItemler = new ArrayList<>(Collections.nCopies(6, null));

        // Manuel seçim butonunu görünür yap
        zarAtButton.setVisibility(View.VISIBLE);

        // Grid'i başlangıçta gizle
        itemGrid.setVisibility(View.INVISIBLE);
    }

    private void zarAt() {
        // Farklı zar değerleri oluştur (1-3 arası)
        do {
            oyuncu1ZarDegeri = random.nextInt(3) + 1;  // 1-3 arası
            oyuncu2ZarDegeri = random.nextInt(3) + 1;  // 1-3 arası
        } while (oyuncu1ZarDegeri == oyuncu2ZarDegeri);

        // Zarları göster
        oyuncu1Zar.setImageResource(getResources().getIdentifier("zar" + oyuncu1ZarDegeri, "drawable", getPackageName()));
        oyuncu2Zar.setImageResource(getResources().getIdentifier("zar" + oyuncu2ZarDegeri, "drawable", getPackageName()));
        
        // Her iki zarı da görünür yap
        oyuncu1Zar.setVisibility(View.VISIBLE);
        oyuncu2Zar.setVisibility(View.VISIBLE);

        // Zar atma butonunu gizle
        zarAtButton.setVisibility(View.INVISIBLE);

        // Sırayı belirle (büyük zar atan başlar)
        oyuncu1Sirasi = oyuncu1ZarDegeri > oyuncu2ZarDegeri;

        // Kimin başlayacağını göster
        String kazananOyuncu = oyuncu1Sirasi ? "Oyuncu 1" : "Oyuncu 2";
        uyariText.setText(kazananOyuncu + " başlıyor! (" + 
            (oyuncu1Sirasi ? oyuncu1ZarDegeri : oyuncu2ZarDegeri) + " > " + 
            (oyuncu1Sirasi ? oyuncu2ZarDegeri : oyuncu1ZarDegeri) + ")");
        uyariText.setVisibility(View.VISIBLE);

        // 2 saniye sonra uyarıyı gizle ve itemleri göster
        new Handler().postDelayed(() -> {
            uyariText.setVisibility(View.INVISIBLE);
            itemleriGoster(false);
        }, 2000);
    }

    private void itemleriGoster(boolean itemSecimiBitti) {
        if (itemSecimiBitti) {
            // Oyuncu 1'in itemlerini geçici bir listede sakla
            ArrayList<View> oyuncu1ItemViews = new ArrayList<>();
            for (int i = 0; i < oyuncu1Items.getChildCount(); i++) {
                oyuncu1ItemViews.add(oyuncu1Items.getChildAt(i));
            }
            oyuncu1Items.removeAllViews();

            // Oyuncu 2'nin itemlerini geçici bir listede sakla
            ArrayList<View> oyuncu2ItemViews = new ArrayList<>();
            for (int i = 0; i < oyuncu2Items.getChildCount(); i++) {
                oyuncu2ItemViews.add(oyuncu2Items.getChildAt(i));
            }
            oyuncu2Items.removeAllViews();

            // Oyuncu 1'in itemlerini yeniden düzenle
            for (View view : oyuncu1ItemViews) {
                ImageView itemView = (ImageView) view;
                String itemId = (String) itemView.getTag();
                String[] itemParcalari = itemId.split("_");
                String seviye = itemParcalari[1].toUpperCase();
                String tur = itemParcalari[2].replace("kilic", "Kılıç")
                                           .replace("asa", "Asa")
                                           .replace("kalkan", "Kalkan");
                String isim = itemParcalari[3].replace("kritikseven", "Kritik Seven")
                                            .replace("dengesizkesen", "Dengesiz Kesen")
                                            .replace("kahkahalicelik", "Kahkahalı Çelik")
                                            .replace("paslidovuscu", "Paslı Dövüşçü")
                                            .replace("topal", "Topal")
                                            .replace("garipyarmac", "Garip Yarmaç")
                                            .replace("simsekyoldasi", "Şimşek Yoldaşı")
                                            .replace("dengesizparlayan", "Dengesiz Parlayan")
                                            .replace("dogaefsunu", "Doğa Efsunu")
                                            .replace("catlakahsap", "Çatlak Ahşap")
                                            .replace("titrekalev", "Titre Kalev")
                                            .replace("garipfisilti", "Garip Fısıltı")
                                            .replace("kaderkoruyucusu", "Kader Koruyucusu")
                                            .replace("yaraligardiyan", "Yaralı Gardiyan")
                                            .replace("dengesizdefans", "Dengesiz Defans")
                                            .replace("egribugru", "Eğri Büğrü")
                                            .replace("delikdesik", "Delik Deşik")
                                            .replace("ahsapharabe", "Ahşap Harabe");

                // Yeni bir ImageView oluştur
                ImageView yeniItemView = new ImageView(this);
                yeniItemView.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
                yeniItemView.setImageDrawable(itemView.getDrawable());
                yeniItemView.setTag(itemId);

                // Oyuncu 1 için container ayarları
                LinearLayout itemContainer = new LinearLayout(this);
                itemContainer.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,  // Esnek genişlik
                    LinearLayout.LayoutParams.WRAP_CONTENT);
                itemContainer.setLayoutParams(containerParams);
                itemContainer.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                itemContainer.setPadding(0, 5, 0, 5);

                // Text ayarları
                TextView itemText = new TextView(this);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
                textParams.setMargins(20, 0, 20, 0);
                itemText.setLayoutParams(textParams);
                itemText.setText(String.format("Seviye %s\n%s %s", seviye, isim, tur));
                itemText.setTextColor(getResources().getColor(android.R.color.white));
                itemText.setTextSize(11);
                itemText.setTypeface(getResources().getFont(R.font.alagard));
                itemText.setPadding(10, 5, 10, 5);

                itemContainer.addView(yeniItemView);
                itemContainer.addView(itemText);
                oyuncu1Items.addView(itemContainer);
            }

            // Oyuncu 2 için aynı işlemleri yap
            for (View view : oyuncu2ItemViews) {
                ImageView itemView = (ImageView) view;
                String itemId = (String) itemView.getTag();
                String[] itemParcalari = itemId.split("_");
                String seviye = itemParcalari[1].toUpperCase();
                String tur = itemParcalari[2].replace("kilic", "Kılıç")
                                           .replace("asa", "Asa")
                                           .replace("kalkan", "Kalkan");
                String isim = itemParcalari[3].replace("kritikseven", "Kritik Seven")
                                            .replace("dengesizkesen", "Dengesiz Kesen")
                                            .replace("kahkahalicelik", "Kahkahalı Çelik")
                                            .replace("paslidovuscu", "Paslı Dövüşçü")
                                            .replace("topal", "Topal")
                                            .replace("garipyarmac", "Garip Yarmaç")
                                            .replace("simsekyoldasi", "Şimşek Yoldaşı")
                                            .replace("dengesizparlayan", "Dengesiz Parlayan")
                                            .replace("dogaefsunu", "Doğa Efsunu")
                                            .replace("catlakahsap", "Çatlak Ahşap")
                                            .replace("titrekalev", "Titre Kalev")
                                            .replace("garipfisilti", "Garip Fısıltı")
                                            .replace("kaderkoruyucusu", "Kader Koruyucusu")
                                            .replace("yaraligardiyan", "Yaralı Gardiyan")
                                            .replace("dengesizdefans", "Dengesiz Defans")
                                            .replace("egribugru", "Eğri Büğrü")
                                            .replace("delikdesik", "Delik Deşik")
                                            .replace("ahsapharabe", "Ahşap Harabe");

                // Yeni bir ImageView oluştur
                ImageView yeniItemView = new ImageView(this);
                yeniItemView.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
                yeniItemView.setImageDrawable(itemView.getDrawable());
                yeniItemView.setTag(itemId);

                // Oyuncu 2 için container ayarları
                LinearLayout itemContainer = new LinearLayout(this);
                itemContainer.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,  // Esnek genişlik
                    LinearLayout.LayoutParams.WRAP_CONTENT);
                containerParams.gravity = Gravity.END;
                itemContainer.setLayoutParams(containerParams);
                itemContainer.setGravity(Gravity.CENTER_VERTICAL);  // Sadece dikey ortalama
                itemContainer.setPadding(0, 5, 0, 5);

                // Text ayarları
                TextView itemText = new TextView(this);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
                textParams.setMargins(20, 0, 20, 0);
                textParams.gravity = Gravity.CENTER_VERTICAL;  // Dikey ortalama
                itemText.setLayoutParams(textParams);
                itemText.setText(String.format("Seviye %s\n%s %s", seviye, isim, tur));
                itemText.setTextColor(getResources().getColor(android.R.color.white));
                itemText.setTextSize(11);
                itemText.setTypeface(getResources().getFont(R.font.alagard));
                itemText.setPadding(10, 5, 10, 5);
                itemText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);  // Sadece metin sağa hizalı

                itemContainer.addView(itemText);
                itemContainer.addView(yeniItemView);
                oyuncu2Items.addView(itemContainer);
            }

            // Oyuncu 2'nin parent layout'unu da sağa hizala
            oyuncu2Items.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        } else {
            // Grid'i temizle
            itemGrid.removeAllViews();
            itemGrid.setVisibility(View.VISIBLE);

            ArrayList<String> secilecekItemler = new ArrayList<>();
            ArrayList<String> tempItemler = new ArrayList<>(tumItemler); // Orijinal listeyi korumak için kopya oluştur
            Collections.shuffle(tempItemler);
            
            // Her tipten en az 2'şer tane ekle
            int kilicSayisi = 0, asaSayisi = 0, kalkanSayisi = 0;
            
            // İlk geçiş: Her tipten 2'şer tane seç
            for (String item : tempItemler) {
                if (kilicSayisi < 2 && item.contains("kilic")) {
                    secilecekItemler.add(item);
                    kilicSayisi++;
                    continue;
                }
                if (asaSayisi < 2 && item.contains("asa")) {
                    secilecekItemler.add(item);
                    asaSayisi++;
                    continue;
                }
                if (kalkanSayisi < 2 && item.contains("kalkan")) {
                    secilecekItemler.add(item);
                    kalkanSayisi++;
                    continue;
                }
            }

            // Her tipten 2 tane seçildiğinden emin ol
            if (kilicSayisi < 2 || asaSayisi < 2 || kalkanSayisi < 2) {
                // Yeterli item yoksa tekrar dene
                itemleriGoster(false);
                return;
            }

            // Kalan boşlukları doldur (toplam 10 item olana kadar)
            for (String item : tempItemler) {
                if (secilecekItemler.size() >= 10) break;
                if (!secilecekItemler.contains(item)) {
                    secilecekItemler.add(item);
                }
            }

            // Son bir kez karıştır
            Collections.shuffle(secilecekItemler);

            // Itemleri grid'e ekle
            for (String item : secilecekItemler) {
                ImageButton itemButton = new ImageButton(this);
                
                // Buton boyutlarını ayarla
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 200;
                params.height = 200;
                params.setMargins(10, 10, 10, 10);
                itemButton.setLayoutParams(params);
                
                itemButton.setBackgroundResource(android.R.color.transparent);
                itemButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                itemButton.setImageResource(getResources().getIdentifier(item, "drawable", getPackageName()));
                itemButton.setTag(item);
                itemButton.setOnClickListener(v -> itemSec(itemButton));
                itemGrid.addView(itemButton);
            }
        }
    }

    private void itemSec(ImageButton secilenItem) {
        String itemId = (String) secilenItem.getTag();
        LinearLayout hedefLayout = oyuncu1Sirasi ? oyuncu1Items : oyuncu2Items;

        // Item tipini kontrol et
        String itemTipi = "";
        if (itemId.contains("kilic")) itemTipi = "kilic";
        else if (itemId.contains("asa")) itemTipi = "asa";
        else if (itemId.contains("kalkan")) itemTipi = "kalkan";

        // Oyuncunun seçtiği item tiplerini kontrol et
        boolean ayniTipVar = false;
        for (int i = 0; i < hedefLayout.getChildCount(); i++) {
            View child = hedefLayout.getChildAt(i);
            String oncekiItemId = (String) child.getTag();
            if ((itemTipi.equals("kilic") && oncekiItemId.contains("kilic")) ||
                (itemTipi.equals("asa") && oncekiItemId.contains("asa")) ||
                (itemTipi.equals("kalkan") && oncekiItemId.contains("kalkan"))) {
                ayniTipVar = true;
                break;
            }
        }

        if (ayniTipVar) {
            // Uyarı göster
            uyariText.setText("Lütfen farklı tipte bir item seçin!");
            uyariText.setVisibility(View.VISIBLE);
            
            // 2 saniye sonra uyarıyı gizle
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> {
                uyariText.setVisibility(View.INVISIBLE);
            }, 2000);
            
            return;
        }

        // Seçilen itemi grid'den kaldır
        itemGrid.removeView(secilenItem);

        // İtem görselini ekle
        ImageView itemView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
        params.setMargins(0, 5, 0, 5);
        itemView.setLayoutParams(params);
        itemView.setImageDrawable(secilenItem.getDrawable());
        itemView.setTag(itemId);

        // İtemi doğru sırayla ekle
        if (itemId.contains("kilic")) {
            hedefLayout.addView(itemView, 0);
            // Seçilen itemi listeye ekle (oyuncu sırasına göre)
            if (oyuncu1Sirasi) {
                secilenItemler.set(0, itemId); // Oyuncu 1'in kılıcı
            } else {
                secilenItemler.set(3, itemId); // Oyuncu 2'nin kılıcı
            }
        } else if (itemId.contains("asa")) {
            int asaIndex = 1;
            if (hedefLayout.getChildCount() == 0) asaIndex = 0;
            hedefLayout.addView(itemView, asaIndex);
            // Seçilen itemi listeye ekle
            if (oyuncu1Sirasi) {
                secilenItemler.set(1, itemId); // Oyuncu 1'in asası
            } else {
                secilenItemler.set(4, itemId); // Oyuncu 2'nin asası
            }
        } else if (itemId.contains("kalkan")) {
            hedefLayout.addView(itemView);
            // Seçilen itemi listeye ekle
            if (oyuncu1Sirasi) {
                secilenItemler.set(2, itemId); // Oyuncu 1'in kalkanı
            } else {
                secilenItemler.set(5, itemId); // Oyuncu 2'nin kalkanı
            }
        }

        // Sırayı değiştir
        oyuncu1Sirasi = !oyuncu1Sirasi;

        // Her oyuncunun 3 itemi seçildi mi kontrol et
        if (oyuncu1Items.getChildCount() == 3 && oyuncu2Items.getChildCount() == 3) {
            // İtem isimlerini göster
            itemleriGoster(true);
            
            // Devam butonunu göster
            devamButton.setVisibility(View.VISIBLE);
            devamButton.setOnClickListener(v -> oyunaGec());
            
            // Kalan itemleri gizle
            itemGrid.setVisibility(View.INVISIBLE);
        }
    }

    private void oyunaGec() {
        Intent intent = new Intent(itemSec.this, gameActivity.class);
        
        // Seçilen itemleri kontrol et ve boş olmayanları gönder
        ArrayList<String> gecerliItemler = new ArrayList<>();
        for (String item : secilenItemler) {
            if (item != null) {
                gecerliItemler.add(item);
            }
        }
        
        // Seçilen itemleri sıralı bir şekilde gönder
        intent.putExtra("oyuncu1Kilic", secilenItemler.get(0));  // Oyuncu 1'in kılıcı
        intent.putExtra("oyuncu1Asa", secilenItemler.get(1));    // Oyuncu 1'in asası
        intent.putExtra("oyuncu1Kalkan", secilenItemler.get(2)); // Oyuncu 1'in kalkanı
        intent.putExtra("oyuncu2Kilic", secilenItemler.get(3));  // Oyuncu 2'nin kılıcı
        intent.putExtra("oyuncu2Asa", secilenItemler.get(4));    // Oyuncu 2'nin asası
        intent.putExtra("oyuncu2Kalkan", secilenItemler.get(5)); // Oyuncu 2'nin kalkanı
        
        startActivity(intent);
        finish();
    }

    private void itemListesiniOlustur() {
        tumItemler = new ArrayList<>();
        // Kılıçlar
        tumItemler.add("id01_a_kilic_kritikseven");
        tumItemler.add("id02_b_kilic_dengesizkesen");
        tumItemler.add("id03_b_kilic_kahkahalicelik");
        tumItemler.add("id04_c_kilic_paslidovuscu");
        tumItemler.add("id05_c_kilic_topal");
        tumItemler.add("id06_c_kilic_garipyarmac");
        // Asalar
        tumItemler.add("id07_a_asa_simsekyoldasi");
        tumItemler.add("id08_b_asa_dengesizparlayan");
        tumItemler.add("id09_b_asa_dogaefsunu");
        tumItemler.add("id10_c_asa_catlakahsap");
        tumItemler.add("id11_c_asa_titrekalev");
        tumItemler.add("id12_c_asa_garipfisilti");
        // Kalkanlar
        tumItemler.add("id13_a_kalkan_kaderkoruyucusu");
        tumItemler.add("id14_b_kalkan_yaraligardiyan");
        tumItemler.add("id15_b_kalkan_dengesizdefans");
        tumItemler.add("id16_c_kalkan_egribugru");
        tumItemler.add("id17_c_kalkan_delikdesik");
        tumItemler.add("id18_c_kalkan_ahsapharabe");
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

    private void otomatikItemSec() {
        // Tüm seçim butonlarını gizle
        zarAtButton.setVisibility(View.INVISIBLE);
        otomatikSecButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.manuelSecButton).setVisibility(View.INVISIBLE); // Manuel seçim butonunu gizle

        // Item grid'ini gizle
        itemGrid.setVisibility(View.INVISIBLE);

        // Tüm itemleri içeren listeleri oluştur
        ArrayList<String> kiliclar = new ArrayList<>();
        ArrayList<String> asalar = new ArrayList<>();
        ArrayList<String> kalkanlar = new ArrayList<>();

        // Itemleri kategorilerine göre ayır
        for (String item : tumItemler) {
            if (item.contains("kilic")) {
                kiliclar.add(item);
            } else if (item.contains("asa")) {
                asalar.add(item);
            } else if (item.contains("kalkan")) {
                kalkanlar.add(item);
            }
        }

        // Oyuncu 1 için rastgele seçim yap
        String oyuncu1Kilic = secVeKaldir(kiliclar);
        String oyuncu1Asa = secVeKaldir(asalar);
        String oyuncu1Kalkan = secVeKaldir(kalkanlar);

        // Oyuncu 2 için rastgele seçim yap
        String oyuncu2Kilic = secVeKaldir(kiliclar);
        String oyuncu2Asa = secVeKaldir(asalar);
        String oyuncu2Kalkan = secVeKaldir(kalkanlar);

        // Seçilen itemleri oyuncu layoutlarına ekle
        ekleItemOyuncuya(oyuncu1Items, oyuncu1Kilic, true);
        ekleItemOyuncuya(oyuncu1Items, oyuncu1Asa, true);
        ekleItemOyuncuya(oyuncu1Items, oyuncu1Kalkan, true);
        
        ekleItemOyuncuya(oyuncu2Items, oyuncu2Kilic, false);
        ekleItemOyuncuya(oyuncu2Items, oyuncu2Asa, false);
        ekleItemOyuncuya(oyuncu2Items, oyuncu2Kalkan, false);

        // Seçilen itemları kaydet
        secilenItemler.set(0, oyuncu1Kilic);
        secilenItemler.set(1, oyuncu1Asa);
        secilenItemler.set(2, oyuncu1Kalkan);
        secilenItemler.set(3, oyuncu2Kilic);
        secilenItemler.set(4, oyuncu2Asa);
        secilenItemler.set(5, oyuncu2Kalkan);

        // İtem isimlerini göster
        itemleriGoster(true);

        // Devam butonunu göster
        devamButton.setVisibility(View.VISIBLE);
        devamButton.setOnClickListener(v -> oyunaGec());
    }

    // Yeni yardımcı metod
    private void ekleItemOyuncuya(LinearLayout hedefLayout, String itemId, boolean isOyuncu1) {
        ImageView itemView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
        params.setMargins(0, 5, 0, 5);
        itemView.setLayoutParams(params);
        itemView.setImageResource(getResources().getIdentifier(itemId, "drawable", getPackageName()));
        itemView.setTag(itemId);

        // İtemi doğru sırayla ekle
        if (itemId.contains("kilic")) {
            hedefLayout.addView(itemView, 0);
        } else if (itemId.contains("asa")) {
            int asaIndex = hedefLayout.getChildCount() == 0 ? 0 : 1;
            hedefLayout.addView(itemView, asaIndex);
        } else {
            hedefLayout.addView(itemView);
        }
    }

    private String secVeKaldir(ArrayList<String> liste) {
        if (liste.isEmpty()) return null;
        int index = random.nextInt(liste.size());
        return liste.remove(index);
    }

    private void gosterSecilenItem(ImageView imageView, TextView textView, String itemId) {
        // Görseli ayarla
        imageView.setImageResource(getResources().getIdentifier(itemId, "drawable", getPackageName()));
        imageView.setVisibility(View.VISIBLE);

        // İsmi ayarla
        String itemAdi = itemId.split("_")[3]; // id01_a_kilic_kritikseven formatından ismi al
        textView.setText(itemAdi);
        textView.setVisibility(View.VISIBLE);
    }
}