package io.github.nearchos.favourite.Login;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import io.github.nearchos.favourite.Database.RegisterDBHelper;
import io.github.nearchos.favourite.Home.Settings;
import io.github.nearchos.favourite.R;

public class Register extends AppCompatActivity
{
    RegisterDBHelper db;
    EditText username,email,password,confirm_password;
    Button register,back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //getting values from text boxes
        username= (EditText)findViewById(R.id.enter_register_username);
        email= (EditText)findViewById(R.id.enter_register_email);
        password= (EditText)findViewById(R.id.enter_register_password);
        confirm_password= (EditText)findViewById(R.id.enter_register_confirm);

        //button values
        register= (Button)findViewById(R.id.register);
        back= (Button)findViewById(R.id.back_button);


        //when register button is clicked
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                db = new RegisterDBHelper(Register.this);

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

                //converting them to string values
                String user = username.getText().toString();
                String em = email.getText().toString();
                String pass = password.getText().toString();
                String confirm = confirm_password.getText().toString();

                if (user.equals("") || em.equals("") || pass.equals("") || confirm.equals("")) {
                    Toast.makeText(getApplicationContext(), "fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(confirm)) {
                        boolean check_username = db.checkUsername(user);

                        if (check_username == true) {
                            boolean insert = db.insert(user, em, pass);
                            if (insert == true) {
                                Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Login.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "username already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "passwords do not match", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });


        back.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

    }

    public void login(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }

    public void help(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
    }

}


