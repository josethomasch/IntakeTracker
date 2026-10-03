package com.example.intaketracker



import android.app.Application

import com.example.intaketracker.data.local.IntakeDatabase

import com.example.intaketracker.data.local.TrackerDao



class IntakeApplication : Application() {

    val database: IntakeDatabase by lazy { IntakeDatabase.getDatabase(this) }

    val trackerDao: TrackerDao by lazy { database.trackerDao() }

}

