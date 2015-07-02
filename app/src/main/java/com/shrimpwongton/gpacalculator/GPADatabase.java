package com.shrimpwongton.gpacalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonywong on 6/30/15.
 */
public class GPADatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=3;
    private static final String DATABASE_NAME= "termManager";
    private static final String TABLE_TERM = "term";

    private static final String KEY_ID = "id";
    private static final String KEY_TERM = "term";
    private static final String KEY_GPA = "gpa";

    public GPADatabase (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GPA_TABLE = "CREATE TABLE " + TABLE_TERM + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TERM + " TEXT,"
                + KEY_GPA + " REAL" + ")";
        db.execSQL(CREATE_GPA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM);
        onCreate(db);
    }

    public void addTerm(Term term) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TERM, term.getTerm());
        values.put(KEY_GPA, term.getGPA());
        db.insert(TABLE_TERM, null, values);
        db.close();
    }

    public Term getTerm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TERM, new String [] {
            KEY_ID,KEY_TERM,KEY_GPA}, KEY_ID + "=?",
            new String [] { String.valueOf(id) }, null, null, null, null);
        if ( cursor != null )
            cursor.moveToFirst();
        Term term = new Term(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getDouble(2));
        cursor.close();
        return term;
    }

    public List<Term> getAllTerms() {
        List<Term> termList = new ArrayList<Term>();
        String selectQuery = "SELECT  * FROM " + TABLE_TERM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if ( cursor.moveToFirst()) {
            do{
                Term term = new Term();
                term.setId(Integer.parseInt(cursor.getString(0)));
                term.setTerm(cursor.getString(1));
                term.setGPA(cursor.getDouble(2));
                termList.add(term);
            }  while(cursor.moveToNext());
        }
        return termList;
    }

    public int getTermCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TERM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int updateTerm (Term term) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TERM, term.getTerm());
        values.put(KEY_GPA, term.getGPA());
        return db.update(TABLE_TERM, values, KEY_ID + " =?",
                new String[] { String.valueOf(term.getId())});
    }

    public void deleteTerm (Term term) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TERM, KEY_ID + " = ?",
                new String[] { String.valueOf(term.getId())});
        db.close();
    }
}
