package io.github.nearchos.favourite.City;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.github.nearchos.favourite.Country.DisplayMoreCountry;
import io.github.nearchos.favourite.Database.CountryDatabase;
import io.github.nearchos.favourite.Home.NavigationDrawerActivity;
import io.github.nearchos.favourite.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowCityDetails extends NavigationDrawerActivity
{
    CountryDatabase db;
    TextView pop,air,city,coun;
    ImageView city_image;
    Button more_wiki,back;

    //weather display
    TextView t2_temp,t3_desc;
    ImageView img_weather;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_new_city);


        db= new CountryDatabase(this);

        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }



        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("city_name");


        coun = findViewById(R.id.textViewCountryName);
        pop = findViewById(R.id.textViewPopulation);
        air = findViewById(R.id.textViewNearAirport);
        city = findViewById(R.id.display_city_name);
        city_image = findViewById(R.id.city_image_display);

        more_wiki = findViewById(R.id.city_web);
        back = findViewById(R.id.back_button);


        city.setText(message);

        Bitmap img= db.getImage(city.getText().toString());
        String population = db.getPopulation(city.getText().toString());
        String countryName = db.getCityCountryName(city.getText().toString());
        String airportNear = db.getAirportNear(city.getText().toString());

        city_image.setImageBitmap(img);
        pop.setText(population);
        coun.setText(countryName);
        air.setText(airportNear);


        //directing to wikipedia for more city data
        more_wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/"+city.getText().toString()+""));
                startActivity(browser);
            }
        });

        //directing to google maps app to check the nearest airport
        air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q='"+air.getText().toString()+"'");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        //directing back to country details page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(getApplicationContext(), DisplayMoreCountry.class);
                startActivity(browser);
            }
        });



        //getting weather information
        t2_temp= findViewById(R.id.temp);
        t2_temp.setText("");
        t3_desc= findViewById(R.id.description);
        t3_desc.setText("");

        img_weather= findViewById(R.id.weather_image);

        api_key(message);


    }

    private void api_key(final String City)
    {
        OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=29378a47c179b0fec5e677db974f8af9&units=metric")
                .get()
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response= client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
                {
                    String responseData= response.body().string();
                    try {
                        JSONObject json=new JSONObject(responseData);
                        JSONArray array=json.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description=object.getString("description");
                        String icons = object.getString("icon");

                        JSONObject temp1= json.getJSONObject("main");
                        Double Temperature=temp1.getDouble("temp");

                     //   setText(t1_town,City);

                        String temps=Math.round(Temperature)+" °C";
                        setText(t2_temp,temps);
                        setText(t3_desc,description);
                        setImage(img_weather,icons);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void setImage(final ImageView imageView, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //paste switch
                switch (value){
                    case "01d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "01n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "02d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "02n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "03d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "03n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "04d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "04n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "09d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d09d));
                        break;
                    case "09n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d09d));
                        break;
                    case "10d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "10n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "11d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "11n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "13d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    case "13n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    default:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));

                }
            }
        });
    }









}
