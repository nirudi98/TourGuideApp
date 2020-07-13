package io.github.nearchos.favourite.Notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.nearchos.favourite.Database.NoteDatabase;
import io.github.nearchos.favourite.Home.Home;
import io.github.nearchos.favourite.Home.NavigationDrawerActivity;
import io.github.nearchos.favourite.R;


public class Note extends NavigationDrawerActivity
{

    RecyclerView recyclerView;
    Button back;

    Adapter adapter;
    List<NoteModel> notes;
    NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.note_activity_note);
        getLayoutInflater().inflate(R.layout.note_activity_note, frameLayout);

        //button to go back to home
        back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
            }
        });



        //getting logged in username
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user_name =  sharedPreferences.getString("Name", "name");

        db = new NoteDatabase(this);

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

        notes = db.getNotes(user_name);

        recyclerView = findViewById(R.id.listOfNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,notes);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add){
            Toast.makeText(this,"please select country name before adding a note",Toast.LENGTH_SHORT).show();

        }
        if(item.getItemId() == R.id.back){
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);

    }
}
