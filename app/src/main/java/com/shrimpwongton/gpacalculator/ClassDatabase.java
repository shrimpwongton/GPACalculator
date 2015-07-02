package com.shrimpwongton.gpacalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonywong on 7/1/15.
 */
public class ClassDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=3;
    private static final String DATABASE_NAME="classManager";
    private static final String TABLE_CLASS = "class";

    private static final String KEY_ID = "id";
    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_CLASS = "class";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_GRADE = "grade";

    public ClassDatabase(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLASS_TABLE = "CREATE TABLE " + TABLE_CLASS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PARENT_ID + " INTEGER,"
                + KEY_CLASS + " TEXT," + KEY_UNIT + " REAL" + KEY_GRADE + " REAL" + ")";
        db.execSQL(CREATE_CLASS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        onCreate(db);
    }

    public void addClass(Class cla) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PARENT_ID, cla.getParentID());
        values.put(KEY_CLASS, cla.getClassName());
        values.put(KEY_UNIT, cla.getUnits());
        values.put(KEY_GRADE, cla.getGrade());
        db.insert(TABLE_CLASS, null, values);
        db.close();
    }

    public Class getClass(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLASS, new String [] {
                        KEY_ID,KEY_PARENT_ID,KEY_CLASS,KEY_UNIT,KEY_GRADE}, KEY_ID + "=?",
                new String [] { String.valueOf(id) }, null, null, null, null);
        if ( cursor != null )
            cursor.moveToFirst();
        Class cla = new Class(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4));
        cursor.close();
        return cla;
    }

    public List<Class> getAllClassesWithParentID(int id) {
        List<Class> classList = new ArrayList<Class>();
        String selectQuery = "SELECT  * FROM " + TABLE_CLASS + " WHERE parent_id=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if ( cursor.moveToFirst()) {
            do{
                Class cla = new Class();
                cla.setId(Integer.parseInt(cursor.getString(0)));
                cla.setParentID(Integer.parseInt(cursor.getString(1)));
                cla.setClassName(cursor.getString(2));
                cla.setUnits(cursor.getDouble(3));
                cla.setGrade(cursor.getDouble(4));
                classList.add(cla);
            }  while(cursor.moveToNext());
        }
        return classList;

    }

    public List<Class> getAllClasses() {
        List<Class> classList = new ArrayList<Class>();
        String selectQuery = "SELECT  * FROM " + TABLE_CLASS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if ( cursor.moveToFirst()) {
            do{
                Class cla = new Class();
                cla.setId(Integer.parseInt(cursor.getString(0)));
                cla.setParentID(Integer.parseInt(cursor.getString(1)));
                cla.setClassName(cursor.getString(2));
                cla.setUnits(cursor.getDouble(3));
                cla.setGrade(cursor.getDouble(4));
                classList.add(cla);
            }  while(cursor.moveToNext());
        }
        return classList;
    }

    public int getClassCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CLASS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int updateClass (Class cla) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PARENT_ID, cla.getParentID());
        values.put(KEY_CLASS, cla.getClassName());
        values.put(KEY_UNIT, cla.getUnits());
        values.put(KEY_GRADE, cla.getGrade());
        return db.update(TABLE_CLASS, values, KEY_ID + " =?",
                new String[] { String.valueOf(cla.getId())});
    }

    public void deleteClass (Term term) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLASS, KEY_ID + " = ?",
                new String[]{String.valueOf(term.getId())});
        db.close();
    }
}
