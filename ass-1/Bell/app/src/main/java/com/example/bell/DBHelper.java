package com.example.bell;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "words_db";
    public final static Integer VERISON = 1;


    public DBHelper(@Nullable Context context){
        super(context, DB_NAME, null, VERISON);
    }


    private static String CREATE_WORD_TABLE =
            "CREATE TABLE " + DBContract.WORD_TABLE + "(" +
                    DBContract.WordTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContract.WordTable.TEXT + " TEXT )";

    private static final String DELETE_WORD_TABLE =
            "DROP TABLE IF EXISTS " + DBContract.WORD_TABLE;

    @Override   // Called automatically when database does not already exist
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORD_TABLE);

    }

    @Override   // Called automatically when database version increases
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_WORD_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
    }
}
