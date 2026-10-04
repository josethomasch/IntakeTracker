package com.example.intaketracker.data.local



import android.content.Context

import androidx.room.Database

import androidx.room.Room

import androidx.room.RoomDatabase

import androidx.room.TypeConverters



@Database(entities = [Schedule::class, Log::class], version = 1, exportSchema = false)

@TypeConverters(Converters::class)

abstract class IntakeDatabase : RoomDatabase() {

    abstract fun trackerDao(): TrackerDao



    companion object {

        @Volatile

        private var INSTANCE: IntakeDatabase? = null



        fun getDatabase(context: Context): IntakeDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(

                    context.applicationContext,

                    IntakeDatabase::class.java,

                    "intake_tracker_db"

                ).build()

                INSTANCE = instance

                instance

            }

        }

    }

}

