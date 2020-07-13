package io.github.nearchos.favourite.Country;

import android.graphics.Bitmap;

import java.io.Serializable;

public class CountryDetailsModel implements Serializable
{
    private String country_name,capitalCity,currency,language,city;
    private Bitmap image;

    public CountryDetailsModel(String country_name, String capitalCity, String currency, String language, String city, Bitmap image) {
        this.country_name = country_name;
        this.capitalCity = capitalCity;
        this.currency = currency;
        this.language = language;
        this.city = city;
        this.image = image;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
