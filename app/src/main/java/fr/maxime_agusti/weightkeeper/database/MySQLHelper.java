package fr.maxime_agusti.weightkeeper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLHelper extends SQLiteOpenHelper {
    
    private static final int DATABASE_VERSION = 1;
    
    public static final String DATABASE_NAME = "database";

    // Table names
    private static final String TABLE_RECORD = "record";

    // Common column names
    private static final String KEY_ID = "id";
    
    
    // Table record columns
    private static final String KEY_INSTANT = "instant";
    private static final String KEY_WEIGHT = "weight";
    // record table create statement
    private static final String CREATE_TABLE_RECORD = 
        "CREATE TABLE " + TABLE_RECORD + " ("
    +    KEY_ID + " INTEGER, "
    +    KEY_INSTANT + " TEXT, "
    +    KEY_WEIGHT + " REAL, "
    
    +   "PRIMARY KEY(id) "
    +   ")";
    
    
    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        super.onOpen(db);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_RECORD);
    }
    
     @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
 
        // create new tables
        onCreate(db);
    }
    
    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    
}

