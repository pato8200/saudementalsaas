package com.mentaltrack.ai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mentaltrack.ai.R
import com.mentaltrack.ai.data.model.MoodEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AiInsightsCard(
    moodEntries: List<MoodEntry>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Mentor",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.ai_mentor),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (moodEntries.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Insight 1: Welcome message
                    item {
                        InsightItem(
                            icon = Icons.Default.Lightbulb,
                            title = "Welcome Back!",
                            message = stringResource(R.string.ai_welcome_message)
                        )
                    }

                    // Insight 2: Mood trend
                    if (moodEntries.size >= 7) {
                        val recentEntries = moodEntries.takeLast(7).map { it.moodLevel }
                        val previousEntries = moodEntries.dropLast(7).takeLast(7).map { it.moodLevel }
                        
                        if (previousEntries.isNotEmpty()) {
                            val recentAvg = recentEntries.average()
                            val previousAvg = previousEntries.average()
                            
                            if (recentAvg > previousAvg) {
                                item {
                                    InsightItem(
                                        icon = Icons.Default.Lightbulb,
                                        title = "Positive Trend!",
                                        message = stringResource(R.string.ai_insight_positive)
                                    )
                                }
                            }
                        }
                    }

                    // Insight 3: Activity correlation
                    if (moodEntries.size >= 5) {
                        val meditationMoodAvg = moodEntries
                            .filter { "meditate" in it.activities }
                            .map { it.moodLevel }
                            .average()
                        
                        val generalMoodAvg = moodEntries.map { it.moodLevel }.average()
                        
                        if (meditationMoodAvg > generalMoodAvg && meditationMoodAvg > 0) {
                            val improvement = ((meditationMoodAvg - generalMoodAvg) / generalMoodAvg * 100).toInt()
                            
                            item {
                                InsightItem(
                                    icon = Icons.Default.Lightbulb,
                                    title = "Activity Correlation",
                                    message = stringResource(R.string.ai_insight_correlation)
                                )
                            }
                        }
                    }

                    // Insight 4: Weekly pattern
                    if (moodEntries.size >= 14) {
                        val entriesByDayOfWeek = moodEntries.groupingBy {
                            it.timestamp.dayOfWeek.value // 1 = Monday, 7 = Sunday
                        }.aggregate { _, accumulator: Double?, element, _ ->
                            if (accumulator == null) element.moodLevel.toDouble() else accumulator + element.moodLevel
                        }
                        
                        val avgByDay = entriesByDayOfWeek.mapValues { entry ->
                            moodEntries.count { it.timestamp.dayOfWeek.value == entry.key }.toDouble()
                        }.mapValues { (day, total) ->
                            total / moodEntries.count { it.timestamp.dayOfWeek.value == day }
                        }
                        
                        val bestDay = avgByDay.maxByOrNull { it.value }
                        
                        if (bestDay != null) {
                            val dayName = when (bestDay.key) {
                                1 -> "Monday"
                                2 -> "Tuesday"
                                3 -> "Wednesday"
                                4 -> "Thursday"
                                5 -> "Friday"
                                6 -> "Saturday"
                                7 -> "Sunday"
                                else -> "Day"
                            }
                            
                            item {
                                InsightItem(
                                    icon = Icons.Default.Lightbulb,
                                    title = "Your Best Day",
                                    message = "It looks like you feel your best on $dayName. Consider scheduling important activities then!"
                                )
                            }
                        }
                    }
                }
            } else {
                InsightItem(
                    icon = Icons.Default.Lightbulb,
                    title = "Getting Started",
                    message = "Track your mood regularly to receive personalized insights from your AI mentor."
                )
            }
        }
    }
}

@Composable
fun InsightItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}