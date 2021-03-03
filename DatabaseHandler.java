package com.example.noteapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.noteapp.Model.ToDoModel;
import com.example.noteapp.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String LOGIN_TABLE = "login_table";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";


    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "+EMAIL+ " TEXT, "
            + STATUS + " INTEGER)";

    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL + " TEXT, "
            + PASSWORD + " TEXT)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public long insertUserData(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, user.getEmail());
        values.put(PASSWORD, user.getPassword());
        // Inserting Row
        long value=db.insert(LOGIN_TABLE, null, values);
        db.close();
        return value;
    }
    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = EMAIL + " = ?" + " AND " + PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'test@abc.com';
         */
        Cursor cursor = db.query(LOGIN_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkUniqueEmail(String email) {
        // array of columns to fetch
        String[] columns = {
                ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = EMAIL + " = ?" ;
        // selection arguments
        String[] selectionArgs = {email};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'test@abc.com' AND user_password = '123456';
         */
        Cursor cursor = db.query(LOGIN_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public void insertTask(ToDoModel task){
        Log.e("Cursor value","Cursor value"+task.getEmail());
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        cv.put(EMAIL, task.getEmail());
        db.insert(TODO_TABLE, null, cv);
    }

    public List<ToDoModel> getAllTasks(String email){
        List<ToDoModel> taskList = new ArrayList<>();

        Cursor cursor = null;

        // array of columns to fetch
        String[] columns = {
                ID,TASK,STATUS
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = EMAIL + " = ?" ;
        // selection arguments
        String[] selectionArgs = {email};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'test@abc.com';
         */



        db.beginTransaction();
        try{
            cursor = db.query(TODO_TABLE, //Table to query
                    columns,                    //columns to return
                    selection,                  //columns for the WHERE clause
                    selectionArgs,              //The values for the WHERE clause
                    null,                       //group the rows
                    null,                       //filter by row groups
                    null);
            int cursorCount = cursor.getCount();
            Log.e("Cursor value","Cursor value"+cursorCount );
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));

                        taskList.add(task);
                    }
                    while(cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
