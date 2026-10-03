package com.example.intaketracker



import android.os.Bundle

import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent

import androidx.lifecycle.ViewModelProvider

import com.example.intaketracker.ui.TrackerScreen

import com.example.intaketracker.ui.TrackerViewModel

import com.example.intaketracker.ui.TrackerViewModelFactory



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val app = application as IntakeApplication

        val factory = TrackerViewModelFactory(app.trackerDao)

        val viewModel = ViewModelProvider(this, factory)[TrackerViewModel::class.java]



        setContent {

            TrackerScreen(viewModel = viewModel)

        }

    }

}

