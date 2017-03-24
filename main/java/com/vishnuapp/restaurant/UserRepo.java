package com.vishnuapp.restaurant;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserRepo {

    static String username;
    static long userId;
    Activity activity;
    SQLiteDatabase db;
    RestaurantContract restaurantContract;

    public UserRepo(Activity activity) {
        this.activity = activity;
        restaurantContract = new RestaurantContract(activity);

        // Gets the data repository in write mode
        db = restaurantContract.getWritableDatabase();
    }

    public boolean save(String name, String email, String password) {


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RestaurantContract.User.COLUMN_NAME_EMAIL, email);
        values.put(RestaurantContract.User.COLUMN_NAME_NAME, name);
        values.put(RestaurantContract.User.COLUMN_NAME_PASSWORD, password);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                RestaurantContract.User.TABLE_NAME,
                null,
                values);

        if (newRowId > 0) {
            username = name;
            userId=newRowId;
            return true;
        }
        return false;
    }

    public boolean authenticate(String email, String password) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                RestaurantContract.User._ID
                , RestaurantContract.User.COLUMN_NAME_NAME
                //, RestaurantContract.User.COLUMN_NAME_EMAIL
                //, RestaurantContract.User.COLUMN_NAME_PASSWORD
        };

        String whereClause = RestaurantContract.User.COLUMN_NAME_EMAIL + " = ? AND " + RestaurantContract.User.COLUMN_NAME_PASSWORD + " = ?";
        String[] whereArgs = new String[]{
                email,
                password
        };
        // How you want the results sorted in the resulting Cursor
        //String sortOrder =        RestaurantContract.User.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = db.query(
                RestaurantContract.User.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(cursor.getCount() > 0){

        cursor.moveToFirst();
        userId = cursor.getLong(
                cursor.getColumnIndexOrThrow(RestaurantContract.User._ID)
        );
        username = cursor.getString(
                cursor.getColumnIndexOrThrow(RestaurantContract.User.COLUMN_NAME_NAME)
        );
        return true;
            /**/
        }
        return false;

    }
}
