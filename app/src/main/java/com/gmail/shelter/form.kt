package com.gmail.shelter

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gmail.shelter.database.PetContract
import com.gmail.shelter.database.PetDbHelper
import com.gmail.shelter.databinding.ActivityFormBinding

class form : AppCompatActivity() {
    /** EditText field to enter the pet's name  */
    private var mNameEditText: EditText? = null

    /** EditText field to enter the pet's breed  */
    private var mBreedEditText: EditText? = null

    /** EditText field to enter the pet's weight  */
    private var mWeightEditText: EditText? = null

    /** EditText field to enter the pet's gender  */
    private var mGenderSpinner: Spinner? = null

    /**
     * Gender of the pet. The possible valid values are in the PetContract.kt file:
     * [PetEntry.GENDER_UNKNOWN], [PetEntry.GENDER_MALE], or
     * [PetEntry.GENDER_FEMALE].
     */
    private var mGender: Int = PetContract.PetEntry.GENDER_UNKNOWN

    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find all relevant views that we will need to read user input from
        mNameEditText = binding.editPetName
        mBreedEditText = binding.editPetBreed
        mWeightEditText = binding.editPetWeight
        mGenderSpinner = binding.spinnerGender

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private fun setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        val genderSpinnerAdapter : ArrayAdapter<*> = ArrayAdapter.createFromResource(this,
        R.array.array_gender_options,
        android.R.layout.simple_spinner_item)

        // Specify dropdown layout style - simple list view with 1 item per line
        // Specify the layout to use when the list of choices appears
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Apply the adapter to the spinner
        mGenderSpinner!!.adapter = genderSpinnerAdapter

        // Set the integer mSelected to the constant values
        mGenderSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selection = parent.getItemAtPosition(position) as String
                // TextUtils.isEmpty(string) is same as string.isEmpty()
                // but TextUtils.isEmpty(String) always throws boolean when string is null
                // but string.isEmpty() throws null pointer exception when string is empty
                if (!TextUtils.isEmpty(selection)) {
                    mGender = when (selection) {
                        getString(R.string.gender_male) -> {
                            PetContract.PetEntry.GENDER_MALE
                        }
                        getString(R.string.gender_female) -> {
                            PetContract.PetEntry.GENDER_FEMALE
                        }
                        else -> {
                            PetContract.PetEntry.GENDER_UNKNOWN
                        }
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mGender = PetContract.PetEntry.GENDER_UNKNOWN
            }
        }
    }

    /**
     * Get user input from editor and save new pet into database.
     */
    private fun insertPet() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        val nameString: String = mNameEditText?.text.toString().trim { it <= ' ' }
        val breedString: String = mBreedEditText?.text.toString().trim { it <= ' ' }
        val weightString: String = mWeightEditText?.text.toString().trim { it <= ' ' }
        val weight = weightString.toInt()

        // Create database helper
        val mDbHelper = PetDbHelper(this)

        // Gets the database in write mode
        val db: SQLiteDatabase = mDbHelper.writableDatabase

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        val values = ContentValues()
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, nameString)
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, breedString)
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, mGender)
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, weight)

        // Insert a new row for pet in the database, returning the ID of that new row.
        val newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, values)

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1L) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show()
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Pet saved with row id: $newRowId", Toast.LENGTH_SHORT).show()
        }
    }

    // adding options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.form_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
            // Respond to a click on the "Save" menu option
            R.id.action_save -> {
                // Save pet to database
                insertPet()
                // Exit activity
                finish()
                return true
            }
            R.id.action_delete ->
                // Do nothing for now
                return true
        }
        return super.onOptionsItemSelected(item)
    }
}