package com.aaratechnologies.lawtrend.menuwiseactivities;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Belal on 10/20/2017.
 */

//The class is extending SQLiteOpenHelper
public class DatabaseManager extends SQLiteOpenHelper {

    /*
     * This time we will not be using the hardcoded string values
     * Instead here we are defining all the Strings that is required for our database
     * for example databasename, table name and column names.
     * */
    private static final String DATABASE_NAME = "BookmarkedNews";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Bookmark";
    private static final String COLUMN_ID = "id";
    private static final String POST_ID = "post_id";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String IMAGEURL = "image_url";
    private static final String TIME = "time";

    /*
     * We need to call the super i.e. parent class constructur
     * And we need to pass 4 parameters
     * 1. Context context -> It is the context object we will get it from the activity while creating the instance
     * 2. String databasename -> It is the name of the database and here we are passing the constant that we already defined
     * 3. CursorFactory cursorFactory -> If we want a cursor to be initialized on the creation we can use cursor factory, it is optionall and that is why we passed null here
     * 4. int version -> It is an int defining our database version
     * */
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * The query to create our table
         * It is same as we had in the previous post
         * The only difference here is we have changed the
         * hardcoded string values with String Variables
         * */

        String sql = "CREATE TABLE " + TABLE_NAME + " (\n" +
                "    " + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + POST_ID + " varchar(200) NOT NULL,\n" +
                "    " + TITLE + " text NOT NULL,\n" +
                "    " + CONTENT + " text NOT NULL,\n" +
                "    " + IMAGEURL + " text NOT NULL,\n" +
                "    " + TIME + " varchar(200) NOT NULL\n" +
                ");";
        /*
         * Executing the string to create the table
         * */
        sqLiteDatabase.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String id, String title, String content, String imageurl, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, id);
        contentValues.put(TITLE, title);
        contentValues.put(CONTENT, content);
        contentValues.put(IMAGEURL, imageurl);
        contentValues.put(TIME, time);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    /*
     * READ OPERATION
     * =================
     * Here we are reading values from the database
     * First we called the getReadableDatabase() method it will return us the SQLiteDatabase instance
     * but using it we can only perform the read operations.
     *
     * We are running rawQuery() method by passing the select query.
     * rawQuery takes two parameters
     * 1. The query
     * 2. String[] -> Arguments that is to be binded -> We use it when we have a where clause in our query to bind the where value
     *
     * rawQuery returns a Cursor object having all the data fetched from database
     * */
   Cursor getAllData() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
   public boolean deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }
}