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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.github.nearchos.favourite.Database.NoteDatabase;
import io.github.nearchos.favourite.R;


public class AddNote extends AppCompatActivity
{
    EditText title, details;
    TextView country;
    Toolbar toolbar;
    Calendar calendar;
    String todayDate, todayTime;

    String user_name;


    NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity_add);

        toolbar = findViewById(R.id.tool1);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("country_name");

        //setting the logged in username
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_name =  sharedPreferences.getString("Name", "name");


        title = findViewById(R.id.titleId);
        details = findViewById(R.id.noteDetails);
        country = findViewById(R.id.noteCountry);

        country.setText(message);

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

    //for calendar stuff
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
        if(item.getItemId() == R.id.save){
          NoteModel note = new NoteModel(title.getText().toString(),details.getText().toString(),country.getText().toString(),todayDate,todayTime,user_name);
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

            db.addNote(note);
            Toast.makeText(this, "note saved", Toast.LENGTH_SHORT).show();
          onBackPressed();
      //    goToMain();  //go to main to see added ones
        }
        if(item.getItemId()==R.id.delete)
        {
            Toast.makeText(this, "note deleted", Toast.LENGTH_SHORT).show();
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
