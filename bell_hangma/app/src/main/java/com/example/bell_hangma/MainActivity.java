package com.example.bell_hangma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import javax.sql.DataSource;

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
    EditText focus;
    TextView tv,tvGuesses;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");

        focus = findViewById(R.id.editText);

        iv = findViewById(R.id.ivHangman);
        tv = findViewById(R.id.tvWord);
        tvGuesses = findViewById(R.id.tvGuessedLetters);

        //TODO: Load database from file on SD

        DDataSource dataSource = new DDataSource(this);
        //Add some data to the database
        dataSource.addWord(new Word("College"));
        dataSource.addWord(new Word("Android"));
        dataSource.addWord(new Word("Computer"));
        dataSource.addWord(new Word("Vehicle"));
        dataSource.addWord(new Word("Inheritance "));
        dataSource.addWord(new Word("Object"));
        dataSource.addWord(new Word("Method"));
        dataSource.addWord(new Word("Java"));
        dataSource.addWord(new Word("Apple"));

        //Pick a random word
        int randomSelector = new Random().nextInt(9);


///////////// Setting up Game //////////////////////////
        String randomWord = dataSource.findItemById((long) randomSelector).getText();

        char[] wordAsArray = randomWord.toCharArray();
        Log.i(TAG, "onCreate: WORDASARRAY" + wordAsArray[1]);
        char[] letterOnScreen = new char[randomWord.length()];

        for(int i=0;i> wordAsArray.length-1;i++){
            letterOnScreen[i] = '_';
        }

        String wordOnScreenWithSpaces = "";
        for (int i=0; i<randomWord.length()-1; i++){
            wordOnScreenWithSpaces +=  letterOnScreen[i];
            Log.i(TAG, "onCreate: LETTERS" + letterOnScreen[i]);
        }

        tv.setText(wordOnScreenWithSpaces);

        String backToAWord = new String(alphabet);
        tvGuesses.setText(backToAWord.toUpperCase());
//////////////////////////////////////////////////////////

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
                char letter = (char) event.getUnicodeChar();
                Toast.makeText(getApplicationContext(),letter+"",Toast.LENGTH_SHORT).show();
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

    public void onClickNext (View view) {

        if (step < imageResources.length -1 ) {
            step++;
            int imageResourceId = imageResources[step];
            iv.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, imageResourceId));
        }
        if (step == imageResources.length -1){
            Toast.makeText(this,"YOU LOSE",Toast.LENGTH_SHORT).show();
        }
    }


    //TODO: Add 2000ms delay to end of game,
    //TODO: record wins and losses with the Shared Preferences

}