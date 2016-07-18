package com.example.admin.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/6/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";


    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public  DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder CREATE_CONTACTS_TABLE = new StringBuilder(100);
        CREATE_CONTACTS_TABLE.append("CREATE TABLE ").append(TABLE_CONTACTS).append("( ")
                .append(KEY_ID).append(" INTEGER PRIMARY KEY, ")
                .append(KEY_NAME).append(" TEXT, ")
                .append(KEY_PH_NO).append(" TEXT )");

        db.execSQL(CREATE_CONTACTS_TABLE.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop tables if they exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        //Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addContact(Contact c){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, c.getName());
        values.put(KEY_PH_NO, c.getPhone_number());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_NAME, KEY_PH_NO }, KEY_ID + "=+?",
            new String[] {String.valueOf(id)}, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Contact contact = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        return contact;
    }

    public List<Contact> getAllContact(){
        List<Contact> contacts = new ArrayList<>();

        StringBuilder selectQuery = new StringBuilder(30);
        selectQuery.append("SELECT * FROM ").append(TABLE_CONTACTS);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery.toString(), null);

        if(c.moveToFirst()){
            do{
                Contact contact = new Contact(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(1)
                );
                contacts.add(contact);
            }while (c.moveToNext());
        }
        return contacts;
    }

    public int getContactsCount(){
        String query = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();

        return cursor.getCount();
    }

    public int updateContact(Contact c){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, c.getName());
        values.put(KEY_PH_NO, c.getPhone_number());
        values.put(KEY_ID, c.getId());

        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] {String.valueOf(c.getId())});
    }

    public void deleteContact(Contact c){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ? ", new String[] {String.valueOf(c.getId())});
        db.close();
    }
}
