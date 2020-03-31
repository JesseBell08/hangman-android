package com.example.bell_hangma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bell_hangma.DBContract;
import com.example.bell_hangma.DBHelper;
import com.example.bell_hangma.Word;


public class DDataSource {
    DBHelper helper;
    SQLiteDatabase dbOut;
    SQLiteDatabase dbIn;

    private static final String TAG = "DataSource";

    public DDataSource(Context context) {

        helper = new DBHelper(context);
        dbOut = helper.getWritableDatabase();
        dbIn = helper.getReadableDatabase();
    }

    public void clearWords() {
        dbOut.execSQL("DELETE FROM word");
        Log.d(TAG, "clearWords: was called");
    }

    //Create
    public Word addWord(Word word) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.WordTable._ID, word.getId());
        cv.put(DBContract.WordTable.TEXT, word.getText());
        Long rowId = dbOut.insert(DBContract.WORD_TABLE, null, cv);
        word.setId(rowId);
        return word;
    }

    //Read
    public Word findItemById(Long id) {
        Cursor c = dbIn.query(DBContract.WORD_TABLE, new String[]{"text"},
                DBContract.WordTable._ID + "=?",
                new String[]{id.toString()},
                null,
                null,
                null
        );
        c.moveToFirst();
        Word returnWord = new Word(c.getString(0));
        return returnWord;

    }
}


//TODO: Read ALL Words
//TODO: Update Word by ID
//TODO: Delete Word by ID

