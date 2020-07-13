package io.github.nearchos.favourite.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.github.nearchos.favourite.Country.CountryListDisplay;
import io.github.nearchos.favourite.Favourite.DisplayFavouriteList;
import io.github.nearchos.favourite.Featured.FeaturedMain;
import io.github.nearchos.favourite.Notes.Note;
import io.github.nearchos.favourite.R;

public class Home extends NavigationDrawerActivity
{
    TextView nameDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home, frameLayout);

        nameDisplay = findViewById(R.id.txt_dashboard);

        //setting the logged in username
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user_name =  sharedPreferences.getString("Name", "name");

        nameDisplay.setText(user_name);
    }

     public void feature(View view)
    {
            Intent i = new Intent(getApplicationContext(), FeaturedMain.class);
            startActivity(i);
    }

    public void favorite(View view)
    {
        Intent i = new Intent(getApplicationContext(), DisplayFavouriteList.class);
        startActivity(i);
    }

    public void settings(View view)
    {
        Intent i = new Intent(getApplicationContext(), Settings.class);
        startActivity(i);
    }

    public void note(View view)
    {
        Intent i = new Intent(getApplicationContext(), Note.class);
        startActivity(i);
    }

    public void home(View view)
    {
        Intent i = new Intent(getApplicationContext(), CountryListDisplay.class);
        startActivity(i);
    }

    public void location(View view)
    {
        Toast.makeText(this,"please enable location!",Toast.LENGTH_SHORT).show();
    }

}

