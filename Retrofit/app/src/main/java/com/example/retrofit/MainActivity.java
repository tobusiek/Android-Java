package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPress(view);
            }
        });
    }

    public void buttonPress(View view) {
        getIPInfo();
    }

    private String getIP() {
        EditText e1 = (EditText) findViewById(R.id.ip1);
        if (parseTextToInt(e1) < 1 || parseTextToInt(e1) > 255) {
            Snackbar.make(findViewById(R.id.textView),
                    "Pierwsza część adresu ip jest niepoprawna, wybierz wartość [1-255].",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

            return null;
        }

        EditText e2 = (EditText) findViewById(R.id.ip2);
        if (parseTextToInt(e2) < 0 || parseTextToInt(e2) > 255) {
            Snackbar.make(findViewById(R.id.textView),
                    "Druga część adresu ip jest niepoprawna, wybierz wartość [0-255].",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

            return null;
        }

        EditText e3 = (EditText) findViewById(R.id.ip3);
        if (parseTextToInt(e3) < 0 || parseTextToInt(e3) > 255) {
            Snackbar.make(findViewById(R.id.textView),
                    "Trzecia część adresu ip jest niepoprawna, wybierz wartość [0-255].",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

            return null;
        }

        EditText e4 = (EditText) findViewById(R.id.ip4);
        if (parseTextToInt(e4) < 0 || parseTextToInt(e4) > 255) {
            Snackbar.make(findViewById(R.id.textView),
                    "Czwarta część adresu ip jest niepoprawna, wybierz wartość [0-255].",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

            return null;
        }

        String ip = e1.getText().toString() + "." + e2.getText().toString() + "." +
                e3.getText().toString() + "." + e4.getText().toString();

        Log.i("-->", ip);

        return ip;
    }

    private void printInfo(IPInfo info) {
        TextView textView = (TextView) findViewById(R.id.textView);

        String s;

        if (info == null)
            s = "Failed";
        else
            s = "ip: " + info.getIp() + "\n" +
                    "hostname: " + info.getHostname() + "\n" +
                    "city: " + info.getCity() + "\n" +
                    "region: " + info.getRegion() + "\n" +
                    "country: " + info.getCountry() + "\n" +
                    "loc: " + info.getLoc() + "\n" +
                    "org: " + info.getOrg() + "\n" +
                    "postal: " + info.getPostal() + "\n" +
                    "timezone: " + info.getTimezone() + "\n" +
                    "readme: " + info.getReadme() + "\n" +
                    "city: " + info.getCity();

        textView.setText(s);
    }

    private void getIPInfo() {
        String ip = getIP();
        if (ip != null) {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, getIP());

            Call<IPInfo> call = apiInterface.getIPInfo();

            call.enqueue(new Callback<IPInfo>() {
                @Override
                public void onResponse(Call<IPInfo> call, Response<IPInfo> response) {
                    printInfo(response.body());
                }

                @Override
                public void onFailure(Call<IPInfo> call, Throwable t) {
                    printInfo(null);
                }
            });
        }
    }

    private int parseTextToInt(EditText e) {
        return Integer.parseInt(String.valueOf(e.getText()));
    }
}