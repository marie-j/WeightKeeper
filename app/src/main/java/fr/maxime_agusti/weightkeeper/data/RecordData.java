package fr.maxime_agusti.weightkeeper.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.maxime_agusti.weightkeeper.entity.Record;
import fr.maxime_agusti.weightkeeper.database.MySQLHelper;

public class RecordData {
    
    private static final String LOG = "RecordData";
    
    public static final String TABLE_RECORD = "record";
    
    public static final String KEY_ID = "id";
    public static final String KEY_INSTANT = "instant";
    public static final String KEY_WEIGHT = "weight";

    private MySQLHelper helper;
    
    public RecordData(MySQLHelper helper) {
        this.helper = helper;
    }
    
    public Record fromCursor(Cursor c) {
 
        Record entry = new Record();
        
        entry.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        entry.setInstant(c.getString(c.getColumnIndex(KEY_INSTANT)));
        entry.setWeight(c.getDouble(c.getColumnIndex(KEY_WEIGHT)));
        
        return entry;
    }
    
    public ContentValues toContentValues(Record arg) {
    
        ContentValues values = new ContentValues();
        
        values.put(KEY_INSTANT, arg.getInstant());
        values.put(KEY_WEIGHT, arg.getWeight());
        
        return values;
    }
    
    public long createRecord(Record arg) {
        SQLiteDatabase db = helper.getWritableDatabase();
 
        ContentValues values = toContentValues(arg);
 
        return db.insert(TABLE_RECORD, null, values);
    }
    
    public List<Record> getAllRecordByQuery(String query) {
        
        List<Record> list = new ArrayList<Record>();
            
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
 
        if (c.moveToFirst()) {
            do {
                Record entry = fromCursor(c);
 
                // adding to todo list
                list.add(entry);
            } while (c.moveToNext());
        }

        c.close();
 
        return list;
    }
    
    public Record getRecordByQuery(String query) {
        
        SQLiteDatabase db = helper.getReadableDatabase();
        Log.e(LOG, query);
 
        Cursor c = db.rawQuery(query, null);
 
        if (c != null) {
            c.moveToFirst();
        }
        
        Record entry = fromCursor(c);

        c.close();
 
        return entry;
    }
    
    public Record getRecordById(long id) {

        String selectQuery = "SELECT  * FROM " + TABLE_RECORD + " WHERE "
            + KEY_ID + " = " + id
            ;

        return getRecordByQuery(selectQuery);
    }
    
    
    
    public List<Record> getAllRecord() {
    
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD;

        return getAllRecordByQuery(selectQuery);
    }
    
    public long updateRecordById(Record arg, long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
 
        ContentValues values = toContentValues(arg);
 
        // updating row
        return db.update(TABLE_RECORD, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    
    public void deleteRecordById(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_RECORD, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
}
