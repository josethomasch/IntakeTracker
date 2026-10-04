package com.example.intaketracker.integration



import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.room.Room

import androidx.test.core.app.ApplicationProvider

import androidx.test.ext.junit.runners.AndroidJUnit4

import app.cash.turbine.test

import com.example.intaketracker.data.local.IntakeDatabase

import com.example.intaketracker.data.local.ItemType

import com.example.intaketracker.ui.IntakeEntry

import com.example.intaketracker.ui.TrackerViewModel

import com.example.intaketracker.ui.UiState

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.test.*

import org.junit.After

import org.junit.Assert.*

import org.junit.Before

import org.junit.Rule

import org.junit.Test

import org.junit.runner.RunWith

import java.time.LocalTime



@OptIn(ExperimentalCoroutinesApi::class)

@RunWith(AndroidJUnit4::class)

class FullPhase1Phase2IntegrationTest {



    @get:Rule

    val instantTaskExecutorRule = InstantTaskExecutorRule()



    private val testDispatcher = StandardTestDispatcher()

    private lateinit var database: IntakeDatabase

    private lateinit var viewModel: TrackerViewModel



    @Before

    fun setup() {

        Dispatchers.setMain(testDispatcher)

        database = Room.inMemoryDatabaseBuilder(

            ApplicationProvider.getApplicationContext(),

            IntakeDatabase::class.java

        ).allowMainThreadQueries().build()



        viewModel = TrackerViewModel(database.trackerDao())

    }



    @After

    fun tearDown() {

        database.close()

        Dispatchers.resetMain()

    }



    @Test

    fun verifyCombinedIntakeView() = runTest {

        viewModel.uiState.test {

            assertEquals(UiState.Loading, awaitItem())



            val batch = listOf(

                IntakeEntry("Paracetamol", "500 mg", ItemType.MEDICINE),

                IntakeEntry("Water Flush", "200 ml", ItemType.PEG_FEED)

            )

            viewModel.addBatchSchedules(batch, LocalTime.of(8, 0), "STANDARD")

            testScheduler.advanceUntilIdle()



            val state = awaitItem() as UiState.Success

            assertEquals(2, state.tasks.size)

            assertEquals(ItemType.MEDICINE, state.tasks[0].type)

            assertEquals(ItemType.PEG_FEED, state.tasks[1].type)

        }

    }

}

