package com.example.intaketracker.ui



import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.viewModelScope

import com.example.intaketracker.data.local.*

import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch

import java.time.LocalDate

import java.time.LocalTime

import java.time.ZoneId



enum class RegimenTab(val title: String, val tag: String) {

    STANDARD("Standard Plan", "STANDARD"),

    ALTERNATE("Alternate Pattern", "ALTERNATE"),

    LIGHT_DAY("Flush / Light Day", "LIGHT_DAY")

}



sealed interface UiState {

    object Loading : UiState

    data class Success(

        val tasks: List<DailyTask>,

        val selectedTab: RegimenTab

    ) : UiState

}



data class IntakeEntry(

    val title: String,

    val dosageQuantity: String,

    val type: ItemType = ItemType.MEDICINE

)



class TrackerViewModel(private val dao: TrackerDao) : ViewModel() {

    private val currentDate = MutableStateFlow(LocalDate.now())

    private val selectedTab = MutableStateFlow(RegimenTab.STANDARD)



    @OptIn(ExperimentalCoroutinesApi::class)

    val uiState: StateFlow<UiState> = combine(

        currentDate.flatMapLatest { date ->

            val zoneId = ZoneId.systemDefault()

            val startOfDayMillis = date.atStartOfDay(zoneId).toInstant().toEpochMilli()

            val endOfDayMillis = date.atTime(LocalTime.MAX).atZone(zoneId).toInstant().toEpochMilli()

            dao.getTasksForDay(startOfDayMillis, endOfDayMillis)

        },

        selectedTab

    ) { tasks, tab ->

        val patternTasks = tasks.filter { it.regimenTag == tab.tag }

        UiState.Success(tasks = patternTasks, selectedTab = tab)

    }.stateIn(

        scope = viewModelScope,

        started = SharingStarted.WhileSubscribed(5000),

        initialValue = UiState.Loading

    )



    fun selectTab(tab: RegimenTab) { selectedTab.value = tab }



    fun toggleTask(task: DailyTask) {

        viewModelScope.launch {

            val zoneId = ZoneId.systemDefault()

            val startOfDayMillis = currentDate.value.atStartOfDay(zoneId).toInstant().toEpochMilli()

            val endOfDayMillis = currentDate.value.atTime(LocalTime.MAX).atZone(zoneId).toInstant().toEpochMilli()



            if (task.isCompleted) {

                dao.uncheckTask(task.scheduleId, startOfDayMillis, endOfDayMillis)

            } else {

                dao.insertLog(Log(scheduleId = task.scheduleId, completedAt = System.currentTimeMillis()))

            }

        }

    }



    fun addBatchSchedules(entries: List<IntakeEntry>, time: LocalTime, regimenTag: String = "STANDARD") {

        viewModelScope.launch {

            val schedules = entries

                .filter { it.title.isNotBlank() }

                .map { entry ->

                    Schedule(

                        title = entry.title.trim(),

                        dosageQuantity = entry.dosageQuantity.ifBlank { "1 dose" }.trim(),

                        type = entry.type,

                        scheduledTime = time,

                        regimenTag = regimenTag

                    )

                }

            if (schedules.isNotEmpty()) { dao.insertSchedules(schedules) }

        }

    }



    fun deleteSchedule(scheduleId: Long) {

        viewModelScope.launch { dao.deleteSchedule(scheduleId) }

    }

}



class TrackerViewModelFactory(private val dao: TrackerDao) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return TrackerViewModel(dao) as T

    }

}

