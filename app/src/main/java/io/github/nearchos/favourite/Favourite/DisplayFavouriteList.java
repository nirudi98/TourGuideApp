package io.github.nearchos.favourite.Favourite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.Database.CountryDatabase;
import io.github.nearchos.favourite.Home.Home;
import io.github.nearchos.favourite.R;

public class DisplayFavouriteList extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private CountryDatabase db;
    private List<CountryFavItem> countryfavItemList = new ArrayList<>();
    private FavAdapter favAdapter;

    String user_name;

    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

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

        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getting logged in username
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_name =  sharedPreferences.getString("Name", "name");

        home = findViewById(R.id.goToCountryList);

        // add item touch helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); // set swipe to recyclerview

        loadData();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showContent = new Intent(getApplicationContext(),
                        Home.class);
                startActivity(showContent);
            }
        });

    }

    private void loadData() {
        if (countryfavItemList != null) {
            countryfavItemList.clear();
        }
        SQLiteDatabase db1 = db.getReadableDatabase();
        Cursor cursor = db.select_all_favorite_list(user_name);
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(CountryDatabase.ITEM_TITLE));
                String id = cursor.getString(cursor.getColumnIndex(CountryDatabase.KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(CountryDatabase.ITEM_USER));
                CountryFavItem favItem = new CountryFavItem(title, id, name);
                countryfavItemList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db1.close();
        }

        favAdapter = new FavAdapter(this, countryfavItemList);

        recyclerView.setAdapter(favAdapter);

    }

    // remove item after swipe
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition(); // get position which is swipe
            final CountryFavItem favItem = countryfavItemList.get(position);
            if (direction == ItemTouchHelper.LEFT) { //if swipe left
                favAdapter.notifyItemRemoved(position); // item removed from recyclerview
                countryfavItemList.remove(position); //then remove item
                db.remove_fav(favItem.getKey_id(),favItem.getUsername()); // remove item from database
            }
        }
    };

}
