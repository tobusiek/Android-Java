package com.example.portfelwalutowy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String URL_EUR = "api/exchangerates/rates/a/eur/?format=json";
    private final String URL_USD = "api/exchangerates/rates/a/usd/?format=json";
    private final String URL_CHF = "api/exchangerates/rates/a/chf/?format=json";

    private final double balancePLN = 1000;
    private final double balanceEUR = 1500;
    private final double balanceUSD = 1000;
    private final double balanceCHF = 1000;
    private double balance;

    private double midEUR;
    private double midUSD;
    private double midCHF;

    private String selectedCurrency;

    private TextView textViewPLNBalance;
    private TextView textViewEURBalance;
    private TextView textViewUSDBalance;
    private TextView textViewCHFBalance;
    private TextView textViewBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPLNBalance = (TextView) findViewById(R.id.textViewPLNBalance);
        textViewPLNBalance.setText(String.valueOf(this.balancePLN));

        textViewEURBalance = (TextView) findViewById(R.id.textViewEURBalance);
        textViewEURBalance.setText(String.valueOf(this.balanceEUR));

        textViewUSDBalance = (TextView) findViewById(R.id.textViewUSDBalance);
        textViewUSDBalance.setText(String.valueOf(this.balanceUSD));

        textViewCHFBalance = (TextView) findViewById(R.id.textViewCHFBalance);
        textViewCHFBalance.setText(String.valueOf(this.balanceCHF));

        textViewBalance = (TextView) findViewById(R.id.textViewBalance);

        getMids();

        Spinner spinner = (Spinner) findViewById(R.id.currencySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrency = adapter.getItem(position).toString();
                calculateBalance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    private void getMids() {
        if (midEUR == 0 || midUSD == 0 || midCHF == 0) {
            this.balance = 0;

            Thread threadEUR = new Thread() {
                @Override
                public void run() {
                    getCurrencyInfo(URL_EUR);
                }
            };
            threadEUR.start();

            Thread threadUSD = new Thread() {
                @Override
                public void run() {
                    getCurrencyInfo(URL_USD);
                }
            };
            threadUSD.start();

            Thread threadCHF = new Thread() {
                @Override
                public void run() {
                    getCurrencyInfo(URL_CHF);
                }
            };
            threadCHF.start();
        }
    }

    @SuppressLint("DefaultLocale")
    private void calculateBalance() {
        switch (selectedCurrency) {
            case "Złotówki":
                this.balance = this.balancePLN + this.midEUR * this.balanceEUR +
                        this.midUSD * this.balanceUSD + this.midCHF * this.balanceCHF;
                break;

            case "Euro":
                this.balance = this.balanceEUR + this.balancePLN / this.midEUR +
                        this.balanceUSD * this.midUSD / this.midEUR +
                        this.balanceCHF * this.midCHF / this.midEUR;
                break;

            case "Dolary":
                this.balance = this.balanceUSD + this.balancePLN / this.midUSD +
                        this.balanceEUR * this.midEUR / this.midUSD +
                        this.balanceCHF * this.midCHF / this.midUSD;
                break;

            case "Franki Szwajcarskie":
                this.balance = this.balanceCHF + this.balancePLN / this.midCHF +
                        this.balanceEUR * this.midEUR / this.midCHF +
                        this.balanceUSD * this.midUSD / this.midCHF;
                break;
        }

        this.textViewBalance.setText(String.format("%.4f", this.balance));

        calculatePercentages();
    }

    private void getCurrencyInfo(final String URL) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, URL);
        Call<CurrencyInfo> call = apiInterface.getCurrencyInfo();

        call.enqueue(new Callback<CurrencyInfo>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyInfo> call, @NonNull Response<CurrencyInfo> response) {
                getRate(response.body());
            }

            @Override
            public void onFailure(Call<CurrencyInfo> call, Throwable t) {
                getRate(null);
            }
        });
    }

    private void getRate(CurrencyInfo info) {
        if (info != null) {
            switch (info.getCode()) {
                case "EUR":
                    this.midEUR = info.getRates().get(0).getMid();
                    break;

                case "USD":
                    this.midUSD = info.getRates().get(0).getMid();
                    break;

                case "CHF":
                    this.midCHF = info.getRates().get(0).getMid();
                    break;
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private void calculatePercentages() {
        double walletValue = this.balancePLN + this.balanceEUR * this.midEUR +
               this.balanceUSD * this.midUSD + this.balanceCHF * this.midCHF;

        this.textViewPLNBalance.setText(String.format("%.4f, %.2f%s", this.balancePLN,
                (100 * this.balancePLN / walletValue), "%"));

        this.textViewEURBalance.setText(String.format("%.4f, %.2f%s", this.balanceEUR,
                (100 * this.balanceEUR * this.midEUR / walletValue), "%"));

        this.textViewUSDBalance.setText(String.format("%.4f, %.2f%s", this.balanceUSD,
                (100 * this.balanceUSD * this.midUSD / walletValue), "%"));

        this.textViewCHFBalance.setText(String.format("%.4f, %.2f%s", this.balanceCHF,
                (100 * this.balanceCHF * this.midCHF) / walletValue, "%"));
    }
}