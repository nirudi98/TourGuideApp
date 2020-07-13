package io.github.nearchos.favourite.Notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.github.nearchos.favourite.Database.NoteDatabase;
import io.github.nearchos.favourite.R;


public class Edit extends AppCompatActivity {

    EditText title, details;
    Toolbar toolbar;
    Calendar calendar;
    String todayDate;
    String todayTime;
    NoteModel noteModel;

    NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_edit);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user_name =  sharedPreferences.getString("Name", "name");

        Intent i = getIntent();
        Long id = i.getLongExtra("ID",0);

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

        noteModel = db.getNote(id,user_name);




        toolbar = findViewById(R.id.tool1);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(noteModel.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.titleId);
        details = findViewById(R.id.noteDetails);

        title.setText(noteModel.getTitle());
        details.setText(noteModel.getContent());

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //getting the current date and time
        calendar = Calendar.getInstance();
        todayDate = calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH);
        todayTime = pad(calendar.get(Calendar.HOUR))+":"+pad(calendar.get(Calendar.MINUTE));

        Log.d("calendar","Date and time :"+todayDate+" and" +todayTime);
    }

    private String pad(int i)
    {
        if(i<10)
        {
            return "0"+i;
        }
        return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save)
        {
            if(title.getText().length()!=0)
            {
                noteModel.setTitle(title.getText().toString());
                noteModel.setContent(details.getText().toString());
                noteModel.setDate(todayDate);
                noteModel.setTime(todayTime);
                int id = db.editNote(noteModel); //if successfully updated will return the id of the current note
                if(id == noteModel.getID())
                {
                    Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show();
                }
            }
            Intent i = new Intent(getApplicationContext(),Details.class);
            i.putExtra("ID",noteModel.getID());
            startActivity(i);
        }
        else
        {
            title.setError("title can't be blanked");
        }
        if(item.getItemId()==R.id.delete)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }

    private void goToMain()
    {
        Intent i = new Intent(this,Note.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
