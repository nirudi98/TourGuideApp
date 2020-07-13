package io.github.nearchos.favourite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DatabaseHelper extends SQLiteOpenHelper
{

    //database name
    public static final String DATABASE_NAME = "TourGuide1";

    //table names
    public static final String USER_TABLE_NAME = "register";
    public static final String COUNTRY_TABLE_NAME = "countryTable";
    public static final String CITY_TABLE_NAME = "cityTable";

    //register table
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";


    //country table
    public static final String NAME = "countryName";
    public static final String CURRENCY = "currency";
    public static final String CAPITAL = "capitalCity";
    public static final String LANGUAGE = "language";
    public static final String CITY = "cities";
    public static final String COUNTRY_IMAGE= "countryImage";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    //city table
    public static final String CITY_NAME = "cityName";
    public static final String CITY_COUNTRY_NAME = "countryName";
    public static final String CITY_POPULATION = "population";
    public static final String CITY_AIRPORT = "airport";
    public static final String CITY_IMAGE = "image";





    Context context;


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 10);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable1 = "CREATE TABLE " + USER_TABLE_NAME + "(username text PRIMARY KEY , "+ "email text, " + "password text)";
        String createTable2 = "CREATE TABLE " + COUNTRY_TABLE_NAME + "(countryName  text PRIMARY KEY, " + " currency text, " + " capitalCity text, " + " language text, " + "cities text, " + "countryImage blob, " + "latitude text, " + "longitude text)";
        String createTable3 = "CREATE TABLE " + CITY_TABLE_NAME + "(cityName  text PRIMARY KEY, " + " countryName text, " + " population text, " + " airport text, " + " image blob)";



        db.execSQL(createTable1);
        db.execSQL(createTable2);
        db.execSQL(createTable3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE_NAME);

        onCreate(db);
    }



    //inserting country data to country table
    public void insertCountryData(String name, String curr,String cap,String lang,String city, byte[] image,String latitude,String longitude)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME,name);
        contentValues.put(CURRENCY, curr);
        contentValues.put(CAPITAL,cap);
        contentValues.put(LANGUAGE,lang);
        contentValues.put(CITY,city);
        contentValues.put(COUNTRY_IMAGE,image);
        contentValues.put(LATITUDE,latitude);
        contentValues.put(LONGITUDE,longitude);

        long insert = db.insert("countryTable", null, contentValues);
        if (insert != -1) {
            Toast.makeText(context, "data added successfully", Toast.LENGTH_SHORT).show();
            db.close();
        } else {
            Toast.makeText(context, "failed to add", Toast.LENGTH_SHORT).show();
        }
    }


    //reading country table image and name
    public Cursor getData(String query)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query,null);
    }


    //reading country name
    public Cursor getList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("Select " + NAME + " from " + COUNTRY_TABLE_NAME, null);
        return data;
    }



    //add details to city table
    public void insertCityData(String city, String country,String population,String airport, byte[] cityImage)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CITY_NAME,city);
        contentValues.put(CITY_COUNTRY_NAME, country);
        contentValues.put(CITY_POPULATION,population);
        contentValues.put(CITY_AIRPORT,airport);
        contentValues.put(CITY_IMAGE,cityImage);

        long insert = db.insert("cityTable", null, contentValues);
        if (insert != -1) {
            Toast.makeText(context, "data added successfully", Toast.LENGTH_SHORT).show();
            db.close();
        } else {
            Toast.makeText(context, "failed to add", Toast.LENGTH_SHORT).show();
        }
    }





}























