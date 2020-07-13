package io.github.nearchos.favourite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CountryDatabase extends SQLiteOpenHelper
{
    String DB_PATH = null;
    private static String DB_NAME = "TourGuide1";
    private SQLiteDatabase myDataBase;
    private final Context myContext;



    //favourite table
    public static String FAV_TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String ITEM_USER = "itemUser";
    public static String ITEM_TITLE = "itemTitle";
    public static String ITEM_IMAGE = "itemImage";
    public static String FAVORITE_STATUS = "fStatus";


    private static String CREATE_FAV_TABLE = "CREATE TABLE " + FAV_TABLE_NAME + "(" + KEY_ID + " TEXT," + ITEM_USER+ " TEXT," + ITEM_TITLE+ " TEXT," + ITEM_IMAGE + " TEXT," + FAVORITE_STATUS+" TEXT)";





    public CountryDatabase(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }


    // create empty table
    public void insertEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // enter your value
        for (int x = 1; x < 11; x++) {
            cv.put(KEY_ID, x);
            cv.put(FAVORITE_STATUS, "0");

            db.insert(FAV_TABLE_NAME,null, cv);
        }
    }

    // insert data into database
    public void insertIntoTheDatabase(String item_title,String id, String fav_status,String name)
    {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLE, item_title);
        cv.put(KEY_ID,id);
        cv.put(ITEM_USER,name);
        cv.put(FAVORITE_STATUS, fav_status);
        db.insert(FAV_TABLE_NAME,null, cv);
        Log.d("FavDB Status", item_title + ", favstatus - "+fav_status+" - . " + cv);
    }

    // read all data
    public Cursor read_all_data(String id,String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from favoriteTable where itemUser=? and id=?",new String[]{name,id});
    }


    // remove line from database
    public void remove_fav(String id,String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Update favoriteTable set fStatus='0' where itemUser=? and id=?",new String[]{name,id});
        Log.d("remove", id);
    }


    // select all favorite list
    public Cursor select_all_favorite_list(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select * from favoriteTable where itemUser=? and fStatus='1'",new String[]{name});
    }





    //reading country table image and name
    public Cursor getData(String query)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query,null);
    }


    //retrieving longitude to get country position in maps
    public String getLong(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select longitude from countryTable where countryName=?", new String[]{name});
        cursor.moveToFirst();
        String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
        cursor.close();
        return longitude;
    }


    //retrieving latitude to get country position in maps
    public String getLat(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select latitude from countryTable where countryName=?", new String[]{name});
        cursor.moveToFirst();
        String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
        cursor.close();
        return latitude;
    }


    //retrieving country capital city from country table
    public String getCapitalCity(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select capitalCity from countryTable where countryName=?", new String[]{name});
        cursor.moveToFirst();
        String capital = cursor.getString(cursor.getColumnIndex("capitalCity"));
        cursor.close();
        return capital;
    }


    //retrieving country currency from country table
    public String getCurrency(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select currency from countryTable where countryName=?", new String[]{name});
        cursor.moveToFirst();
        String currency = cursor.getString(cursor.getColumnIndex("currency"));
        cursor.close();
        return currency;
    }


    //retrieving country languages from country table
    public String getLanguage(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select language from countryTable where countryName=?", new String[]{name});
        cursor.moveToFirst();
        String lang = cursor.getString(cursor.getColumnIndex("language"));
        cursor.close();
        return lang;
    }


    //selecting country image from country table
    public Bitmap getCountryImage(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select countryImage from countryTable where countryName=?", new String[]{name});
        cursor.moveToFirst();
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("countryImage"));
        Bitmap bitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
        cursor.close();
        return  bitmap;
    }



    //retrieving country cities from country table
    public String getCity(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select cities from countryTable where countryName=?", new String[]{name});
        cursor.moveToFirst();
        String cities = cursor.getString(cursor.getColumnIndex("cities"));
        cursor.close();
        return cities;
    }


    //retrieving the cities from country table
    public ArrayList<String> getCities(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> Cities = new ArrayList<String>();

        try {
            Cursor cursor = db.rawQuery("Select cities from countryTable where countryName=?", new String[]{name});
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    Cities.add(cursor.getString(cursor.getColumnIndex("cities")));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
        }
        return Cities;
    }


    //selecting city image from city table
    public Bitmap getImage(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select image from cityTable where cityName=?", new String[]{name});
        cursor.moveToFirst();
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
        Bitmap bitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
        cursor.close();
        return  bitmap;
    }

    //selecting population from city table
    public String getPopulation(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select population from cityTable where cityName=?", new String[]{name});
        cursor.moveToFirst();
        String popu = cursor.getString(cursor.getColumnIndex("population"));
        cursor.close();
        return  popu;
    }


    //selecting country name from city table
    public String getCityCountryName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select countryName from cityTable where cityName=?", new String[]{name});
        cursor.moveToFirst();
        String coun = cursor.getString(cursor.getColumnIndex("countryName"));
        cursor.close();
        return  coun;
    }


    //selecting airport from city table
    public String getAirportNear(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select airport from cityTable where cityName=?", new String[]{name});
        cursor.moveToFirst();
        String airport = cursor.getString(cursor.getColumnIndex("airport"));
        cursor.close();
        return  airport;
    }








}
