package com.example.portfelwalutowy;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CurrencyInfo {
    @SerializedName("code")
    private String code;

    @SerializedName("rates")
    private List<Rates> rates;


    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public List<Rates> getRates() {
        return this.rates;
    }

    public void setRates(final List<Rates> rates) {
        this.rates = rates;
    }


    class Rates {
        @SerializedName("mid")
        private double mid;

        public double getMid() {
            return mid;
        }

        public void setMid(final double mid) {
            this.mid = mid;
        }
    }
}
