package com.mentaltrack.ai.ui.components

import androidx.compose.foundation.layout.*
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
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuideline
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.Shapes
import java.time.format.DateTimeFormatter

@Composable
fun MoodChartCard(
    moodEntries: List<MoodEntry>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.mood_tracker),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (moodEntries.isNotEmpty()) {
                // Prepare chart data
                val chartModelProducer = remember { CartesianChartModelProducer() }
                
                // Convert mood entries to chart data
                val moodData = moodEntries.sortedBy { it.timestamp }.takeLast(30) // Last 30 entries
                
                if (moodData.isNotEmpty()) {
                    val lineModels = mutableListOf<LineCartesianLayerModel.LineModel>()
                    
                    moodData.forEachIndexed { index, entry ->
                        lineModels.add(
                            LineCartesianLayerModel.LineModel(
                                x = index.toFloat(),
                                y = entry.moodLevel.toFloat(),
                                text = entry.timestamp.format(DateTimeFormatter.ofPattern("MM/dd"))
                            )
                        )
                    }
                    
                    val lineLayer = rememberLineCartesianLayer(
                        lines = listOf(
                            LineCartesianLayer.Line(
                                lineModels,
                                color = Color(0xFF4CAF50), // Green color for mood trend
                                guideline = rememberAxisGuideline(),
                                point = rememberShapeComponent(
                                    shape = Shapes.pillShape,
                                    sizeDp = 6f,
                                    color = Color(0xFF4CAF50)
                                )
                            )
                        )
                    )
                    
                    val startAxis = rememberStartAxis(
                        guideline = rememberAxisGuideline(),
                        title = rememberTextComponent(text = "Mood Level"),
                        valueFormatter = { value, _ -> value.toInt().toString() }
                    )
                    
                    val bottomAxis = rememberBottomAxis(
                        guideline = rememberAxisGuideline(),
                        title = rememberTextComponent(text = "Days"),
                        valueFormatter = { _, index ->
                            if (index < moodData.size) {
                                moodData[index.toInt()].timestamp.format(DateTimeFormatter.ofPattern("MM/dd"))
                            } else ""
                        }
                    )
                    
                    val chart = rememberCartesianChart(
                        layers = listOf(lineLayer),
                        startAxis = startAxis,
                        bottomAxis = bottomAxis,
                        horizontalLayout = HorizontalLayout.fixedInterval()
                    )
                    
                    CartesianChartHost(
                        chart = chart,
                        modelProducer = chartModelProducer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                    
                    LaunchedEffect(moodData) {
                        chartModelProducer.runTransaction {
                            CartesianChartModelProducer.Model(
                                layers = listOf(lineLayer)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Not enough data to show chart")
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No mood entries yet. Start tracking your mood!")
                }
            }
        }
    }
}