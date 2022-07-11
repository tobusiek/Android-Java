package pollub.ism.lab06;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pollub.ism.lab06.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;

    private String wybraneWarzywoNazwa = null;
    private Integer wybraneWarzywoIlosc = null;
    private String dataZmiany = null;

    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};

    private BazaMagazynowa bazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);

        binding.przyciskSkladuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zmienStan(OperacjaMagazynowa.SKLADUJ);
            }
        });

        binding.przyciskWydaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zmienStan(OperacjaMagazynowa.WYDAJ);
            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wybraneWarzywoNazwa = adapter.getItem(i).toString();
                binding.historia.setText("");
                aktualizuj();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        bazaDanych = Room.databaseBuilder(getApplicationContext(), BazaMagazynowa.class, BazaMagazynowa.NAZWA_BAZY).allowMainThreadQueries().build();

        if (bazaDanych.pozycjaMagazynowaDAO().size() == 0) {
            String[] asortyment = getResources().getStringArray(R.array.Asortyment);
            for (String nazwa : asortyment) {
                PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME = nazwa;
                pozycjaMagazynowa.QUANTITY = 0;
                pozycjaMagazynowa.DATEOFLASTCHANGE = null;
                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
            }
        }
    }

    private void aktualizuj() {
        wybraneWarzywoIlosc = bazaDanych.pozycjaMagazynowaDAO().znajdzIloscPoNazwie(wybraneWarzywoNazwa);
        binding.tekstStanMagazynu.setText("Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc);
        dataZmiany = bazaDanych.pozycjaMagazynowaDAO().znajdzDatePoNazwie(wybraneWarzywoNazwa);
        binding.tekstDataCzas.setText(dataZmiany);
        binding.historia.setText("");

        HistoriaPozycji[] historiaPozycji = bazaDanych.pozycjaMagazynowaDAO().pokazHistoriePoNazwie(wybraneWarzywoNazwa);
        for (HistoriaPozycji hp : historiaPozycji)
            binding.historia.append(hp.DATEOFCHANGE + ", " + hp.OLDQUANTITY + "->" + hp.NEWQUANTITY + "\n");
    }

    private void zmienStan(OperacjaMagazynowa operacja) {
        Integer zmianaIlosci = null, nowaIlosc = null;

        try {
            zmianaIlosci = Integer.parseInt(binding.edycjaIlosc.getText().toString());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy, hh:mm:ss");
            dataZmiany = simpleDateFormat.format(calendar.getTime());
        } catch(NumberFormatException ex) {
            return;
        } finally {
            binding.edycjaIlosc.setText("");
        }

        switch (operacja) {
            case SKLADUJ: nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci; break;
            case WYDAJ: nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci; break;
        }

        bazaDanych.pozycjaMagazynowaDAO().aktualizujDatePoNazwie(dataZmiany, wybraneWarzywoNazwa);
        bazaDanych.pozycjaMagazynowaDAO().aktualizujIloscPoNazwie(wybraneWarzywoNazwa, nowaIlosc);

        HistoriaPozycji historiaPozycji = new HistoriaPozycji();
        historiaPozycji.NAME = wybraneWarzywoNazwa;
        historiaPozycji.DATEOFCHANGE = dataZmiany;
        historiaPozycji.OLDQUANTITY = wybraneWarzywoIlosc;
        historiaPozycji.NEWQUANTITY = nowaIlosc;
        bazaDanych.pozycjaMagazynowaDAO().insert(historiaPozycji);

        aktualizuj();
    }
}