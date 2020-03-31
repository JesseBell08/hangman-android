package com.example.bell_hangma;

import android.provider.BaseColumns; // BaseColumns includes _ID field

/** Use contract class to define Strings for
 *  database, table names, columns names, etc. so
 *  they can be used elsewhere in the program
 *  (e.g. by open helper)
 */

//(shouldn't it be "final"? to prevent being extended)
public final class DBContract {

    // Prevent this class from being instantiated. (shouldn't it be "final"?)
    private DBContract () {}

    public final static String WORD_TABLE   = "word";

    public static class WordTable implements BaseColumns {
        public static final String TEXT = "text";

    }

}
