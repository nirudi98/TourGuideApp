package io.github.nearchos.favourite.Country;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import io.github.nearchos.favourite.City.ShowCityDetails;
import io.github.nearchos.favourite.Database.CountryDatabase;
import io.github.nearchos.favourite.Home.NavigationDrawerActivity;
import io.github.nearchos.favourite.Notes.AddNote;
import io.github.nearchos.favourite.R;


public class DisplayMoreCountry extends NavigationDrawerActivity
{

    TextView tvUser,displayCapital,displayCurrency,displayLanguages;
    ImageView country_image;
    Button back_button, addNotes;
    CountryDatabase db;
    Bitmap img;

    ArrayList<String>  City_List = new ArrayList<String>();
    ArrayList<String> city_row ;


    String r1;
    String[] cityArr;


    String coun_name;

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_new_display_more_country);
        getLayoutInflater().inflate(R.layout.activity_new_display_more_country, frameLayout);

        db = new CountryDatabase(this);

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


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        coun_name =  sharedPreferences.getString("country_name", "name");


        //setting country name
        tvUser= findViewById(R.id.tvUser);
        tvUser.setText(coun_name);


        displayCapital = findViewById(R.id.textViewCapitalCity);
        displayCurrency = findViewById(R.id.textViewCurrency);
        displayLanguages = findViewById(R.id.textViewLanguage);


        //back button assigning
        back_button = findViewById(R.id.back_button);

        //add notes
        addNotes = findViewById(R.id.addNotes);


        //retrieving country details, capital city,languages,currency from database
        String cap= db.getCapitalCity(coun_name);
        String cur = db.getCurrency(coun_name);
        String lang = db.getLanguage(coun_name);


        // an image displayed in the country details display
        country_image= findViewById(R.id.show_country_image);
        img= db.getCountryImage(coun_name);
        country_image.setImageBitmap(img);


        //displaying the above retrieved data in text views
        displayCapital.setText(cap);
        displayCurrency.setText(cur);
        displayLanguages.setText(lang);



        //assigning city values retrieved from database to the city_List array
        try
        {
            city_row = db.getCities(coun_name);
            r1 =city_row.get(0);
        }
        catch(Exception e) { }

        StringTokenizer st2 = new StringTokenizer(r1);

        while(st2.hasMoreTokens())
        {
            String Desc = st2.nextToken();
            City_List.add(Desc);
        }


        //converting array list to string array to send the city name to the showCityDetails class
        cityArr = new String[City_List.size()];
        cityArr = City_List.toArray(cityArr);


        //list view for the cities
        listView = findViewById( R.id.list_view );

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1,  City_List );
        listView.setAdapter( adapter1 );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String TempListViewClickedValue = cityArr[position];


                Intent i = new Intent(DisplayMoreCountry.this, ShowCityDetails.class);
                i.putExtra("city_name",TempListViewClickedValue);
                startActivity(i);


            }
        });


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CountryMapsActivity.class);
                startActivity(i);
            }
        });

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayMoreCountry.this, AddNote.class).putExtra("country_name",coun_name));
            }
        });






    }
}
