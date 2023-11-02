package com.rutvi.mad_practical11_21012011123


class PersonTable {
    companion object{
        const val TABLE_NAME= "Person"
        const val COLUMN_ID ="Id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_PHONE = "PhoneNo"
        const val COLUMN_EMAIL = "Email"
        const val COLUMN_ADDRESS = "Address"
        const val COLUMN_GPS_LAT = "Latitude"
        const val COLUMN_GPS_LONG = "Longitude"

        const val CREATE_TABLE = ("CREATE TABLE" + TABLE_NAME + "(" +
                COLUMN_ID + "TEXT PRIMARY KEY" +
                COLUMN_NAME + "TEXT," +
                COLUMN_PHONE + "TEXT," +
                COLUMN_EMAIL + "TEXT," +
                COLUMN_ADDRESS + "TEXT," +
                COLUMN_GPS_LAT + "REAL" +
                COLUMN_GPS_LONG + "REAL" + ")")
    }
}