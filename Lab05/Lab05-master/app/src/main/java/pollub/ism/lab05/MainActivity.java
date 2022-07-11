package pollub.ism.lab05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorCompletionService;

public class MainActivity extends AppCompatActivity {
    private Button zapisz = null, odczytaj = null;
    private EditText nazwaZapisz = null, notatka = null;
    private Spinner nazwaCzytaj = null;

    private ArrayList<String> nazwyPlikow = null;
    private ArrayAdapter<String> adapterSpinera = null;

    private final String NAZWA_PREFERENCES = "Aplikacja do notatek";
    private final String KLUCZ_DO_PREFERENCES = "Zapisanie nazwy plików";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zapisz = (Button) findViewById(R.id.przyciskZapisz);
        odczytaj = (Button) findViewById(R.id.przyciskCzytaj);
        nazwaZapisz = (EditText) findViewById(R.id.editTextNazwaZapisz);
        notatka = (EditText) findViewById(R.id.editTextNotatka);
        nazwaCzytaj = (Spinner) findViewById(R.id.spinnerNazwaCzytaj);

        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zapisanieNotatki();
            }
        });

        odczytaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odczytanieNotatki();
            }
        });
    }

    @Override
    protected void onPause() {
        zapiszSharePreferences();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        nazwyPlikow = new ArrayList<>();
        adapterSpinera = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nazwyPlikow);
        nazwaCzytaj.setAdapter(adapterSpinera);

        odczytajSharePreferences();
    }

    private void zapisanieNotatki() {
        String nazwaPliku = nazwaZapisz.getText().toString(), informacja = "Udało się zapisać";

        if (!zapiszDoPliku(nazwaPliku, notatka)) {
            informacja = "Nie udało się zapisać";
        }

        Toast.makeText(this, informacja, Toast.LENGTH_SHORT).show();
    }

    private void odczytanieNotatki() {
        String nazwaPliku = null, informacja = "Udało się przeczytać";
        try {
            nazwaPliku = nazwaCzytaj.getSelectedItem().toString();
        } catch (Exception e) {
            Toast.makeText(this, "Nie wybrano pliku", Toast.LENGTH_SHORT).show();
            return;
        }

        notatka.getText().clear();

        if (!odczytajZPliku(nazwaPliku, notatka)) {
            informacja = "Nie udało się przeczytać";
        }

        Toast.makeText(this, informacja, Toast.LENGTH_SHORT).show();
    }

    private boolean zapiszDoPliku(final String nazwaPliku, final EditText poleEdycyjne) {
        boolean sukces = true;

        File katalog = getApplicationContext().getExternalFilesDir(null);
        File plik = new File(katalog + File.separator + nazwaPliku);
        BufferedWriter zapisywacz = null;

        try {
            zapisywacz = new BufferedWriter(new FileWriter(plik));
            zapisywacz.write(poleEdycyjne.getText().toString());
        } catch (Exception e) {
            sukces = false;
        } finally {
            try {
                zapisywacz.close();
            } catch (Exception e) {
                sukces = false;
            }
        }

        if (sukces && !nazwyPlikow.contains(nazwaPliku)) {
            nazwyPlikow.add(nazwaPliku);
            adapterSpinera.notifyDataSetChanged();
        }

        return sukces;
    }

    private boolean odczytajZPliku(final String nazwaPliku, final EditText poleEdycyjne) {
        boolean sukces = true;

        File katalog = getApplicationContext().getExternalFilesDir(null);
        File plik = new File(katalog + File.separator + nazwaPliku);
        BufferedReader odczytywacz = null;

        if (plik.exists()) {
            try {
                odczytywacz = new BufferedReader(new FileReader(plik));
                String linia = odczytywacz.readLine() + "\n";
                while (linia != null) {
                    poleEdycyjne.getText().append(linia);
                    linia = odczytywacz.readLine();
                }
            } catch (Exception e) {
                sukces = false;
            } finally {
                if (odczytywacz != null) {
                    try {
                        odczytywacz.close();
                    } catch (Exception e) {
                        sukces = false;
                    }
                }
            }

            return sukces;
        } else
            return false;
    }

    private void zapiszSharePreferences() {
        SharedPreferences preferences = getSharedPreferences(NAZWA_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor edytor = preferences.edit();
        edytor.putStringSet(KLUCZ_DO_PREFERENCES, new HashSet<String>(nazwyPlikow));
        edytor.apply();
    }

    private void odczytajSharePreferences() {
        SharedPreferences sh = getSharedPreferences(NAZWA_PREFERENCES, MODE_PRIVATE);
        Set<String> zapisaneNazwy = sh.getStringSet(KLUCZ_DO_PREFERENCES, null);

        if (zapisaneNazwy != null) {
            nazwyPlikow.clear();
            for (String nazwa: zapisaneNazwy)
                nazwyPlikow.add(nazwa);
            adapterSpinera.notifyDataSetChanged();
        }
    }
}