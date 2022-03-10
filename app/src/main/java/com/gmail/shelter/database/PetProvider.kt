package com.gmail.shelter.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.gmail.shelter.database.PetContract.PetEntry.CONTENT_AUTHORITY
import java.lang.IllegalArgumentException

public class PetProvider : ContentProvider() {
    /** URI matcher code for the content URI for the pets table
     * If provided URI matches the added URI in sURIMatcher then
     * sURI matcher will return PETS / PET_ID*/
    private val PETS = 100
    /** URI matcher code for the content URI for a single pet in the pets table */
    private val PET_ID = 101
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    val sUriMatcher : UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    /** Database helper object */
    lateinit var mDbHelper : PetDbHelper
    /**
     * Initialize the provider and the database helper object.
     */
    override fun onCreate(): Boolean {
            // The calls to addURI() go here, for all of the content URI patterns that the provider
            // should recognize. All paths added to the UriMatcher have a corresponding code to return
            // when a match is found.

            // The content URI of the form "content://com.example.android.pets/pets" will map to the
            // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
            // of the pets table.
            sUriMatcher.addURI(PetContract.PetEntry.CONTENT_AUTHORITY, PetContract.PetEntry.PATH_PETS, PETS);

            // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
            // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
            // of the pets table.
            //
            // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
            // For example, "content://com.example.android.pets/pets/3" matches, but
            // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
            sUriMatcher.addURI(PetContract.PetEntry.CONTENT_AUTHORITY, PetContract.PetEntry.PATH_PETS + "/#", PET_ID);

        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        val mDbHelper = PetDbHelper(context)
        return true
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {

        // get readable database
        val database = mDbHelper.readableDatabase
        // This cursor will hold the result of the query
        val cursor : Cursor
        // Figure out if the URI matcher can match the URI to a specific code
        val matchId = sUriMatcher.match(uri)

        when(matchId){
            PETS -> {
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            PET_ID ->{
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.

                val selectionInURI = PetContract.PetEntry._ID + "=?"
                val selectionArgsInURI  =  arrayOf(ContentUris.parseId(uri).toString())

                cursor = database.query(PetContract.PetEntry.TABLE_NAME, projection, selectionInURI,
                    selectionArgsInURI, null, null, sortOrder)
            }
            else->{
                throw IllegalArgumentException("Cannot Query Unknown URI$uri")
            }
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {

        val matchId = sUriMatcher.match(uri)

        when(matchId){
            PETS -> {
                val database = mDbHelper.writableDatabase
                database.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues)

            }
            else -> {
                throw IllegalArgumentException("Insertion is not supported for $uri")
            }
        }
        val database = mDbHelper.readableDatabase
        database.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues)
        context?.contentResolver?.notifyChange(uri, null)
        // returns uri to symbolize that insertion was done
        return uri
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    override fun update(uri: Uri, contentValues: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}