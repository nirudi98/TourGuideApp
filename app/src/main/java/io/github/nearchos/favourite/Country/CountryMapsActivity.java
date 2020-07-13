package io.github.nearchos.favourite.Country;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import androidx.fragment.app.FragmentActivity;
import io.github.nearchos.favourite.Database.CountryDatabase;
import io.github.nearchos.favourite.R;

public class CountryMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //text view to display country name
    TextView tvUser;

    //click to display more country details
    Button more_details;

    //click to go back to the main country list display
    Button back;

    //country name string
    String coun_name;

    CountryDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_map_display);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

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


        tvUser= findViewById(R.id.selected_user);
        more_details = findViewById(R.id.more_details);
        back = findViewById(R.id.back_button);



        //setting the country name from country adapter
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        coun_name =  sharedPreferences.getString("country_name", "name");

        tvUser.setText(coun_name);



        more_details.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),DisplayMoreCountry.class);
                startActivity(i);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CountryListDisplay.class);
                startActivity(i);
            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;


        String log= db.getLong(coun_name);
        double n1= Double.parseDouble(log);

        String lat= db.getLat(coun_name);
        double n2 = Double.parseDouble(lat);




        // Add a marker in Sydney and move the camera
        LatLng place = new LatLng(n2, n1);
        mMap.addMarker(new MarkerOptions().position(place).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
    }
}
