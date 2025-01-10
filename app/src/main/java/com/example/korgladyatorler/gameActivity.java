package com.example.korgladyatorler;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.os.Build;

public class gameActivity extends AppCompatActivity {
    // Sabitler
    private static final int BASLANGIC_CAN = 100;
    private static final int ANIMASYON_SURESI = 1000;

    // UI Elemanları
    private ImageButton oyuncu1Kilic, oyuncu1Asa, oyuncu1Kalkan;
    private ImageButton oyuncu2Kilic, oyuncu2Asa, oyuncu2Kalkan;
    private TextView oyuncu1Hp, oyuncu2Hp;
    private ImageButton baslaButton, yenidenButton;
    private ImageView oyuncu1Zar, oyuncu2Zar, oyuncu1Char, oyuncu2Char;

    // Oyun Değişkenleri
    private double oyuncu1Can = BASLANGIC_CAN;
    private double oyuncu2Can = BASLANGIC_CAN;
    private int oyuncu1Hasar = 0;
    private int oyuncu2Hasar = 0;
    private double oyuncu1CanYenileme = 0;
    private double oyuncu2CanYenileme = 0;
    private int oyuncu1Koruma = 0;
    private int oyuncu2Koruma = 0;
    private int oyuncu1ZarDegeri = 0;
    private int oyuncu2ZarDegeri = 0;
    private boolean oyuncu1SecimYapti = false;
    private boolean oyuncu2SecimYapti = false;
    private Random random;
    private ArrayList<String> itemListesi;
    private int oyuncu1EnYuksekHasar = 0;
    private int oyuncu2EnYuksekHasar = 0;
    private int oyuncu1ToplamHasar = 0;
    private int oyuncu2ToplamHasar = 0;
    private int turSayisi = 0;
    private long oyunBaslangicZamani;

    // İstatistik değişkenleri
    private int oyuncu1KilicKullanim = 0;
    private int oyuncu1AsaKullanim = 0;
    private int oyuncu1KalkanKullanim = 0;
    private int oyuncu2KilicKullanim = 0;
    private int oyuncu2AsaKullanim = 0;
    private int oyuncu2KalkanKullanim = 0;

    // Yeni istatistik değişkenleri ekleyelim
    private double oyuncu1ToplamCanYenileme = 0;
    private double oyuncu2ToplamCanYenileme = 0;
    private double oyuncu1EnYuksekCanYenileme = 0;
    private double oyuncu2EnYuksekCanYenileme = 0;
    private int oyuncu1EnYuksekKoruma = 0;
    private int oyuncu2EnYuksekKoruma = 0;
    private int oyuncu1ToplamKoruma = 0;
    private int oyuncu2ToplamKoruma = 0;

    // Sınıf değişkenlerinde bunları tut:
    private boolean oyuncu1KilicKullanildi = false;
    private boolean oyuncu1AsaKullanildi = false;
    private boolean oyuncu1KalkanKullanildi = false;
    private boolean oyuncu2KilicKullanildi = false;
    private boolean oyuncu2AsaKullanildi = false;
    private boolean oyuncu2KalkanKullanildi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Tam ekran modu ve sistem çubuklarını gizleme
        hideSystemUI();

        // UI elemanlarını tanımla
        uiElemanlariniTanimla();

        // Random nesnesini oluştur
        random = new Random();

        // Item listesini oluştur
        itemListesiniOlustur();

        // Butonları ayarla
        butonlariAyarla();

        // Başlangıç durumunu ayarla
        oyuncu1Can = BASLANGIC_CAN;
        oyuncu2Can = BASLANGIC_CAN;
        oyuncu1Hp.setText("HP: " + oyuncu1Can);
        oyuncu2Hp.setText("HP: " + oyuncu2Can);
        oyunBaslangicZamani = System.currentTimeMillis();

        // Itemlerin başlangıçta görünmez olmasını sağla
        findViewById(R.id.oyuncu1Asa).setVisibility(View.GONE);
        findViewById(R.id.oyuncu1Kalkan).setVisibility(View.GONE);
        findViewById(R.id.oyuncu2Asa).setVisibility(View.GONE);
        findViewById(R.id.oyuncu2Kalkan).setVisibility(View.GONE);
    }

    private void uiElemanlariniTanimla() {
        // Oyuncu 1 itemleri
        oyuncu1Kilic = findViewById(R.id.oyuncu1Kilic);
        oyuncu1Asa = findViewById(R.id.oyuncu1Asa);
        oyuncu1Kalkan = findViewById(R.id.oyuncu1Kalkan);

        // Oyuncu 2 itemleri
        oyuncu2Kilic = findViewById(R.id.oyuncu2Kilic);
        oyuncu2Asa = findViewById(R.id.oyuncu2Asa);
        oyuncu2Kalkan = findViewById(R.id.oyuncu2Kalkan);

        // Diğer UI elemanları
        oyuncu1Hp = findViewById(R.id.oyuncu1Hp);
        oyuncu2Hp = findViewById(R.id.oyuncu2Hp);
        baslaButton = findViewById(R.id.baslaButton);
        yenidenButton = findViewById(R.id.yenidenButton);
        oyuncu1Zar = findViewById(R.id.oyuncu1Zar);
        oyuncu2Zar = findViewById(R.id.oyuncu2Zar);
        oyuncu1Char = findViewById(R.id.oyuncu1Char);
        oyuncu2Char = findViewById(R.id.oyuncu2Char);

        // Başlangıçta bazı elemanları gizle
        yenidenButton.setVisibility(View.INVISIBLE);

        // Başlama butonu için listener zaten uiElemanlariniTanimla'da ayarlandı
        baslaButton.setOnClickListener(v -> oyunuBaslat());
    }

    private void butonlariAyarla() {
        // Başlama butonu için listener zaten uiElemanlariniTanimla'da ayarlandı

        // Oyuncu 1 butonları
        oyuncu1Kilic.setOnClickListener(v -> itemKullan(1, "kilic"));
        oyuncu1Asa.setOnClickListener(v -> itemKullan(1, "asa"));
        oyuncu1Kalkan.setOnClickListener(v -> itemKullan(1, "kalkan"));

        // Oyuncu 2 butonları
        oyuncu2Kilic.setOnClickListener(v -> itemKullan(2, "kilic"));
        oyuncu2Asa.setOnClickListener(v -> itemKullan(2, "asa"));
        oyuncu2Kalkan.setOnClickListener(v -> itemKullan(2, "kalkan"));

        // Yeniden başlatma butonu
        yenidenButton.setOnClickListener(v -> {
            yenidenOyna();
        });
    }

    private void itemKullan(int oyuncu, String itemTipi) {
        // İtemin kullanılıp kullanılamayacağını kontrol et
        if (!itemKullanilabilirMi(oyuncu, itemTipi)) {
            return;
        }

        int zarDegeri = random.nextInt(3) + 1;
        Log.d("Item", "Oyuncu " + oyuncu + " " + itemTipi + " kullandı. Zar: " + zarDegeri);
        
        // Zar değerini kaydet ve zarı gizle
        if(oyuncu == 1) {
            oyuncu1ZarDegeri = zarDegeri;
            oyuncu1SecimYapti = true;
            oyuncu1Zar.setVisibility(View.INVISIBLE);
        } else {
            oyuncu2ZarDegeri = zarDegeri;
            oyuncu2SecimYapti = true;
            oyuncu2Zar.setVisibility(View.INVISIBLE);
        }

        // Item kullanımını işaretle
        ImageButton secilenItem;
        switch(itemTipi) {
            case "kilic":
                if(oyuncu == 1) {
                    oyuncu1KilicKullanildi = true;
                    secilenItem = oyuncu1Kilic;
                } else {
                    oyuncu2KilicKullanildi = true;
                    secilenItem = oyuncu2Kilic;
                }
                break;
            case "asa":
                if(oyuncu == 1) {
                    oyuncu1AsaKullanildi = true;
                    secilenItem = oyuncu1Asa;
                } else {
                    oyuncu2AsaKullanildi = true;
                    secilenItem = oyuncu2Asa;
                }
                break;
            case "kalkan":
                if(oyuncu == 1) {
                    oyuncu1KalkanKullanildi = true;
                    secilenItem = oyuncu1Kalkan;
                } else {
                    oyuncu2KalkanKullanildi = true;
                    secilenItem = oyuncu2Kalkan;
                }
                break;
            default:
                return;
        }

        // Hasar hesapla ve uygula
        hesaplaVeUygula(oyuncu, itemTipi, (String) secilenItem.getTag(), zarDegeri);

        // Karakter saldırı animasyonu
        ImageView charGorseli = oyuncu == 1 ? oyuncu1Char : oyuncu2Char;
        charGorseli.setImageResource(R.drawable.char_attack);

        // Her iki oyuncu da seçim yaptıysa sonuçları uygula
        if(oyuncu1SecimYapti && oyuncu2SecimYapti) {
            // Kullanılan itemleri hemen gizle
            gizleKullanilanItemler();
            
            new Handler().postDelayed(() -> {
                turSonuIslemleri();
            }, ANIMASYON_SURESI);
        }
    }

    private void gizleKullanilanItemler() {
        if(oyuncu1KilicKullanildi) oyuncu1Kilic.setVisibility(View.INVISIBLE);
        if(oyuncu1AsaKullanildi) oyuncu1Asa.setVisibility(View.INVISIBLE);
        if(oyuncu1KalkanKullanildi) oyuncu1Kalkan.setVisibility(View.INVISIBLE);
        if(oyuncu2KilicKullanildi) oyuncu2Kilic.setVisibility(View.INVISIBLE);
        if(oyuncu2AsaKullanildi) oyuncu2Asa.setVisibility(View.INVISIBLE);
        if(oyuncu2KalkanKullanildi) oyuncu2Kalkan.setVisibility(View.INVISIBLE);
    }

    private void hesaplaVeUygula(int oyuncu, String itemTipi, String itemId, int zarDegeri) {
        String[] parcalar = itemId.split("_");
        char seviye = parcalar[1].charAt(0);

        if (oyuncu == 1) {
            switch (itemTipi) {
                case "kilic":
                    oyuncu1Hasar = hesaplaKilicHasari(seviye, zarDegeri);
                    oyuncu1EnYuksekHasar = Math.max(oyuncu1EnYuksekHasar, oyuncu1Hasar);
                    oyuncu1ToplamHasar += oyuncu1Hasar;
                    oyuncu1KilicKullanim++;
                    break;
                case "asa":
                    double[] asaSonuc = hesaplaAsaHasari(seviye, zarDegeri);
                    oyuncu1Hasar = (int) asaSonuc[0];
                    oyuncu1CanYenileme = asaSonuc[1];
                    oyuncu1EnYuksekHasar = Math.max(oyuncu1EnYuksekHasar, oyuncu1Hasar);
                    oyuncu1ToplamHasar += oyuncu1Hasar;
                    oyuncu1EnYuksekCanYenileme = Math.max(oyuncu1EnYuksekCanYenileme, oyuncu1CanYenileme);
                    oyuncu1ToplamCanYenileme += oyuncu1CanYenileme;
                    oyuncu1AsaKullanim++;
                    break;
                case "kalkan":
                    oyuncu1Hasar = 0;
                    if (seviye == 'a' && zarDegeri == 3) {
                        oyuncu1Koruma = Integer.MAX_VALUE;
                        oyuncu1CanYenileme = 10.0;
                    } else {
                        oyuncu1Koruma = hesaplaKalkanKorumasi(seviye, zarDegeri);
                        oyuncu1CanYenileme = 0;
                    }
                    oyuncu1EnYuksekKoruma = Math.max(oyuncu1EnYuksekKoruma, oyuncu1Koruma);
                    oyuncu1ToplamKoruma += oyuncu1Koruma;
                    oyuncu1KalkanKullanim++;
                    break;
            }
        } else {
            switch (itemTipi) {
                case "kilic":
                    oyuncu2Hasar = hesaplaKilicHasari(seviye, zarDegeri);
                    oyuncu2EnYuksekHasar = Math.max(oyuncu2EnYuksekHasar, oyuncu2Hasar);
                    oyuncu2ToplamHasar += oyuncu2Hasar;
                    oyuncu2KilicKullanim++;
                    break;
                case "asa":
                    double[] asaSonuc = hesaplaAsaHasari(seviye, zarDegeri);
                    oyuncu2Hasar = (int) asaSonuc[0];
                    oyuncu2CanYenileme = asaSonuc[1];
                    oyuncu2EnYuksekHasar = Math.max(oyuncu2EnYuksekHasar, oyuncu2Hasar);
                    oyuncu2ToplamHasar += oyuncu2Hasar;
                    oyuncu2EnYuksekCanYenileme = Math.max(oyuncu2EnYuksekCanYenileme, oyuncu2CanYenileme);
                    oyuncu2ToplamCanYenileme += oyuncu2CanYenileme;
                    oyuncu2AsaKullanim++;
                    break;
                case "kalkan":
                    oyuncu2Hasar = 0;
                    if (seviye == 'a' && zarDegeri == 3) {
                        oyuncu2Koruma = Integer.MAX_VALUE;
                        oyuncu2CanYenileme = 10.0;
                    } else {
                        oyuncu2Koruma = hesaplaKalkanKorumasi(seviye, zarDegeri);
                        oyuncu2CanYenileme = 0;
                    }
                    oyuncu2EnYuksekKoruma = Math.max(oyuncu2EnYuksekKoruma, oyuncu2Koruma);
                    oyuncu2ToplamKoruma += oyuncu2Koruma;
                    oyuncu2KalkanKullanim++;
                    break;
            }
        }

        Log.d("Hasar", "Oyuncu " + oyuncu + " Hasar: " + (oyuncu == 1 ? oyuncu1Hasar : oyuncu2Hasar));
        Log.d("Can", "Oyuncu " + oyuncu + " Can: " + (oyuncu == 1 ? oyuncu1Can : oyuncu2Can));
    }

    private int hesaplaKilicHasari(char seviye, int zarDegeri) {
        Log.d("KilicHasar", "Seviye: " + seviye + ", Zar: " + zarDegeri);

        // Seviye kontrolü için küçük harfe çevir
        seviye = Character.toLowerCase(seviye);

        if (seviye == 'a') {
            switch (zarDegeri) {
                case 1:
                    return 27;
                case 2:
                    return 30;
                case 3:
                    return 35;
            }
        } else if (seviye == 'b') {
            switch (zarDegeri) {
                case 1:
                    return 13;
                case 2:
                    return 18;
                case 3:
                    return 23;
            }
        } else if (seviye == 'c') {
            switch (zarDegeri) {
                case 1:
                    return 5;
                case 2:
                    return 10;
                case 3:
                    return 15;
            }
        }

        Log.e("KilicHasar", "Geçersiz seviye veya zar değeri! Seviye: " + seviye + ", Zar: " + zarDegeri);
        return 0;
    }

    private double[] hesaplaAsaHasari(char seviye, int zarDegeri) {
        double[] sonuc = new double[2]; // [0] hasar, [1] can yenileme
        
        switch (seviye) {
            case 'c':
                switch (zarDegeri) {
                    case 1:
                        sonuc[0] = 3;
                        sonuc[1] = 1.0;
                        break;
                    case 2:
                        sonuc[0] = 8;
                        sonuc[1] = 2.6;
                        break;
                    case 3:
                        sonuc[0] = 12;
                        sonuc[1] = 4.8;
                        break;
                }
                break;
                
            case 'b':
                switch (zarDegeri) {
                    case 1:
                        sonuc[0] = 10;
                        sonuc[1] = 3.3;
                        break;
                    case 2:
                        sonuc[0] = 14;
                        sonuc[1] = 5.0;
                        break;
                    case 3:
                        sonuc[0] = 18;
                        sonuc[1] = 7.0;
                        break;
                }
                break;
                
            case 'a':
                switch (zarDegeri) {
                    case 1:
                        sonuc[0] = 21;
                        sonuc[1] = 8.5;
                        break;
                    case 2:
                        sonuc[0] = 23;
                        sonuc[1] = 10.0;
                        break;
                    case 3:
                        sonuc[0] = 25;
                        sonuc[1] = 20.0;
                        break;
                }
                break;
        }
        
        Log.d("AsaHasar", String.format("Seviye: %c, Zar: %d, Hasar: %.1f, Can Yenileme: %.1f", 
              seviye, zarDegeri, sonuc[0], sonuc[1]));
        
        return sonuc;
    }

    private int hesaplaKalkanKorumasi(char seviye, int zarDegeri) {
        // Seviye kontrolü için küçük harfe çevir
        seviye = Character.toLowerCase(seviye);

        if (seviye == 'a') {
            switch (zarDegeri) {
                case 1:
                    return 30;
                case 2:
                    return 35;
                case 3:
                    return Integer.MAX_VALUE;
            }
        } else if (seviye == 'b') {
            switch (zarDegeri) {
                case 1:
                    return 13;
                case 2:
                    return 18;
                case 3:
                    return 23;
            }
        } else if (seviye == 'c') {
            switch (zarDegeri) {
                case 1:
                    return 5;
                case 2:
                    return 10;
                case 3:
                    return 15;
            }
        }
        return 0;
    }

    private void turSonuIslemleri() {
        // Zarları göster ve değerlerini ayarla
        oyuncu1Zar.setImageResource(getResources().getIdentifier("zar" + oyuncu1ZarDegeri, "drawable", getPackageName()));
        oyuncu2Zar.setImageResource(getResources().getIdentifier("zar" + oyuncu2ZarDegeri, "drawable", getPackageName()));
        oyuncu1Zar.setVisibility(View.VISIBLE);
        oyuncu2Zar.setVisibility(View.VISIBLE);

        // Koruma ve hasar değerlerini logla
        Log.d("TurSonu", String.format("Başlangıç - Oyuncu1: %.1f, Oyuncu2: %.1f", oyuncu1Can, oyuncu2Can));
        Log.d("TurSonu", String.format("Hasar - Oyuncu1: %d, Oyuncu2: %d", oyuncu1Hasar, oyuncu2Hasar));
        Log.d("TurSonu", String.format("Koruma - Oyuncu1: %d, Oyuncu2: %d", oyuncu1Koruma, oyuncu2Koruma));
        Log.d("TurSonu", String.format("Can Yenileme - Oyuncu1: %.1f, Oyuncu2: %.1f", oyuncu1CanYenileme, oyuncu2CanYenileme));

        // Önce hasarları uygula (koruma değerlerini dikkate alarak)
        if (oyuncu1Hasar > 0 && oyuncu2Koruma != Integer.MAX_VALUE) {
            int gercekHasar = Math.max(0, oyuncu1Hasar - oyuncu2Koruma);
            double hasarOncesiCan = oyuncu2Can;
            oyuncu2Can = Math.max(0, oyuncu2Can - gercekHasar);
            Log.d("TurSonu", String.format("Oyuncu 2'ye uygulanan hasar: %d (%.1f -> %.1f)", 
                gercekHasar, hasarOncesiCan, oyuncu2Can));
        }
        if (oyuncu2Hasar > 0 && oyuncu1Koruma != Integer.MAX_VALUE) {
            int gercekHasar = Math.max(0, oyuncu2Hasar - oyuncu1Koruma);
            double hasarOncesiCan = oyuncu1Can;
            oyuncu1Can = Math.max(0, oyuncu1Can - gercekHasar);
            Log.d("TurSonu", String.format("Oyuncu 1'e uygulanan hasar: %d (%.1f -> %.1f)", 
                gercekHasar, hasarOncesiCan, oyuncu1Can));
        }

        // Sonra can yenileme işlemlerini uygula
        if (oyuncu1CanYenileme > 0) {
            double yenilemeOncesiCan = oyuncu1Can;
            oyuncu1Can = Math.min(BASLANGIC_CAN, oyuncu1Can + oyuncu1CanYenileme);
            Log.d("TurSonu", String.format("Oyuncu 1 can yeniledi: %.1f -> %.1f (%.1f)", 
                yenilemeOncesiCan, oyuncu1Can, oyuncu1CanYenileme));
        }
        if (oyuncu2CanYenileme > 0) {
            double yenilemeOncesiCan = oyuncu2Can;
            oyuncu2Can = Math.min(BASLANGIC_CAN, oyuncu2Can + oyuncu2CanYenileme);
            Log.d("TurSonu", String.format("Oyuncu 2 can yeniledi: %.1f -> %.1f (%.1f)", 
                yenilemeOncesiCan, oyuncu2Can, oyuncu2CanYenileme));
        }

        // Can değerlerini güncelle
        oyuncu1Hp.setText(String.format("HP: %.1f", Math.max(0, oyuncu1Can)));
        oyuncu2Hp.setText(String.format("HP: %.1f", Math.max(0, oyuncu2Can)));

        // Oyun bitti mi kontrol et
        if (oyuncu1Can <= 0 || oyuncu2Can <= 0) {
            oyunBitisKontrolu();
            return;
        }

        // Karakterleri normal duruşa getir
        oyuncu1Char.setImageResource(R.drawable.char_stand);
        oyuncu2Char.setImageResource(R.drawable.char_stand);

        // Yeni tur için hazırlık
        oyuncu1SecimYapti = false;
        oyuncu2SecimYapti = false;

        // Önceki turda kullanılan itemleri serbest bırak
        if (turSayisi > 0) {  // İlk turda serbest bırakma yapma
            // Sadece bu turda kullanılmayan itemleri serbest bırak
            if (!oyuncu1KilicKullanildi) {
                oyuncu1Kilic.setVisibility(View.VISIBLE);
            }
            if (!oyuncu1AsaKullanildi) {
                oyuncu1Asa.setVisibility(View.VISIBLE);
            }
            if (!oyuncu1KalkanKullanildi) {
                oyuncu1Kalkan.setVisibility(View.VISIBLE);
            }
            if (!oyuncu2KilicKullanildi) {
                oyuncu2Kilic.setVisibility(View.VISIBLE);
            }
            if (!oyuncu2AsaKullanildi) {
                oyuncu2Asa.setVisibility(View.VISIBLE);
            }
            if (!oyuncu2KalkanKullanildi) {
                oyuncu2Kalkan.setVisibility(View.VISIBLE);
            }
        }

        // Kullanım durumlarını sıfırla
        oyuncu1KilicKullanildi = false;
        oyuncu1AsaKullanildi = false;
        oyuncu1KalkanKullanildi = false;
        oyuncu2KilicKullanildi = false;
        oyuncu2AsaKullanildi = false;
        oyuncu2KalkanKullanildi = false;

        // Tur sayısını artır
        turSayisi++;

        // Değerleri sıfırla
        oyuncu1Hasar = 0;
        oyuncu2Hasar = 0;
        oyuncu1CanYenileme = 0;
        oyuncu2CanYenileme = 0;
        oyuncu1Koruma = 0;
        oyuncu2Koruma = 0;
    }

    private void gosterItemButonlari() {
        gosterKullanilabilirItemler();
    }

    private void itemListesiniOlustur() {
        itemListesi = new ArrayList<>();
        // Kılıçlar
        itemListesi.add("id01_a_kilic_kritikseven");
        itemListesi.add("id02_b_kilic_dengesizkesen");
        itemListesi.add("id03_b_kilic_kahkahalicelik");
        itemListesi.add("id04_c_kilic_paslidovuscu");
        itemListesi.add("id05_c_kilic_topal");
        itemListesi.add("id06_c_kilic_garipyarmac");
        // Asalar
        itemListesi.add("id07_a_asa_simsekyoldasi");
        itemListesi.add("id08_b_asa_dengesizparlayan");
        itemListesi.add("id09_b_asa_dogaefsunu");
        itemListesi.add("id10_c_asa_catlakahsap");
        itemListesi.add("id11_c_asa_titrekalev");
        itemListesi.add("id12_c_asa_garipfisilti");
        // Kalkanlar
        itemListesi.add("id13_a_kalkan_kaderkoruyucusu");
        itemListesi.add("id14_b_kalkan_yaraligardiyan");
        itemListesi.add("id15_b_kalkan_dengesizdefans");
        itemListesi.add("id16_c_kalkan_egribugru");
        itemListesi.add("id17_c_kalkan_delikdesik");
        itemListesi.add("id18_c_kalkan_ahsapharabe");
    }

    private void itemleriDagit() {
        // Intent'ten itemleri al
        Intent intent = getIntent();

        // Oyuncu 1'in itemlerini ayarla
        String oyuncu1KilicId = intent.getStringExtra("oyuncu1Kilic");
        String oyuncu1AsaId = intent.getStringExtra("oyuncu1Asa");
        String oyuncu1KalkanId = intent.getStringExtra("oyuncu1Kalkan");

        // Oyuncu 2'nin itemlerini ayarla
        String oyuncu2KilicId = intent.getStringExtra("oyuncu2Kilic");
        String oyuncu2AsaId = intent.getStringExtra("oyuncu2Asa");
        String oyuncu2KalkanId = intent.getStringExtra("oyuncu2Kalkan");

        // Itemleri butonlara ata ve görselleri ayarla
        oyuncu1Kilic.setTag(oyuncu1KilicId);
        oyuncu1Asa.setTag(oyuncu1AsaId);
        oyuncu1Kalkan.setTag(oyuncu1KalkanId);

        oyuncu2Kilic.setTag(oyuncu2KilicId);
        oyuncu2Asa.setTag(oyuncu2AsaId);
        oyuncu2Kalkan.setTag(oyuncu2KalkanId);

        // Görselleri ayarla
        ayarlaItemGorseli(oyuncu1Kilic, oyuncu1KilicId);
        ayarlaItemGorseli(oyuncu1Asa, oyuncu1AsaId);
        ayarlaItemGorseli(oyuncu1Kalkan, oyuncu1KalkanId);
        ayarlaItemGorseli(oyuncu2Kilic, oyuncu2KilicId);
        ayarlaItemGorseli(oyuncu2Asa, oyuncu2AsaId);
        ayarlaItemGorseli(oyuncu2Kalkan, oyuncu2KalkanId);

        // UI'ı güncelle
        baslaButton.setVisibility(View.INVISIBLE);
        gosterItemButonlari();
        oyuncu1Hp.setVisibility(View.VISIBLE);
        oyuncu2Hp.setVisibility(View.VISIBLE);
    }

    private void ayarlaItemGorseli(ImageButton button, String itemId) {
        button.setImageResource(getResources().getIdentifier(itemId, "drawable", getPackageName()));
    }

    private void oyunBitisKontrolu() {
        if (oyuncu1Can <= 0 || oyuncu2Can <= 0) {
            // Tüm itemleri görünür yap
            oyuncu1Kilic.setVisibility(View.VISIBLE);
            oyuncu1Asa.setVisibility(View.VISIBLE);
            oyuncu1Kalkan.setVisibility(View.VISIBLE);
            oyuncu2Kilic.setVisibility(View.VISIBLE);
            oyuncu2Asa.setVisibility(View.VISIBLE);
            oyuncu2Kalkan.setVisibility(View.VISIBLE);

            // Zarları gizle
            oyuncu1Zar.setVisibility(View.INVISIBLE);
            oyuncu2Zar.setVisibility(View.INVISIBLE);

            // HP textlerini gizle
            oyuncu1Hp.setVisibility(View.INVISIBLE);
            oyuncu2Hp.setVisibility(View.INVISIBLE);

            // Karakterleri güncelle
            if (oyuncu1Can <= 0) {
                // Oyuncu 1 kaybetti
                oyuncu1Char.setVisibility(View.INVISIBLE);
                oyuncu2Char.setImageResource(R.drawable.char_stand);
            } else {
                // Oyuncu 2 kaybetti
                oyuncu2Char.setVisibility(View.INVISIBLE);
                oyuncu1Char.setImageResource(R.drawable.char_stand);
            }

            // Yeniden oyna butonunu göster
            yenidenButton.setVisibility(View.VISIBLE);

            // İstatistikleri göster
            gosterOyunIstatistikleri();
        }
    }

    private void gosterOyunIstatistikleri() {
        long oyunSuresi = (System.currentTimeMillis() - oyunBaslangicZamani) / 1000;

        String kazananBilgisi = oyuncu1Can <= 0 ?
                String.format("OYUNCU 2 (HP: %.1f)", oyuncu2Can) :
                String.format("OYUNCU 1 (HP: %.1f)", oyuncu1Can);

        String istatistikler = String.format(
                "🏆 %s 🏆\n" +
                        "⏱️ %ds | 🎮 %d Tur\n\n" +
                        "👤 OYUNCU 1\n" +
                        "⚔️ %d | 🔮 %d | 🛡 %d\n" +
                        "💥 Max: %d | ⚔️ Top: %d\n" +
                        "💚 Max: %.1f | ❤️ Top: %.1f\n" +
                        "🛡 Max: %d | 🔰 Top: %d\n\n" +
                        "👤 OYUNCU 2\n" +
                        "⚔️ %d | 🔮 %d | 🛡 %d\n" +
                        "💥 Max: %d | ⚔️ Top: %d\n" +
                        "💚 Max: %.1f | ❤️ Top: %.1f\n" +
                        "🛡 Max: %d | 🔰 Top: %d",
                kazananBilgisi,
                oyunSuresi,
                turSayisi + 1,
                oyuncu1KilicKullanim, oyuncu1AsaKullanim, oyuncu1KalkanKullanim,
                oyuncu1EnYuksekHasar, oyuncu1ToplamHasar,
                oyuncu1EnYuksekCanYenileme, oyuncu1ToplamCanYenileme,
                oyuncu1EnYuksekKoruma, oyuncu1ToplamKoruma,
                oyuncu2KilicKullanim, oyuncu2AsaKullanim, oyuncu2KalkanKullanim,
                oyuncu2EnYuksekHasar, oyuncu2ToplamHasar,
                oyuncu2EnYuksekCanYenileme, oyuncu2ToplamCanYenileme,
                oyuncu2EnYuksekKoruma, oyuncu2ToplamKoruma
        );

        TextView istatistikText = findViewById(R.id.istatistikText);
        istatistikText.setText(istatistikler);
        istatistikText.setVisibility(View.VISIBLE);
    }

    private void oyunuBaslat() {
        // Itemleri dağıt ve oyunu başlat
        itemleriDagit();

        // UI'ı güncelle
        baslaButton.setVisibility(View.INVISIBLE);
        // Tüm itemleri görünür yap
        oyuncu1Kilic.setVisibility(View.VISIBLE);
        oyuncu1Asa.setVisibility(View.VISIBLE);
        oyuncu1Kalkan.setVisibility(View.VISIBLE);
        oyuncu2Kilic.setVisibility(View.VISIBLE);
        oyuncu2Asa.setVisibility(View.VISIBLE);
        oyuncu2Kalkan.setVisibility(View.VISIBLE);

        oyuncu1Hp.setVisibility(View.VISIBLE);
        oyuncu2Hp.setVisibility(View.VISIBLE);

        // Oyun başlangıç zamanını kaydet
        oyunBaslangicZamani = System.currentTimeMillis();
    }

    private void yenidenOyna() {
        // Item seçme ekranına geri dön
        Intent intent = new Intent(gameActivity.this, itemSec.class);
        startActivity(intent);
        finish();  // Mevcut activity'yi kapat
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

    // Yeni metodlar ekle
    private boolean itemKullanilabilirMi(int oyuncu, String itemTipi) {
        if (oyuncu == 1) {
            switch (itemTipi) {
                case "kilic":
                    return !oyuncu1KilicKullanildi;
                case "asa":
                    return !oyuncu1AsaKullanildi;
                case "kalkan":
                    return !oyuncu1KalkanKullanildi;
            }
        } else {
            switch (itemTipi) {
                case "kilic":
                    return !oyuncu2KilicKullanildi;
                case "asa":
                    return !oyuncu2AsaKullanildi;
                case "kalkan":
                    return !oyuncu2KalkanKullanildi;
            }
        }
        return false;
    }

    private void gosterKullanilabilirItemler() {
        // Oyuncu 1'in itemlerini güncelle
        oyuncu1Kilic.setVisibility(oyuncu1KilicKullanildi ? View.INVISIBLE : View.VISIBLE);
        oyuncu1Asa.setVisibility(oyuncu1AsaKullanildi ? View.INVISIBLE : View.VISIBLE);
        oyuncu1Kalkan.setVisibility(oyuncu1KalkanKullanildi ? View.INVISIBLE : View.VISIBLE);

        // Oyuncu 2'nin itemlerini güncelle
        oyuncu2Kilic.setVisibility(oyuncu2KilicKullanildi ? View.INVISIBLE : View.VISIBLE);
        oyuncu2Asa.setVisibility(oyuncu2AsaKullanildi ? View.INVISIBLE : View.VISIBLE);
        oyuncu2Kalkan.setVisibility(oyuncu2KalkanKullanildi ? View.INVISIBLE : View.VISIBLE);
    }
}