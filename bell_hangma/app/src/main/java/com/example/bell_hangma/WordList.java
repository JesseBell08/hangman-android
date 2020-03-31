package com.example.bell_hangma;

import java.util.ArrayList;

public class WordList {
    private static ArrayList<Word> words = new ArrayList<>();
    static{
        words.add( new Word("Word1"));
        words.add( new Word("Word2"));
        words.add( new Word("Word3"));
        words.add( new Word("Word4"));
        words.add( new Word("Word5"));
        words.add( new Word("Word6"));
        words.add( new Word("Word7"));
        words.add( new Word("Word8"));
        words.add( new Word("Word9"));
        words.add( new Word("Word10"));
        words.add( new Word("Word11"));
        words.add( new Word("Word12"));
        words.add( new Word("Word13"));
    }

    public static ArrayList<Word> getWords() {
        return words;
    }
}
