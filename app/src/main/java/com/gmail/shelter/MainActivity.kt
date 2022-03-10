package com.gmail.shelter

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.CalendarContract.Attendees.query
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gmail.shelter.database.PetContract
import com.gmail.shelter.database.PetDbHelper
import com.gmail.shelter.database.PetContract.PetEntry
import com.gmail.shelter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private val mDbHelper = PetDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Create and/or open a database to read from it
        val db: SQLiteDatabase = mDbHelper.readableDatabase

        binding.fabAddPets.setOnClickListener{
            val intent = Intent(this, form :: class.java)
            startActivity(intent)
        }
    }



    override fun onStart() {
        super.onStart()
        displayDatabaseInfo()
    }

    /** Options Menu*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_catalog, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert_dummy_data -> {
                // yet to be implemented
                insertPet()
                // yet to be implemented
                displayDatabaseInfo()
                return true
            }
            R.id.action_delete_all_entries ->                 // Do nothing for now
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private fun displayDatabaseInfo() {
        // Create and/or open a database to read from it
        val db = mDbHelper.readableDatabase
        /** Define a projection that specifies which columns from the database
         you will actually use after this query. */
        val projection = arrayOf<String>(
            PetContract.PetEntry._ID,
            PetContract.PetEntry.COLUMN_PET_NAME,
            PetContract.PetEntry.COLUMN_PET_BREED,
            PetContract.PetEntry.COLUMN_PET_GENDER,
            PetContract.PetEntry.COLUMN_PET_WEIGHT)
        /** Perform a query on the pets table */

        /*
        val cursor = db.query(
            PetContract.PetEntry.TABLE_NAME,  // The table to query
            projection,          // The columns to return
            null,        // The columns for the WHERE clause
            null,     // The values for the WHERE clause
            null,        // Don't group the rows
            null,         // Don't filter by row groups
            null)        // The sort order
        */

        var cursor : Cursor? = contentResolver.query(
            PetEntry.CONTENT_URI,  // The content URI of the words table
            projection,  // The columns to return for each row
            null,  // Selection criteria
            null,  // Selection criteria
            null) // The sort order for the returned rows


        val displayView : TextView = findViewById(R.id.text_view_pet)
        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.text = """The pets table contains ${cursor?.count} pets.""" + "\n"
            displayView.append(PetContract.PetEntry._ID.toString() + " - "+
                    PetContract.PetEntry.COLUMN_PET_NAME + " - " +
                    PetContract.PetEntry.COLUMN_PET_BREED + " - " +
                    PetContract.PetEntry.COLUMN_PET_GENDER + " - " +
                    PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n")

            // Figure out the index of each column
            val idColumnIndex = cursor?.getColumnIndex(PetContract.PetEntry._ID)
            val nameColumnIndex = cursor?.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME)
            val breedColumnIndex = cursor?.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED)
            val genderColumnIndex = cursor?.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER)
            val weightColumnIndex = cursor?.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT)

            // Iterate through all the returned rows in the cursor
            while (cursor?.moveToNext() == true) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                val currentID = cursor?.getInt(idColumnIndex!!)
                val currentName = cursor?.getString(nameColumnIndex!!)
                val currentBreed = cursor?.getString(breedColumnIndex!!)
                val currentGender = cursor?.getInt(genderColumnIndex!!)
                val currentWeight = cursor?.getInt(weightColumnIndex!!)
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(""" $currentID - $currentName - $currentBreed - $currentGender - $currentWeight""")
                displayView.append("\n")
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor?.close()
        }
    }

    // inserting dummy pet in the database
    private fun insertPet() {

        val db: SQLiteDatabase = mDbHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, "Tommy")
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, "Pomeranian")
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE)
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 12)

        db.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues)
    }
}

