package com.example.intaketracker.data.local



import androidx.room.Dao

import androidx.room.Insert

import androidx.room.OnConflictStrategy

import androidx.room.Query

import kotlinx.coroutines.flow.Flow



@Dao

interface TrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertSchedule(schedule: Schedule): Long



    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertSchedules(schedules: List<Schedule>)



    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertLog(log: Log): Long



    @Query("DELETE FROM schedule_table WHERE id = :scheduleId")

    suspend fun deleteSchedule(scheduleId: Long)



    @Query("DELETE FROM log_table WHERE scheduleId = :scheduleId AND completedAt >= :startOfDayMillis AND completedAt <= :endOfDayMillis")

    suspend fun uncheckTask(scheduleId: Long, startOfDayMillis: Long, endOfDayMillis: Long)



    @Query("SELECT s.id AS scheduleId, s.title, s.dosageQuantity, s.type, s.scheduledTime, s.regimenTag, l.id AS logId, l.completedAt FROM schedule_table s LEFT JOIN log_table l ON s.id = l.scheduleId AND l.completedAt >= :startOfDayMillis AND l.completedAt <= :endOfDayMillis ORDER BY s.scheduledTime ASC")

    fun getTasksForDay(startOfDayMillis: Long, endOfDayMillis: Long): Flow<List<DailyTask>>

}

