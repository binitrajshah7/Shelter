package com.gmail.shelter.database
import android.net.Uri
import android.provider.BaseColumns

class PetContract{

    object PetEntry : BaseColumns {
        /**
         * The "Content authority" is a name for the entire content provider, similar to the
         * relationship between a domain name and its website.  A convenient string to use for the
         * content authority is the package name for the app, which is guaranteed to be unique on the
         * device.
         */
         val CONTENT_AUTHORITY : String = "com.example.android.pets"

        /**
         * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
         * the content provider.
         */
         val BASE_CONTENT_URI : Uri = Uri.parse("content://$CONTENT_AUTHORITY")

        /**
         * Possible path (appended to base content URI for possible URI's)
         * For instance, content://com.example.android.pets/pets/ is a valid path for
         * looking at pet data. content://com.example.android.pets/staff/ will fail,
         * as the ContentProvider hasn't been given any information on what to do with "staff".
         */
        // If only PATH_PETS used in URI then whole pets table will be fetched
        // if uri + PATH_PETS/id used in URI then particular row in pets table will be fetched
        val PATH_PETS: String = "pets"

        /** The content URI to access the pet data in the provider */
        val CONTENT_URI : Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS)

        /** Name of database table for pets  */
        const val TABLE_NAME = "pets"

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        const val _ID = BaseColumns._ID

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        const val COLUMN_PET_NAME = "name"

        /**
         * Breed of the pet.
         *
         * Type: TEXT
         */
        const val COLUMN_PET_BREED = "breed"

        /**
         * Gender of the pet.
         *
         * The only possible values are [.GENDER_UNKNOWN], [.GENDER_MALE],
         * or [.GENDER_FEMALE].
         *
         * Type: INTEGER
         */
        const val COLUMN_PET_GENDER = "gender"

        /**
         * Weight of the pet.
         *
         * Type: INTEGER
         */
        const val COLUMN_PET_WEIGHT = "weight"

        /**
         * Possible values for the gender of the pet.
         */
        const val GENDER_UNKNOWN = 0
        const val GENDER_MALE = 1
        const val GENDER_FEMALE = 2
    }
}