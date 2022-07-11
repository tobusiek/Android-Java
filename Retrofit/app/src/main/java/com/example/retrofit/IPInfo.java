package com.example.retrofit;

import com.google.gson.annotations.SerializedName;

public class IPInfo {

    @SerializedName("ip")
    private String ip;

    @SerializedName("hostname")
    private String hostname;

    @SerializedName("anycast")
    private String anycast;

    @SerializedName("city")
    private String city;

    @SerializedName("region")
    private String region;

    @SerializedName("country")
    private String country;

    @SerializedName("loc")
    private String loc;

    @SerializedName("org")
    private String org;

    @SerializedName("postal")
    private String postal;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("readme")
    private String readme;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAnycast() {
        return anycast;
    }

    public void setAnycast(String anycast) {
        this.anycast = anycast;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }
}
