package com.mentaltrack.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mentaltrack.ai.R
import com.mentaltrack.ai.ui.components.MoodChartCard
import com.mentaltrack.ai.ui.components.AiInsightsCard
import com.mentaltrack.ai.ui.components.OneTapEntryCard
import com.mentaltrack.ai.viewmodel.MoodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MoodViewModel = hiltViewModel()
) {
    val moodEntries by viewModel.moodEntries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.dashboard_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // One-Tap Entry Card
            OneTapEntryCard(
                onSaveEntry = { moodLevel, activities, note ->
                    viewModel.saveMoodEntry(moodLevel, activities, note)
                }
            )

            // Mood Chart Card
            MoodChartCard(
                moodEntries = moodEntries
            )

            // AI Insights Card
            AiInsightsCard(
                moodEntries = moodEntries
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}