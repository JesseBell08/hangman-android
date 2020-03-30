package com.example.bell;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class WordListAdapter extends ArrayAdapter {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    //Textview to be created as needed in the listview
    static class ViewHolder {
        TextView word;
    }

    public WordListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Word> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }


}
