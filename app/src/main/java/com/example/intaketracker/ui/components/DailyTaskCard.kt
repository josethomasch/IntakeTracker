package com.example.intaketracker.ui.components



import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.ui.unit.dp

import com.example.intaketracker.data.local.DailyTask

import com.example.intaketracker.data.local.ItemType

import java.time.format.DateTimeFormatter



private val TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a")



@Composable

fun DailyTaskCard(

    task: DailyTask,

    onToggle: () -> Unit,

    onDelete: () -> Unit,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier.fillMaxWidth(),

        colors = CardDefaults.cardColors(

            containerColor = if (task.isCompleted) {

                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)

            } else {

                MaterialTheme.colorScheme.surface

            }

        ),

        elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 1.dp else 3.dp)

    ) {

        Row(

            modifier = Modifier

                .fillMaxWidth()

                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Checkbox(

                checked = task.isCompleted,

                onCheckedChange = { onToggle() }

            )



            Spacer(modifier = Modifier.width(12.dp))



            Column(modifier = Modifier.weight(1f)) {

                Row(

                    verticalAlignment = Alignment.CenterVertically,

                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {

                    Text(

                        text = task.title,

                        style = MaterialTheme.typography.titleMedium.copy(

                            fontWeight = FontWeight.Bold,

                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None

                        ),

                        color = if (task.isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface

                    )



                    ItemTypeBadge(type = task.type)

                }



                Spacer(modifier = Modifier.height(4.dp))



                Text(

                    text = "${task.dosageQuantity} • ${task.scheduledTime.format(TIME_FORMATTER)}",

                    style = MaterialTheme.typography.bodyMedium,

                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )

            }



            IconButton(onClick = onDelete) {

                Icon(

                    imageVector = Icons.Default.Delete,

                    contentDescription = "Delete Schedule",

                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)

                )

            }

        }

    }

}



@Composable

private fun ItemTypeBadge(type: ItemType) {

    val (label, containerColor, contentColor) = when (type) {

        ItemType.MEDICINE -> Triple(

            "Medicine",

            MaterialTheme.colorScheme.primaryContainer,

            MaterialTheme.colorScheme.onPrimaryContainer

        )

        ItemType.PEG_FEED -> Triple(

            "PEG Feed",

            MaterialTheme.colorScheme.tertiaryContainer,

            MaterialTheme.colorScheme.onTertiaryContainer

        )

    }



    Surface(

        color = containerColor,

        contentColor = contentColor,

        shape = MaterialTheme.shapes.extraSmall

    ) {

        Text(

            text = label,

            style = MaterialTheme.typography.labelSmall,

            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)

        )

    }

}

