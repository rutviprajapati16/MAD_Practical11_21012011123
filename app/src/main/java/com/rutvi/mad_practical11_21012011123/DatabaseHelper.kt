package com.rutvi.mad_practical11_21012011123

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "persons_db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(PersonTable.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS" + PersonTable.TABLE_NAME)
        onCreate(db)
    }

    fun insertPerson(person: Person): Long {
        // get writable database as we want to write data
        val db = this.writableDatabase
        // insert row
        val id = db.insert(PersonTable.TABLE_NAME, null, getValues(person))
        // close db connection
        db.close()
        // return newly inserted row id
        return id
    }

    private fun getValues(person: Person): ContentValues {
        val values = ContentValues()
        // `id` will be inserted automatically.
        // no need to add them
        values.put(PersonTable.COLUMN_ID, person.id)
        values.put(PersonTable.COLUMN_NAME, person.name)
        values.put(PersonTable.COLUMN_EMAIL, person.emailId)
        values.put(PersonTable.COLUMN_PHONE, person.phoneNo)
        values.put(PersonTable.COLUMN_GPS_LAT, person.latitude)
        values.put(PersonTable.COLUMN_ADDRESS, person.address)
        values.put(PersonTable.COLUMN_GPS_LONG, person.longitude)
        return values
    }

    fun getPerson(id: String): Person? {
        // get readable database as we are not inserting anything
        val db = this.readableDatabase
        val cursor = db.query(
            PersonTable.TABLE_NAME,
            arrayOf(
                PersonTable.COLUMN_ID,
                PersonTable.COLUMN_NAME,
                PersonTable.COLUMN_EMAIL,
                PersonTable.COLUMN_PHONE,
                PersonTable.COLUMN_ADDRESS,
                PersonTable.COLUMN_GPS_LAT,
                PersonTable.COLUMN_GPS_LONG
            ),
            PersonTable.COLUMN_ID + "=?",
            arrayOf(id),
            null,
            null,
            null,
            null
        ) ?: return null
        cursor.moveToFirst()
        if (cursor.count == 0)
            return null
        val person = getPerson(cursor)
        // close the db connection
        cursor.close()
        return person
    }

    private fun getPerson(cursor: Cursor): Person {
        return Person(
            cursor.getString(cursor.getColumnIndexOrThrow(PersonTable.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonTable.COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonTable.COLUMN_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonTable.COLUMN_PHONE)),
            cursor.getString(cursor.getColumnIndexOrThrow(PersonTable.COLUMN_ADDRESS)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(PersonTable.COLUMN_GPS_LAT)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(PersonTable.COLUMN_GPS_LONG))
        )
    }

    // Select All Query
    val allPersons: ArrayList<Person>
        get() {
            val persons = ArrayList<Person>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + PersonTable.TABLE_NAME + " ORDER BY " +
                    PersonTable.COLUMN_NAME + " DESC"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    persons.add(getPerson(cursor))
                } while (cursor.moveToNext())
            }
            // close db connection
            db.close()
            // return persons list
            return persons
        }
    val personsCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + PersonTable.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val count = cursor.count
            cursor.close()
            // return count
            return count
        }

    fun updatePerson(person: Person): Int {
        val db = this.writableDatabase
        // updating row
        return db.update(
            PersonTable.TABLE_NAME,
            getValues(person),
            PersonTable.COLUMN_ID + " = ?",
            arrayOf(person.id)
        )
    }

    fun deletePerson(person: Person) {
        val db = this.writableDatabase
        db.delete(
            PersonTable.TABLE_NAME,
            PersonTable.COLUMN_ID + " = ?",
            arrayOf(person.id)
        )
        db.close()
    }
}