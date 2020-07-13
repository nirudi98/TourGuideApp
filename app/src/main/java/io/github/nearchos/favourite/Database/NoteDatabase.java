package io.github.nearchos.favourite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.nearchos.favourite.Notes.NoteModel;

public class NoteDatabase extends SQLiteOpenHelper
{

    String DB_PATH = null;
    private static String DB_NAME = "TourGuide1";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    //note table
    public static final String NOTE_TABLE_NAME = "noteTable";
    public static final String NOTE_ID = "noteID";
    public static final String NOTE_USER = "noteUser";
    public static final String NOTE_COUNTRY = "countryName";
    public static final String NOTE_TITLE = "noteTitle";
    public static final String NOTE = "noteContent";
    public static final String DATE = "date";
    public static final String TIME = "time";




    public NoteDatabase(Context context) {
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

        String createTable6 = "CREATE TABLE " + NOTE_TABLE_NAME + "(noteID INTEGER PRIMARY KEY AUTOINCREMENT, " + "noteUser text, " + " countryName text, " + "noteTitle text, " + "noteContent text, " + "date text, " + "time text)";
        db.execSQL(createTable6);
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



    //note table stuff
    public long addNote(NoteModel noteModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COUNTRY,noteModel.getCountry());
        contentValues.put(NOTE_USER,noteModel.getUsername());
        contentValues.put(NOTE_TITLE, noteModel.getTitle());
        contentValues.put(NOTE, noteModel.getContent());
        contentValues.put(DATE, noteModel.getDate());
        contentValues.put(TIME, noteModel.getTime());

        long ID = db.insert(NOTE_TABLE_NAME, null, contentValues);
        Log.d("inserted ", "ID -> " + ID);
        return ID;
        //if successfully inserted will return 1
        //if not will return -1
    }

    public NoteModel getNote(long id, String name) {
        //select * from noteTable where id=1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NOTE_TABLE_NAME, new String[]{NOTE_ID,NOTE_USER,NOTE_COUNTRY, NOTE_TITLE,NOTE,DATE,TIME}, NOTE_ID + "=? AND " + NOTE_USER + "=?", new String[]{String.valueOf(id),String.valueOf(name)}, null, null, null);

        if (cursor.getCount()>0)
            cursor.moveToFirst();
        NoteModel noteModel = new NoteModel(cursor.getLong(0), cursor.getString(3), cursor.getString(4), cursor.getString(2),cursor.getString(5),cursor.getString(6),cursor.getString(1));
        return noteModel;

    }

    public List<NoteModel> getNotes(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<NoteModel> allNotes = new ArrayList<>();
        //select * from database


        Cursor cursor= db.rawQuery("Select * from noteTable where noteUser=?",new String[]{name});


        // String query = "SELECT * FROM " + NOTE_TABLE_NAME+" WHERE " +NOTE_USER+"=?" ORDER BY "+NOTE_ID+" DESC";
        //   Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setID(cursor.getLong(0));
                noteModel.setTitle(cursor.getString(3));
                noteModel.setCountry(cursor.getString(2));
                noteModel.setContent(cursor.getString(4));
                noteModel.setDate(cursor.getString(5));
                noteModel.setTime(cursor.getString(6));

                allNotes.add(noteModel);

            } while (cursor.moveToNext());
        }
        return allNotes;

    }

    public void deleteNote(long id,String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE_NAME,NOTE_ID+"=?" +" AND "+ NOTE_USER+"=?",new String[]{String.valueOf(id),String.valueOf(name)});
        db.close();
    }

    public int editNote(NoteModel noteModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.d("edited","edited title ->"+noteModel.getTitle() + "\n ID -> "+noteModel.getID());
        contentValues.put(NOTE_TITLE,noteModel.getTitle());
        contentValues.put(NOTE,noteModel.getContent());
        contentValues.put(DATE,noteModel.getDate());
        contentValues.put(TIME,noteModel.getTime());
        contentValues.put(NOTE_USER,noteModel.getUsername());
        return db.update(NOTE_TABLE_NAME,contentValues,NOTE_ID+"=?" + "AND " + NOTE_USER+"=?",new String[]{String.valueOf(noteModel.getID()),String.valueOf(noteModel.getUsername())});

    }




}





