package com.gmail.shelter.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
/**
 * Database helper for Pets app. Manages database creation and version management.
 */
class PetDbHelper (context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        /** Name of the database file  */
        private const val DATABASE_NAME = "shelter.db"
        /**
         * Database version. If you change the database schema, you must increment the database version.
         */
        private const val DATABASE_VERSION = 1
    }

    /**
     * This is called when the database is created for the first time.
     */
    override fun onCreate(db: SQLiteDatabase) {
        // Create a String that contains the SQL statement to create the pets table
        val SQL_CREATE_PETS_TABLE = ("CREATE TABLE "
                + PetContract.PetEntry.TABLE_NAME.toString() + " ("
                + PetContract.PetEntry._ID.toString() + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetContract.PetEntry.COLUMN_PET_NAME.toString() + " TEXT NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_BREED.toString() + " TEXT, "
                + PetContract.PetEntry.COLUMN_PET_GENDER.toString() + " INTEGER NOT NULL, "
                + PetContract.PetEntry.COLUMN_PET_WEIGHT.toString() + " INTEGER NOT NULL DEFAULT 0);")

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE)
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}