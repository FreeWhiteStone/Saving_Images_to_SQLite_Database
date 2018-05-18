package com.example.free.grad;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {

    private static final String TAG = Database.class.getSimpleName();


    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "plants_database";

    private static final String TABLE_NAME = "plant_list";
    private static final String TEMP_PICTURE_TABLE = "compare_table";
    private static String PLANT_NAME = "plant_name";
    private static String PLANT_ID = "id";
    private static String PLANT_COLOR = "color";
    private static String IRRIGATION_PERIOD = "irrigation_period";
    private static String IRRIGATION_AMOUNT = "irrigation_amount";
    private static String PLANT_PICTURE = "picture";
    private static String SAVE_DATE ="time_stamp";
    private static String FIRST_DATE ="first_date";
    private static String TEMP_PICTURE = "temp_picture";


    private int addTime = 0;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + PLANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLANT_NAME + " TEXT,"
                + PLANT_COLOR + " TEXT,"
                + IRRIGATION_PERIOD + " TEXT,"
                + IRRIGATION_AMOUNT + " TEXT,"
                + PLANT_PICTURE + " TEXT,"
                + FIRST_DATE + " DATETIME DEFAULT CURRENT_DATE,"
                + SAVE_DATE + " DATETIME DEFAULT CURRENT_DATE " +")";

        String CREATE_TEMP_PICTURE_TABLE = "CREATE TABLE " + TEMP_PICTURE_TABLE + "("
                + PLANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TEMP_PICTURE + " TEXT " +")";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TEMP_PICTURE_TABLE);

        Log.d(TAG, "Database Created Successfully" );
    }


    public void deletePlant(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PLANT_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void addPlant(String plant_name, String plant_color, String irrigation_period, String irrigation_amount, String image) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLANT_NAME, plant_name);
        values.put(PLANT_COLOR, plant_color);
        values.put(IRRIGATION_PERIOD, irrigation_period);
        addTime=Integer.parseInt(irrigation_period);
        values.put(IRRIGATION_AMOUNT, irrigation_amount);
        values.put(PLANT_PICTURE,image);
        Date date = new Date();
        values.put(SAVE_DATE,getDateTime(date,addTime));
        values.put(FIRST_DATE,getDateTime(date,0));

        Log.d("datee",getDateTime(date, addTime));

        long photoid = db.insert(TABLE_NAME, null, values);

        if(photoid!=-1) Log.d("fotoid", String.valueOf(photoid));
        else Log.d("FOTO HATA","kaydedilemedi");
        db.close();
    }


    public HashMap<String, String> plantDetail(int id){

        HashMap<String,String> plant = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            plant.put(PLANT_NAME, cursor.getString(1));
            plant.put(PLANT_COLOR, cursor.getString(2));
            plant.put(IRRIGATION_PERIOD, cursor.getString(3));
            plant.put(IRRIGATION_AMOUNT, cursor.getString(4));
            plant.put(PLANT_PICTURE, cursor.getString(5));

        }
        cursor.close();
        db.close();
        return plant;
    }

    public  ArrayList<HashMap<String, String>> plants(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> plantlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                plantlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();

        return plantlist;
    }

    public HashMap<String, String> getProperImage(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT picture FROM " + TABLE_NAME +" ORDER BY " +SAVE_DATE+ " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        HashMap<String, String> getImage = new HashMap<String, String>();
        if (cursor.moveToFirst()) {
            getImage.put("picture",cursor.getString(0));

        }
        db.close();

        return getImage;
    }


    public HashMap<String, String> getProperDate(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT time_stamp FROM " + TABLE_NAME +" ORDER BY " +SAVE_DATE+ " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        HashMap<String, String> getDate = new HashMap<String, String>();
        if (cursor.moveToFirst()) {
            getDate.put("time_stamp",cursor.getString(0));

        }
        db.close();

        return getDate;
    }

    public void editPlant(String plant_name, String plant_color,String irrigation_period,String irrigation_amount,int id) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLANT_NAME, plant_name);
        values.put(PLANT_COLOR, plant_color);
        values.put(IRRIGATION_PERIOD, irrigation_period);
        addTime=Integer.parseInt(irrigation_period);
        values.put(IRRIGATION_AMOUNT, irrigation_amount);

        HashMap<String,String> plant = new HashMap<String,String>();
        String selectQuery = "SELECT first_date FROM " + TABLE_NAME+ " WHERE id="+id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            plant.put(FIRST_DATE, cursor.getString(0));

        }
        Log.d("formaatt",cursor.getString(0));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(cursor.getString(0));
        values.put(SAVE_DATE,getDateTime(date, addTime));

        db.update(TABLE_NAME, values, PLANT_ID + " = ?",
                new String[] { String.valueOf(id) });

        cursor.close();
        db.close();

    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }


    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    private String getDateTime(Date date, int addition) {
        String dateInString = date.toString();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, addition);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        System.out.println("String date:"+dateInString);
        return dateInString;
    }

    public void addTempPicture(String tempPicture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEMP_PICTURE,tempPicture);
        db.insert(TEMP_PICTURE_TABLE, null, values);
        db.close();

    }

}
