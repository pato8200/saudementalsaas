package com.mentaltrack.ai.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mentaltrack.ai.R
import com.mentaltrack.ai.data.model.ActivityType
import com.mentaltrack.ai.ui.theme.*

@Composable
fun OneTapEntryCard(
    onSaveEntry: (Int, List<String>, String) -> Unit
) {
    var selectedMoodLevel by remember { mutableIntStateOf(0) }
    var selectedActivities by remember { mutableStateOf(emptyList<String>()) }
    var noteText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.one_tap_entry),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mood Selection Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items((1..5).toList()) { level ->
                    val isSelected = selectedMoodLevel == level
                    val moodColor = when (level) {
                        1 -> MoodVeryUnhappy
                        2 -> MoodUnhappy
                        3 -> MoodNeutral
                        4 -> MoodHappy
                        5 -> MoodVeryHappy
                        else -> Color.Gray
                    }

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .aspectRatio(1f)
                            .clickable {
                                selectedMoodLevel = if (isSelected) 0 else level
                            }
                            .background(
                                color = if (isSelected) moodColor else Color.Transparent,
                                shape = MaterialTheme.shapes.medium
                            )
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color.White else moodColor,
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mood,
                            contentDescription = "Mood Level $level",
                            tint = if (isSelected) Color.White else moodColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "What did you do today?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Activities Selection
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ActivityType.values().filter { it != ActivityType.OTHER }) { activity ->
                    val isSelected = selectedActivities.contains(activity.value)
                    
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedActivities = if (isSelected) {
                                selectedActivities.filter { it != activity.value }
                            } else {
                                selectedActivities + activity.value
                            }
                        },
                        label = {
                            Text(text = activity.displayName)
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Additional Notes (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedMoodLevel > 0) {
                        onSaveEntry(selectedMoodLevel, selectedActivities, noteText)
                        // Reset selections after saving
                        selectedMoodLevel = 0
                        selectedActivities = emptyList()
                        noteText = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedMoodLevel > 0
            ) {
                Text("Save Entry")
            }
        }
    }
}