package com.example.bell_hangma;

/**
 * This is an android hangman game, uses shared preferences on the device to keep the score
 * This program filters keyboard input to only accept letters, and will convert to lowercase
 * a reveal word button has been added for testing. at the end of each game, there is a 2 second delay, the score is saved and then the main activity restarts.
 * Author: Jesse Bell
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    char[] alphabet = {'a','b','c','d','e','f','g','h','i',
            'j','k','l','m','n','o','p','q','r',
            's','t','u','v','w','x','y','z'};

    int[]  imageResources = {
            R.drawable.h,
            R.drawable.h0,
            R.drawable.h1,
            R.drawable.h2,
            R.drawable.h3,
            R.drawable.h4,
            R.drawable.h5,
            R.drawable.h6,
            R.drawable.h7
    };
    int step = 0;

    ImageView iv;
    TextView focus, wins, loses,tv,tvGuesses;
    String reveal;

    private static final String TAG = "MainActivity";
    SharedPreferences preferences;
    private int winCount;
    private int loseCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");

        focus = findViewById(R.id.tvMsg);
        wins = findViewById(R.id.tvScoreWins);
        loses = findViewById(R.id.tvScoreLoses);
        iv = findViewById(R.id.ivHangman);
        tv = findViewById(R.id.tvWord);
        tvGuesses = findViewById(R.id.tvGuessedLetters);


        preferences = this.getSharedPreferences("win",MODE_PRIVATE);
        preferences = this.getSharedPreferences("loses",MODE_PRIVATE);
        winCount = preferences.getInt("win",0);
        wins.setText("Wins = " + winCount);
        loseCount = preferences.getInt("loses",0);
        loses.setText("Loses = " + loseCount);

        DDataSource dataSource = new DDataSource(this);

        InputStream myInputStream = null;
        Scanner in = null;
        String aWord = "";

        try {
            myInputStream = getAssets().open("database_file.txt");
            in = new Scanner(myInputStream);
            while(in.hasNext()){
                aWord = in.next();
                dataSource.addWord(new Word(aWord));
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getClass().getSimpleName() + ": " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } finally {
            if(in != null) {
                in.close();
            }
            try {
                myInputStream.close();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this,
                        e.getClass().getSimpleName() + ": " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
///////////////////////////


        //gets a random word
        int randomSelector = new Random().nextInt(200);
        final String randomWord = dataSource.findItemById((long) randomSelector).getText().toLowerCase();
        reveal = randomWord;
        Log.i(TAG, "randomWord: " + randomWord);

///// Setting up Game ////////////

       //take the random word and replace/represent it with '-' then put on the screen
        final char[] filler = new char[randomWord.length()];
        int i = 0;
        while (i < randomWord.length()) {
            filler[i] = '-';
            if (randomWord.charAt(i) == ' ') {      //to handle spaces in the case of multi words.
                filler[i] = ' ';
            }
            i++;
        }
        //rebuild a word with the char array and display in on the screen
        String outputWord = new String(filler);
        tv.setText(outputWord);

        final ArrayList<Character> l = new ArrayList<Character>();      //arraylist to hold letters pressed.

        ArrayList<Character> guessedLetters = new ArrayList<Character>();

//////////////////////////////////


        focus.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 0;
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                return false;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                char letter = Character.toLowerCase((char) event.getUnicodeChar());

                if (step < imageResources.length -1 ) {
                    //compare char to word. handle (if its already pressed, correct or wrong)
                    if(!l.contains(letter) && contains(letter,alphabet)){
                        if(randomWord.contains(letter + "")) {
                            for (int y = 0; y < randomWord.length(); y++) {
                                if (randomWord.charAt(y) == letter) {           // if characters match
                                    filler[y] = letter; // change that index pos value from dash to character guessed.
                                }
                            }
                            String outputWord = new String(filler);
                            tv.setText(outputWord);
                            focus.setText("Correct!");
                            if (randomWord.equals(String.valueOf(filler))){
                                Toast.makeText(getApplicationContext(), "YOU WIN!", Toast.LENGTH_SHORT).show();
                                win();
                             }
                        }
                        else{
                            step++;
                            focus.setText("Wrong letter, body part added!!!");
                        }
                        l.add(letter);
                        Collections.sort(l);
                        StringBuilder sb = new StringBuilder();
                        for(char i:l) {
                            sb.append(i);
                        }
                        tvGuesses.setText(sb.toString());
                    }else{
                    focus.setText("Try another letter");
                    }

                    int imageResourceId = imageResources[step];
                    iv.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, imageResourceId));
                }
                if (step == imageResources.length -1){      //check if there is any more body parts left/guesses remaining, if there is not then do this...
                    Toast.makeText(getApplicationContext(),"YOU LOSE!",Toast.LENGTH_SHORT).show();
                    lose();
                }
                return true;
            }

            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });

    }

    void win(){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    winCount++;
                    wins.setText("Wins = " + winCount);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("win",winCount);                      //record loss in the Shared Preferences
                    editor.commit();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }   //Add 2000ms delay to end of game, (freeze/ignore user input, then close and restart the activity)
            }, 2000);
    }
    void lose(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loseCount++;
                loses.setText("Loses = " + loseCount);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("loses",loseCount);                   //record loss in the Shared Preferences
                editor.commit();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }   //Add 2000ms delay to end of game, (freeze/ignore user input, then close and restart the activity)
        }, 2000);
    }

    boolean contains(char c, char[] array) {
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    public void onClickReveal (View view) {

        focus.setText(reveal);
    }
    public void onClickResetScore (View view) {

        preferences.edit().clear().commit();
        winCount = preferences.getInt("win",0);
        wins.setText("Wins = " + winCount);
        loseCount = preferences.getInt("loses",0);
        loses.setText("Loses = " + loseCount);

    }


}