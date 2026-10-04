package com.example.intaketracker.ui



import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import com.example.intaketracker.ui.components.BatchAddDialog

import com.example.intaketracker.ui.components.DailyTaskCard



@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun TrackerScreen(

    viewModel: TrackerViewModel,

    modifier: Modifier = Modifier

) {

    val uiState by viewModel.uiState.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }



    Scaffold(

        topBar = {

            Column {

                TopAppBar(

                    title = { Text("Intake Schedule", fontWeight = FontWeight.Bold) }

                )

                if (uiState is UiState.Success) {

                    val currentTab = (uiState as UiState.Success).selectedTab

                    ScrollableTabRow(

                        selectedTabIndex = currentTab.ordinal,

                        edgePadding = 16.dp,

                        containerColor = MaterialTheme.colorScheme.surfaceVariant

                    ) {

                        RegimenTab.values().forEach { tab ->

                            Tab(

                                selected = currentTab == tab,

                                onClick = { viewModel.selectTab(tab) },

                                text = {

                                    Text(

                                        text = tab.title,

                                        fontWeight = if (currentTab == tab) FontWeight.Bold else FontWeight.Normal

                                    )

                                }

                            )

                        }

                    }

                }

            }

        },

        floatingActionButton = {

            FloatingActionButton(

                onClick = { showAddDialog = true },

                containerColor = MaterialTheme.colorScheme.primary

            ) {

                Icon(Icons.Default.Add, contentDescription = "Add Intake Schedule")

            }

        }

    ) { paddingValues ->

        Box(

            modifier = modifier

                .fillMaxSize()

                .padding(paddingValues)

        ) {

            when (val state = uiState) {

                is UiState.Loading -> {

                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                }

                is UiState.Success -> {

                    if (state.tasks.isEmpty()) {

                        Text(

                            text = "No schedules in ${state.selectedTab.title}.
Tap + to add medicines or PEG feeds.",

                            style = MaterialTheme.typography.bodyLarge,

                            textAlign = TextAlign.Center,

                            color = MaterialTheme.colorScheme.outline,

                            modifier = Modifier.align(Alignment.Center)

                        )

                    } else {

                        LazyColumn(

                            contentPadding = PaddingValues(16.dp),

                            verticalArrangement = Arrangement.spacedBy(12.dp)

                        ) {

                            items(

                                items = state.tasks,

                                key = { it.scheduleId }

                            ) { task ->

                                DailyTaskCard(

                                    task = task,

                                    onToggle = { viewModel.toggleTask(task) },

                                    onDelete = { viewModel.deleteSchedule(task.scheduleId) }

                                )

                            }

                        }

                    }



                    if (showAddDialog) {

                        BatchAddDialog(

                            initialTab = state.selectedTab,

                            onDismiss = { showAddDialog = false },

                            onConfirm = { entries, time, regimenTag ->

                                viewModel.addBatchSchedules(entries, time, regimenTag)

                                showAddDialog = false

                            }

                        )

                    }

                }

            }

        }

    }

}

