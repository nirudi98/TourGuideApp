package io.github.nearchos.favourite.Notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.github.nearchos.favourite.Database.NoteDatabase;
import io.github.nearchos.favourite.R;


public class Details extends AppCompatActivity {

    TextView content;
    NoteDatabase db;
    public Long id;
    NoteModel noteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteModel = new NoteModel();

        content = findViewById(R.id.detailsOfNote);


        //getting logged in user
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user_name =  sharedPreferences.getString("Name", "name");

        Intent i = getIntent();
        id = i.getLongExtra("ID",0);

        db= new NoteDatabase(this);

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

        noteModel = db.getNote(id,user_name);
        getSupportActionBar().setTitle(noteModel.getTitle());
        content.setText(noteModel.getContent());
        content.setMovementMethod(new ScrollingMovementMethod());



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              db.deleteNote(noteModel.getID(),noteModel.getUsername());
                Toast.makeText(Details.this, "note is deleted", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(getApplicationContext(),Note.class ));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.editNote)
        {
           Intent i = new Intent(this,Edit.class);
           i.putExtra("ID",id);
           startActivity(i);
        }

        return super.onOptionsItemSelected(item);

    }


}
