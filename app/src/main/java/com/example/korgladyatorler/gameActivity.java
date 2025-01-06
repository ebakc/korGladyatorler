package com.example.korgladyatorler;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class gameActivity extends AppCompatActivity {

    private void uiElemanlariTanimlama() {
        // Diğer UI elemanlarını tanımlama
        oyuncu1Kilic = findViewById(R.id.oyuncu1Kilic);
        oyuncu1Asa = findViewById(R.id.oyuncu1Asa);
        oyuncu1Kalkan = findViewById(R.id.oyuncu1Kalkan);

        oyuncu2Kilic = findViewById(R.id.oyuncu2Kilic);
        oyuncu2Asa = findViewById(R.id.oyuncu2Asa);
        oyuncu2Kalkan = findViewById(R.id.oyuncu2Kalkan);

        oyuncu1Hp = findViewById(R.id.oyuncu1Hp);
        oyuncu2Hp = findViewById(R.id.oyuncu2Hp);

        itemsecButton = findViewById(R.id.itemsecButton);

        // Zarların tanımlanması
        oyuncu1Zar = findViewById(R.id.oyuncu1Zar);
        oyuncu2Zar = findViewById(R.id.oyuncu2Zar);

        oyuncu1Char = findViewById(R.id.oyuncu1Char);
        oyuncu2Char = findViewById(R.id.oyuncu2Char);

        // Yeniden başlatma butonunu tanımlama
        yenidenButton = findViewById(R.id.yenidenButton);

        // Kazanan TextView'i tanımlayın
        kazananText = findViewById(R.id.kazananText);
    }

    private ImageButton oyuncu1Kilic, oyuncu1Asa, oyuncu1Kalkan;
    private ImageButton oyuncu2Kilic, oyuncu2Asa, oyuncu2Kalkan;
    private TextView oyuncu1Hp, oyuncu2Hp;
    private ImageButton itemsecButton;
    private ImageButton yenidenButton; // Yeniden başlatma butonu
    private TextView kazananText;
    private ImageView oyuncu1Zar, oyuncu2Zar, oyuncu1Char, oyuncu2Char;

    private ArrayList<String> kiliclar;
    private ArrayList<String> asalar;
    private ArrayList<String> kalkanlar;
    private Random random;

    private boolean oyuncu1SecimYapti = false;
    private boolean oyuncu2SecimYapti = false;
    int oyuncu1Can=100;
    int oyuncu2Can=100;
    private int oyuncu1Hasar; // Oyuncu 1'in hasarını saklamak için
    private int oyuncu2Hasar; // Oyuncu 2'nin hasarını saklamak için

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

        random = new Random(); // Random sınıfını tanımla

        yenidenButton.setOnClickListener(v -> yenidenBaslat());

        // Item butonlarına tıklama işlemi ekle
        oyuncu1Kilic.setOnClickListener(v -> zarAt(1, false, false)); // Kılıç kullanımı
        oyuncu1Asa.setOnClickListener(v -> zarAt(1, true, false));  // Asa kullanımı
        oyuncu1Kalkan.setOnClickListener(v -> zarAt(1, false, true)); // Kalkan kullanımı

        oyuncu2Kilic.setOnClickListener(v -> zarAt(2, false, false)); // Kılıç kullanımı
        oyuncu2Asa.setOnClickListener(v -> zarAt(2, true, false));  // Asa kullanımı
        oyuncu2Kalkan.setOnClickListener(v -> zarAt(2, false, true)); // Kalkan kullanımı

        yenidenButton.setOnClickListener(v -> yenidenBaslat());

        // Window insets ayarları
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });
    }

    private void yenidenBaslat() {
        // Activity'yi yeniden başlat
        recreate();
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
            itemsecButton.setVisibility(View.INVISIBLE);

            // Resimleri değiştirme
            oyuncu1Kilic.setImageResource(getResources().getIdentifier(kilic1, "drawable", getPackageName()));
            oyuncu1Asa.setImageResource(getResources().getIdentifier(asa1, "drawable", getPackageName()));
            oyuncu1Kalkan.setImageResource(getResources().getIdentifier(kalkan1, "drawable", getPackageName()));

            oyuncu2Kilic.setImageResource(getResources().getIdentifier(kilic2, "drawable", getPackageName()));
            oyuncu2Asa.setImageResource(getResources().getIdentifier(asa2, "drawable", getPackageName()));
            oyuncu2Kalkan.setImageResource(getResources().getIdentifier(kalkan2, "drawable", getPackageName()));

            // Kılıç butonlarının tag'lerini ayarlama
            oyuncu1Kilic.setTag(kilic1);
            oyuncu1Asa.setTag(asa1);
            oyuncu1Kalkan.setTag(kalkan1);

            oyuncu2Kilic.setTag(kilic2);
            oyuncu2Asa.setTag(asa2);
            oyuncu2Kalkan.setTag(kalkan2);
        });
    }

    private void zarAt(int oyuncu, boolean asaKullaniliyor, boolean kalkanKullaniliyor) {
        // Zar atma işlemleri
        int zarDegeri = random.nextInt(3) + 1; // Zar değeri 1-3 arasında

        // Animasyonu başlat ve resimleri değiştir
        if (oyuncu == 1) {
            // Oyuncu 1'in zarını at
            oyuncu1Zar.setVisibility(View.INVISIBLE); // Zar ilk başta görünmesin
            oyuncu1Zar.setImageResource(getZarResId(zarDegeri));

            // Oyuncu 1'in item butonlarını gizle
            oyuncu1Kilic.setVisibility(View.INVISIBLE);
            oyuncu1Asa.setVisibility(View.INVISIBLE);
            oyuncu1Kalkan.setVisibility(View.INVISIBLE);
            oyuncu1SecimYapti = true;

            // Karakter fotoğrafını değiştir (saldırı fotoğrafı)
            oyuncu1Char.setImageResource(R.drawable.char_attack); // char_attack, saldırı fotoğrafınız

            if (asaKullaniliyor) {
                // Asa hasar hesaplamayı burada başlatıyoruz
                int[] asaSonuc = asaHasarHesapla(1, zarDegeri); // Oyuncu 1 için hasar ve can yenileme değerini al
                oyuncu1Hasar = asaSonuc[0]; // Oyuncu 1'in hasarını sakla
                oyuncu1Can += asaSonuc[1]; // Oyuncu 1'in canını yenile
            } else if (kalkanKullaniliyor) {
                // Kalkan kullanımı
                // Kalkanın koruma değerini hesapla
                int koruma = kalkanKorumaHesapla(1, zarDegeri); // Oyuncu 1 için kalkan korumasını hesapla
                // Kalkan kullanıldığında hasar 0 olmalı
                oyuncu1Hasar = 0; // Oyuncu 1 hasar vermiyor, sadece kalkan kullanıyor
            } else {
                // Kılıç hasar hesaplamayı burada başlatıyoruz
                oyuncu1Hasar = kilicHasarHesapla(1, zarDegeri); // Oyuncu 1 için hasar değerini al
            }

        } else {
            // Oyuncu 2'nin zarını at
            oyuncu2Zar.setVisibility(View.INVISIBLE); // Zar ilk başta görünmesin
            oyuncu2Zar.setImageResource(getZarResId(zarDegeri));

            // Oyuncu 2'nin item butonlarını gizle
            oyuncu2Kilic.setVisibility(View.INVISIBLE);
            oyuncu2Asa.setVisibility(View.INVISIBLE);
            oyuncu2Kalkan.setVisibility(View.INVISIBLE);
            oyuncu2SecimYapti = true;

            // Karakter fotoğrafını değiştir (saldırı fotoğrafı)
            oyuncu2Char.setImageResource(R.drawable.char_attack); // char_attack, saldırı fotoğrafınız

            if (asaKullaniliyor) {
                // Asa hasar hesaplamayı burada başlatıyoruz
                int[] asaSonuc = asaHasarHesapla(2, zarDegeri); // Oyuncu 2 için hasar ve can yenileme değerini al
                oyuncu2Hasar = asaSonuc[0]; // Oyuncu 2'nin hasarını sakla
                oyuncu2Can += asaSonuc[1]; // Oyuncu 2'nin canını yenile
            } else if (kalkanKullaniliyor) {
                // Kalkan kullanımı
                // Kalkanın koruma değerini hesapla
                int koruma = kalkanKorumaHesapla(2, zarDegeri); // Oyuncu 2 için kalkan korumasını hesapla
                // Kalkan kullanıldığında hasar 0 olmalı
                oyuncu2Hasar = 0; // Oyuncu 2 hasar vermiyor, sadece kalkan kullanıyor
            } else {
                // Kılıç hasar hesaplamayı burada başlatıyoruz
                oyuncu2Hasar = kilicHasarHesapla(2, zarDegeri); // Oyuncu 2 için hasar değerini al
            }
        }

        // Her iki oyuncu da zar atarsa
        if (oyuncu1SecimYapti && oyuncu2SecimYapti) {
            // 1 saniye bekleyip hasarları güncelle
            new Handler().postDelayed(() -> {
                // Oyuncu 1'in hasarını Oyuncu 2'ye uygula
                int koruma1 = kalkanKorumaHesapla(2, zarDegeri); // Oyuncu 2'nin kalkan korumasını hesapla
                int hasarUygula1 = Math.max(0, oyuncu1Hasar - koruma1); // Kalkan korumasını hasardan çıkar
                oyuncu2Can -= hasarUygula1; // Oyuncu 2'nin canını güncelle

                if (oyuncu2Hp != null) {
                    Log.d("Hasar Hesapla", "Oyuncu 2 HP: " + oyuncu2Can);
                    oyuncu2Hp.setText("HP: " + oyuncu2Can); // Oyuncu 2'nin HP'sini TextView'e yazma
                }

                // Oyuncu 2'nin hasarını Oyuncu 1'e uygula
                int koruma2 = kalkanKorumaHesapla(1, zarDegeri); // Oyuncu 1'in kalkan korumasını hesapla
                int hasarUygula2 = Math.max(0, oyuncu2Hasar - koruma2); // Kalkan korumasını hasardan çıkar
                oyuncu1Can -= hasarUygula2; // Oyuncu 1'in canını güncelle

                if (oyuncu1Hp != null) {
                    Log.d("Hasar Hesapla", "Oyuncu 1 HP: " + oyuncu1Can);
                    oyuncu1Hp.setText("HP: " + oyuncu1Can); // Oyuncu 1'in HP'sini TextView'e yazma
                }

                // Oyun bitiş kontrolü
                if (oyuncu1Can <= 0) {
                    // Oyuncu 1 kaybetti
                    kazananText.setText("Oyuncu 2 Kazandı!");
                    kazananText.setVisibility(View.VISIBLE);
                    oyuncu1Hp.setVisibility(View.INVISIBLE);
                    oyuncu1Char.setVisibility(View.INVISIBLE);
                    oyuncu1Kilic.setVisibility(View.INVISIBLE);
                    oyuncu1Asa.setVisibility(View.INVISIBLE);
                    oyuncu1Kalkan.setVisibility(View.INVISIBLE);
                    yenidenButton.setVisibility(View.VISIBLE); // Yeniden başlatma butonunu görünür yap
                } else if (oyuncu2Can <= 0) {
                    // Oyuncu 2 kaybetti
                    kazananText.setText("Oyuncu 1 Kazandı!");
                    kazananText.setVisibility(View.VISIBLE);
                    oyuncu2Hp.setVisibility(View.INVISIBLE);
                    oyuncu2Char.setVisibility(View.INVISIBLE);
                    oyuncu2Kilic.setVisibility(View.INVISIBLE);
                    oyuncu2Asa.setVisibility(View.INVISIBLE);
                    oyuncu2Kalkan.setVisibility(View.INVISIBLE);
                    yenidenButton.setVisibility(View.VISIBLE); // Yeniden başlatma butonunu görünür yap
                }

                // Karakter fotoğraflarını geri çevir (char_stand)
                oyuncu1Char.setImageResource(R.drawable.char_stand); // char_stand, duruş fotoğrafınız
                oyuncu2Char.setImageResource(R.drawable.char_stand); // char_stand, duruş fotoğrafınız

                // Zarları görünür yap
                oyuncu1Zar.setVisibility(View.VISIBLE);
                oyuncu2Zar.setVisibility(View.VISIBLE);

                // Oyuncu 1'in kılıç butonunu görünür hale getir
                oyuncu1Kilic.setVisibility(View.VISIBLE);
                // Oyuncu 2'nin kılıç butonunu görünür hale getir
                oyuncu2Kilic.setVisibility(View.VISIBLE);
                // Diğer item butonlarını da görünür hale getirin
                oyuncu1Asa.setVisibility(View.VISIBLE);
                oyuncu1Kalkan.setVisibility(View.VISIBLE);
                oyuncu2Asa.setVisibility(View.VISIBLE);
                oyuncu2Kalkan.setVisibility(View.VISIBLE);

                // Her iki oyuncunun butonlarını tekrar aktif hale getir
                oyuncu1SecimYapti = false;
                oyuncu2SecimYapti = false;
            }, 1000); // 1 saniye bekleme
        }
    }



    private int kilicHasarHesapla(int oyuncu, int zarDegeri) {
        int hasar = 0;

        // Seçilen kılıcın id'sini belirleyelim
        ImageButton kilic = oyuncu == 1 ? oyuncu1Kilic : oyuncu2Kilic;

        // Kilic'ten id'yi alırken null kontrolü ekliyoruz
        String kilicId = kilic.getTag() != null ? kilic.getTag().toString() : "";

        // Eğer kilicId boşsa, hata mesajı verelim ve metodu sonlandıralım
        if (kilicId.isEmpty()) {
            Log.e("Kılıç Hata", "Kılıç ID'si null veya boş. Kılıç seçilmedi.");
            return 0; // Kılıç seçilmediği için 0 hasar döndür
        }

        // Hasar hesaplama
        switch (kilicId) {
            case "id01_a_kilic_kritikseven":  // A Seviye Kılıç
                if (zarDegeri == 1) {
                    hasar = 27;
                } else if (zarDegeri == 2) {
                    hasar = 30;
                } else if (zarDegeri == 3) {
                    hasar = 35;
                }
                break;
            case "id02_b_kilic_dengesizkesen":  // B Seviye Kılıç (1)
            case "id03_b_kilic_kahkahalicelik": // B Seviye Kılıç (2)
                if (zarDegeri == 1) {
                    hasar = 13;
                } else if (zarDegeri == 2) {
                    hasar = 18;
                } else if (zarDegeri == 3) {
                    hasar = 23;
                }
                break;
            case "id04_c_kilic_paslidovuscu":  // C Seviye Kılıç (1)
            case "id05_c_kilic_topal":         // C Seviye Kılıç (2)
            case "id06_c_kilic_garipyarmac":   // C Seviye Kılıç (3)
                if (zarDegeri == 1) {
                    hasar = 5;
                } else if (zarDegeri == 2) {
                    hasar = 10;
                } else if (zarDegeri == 3) {
                    hasar = 15;
                }
                break;
            default:
                hasar = 0; // Geçersiz kılıç durumu
                break;
        }

        return hasar; // Hesaplanan hasar değerini döndür
    }

    private int[] asaHasarHesapla(int oyuncu, int zarDegeri) {
        int hasar = 0;
        double canYenileme = 0;

        // Seçilen asanın id'sini belirleyelim
        ImageButton asa = oyuncu == 1 ? oyuncu1Asa : oyuncu2Asa;

        // Asa'dan id'yi alırken null kontrolü ekliyoruz
        String asaId = asa.getTag() != null ? asa.getTag().toString() : "";

        // Eğer asaId boşsa, hata mesajı verelim ve metodu sonlandıralım
        if (asaId.isEmpty()) {
            Log.e("Asa Hata", "Asa ID'si null veya boş. Asa seçilmedi.");
            return new int[]{0, 0}; // Asa seçilmediği için 0 hasar ve 0 can yenileme döndür
        }

        // Hasar ve can yenileme hesaplama
        switch (asaId) {
            case "id07_a_asa_simsekyoldasi":  // A Seviye Asa
                if (zarDegeri == 1) {
                    hasar = 21;
                    canYenileme = 8.5;
                } else if (zarDegeri == 2) {
                    hasar = 23;
                    canYenileme = 10;
                } else if (zarDegeri == 3) {
                    hasar = 25;
                    canYenileme = 20;
                }
                break;
            case "id08_b_asa_dengesizparlayan": // B Seviye Asa (1)
            case "id09_b_asa_dogaefsunu":      // B Seviye Asa (2)
                if (zarDegeri == 1) {
                    hasar = 10;
                    canYenileme = 3.3;
                } else if (zarDegeri == 2) {
                    hasar = 14;
                    canYenileme = 5;
                } else if (zarDegeri == 3) {
                    hasar = 18;
                    canYenileme = 7;
                }
                break;
            case "id10_c_asa_catlakahsap":      // C Seviye Asa (1)
            case "id11_c_asa_titrekalev":       // C Seviye Asa (2)
            case "id12_c_asa_garipfisilti":     // C Seviye Asa (3)
                if (zarDegeri == 1) {
                    hasar = 3;
                    canYenileme = 1;
                } else if (zarDegeri == 2) {
                    hasar = 8;
                    canYenileme = 2.6;
                } else if (zarDegeri == 3) {
                    hasar = 12;
                    canYenileme = 4.8;
                }
                break;
            default:
                hasar = 0; // Geçersiz asa durumu
                break;
        }

        return new int[]{hasar, (int) canYenileme}; // Hasar ve can yenileme değerini döndür
    }

    private int kalkanKorumaHesapla(int oyuncu, int zarDegeri) {
        int koruma = 0;

        // Seçilen kalkanın id'sini belirleyelim
        ImageButton kalkan = oyuncu == 1 ? oyuncu1Kalkan : oyuncu2Kalkan;

        // Kalkan'tan id'yi alırken null kontrolü ekliyoruz
        String kalkanId = kalkan.getTag() != null ? kalkan.getTag().toString() : "";

        // Eğer kalkanId boşsa, hata mesajı verelim ve metodu sonlandıralım
        if (kalkanId.isEmpty()) {
            Log.e("Kalkan Hata", "Kalkan ID'si null veya boş. Kalkan seçilmedi.");
            return 0; // Kalkan seçilmediği için 0 koruma döndür
        }

        // Koruma hesaplama
        switch (kalkanId) {
            case "id13_a_kalkan_kaderkoruyucusu":  // A Seviye Kalkan
                if (zarDegeri == 1) {
                    koruma = 30;
                } else if (zarDegeri == 2) {
                    koruma = 35;
                } else if (zarDegeri == 3) {
                    // Hasar görmez, 10 can yeniler
                    koruma = Integer.MAX_VALUE; // Sonsuz koruma
                }
                break;
            case "id14_b_kalkan_yaraligardiyan": // B Seviye Kalkan (1)
            case "id15_b_kalkan_dengesizdefans": // B Seviye Kalkan (2)
                if (zarDegeri == 1) {
                    koruma = 13;
                } else if (zarDegeri == 2) {
                    koruma = 18;
                } else if (zarDegeri == 3) {
                    koruma = 23;
                }
                break;
            case "id16_c_kalkan_egribugru":      // C Seviye Kalkan (1)
            case "id17_c_kalkan_delikdesik":     // C Seviye Kalkan (2)
            case "id18_c_kalkan_ahsapharabe":    // C Seviye Kalkan (3)
                if (zarDegeri == 1) {
                    koruma = 5;
                } else if (zarDegeri == 2) {
                    koruma = 10;
                } else if (zarDegeri == 3) {
                    koruma = 15;
                }
                break;
            default:
                koruma = 0; // Geçersiz kalkan durumu
                break;
        }

        return koruma; // Hesaplanan koruma değerini döndür
    }




    // Zar görseli ID'sini döner
    private int getZarResId(int zarDegeri) {
        switch (zarDegeri) {
            case 1: return R.drawable.zar1;
            case 2: return R.drawable.zar2;
            case 3: return R.drawable.zar3;
            default: return R.drawable.zar1;
        }
    }

    // Zar atma aralığını rastgele belirle
    private int getZarAraligi(int oyuncu) {
        // Rastgele bir değer döndür
        return random.nextInt(3) + 1; // 1-3 aralığında rastgele bir sayı döner
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