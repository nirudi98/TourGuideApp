package io.github.nearchos.favourite.Country;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.Database.CountryDatabase;
import io.github.nearchos.favourite.Home.Home;
import io.github.nearchos.favourite.Home.Settings;
import io.github.nearchos.favourite.R;


public class CountryListDisplay extends AppCompatActivity
{
    RecyclerView recyclerView;
    ArrayList<ModelCountryList> list;


    String user_name;

    SharedPreferences sharedPreferences;

    //CountryListAdapter countryListAdapter = null;

    CountryAdapter countryAdapter = null;

    Toolbar toolbar;
    CountryDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaycountrylist_recycleview);

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


        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle);



        toolbar = findViewById(R.id.tool_bar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);



        sharedPreferences = this.getSharedPreferences("My_Pref", MODE_PRIVATE);

        //getting logged in username
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        user_name =  sharedPreferences1.getString("Name", "name");

        getData();



    }

    public void goToHome(View view)
    {
        Intent i = new Intent(getApplicationContext(), Home.class);
        startActivity(i);
    }

    public void goToSettings(View view)
    {
        Intent i = new Intent(getApplicationContext(), Settings.class);
        startActivity(i);
    }

    public void getData()
    {
        countryAdapter = new CountryAdapter(list,this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(countryAdapter);
        int x=0;

        //get country name and image from sqlite database
        Cursor cursor1 = db.getData("SELECT countryName,countryImage FROM countryTable");
        list.clear();
        if( cursor1.getCount()>0 )
        {
            cursor1.moveToPosition(-1);
            while (cursor1.moveToNext()) {
                String name = cursor1.getString(cursor1.getColumnIndex("countryName"));
                byte[] image = cursor1.getBlob(cursor1.getColumnIndex("countryImage"));
                String status = "0";

                list.add(new ModelCountryList(name,image,status,String.valueOf(x),user_name));
                x++;

            }
            cursor1.close();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"error!",Toast.LENGTH_SHORT).show();
        }

        String mSortSetting = sharedPreferences.getString("Sort","ascending");
        if(mSortSetting.equals("ascending"))
        {
            Collections.sort(list, ModelCountryList.BY_USERNAME_ASCENDING);
        }
        else if (mSortSetting.equals("descending"))
        {
            Collections.sort(list, ModelCountryList.BY_USERNAME_DESCENDING);
        }


        countryAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_sort,menu);

        //search view
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                countryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                countryAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int sort_id = item.getItemId();
        if(sort_id== R.id.sorting)
        {
            sortDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortDialog()
    {
            String[] options = {"Ascending", "Descending"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sort By");
        builder.setIcon(R.drawable.ic_action_sort);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which ==0)
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Sort", "ascending");
                    editor.apply();
                    getData();
                }
                if(which == 1)
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Sort", "descending");
                    editor.apply();
                    getData();
                }
            }
        });

        builder.create().show();


    }


}
