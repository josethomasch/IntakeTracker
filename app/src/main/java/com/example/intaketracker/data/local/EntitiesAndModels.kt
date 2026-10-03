package com.example.intaketracker.data.local



import androidx.room.Entity

import androidx.room.ForeignKey

import androidx.room.Index

import androidx.room.PrimaryKey

import java.time.LocalTime



enum class ItemType {

    MEDICINE,

    PEG_FEED

}



data class DailyTask(

    val scheduleId: Long,

    val title: String,

    val dosageQuantity: String,

    val type: ItemType,

    val scheduledTime: LocalTime,

    val regimenTag: String,

    val logId: Long?,

    val completedAt: Long?

) {

    val isCompleted: Boolean get() = logId != null

}



@Entity(tableName = "schedule_table")

data class Schedule(

    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val title: String,

    val dosageQuantity: String,

    val type: ItemType,

    val scheduledTime: LocalTime,

    val regimenTag: String = "STANDARD"

)



@Entity(

    tableName = "log_table",

    foreignKeys = [

        ForeignKey(

            entity = Schedule::class,

            parentColumns = ["id"],

            childColumns = ["scheduleId"],

            onDelete = ForeignKey.CASCADE

        )

    ],

    indices = [Index(value = ["scheduleId"])]

)

data class Log(

    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val scheduleId: Long,

    val completedAt: Long

)

