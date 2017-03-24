package com.vishnuapp.restaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class RestaurantContract extends SQLiteOpenHelper {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
   // public RestaurantContract() {}

    /* Inner class that defines the user table contents */
    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    /* Inner class that defines the reservation table contents */
    public static abstract class Reservation implements BaseColumns {
        public static final String TABLE_NAME = "reservation";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_RESERVATION_DATE = "reservationDate";
        public static final String COLUMN_NAME_PARTY_SIZE = "partySize";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User._ID + " INTEGER PRIMARY KEY," +
                    User.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    User.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    User.COLUMN_NAME_PASSWORD + TEXT_TYPE + " ); ";

    private static final String SQL_CREATE_RESERVATION_TABLE =
            "CREATE TABLE " + Reservation.TABLE_NAME + " (" +
                    Reservation._ID + " INTEGER PRIMARY KEY," +
                    Reservation.COLUMN_NAME_USER_ID + " INTEGER " + COMMA_SEP +
                    Reservation.COLUMN_NAME_RESERVATION_DATE+ " DATETIME " + COMMA_SEP +
                    Reservation.COLUMN_NAME_PARTY_SIZE + " INTEGER" + " ); ";

    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    private static final String SQL_DELETE_RESERVATION =
            "DROP TABLE IF EXISTS " + Reservation.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Restaurant.db";

    public RestaurantContract(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_RESERVATION_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_RESERVATION);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}