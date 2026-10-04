package com.example.intaketracker.ui.components



import android.app.TimePickerDialog

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.Close

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

import androidx.compose.ui.window.Dialog

import com.example.intaketracker.data.local.ItemType

import com.example.intaketracker.ui.IntakeEntry

import com.example.intaketracker.ui.RegimenTab

import java.time.LocalTime

import java.time.format.DateTimeFormatter



private val TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a")



@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun BatchAddDialog(

    initialTab: RegimenTab,

    onDismiss: () -> Unit,

    onConfirm: (entries: List<IntakeEntry>, time: LocalTime, regimenTag: String) -> Unit

) {

    var selectedTime by remember { mutableStateOf(LocalTime.of(8, 0)) }

    var selectedTab by remember { mutableStateOf(initialTab) }



    val entries = remember {

        mutableStateListOf(

            IntakeEntry(title = "", dosageQuantity = "", type = ItemType.MEDICINE),

            IntakeEntry(title = "", dosageQuantity = "", type = ItemType.PEG_FEED)

        )

    }



    val context = LocalContext.current

    val timePickerDialog = remember {

        TimePickerDialog(

            context,

            { _, hour, minute -> selectedTime = LocalTime.of(hour, minute) },

            selectedTime.hour,

            selectedTime.minute,

            false

        )

    }



    Dialog(onDismissRequest = onDismiss) {

        Card(

            modifier = Modifier

                .fillMaxWidth()

                .padding(vertical = 16.dp),

            shape = MaterialTheme.shapes.large

        ) {

            Column(

                modifier = Modifier

                    .padding(20.dp)

                    .fillMaxHeight(0.85f)

            ) {

                Text(

                    text = "Add Batch Schedule",

                    style = MaterialTheme.typography.headlineSmall,

                    fontWeight = FontWeight.Bold

                )



                Spacer(modifier = Modifier.height(16.dp))



                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceBetween,

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Text(

                        text = "Scheduled Time: ${selectedTime.format(TIME_FORMATTER)}",

                        style = MaterialTheme.typography.bodyLarge,

                        fontWeight = FontWeight.Medium

                    )

                    OutlinedButton(onClick = { timePickerDialog.show() }) {

                        Text("Change Time")

                    }

                }



                Spacer(modifier = Modifier.height(12.dp))



                Text(text = "Target Regimen:", style = MaterialTheme.typography.labelLarge)

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {

                    RegimenTab.values().forEach { tab ->

                        FilterChip(

                            selected = selectedTab == tab,

                            onClick = { selectedTab = tab },

                            label = { Text(tab.title) }

                        )

                    }

                }



                Divider(modifier = Modifier.padding(vertical = 12.dp))



                LazyColumn(

                    modifier = Modifier.weight(1f),

                    verticalArrangement = Arrangement.spacedBy(12.dp)

                ) {

                    itemsIndexed(entries) { index, entry ->

                        BatchEntryRow(

                            entry = entry,

                            onUpdate = { updated -> entries[index] = updated },

                            onRemove = { if (entries.size > 1) entries.removeAt(index) }

                        )

                    }



                    item {

                        TextButton(

                            onClick = {

                                entries.add(IntakeEntry("", "", ItemType.MEDICINE))

                            },

                            modifier = Modifier.fillMaxWidth()

                        ) {

                            Icon(Icons.Default.Add, contentDescription = null)

                            Spacer(modifier = Modifier.width(4.dp))

                            Text("Add Another Item")

                        }

                    }

                }



                Spacer(modifier = Modifier.height(16.dp))



                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.End,

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    TextButton(onClick = onDismiss) { Text("Cancel") }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(

                        onClick = {

                            val validEntries = entries.filter { it.title.isNotBlank() }

                            if (validEntries.isNotEmpty()) {

                                onConfirm(validEntries, selectedTime, selectedTab.tag)

                            }

                        }

                    ) {

                        Text("Save Schedules")

                    }

                }

            }

        }

    }

}



@Composable

private fun BatchEntryRow(

    entry: IntakeEntry,

    onUpdate: (IntakeEntry) -> Unit,

    onRemove: () -> Unit

) {

    Card(

        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),

        modifier = Modifier.fillMaxWidth()

    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                    FilterChip(

                        selected = entry.type == ItemType.MEDICINE,

                        onClick = { onUpdate(entry.copy(type = ItemType.MEDICINE)) },

                        label = { Text("Medicine") }

                    )

                    FilterChip(

                        selected = entry.type == ItemType.PEG_FEED,

                        onClick = { onUpdate(entry.copy(type = ItemType.PEG_FEED)) },

                        label = { Text("PEG Feed") }

                    )

                }



                IconButton(onClick = onRemove) {

                    Icon(Icons.Default.Close, contentDescription = "Remove Item")

                }

            }



            Spacer(modifier = Modifier.height(8.dp))



            OutlinedTextField(

                value = entry.title,

                onValueChange = { onUpdate(entry.copy(title = it)) },

                label = { Text(if (entry.type == ItemType.MEDICINE) "Medicine Name" else "Formula / Flush Name") },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true

            )



            Spacer(modifier = Modifier.height(8.dp))



            OutlinedTextField(

                value = entry.dosageQuantity,

                onValueChange = { onUpdate(entry.copy(dosageQuantity = it)) },

                label = { Text(if (entry.type == ItemType.MEDICINE) "Dosage (e.g. 500mg)" else "Volume (e.g. 200ml)") },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true

            )

        }

    }

}

