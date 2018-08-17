package com.example.priyankam.dynamicform.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.priyankam.dynamicform.Configuration;

public class MainDatabase {

    public static final String TABLE_USER_MASTER = "UserMasterTable";
    public static final String TABLE_USER_DYNAMIC_FORM = "userDynamicFormTable";
    public static final String DYNAMIC_FORM = "DynamicForm";
    public static final String DYNAMIC_FORM_DATA = "DynamicFormData";
    public static final String _ID = "_id";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    private static MainDatabase mainDatabase;
    protected int oldVersion = 0;
    protected int newVersion = 1;
    MyHelper mh;
    SQLiteDatabase sdb;

    public MainDatabase(Context con) {
        mh = new MyHelper(con, "DynamicForm.db", null, newVersion);
    }

    public static MainDatabase getInstance(Context context) {
        if (mainDatabase == null) {
            mainDatabase = new MainDatabase(context);
        }
        return mainDatabase;
    }

    public void open() {
        try {
            sdb = mh.getWritableDatabase();
        } catch (SQLiteCantOpenDatabaseException e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/open/SQLiteCantOpenDatabaseException");
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/open/Exception");
        }
    }

    public void close() {
        try {
            sdb.close();
        } catch (SQLiteException e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/close/SQLiteException");
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/close/Exception");
        }
    }

    public SQLiteDatabase getMyHelper() {
        return sdb;
    }


    public void insertUserMaster(String jUserID, String jUserName, String jUserEmail) {

        try {
            ContentValues cv = new ContentValues();

            cv.put(USER_ID, jUserID);
            cv.put(USER_NAME, jUserName);
            cv.put(USER_EMAIL, jUserEmail);


            sdb.insert(TABLE_USER_MASTER, null, cv);

        } catch (SQLiteException e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/insertUserMaster/SQLiteException");
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/insertUserMaster/Exception");
        }
    }

    public void deleteUserMaster() {
        try {
            sdb.delete(TABLE_USER_MASTER, null, null);
        } catch (SQLiteException e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/deleteUserMaster/SQLiteException");
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/deleteUserMaster/Exception");
        }
    }

    public Cursor getUserMaster() {
        Cursor c = null;
        try {
            c = sdb.query(TABLE_USER_MASTER, null, null, null, null, null, null);
        } catch (SQLiteException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserMaster/SQLiteException" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserMaster/SQLiteException");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserMaster/SQLiteException" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserMaster/SQLiteException");
            }
        }
        return c;
    }

    public void insertUserDynamicForm(String jUserID, String jDynamicForm, String jDynamicFormData) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(USER_ID, jUserID);
            cv.put(DYNAMIC_FORM, jDynamicForm);
            cv.put(DYNAMIC_FORM_DATA, jDynamicFormData);

            sdb.insert(TABLE_USER_DYNAMIC_FORM, null, cv);

        } catch (SQLiteException e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/insertUserDynamicForm/SQLiteException");
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/insertUserDynamicForm/Exception");
        }
    }

    public void deleteUserDynamicForm() {
        try {
            sdb.delete(TABLE_USER_DYNAMIC_FORM, null, null);
        } catch (SQLiteException e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/deleteUserDynamicForm/SQLiteException");
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/deleteUserDynamicForm/Exception");
        }
    }

    public void deleteUserDynamicForm(String jUserID) {
        try {
            sdb.delete(TABLE_USER_DYNAMIC_FORM, USER_ID + "=?", new String[]{"" + jUserID});
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/deleteUserDynamicForm/Exception");
        }
    }

    public Cursor getUserDynamicForm() {
        Cursor c = null;
        try {
            c = sdb.query(TABLE_USER_DYNAMIC_FORM, null, null, null, null, null, null);
        } catch (SQLiteException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserDynamicForm/SQLiteException" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserDynamicForm/SQLiteException");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserDynamicForm/SQLiteException" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getUserDynamicForm/SQLiteException");
            }
        }
        return c;
    }

    public Cursor getUserDynamicForm(String jUserID) {
        Cursor c = null;
        try {
            //read data query from database
            String query = "SELECT * FROM " + TABLE_USER_DYNAMIC_FORM + " WHERE " + USER_ID + " = '" + jUserID + "';";
            c = sdb.rawQuery(query, null);
        } catch (Exception e) {
            if (e != null)
                Log.d(Configuration.TAG_LOG, "MainDatabase/getUserDynamicForm/Exception");
        }

        return c;
    }

    public String getDynamicFormData(String sUserID) {
        String dynFormdata = "";
        try {
            Cursor c = sdb.query(TABLE_USER_DYNAMIC_FORM, new String[]{DYNAMIC_FORM_DATA}, USER_ID + "=?", new String[]{String.valueOf(sUserID)}, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                dynFormdata = c.getString(c.getColumnIndex(DYNAMIC_FORM_DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getSyncFormData/SQLiteException" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainDatabase/getSyncFormData/SQLiteException");
            }
        }

        Log.d("dynFormdata", DYNAMIC_FORM_DATA + "=" + dynFormdata);
        return dynFormdata;
    }


    public int updateUserDynamicForm(String jUserID, String dynamicFormData) {

        int isUpdated = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(MainDatabase.DYNAMIC_FORM_DATA, dynamicFormData);
            isUpdated = sdb.update(MainDatabase.TABLE_USER_DYNAMIC_FORM, cv, MainDatabase.USER_ID + " = '" + jUserID + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;

    }


    private class MyHelper extends SQLiteOpenHelper {


        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,

                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                String createUserMasterTable = "CREATE TABLE " + TABLE_USER_MASTER +
                        "(" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        USER_ID + " TEXT," + // Define a  key
                        USER_NAME + " TEXT," +
                        USER_EMAIL + " TEXT" +
                        ");";


                String createUserDynamicFormTable = "CREATE TABLE " + TABLE_USER_DYNAMIC_FORM +
                        "(" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        USER_ID + " TEXT," + // Define a  key
                        DYNAMIC_FORM + " TEXT," +
                        DYNAMIC_FORM_DATA + " TEXT" +
                        ");";


                db.execSQL(createUserMasterTable);
                db.execSQL(createUserDynamicFormTable);


            } catch (SQLiteException e) {
                if (e != null)
                    Log.d(Configuration.TAG_LOG, "MainDatabase/onCreate/SQLiteException");
            } catch (Exception e) {
                if (e != null)
                    Log.d(Configuration.TAG_LOG, "MainDatabase/onCreate/Exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int _oldVersion, int _newVersion) {

            switch (_newVersion) {

                case 0:
                    break;

                default:
                    throw new IllegalStateException(
                            "onUpgrade() with unknown newVersion" + _newVersion);

            }
        }
    }
}
