package io.github.nearchos.favourite.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import io.github.nearchos.favourite.Database.RegisterDBHelper;
import io.github.nearchos.favourite.Home.Home;
import io.github.nearchos.favourite.R;

public class Login extends AppCompatActivity
{
        RegisterDBHelper db;
        EditText login_user,login_password;
        Button login;


@Override
protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_user= (EditText)findViewById(R.id.enter_login_username);
        login_password= (EditText)findViewById(R.id.enter_login_password);
        login= (Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                db= new RegisterDBHelper(Login.this);

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


                String username_login = login_user.getText().toString();
                String password_login = login_password.getText().toString();


                //adding logged in username to shared Preferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Name",username_login);
                editor.apply();

                Boolean check_usernamepassword = db.usernamePassword(username_login,password_login);
                if(check_usernamepassword==true)
                {
                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                }
                else
                    {
                        Toast.makeText(getApplicationContext(), "incorrect username or password", Toast.LENGTH_SHORT).show();
                        login_user.getText().clear();
                        login_password.getText().clear();
                    }


            }



        });

        }

        public void signup(View view)
        {
            Intent i = new Intent(getApplicationContext(), Register.class);
            startActivity(i);
        }


}

