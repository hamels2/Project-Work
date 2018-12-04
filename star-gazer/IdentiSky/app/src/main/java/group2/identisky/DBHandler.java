package group2.identisky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mattF on 2016-04-02.
 */
public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "showsManager";


    // table name
    private static final String TABLE_OBJECTS = "sky objects";
    // table column names
    private static final String KEY_NAME = "name";
    private static final String KEY_RA = "rightascension";
    private static final String KEY_DEC = "declination";
    private static final String KEY_TYPE = "type";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db){
    	String CREATE_TABLE = "CREATE TABLE " + TABLE_OBJECTS + "(" + KEY_NAME
    			+ " TEXT PRIMARY KEY," + KEY_RA + " REAL," + KEY_DEC + " REAL," +
    			KEY_TYPE + " TEXT" + ")";
  		db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //CRUD operations (Create, Read, Update, Delete)

    //Adding new sky object
    void addSkyOBject(SkyObject skyObject){
    	SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, skyObject.getName()); //sky object name
    	values.put(KEY_RA, skyObject.getRA()); //sky object right ascension
    	values.put(KEY_DEC, skyObject.getDeclination()); //sky object declination
    	values.put(KEY_TYPE, skyObject.getType()); //sky object type

    	//inserting row
    	db.insert(TABLE_OBJECTS, null, values);
    	db.close(); //closing database connection
    }

    SkyObject getSkyObject(String name){
    	SQLiteDatabase db = this.getReadableDatabase();

    	Cursor cursor = db.query(TABLE_OBJECTS, new String[]{KEY_NAME,
    			KEY_RA, KEY_DEC, KEY_TYPE}, KEY_NAME + "=?",
    			new String[]{name}, null, null, null, null);
    	if (cursor != null)
    		cursor.moveToFirst();

    	SkyObject object = new SkyObject(cursor.getString(0),Double.parseDouble(cursor.getString(1)),
                Double.parseDouble(cursor.getString(2)),cursor.getString(3));
    	//return the sky object
    	return object;
    }


    public int updateSkyObject(SkyObject skyObject){
    	SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, skyObject.getName()); //sky object name
    	values.put(KEY_RA, skyObject.getRA()); //sky object right ascension
    	values.put(KEY_DEC, skyObject.getDeclination()); //sky object declination
    	values.put(KEY_TYPE, skyObject.getType()); //sky object type

    	//updating row
    	return db.update(TABLE_OBJECTS, values, KEY_NAME + " = ?",
    		new String[]{skyObject.getName()});
    }

    // Deleting single sky object
    public void deleteSkyObject(SkyObject skyObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBJECTS, KEY_NAME + " LIKE ?", new String[]{skyObject.getName()});
        db.close();
    }

    
    //get all stars
    public ArrayList<SkyObject> getAllStars(){
    	ArrayList<SkyObject> starList = new ArrayList<SkyObject>();
    	//Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBJECTS, new String[]{KEY_NAME,
    			KEY_RA, KEY_DEC, KEY_TYPE}, KEY_TYPE + "=?",
    			new String[]{"star"}, null, null, null, null);

    	//looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			SkyObject star = new SkyObject(cursor.getString(0),
    				Double.parseDouble(cursor.getString(1)),
    				Double.parseDouble(cursor.getString(2)),
    				cursor.getString(3));
    			starList.add(star);
    		} while (cursor.moveToNext());
    	}

    	return starList;
    }

    // Getting stars Count
    public int getStarsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBJECTS, new String[]{KEY_NAME,
                        KEY_RA, KEY_DEC, KEY_TYPE}, KEY_TYPE + "=?",
                new String[]{"star"}, null, null, null, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //get all constellations
    public ArrayList<SkyObject> getAllConstellations(){
    	ArrayList<SkyObject> constellationList = new ArrayList<SkyObject>();
    	//Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_OBJECTS, new String[]{KEY_NAME,
    			KEY_RA, KEY_DEC, KEY_TYPE}, KEY_TYPE + "=?",
    			new String[]{"constellation"}, null, null, null, null);

    	//looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			SkyObject constellation = new SkyObject(cursor.getString(0),
    				Double.parseDouble(cursor.getString(1)),
    				Double.parseDouble(cursor.getString(2)),
    				cursor.getString(3));
    			constellationList.add(constellation);
    		} while (cursor.moveToNext());
    	}

    	return constellationList;
    }

    // Getting constellations Count
    public int getConstellationsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBJECTS, new String[]{KEY_NAME,
    			KEY_RA, KEY_DEC, KEY_TYPE}, KEY_TYPE + "=?",
    			new String[]{"constellation"}, null, null, null, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}