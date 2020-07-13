package io.github.nearchos.favourite.Country;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.github.nearchos.favourite.City.AddCity;
import io.github.nearchos.favourite.Database.DatabaseHelper;
import io.github.nearchos.favourite.R;


public class Country_details extends AppCompatActivity
{
    EditText country_name,currency,country_capital,language,city,latitude,longitude;
    Button next,more_language,more_city,btnChooseImage,back,submit;
    ImageView country_image;


    DatabaseHelper db;
    Context context;


    final int REQUEST_CODE_GALLERY = 999;

    //array lists for storing cities and languages
    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> languageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country_details);


        db = new DatabaseHelper(this);

        //getting edit text data
        country_name =  findViewById(R.id.add_country_name);
        currency =  findViewById(R.id.add_country_currency);
        country_capital =  findViewById(R.id.add_country_capital);
        city =  findViewById(R.id.add_country_cities);
        language =  findViewById(R.id.add_country_languages);


        country_image = findViewById(R.id.add_country_image);
        longitude = findViewById(R.id.add_country_longitude);
        latitude = findViewById(R.id.add_country_latitude);

        //getting button data
        back = findViewById(R.id.back_button);
        next =  findViewById(R.id.next_button);
        submit =  findViewById(R.id.submit_country_details);

        //selecting more cities and languages
        more_city =  findViewById(R.id.more_city);
        more_language =  findViewById(R.id.more_lang);

        //selecting more images
        btnChooseImage= findViewById(R.id.add_images);


        //inserting images to image view by clicking on the button
        btnChooseImage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
               ActivityCompat.requestPermissions(Country_details.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });


        //inserting more cities to the text view
        more_city.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String a = city.getText().toString();
                if (a.length()!=0)
                {
                    cityList.add(a);
                    city.setText("");
                }
            }
        });


        //inserting more languages to the text view
        more_language.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String a = language.getText().toString().trim();
                if (a.length()!=0)
                {
                    languageList.add(a);
                    language.setText("");
                }
            }
        });


        //sending data to database
        submit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String name = country_name.getText().toString().trim();
                String curr = currency.getText().toString().trim();
                String capital = country_capital.getText().toString().trim();
                String lat = latitude.getText().toString();
                String lon = longitude.getText().toString();


                //converting city array list to string
                String result_ScoreP1 = ("" + Arrays.asList(cityList)).replaceAll("(^.|.$)", " ").replace("[", "").replace("]", "").replace(",", "");

                //converting language array list to string
                String result_ScoreP2 = ("" + Arrays.asList(languageList)).replaceAll("(^.|.$)", " ").replace("[", "").replace("]", "").replace(",", "");

                try
                {
                  //  db.addDetails(new CountryDetailsModel(name,curr,capital,result_ScoreP2,result_ScoreP1,myCountryImage));
                    db.insertCountryData(name,curr,capital,result_ScoreP2,result_ScoreP1,imageViewToByte(country_image),lat,lon);
                    Toast.makeText(getApplicationContext(),"Added successfully!",Toast.LENGTH_SHORT).show();


                    //emptying array lists
                    languageList.clear();
                    cityList.clear();

                    country_name.setText("");
                    currency.setText("");
                    country_capital.setText("");
                    language.setText("");
                    city.setText("");
                    latitude.setText("");
                    longitude.setText("");
                    country_image.setImageResource(R.mipmap.ic_launcher_round);

                }
                catch(Exception e)
                {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }


        });


        back.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), AddCity.class);
                startActivity(i);
            }
        });



    }

    public static  byte[] imageViewToByte(ImageView imageView)
    {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_CODE_GALLERY)
        { if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                country_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




}
