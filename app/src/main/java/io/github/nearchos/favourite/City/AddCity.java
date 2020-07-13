package io.github.nearchos.favourite.City;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.github.nearchos.favourite.Database.DatabaseHelper;
import io.github.nearchos.favourite.Login.Login;
import io.github.nearchos.favourite.R;


public class AddCity extends AppCompatActivity
{
    EditText country,city,population,airport;
    ImageView city_image;
    Button more_image,submit_details,next;

    DatabaseHelper db;




    final int REQUEST_CODE_GALLERY = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_details);

        db = new DatabaseHelper(this);

        //finding edit text from activity
        country = findViewById(R.id.add_country_name);
        city = findViewById(R.id.add_city_name);
        population = findViewById(R.id.add_city_population);
        airport = findViewById(R.id.add_city_airport);
        city_image = findViewById(R.id.add_city_image);

        //button for more images, submitting city details, next button
        more_image = findViewById(R.id.add_images);
        submit_details= findViewById(R.id.submit_city_details);
        next= findViewById(R.id.next_button);


        //clicking on button to select images
        more_image.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                ActivityCompat.requestPermissions(AddCity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });


        //directing to next activity to display city details
        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });


        //submitting the city details to the database
        submit_details.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String cityName = city.getText().toString().trim();
                String countryName = country.getText().toString().trim();
                String pop = population.getText().toString().trim();
                String air = airport.getText().toString();

                try
                {
                    db.insertCityData(cityName,countryName,pop,air,imageViewToByte(city_image));
                    Toast.makeText(getApplicationContext(),"Added successfully!",Toast.LENGTH_SHORT).show();

                    country.setText("");
                    city.setText("");
                    population.setText("");
                    airport.setText("");
                    city_image.setImageResource(R.mipmap.ic_launcher_round);

                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

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
                city_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }





    //choosing image from internal storage, method is addressed as onclick through image and add button
    /*
    public void chooseImage(View view)
    {
        try
        {
           Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
           photoPickerIntent.setType("image/*");
           startActivityForResult(photoPickerIntent,SELECT_PHOTO);
           city_image.setImageBitmap(myImage);

        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

     */


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        try
        {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
            switch (requestCode)
            {
                case SELECT_PHOTO:
                    if(resultCode == RESULT_OK)
                    {
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        myImage = BitmapFactory.decodeStream(imageStream);
                    }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    */


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK && requestCode == SELECT_PHOTO)
        {
            Uri uri = imageReturnedIntent.getData();
            String x = getPath(uri);
            Toast.makeText(getApplicationContext(),x,Toast.LENGTH_SHORT).show();
        }
    }


    public String getPath(Uri uri)
    {
        if(uri ==null)return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,projection,null,null,null);
        if(cursor!= null)
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }


     */
    /*
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
    {
        BitmapFactory.Options o= new BitmapFactory.Options();
        o.inJustDecodeBounds=true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage),null,o);

        final int REQUIRED_SIZE = 140;

        int width_tmp= o.outWidth, height_tmp = o.outHeight;
        int scale =1;
        while(true)
        {
            if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 <REQUIRED_SIZE)
            {
                break;
            }
            width_tmp /=2;
            height_tmp /=2;
            scale *=2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage),null,o2);
    }
    */



    /*
    public void storeImage(View view)
    {
        try
        {
            db.storeImage(new ModelCity(country.getText().toString(),city.getText().toString(),population.getText().toString(),airport.getText().toString(),myImage));
        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
     */

}


