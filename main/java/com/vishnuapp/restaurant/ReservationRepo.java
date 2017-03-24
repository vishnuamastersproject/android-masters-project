package com.vishnuapp.restaurant;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class ReservationRepo {

    static long reservationId;
    Activity activity;
    SQLiteDatabase db;
    RestaurantContract restaurantContract;

    public ReservationRepo(Activity activity) {
        this.activity = activity;
        restaurantContract = new RestaurantContract(activity);

        // Gets the data repository in write mode
        db = restaurantContract.getWritableDatabase();
    }

    public boolean save(long userId, String reservationDate, int partySize) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RestaurantContract.Reservation.COLUMN_NAME_USER_ID, userId);
        values.put(RestaurantContract.Reservation.COLUMN_NAME_RESERVATION_DATE, reservationDate);
        values.put(RestaurantContract.Reservation.COLUMN_NAME_PARTY_SIZE, partySize);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                RestaurantContract.Reservation.TABLE_NAME,
                null,
                values);

        if (newRowId > 0) {
            return true;
        }
        return false;
    }


    public Cursor reservationsByUserId(long userId) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                RestaurantContract.Reservation._ID
                , RestaurantContract.Reservation.COLUMN_NAME_USER_ID
                , RestaurantContract.Reservation.COLUMN_NAME_RESERVATION_DATE
                , RestaurantContract.Reservation.COLUMN_NAME_PARTY_SIZE
        };

        String whereClause = RestaurantContract.Reservation.COLUMN_NAME_USER_ID + " = ? ";
        String[] whereArgs = new String[]{
                userId+""
        };
        // How you want the results sorted in the resulting Cursor
        //String sortOrder =        RestaurantContract.User.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = db.query(
                RestaurantContract.Reservation.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(cursor.getCount() > 0){
            return cursor;
        }
        return null;

    }
}
