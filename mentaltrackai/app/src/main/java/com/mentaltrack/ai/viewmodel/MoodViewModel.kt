package com.mentaltrack.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentaltrack.ai.ai.AiService
import com.mentaltrack.ai.data.model.MoodEntry
import com.mentaltrack.ai.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val aiService: AiService
) : ViewModel() {

    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadMoodEntries()
    }

    private fun loadMoodEntries() {
        viewModelScope.launch {
            _isLoading.value = true
            moodRepository.getAllMoodEntries().collect { entries ->
                _moodEntries.value = entries
                _isLoading.value = false
            }
        }
    }

    fun saveMoodEntry(moodLevel: Int, activities: List<String>, note: String = "") {
        viewModelScope.launch {
            val moodEntry = MoodEntry(
                moodLevel = moodLevel,
                activities = activities,
                note = note
            )
            moodRepository.insertMoodEntry(moodEntry)
        }
    }

    fun getMoodEntriesForLastNDays(n: Int): List<MoodEntry> {
        val startDate = LocalDate.now().minusDays(n.toLong())
            .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        return _moodEntries.value.filter { it.createdAt >= startDate }
    }

    fun getAverageMoodForLastNDays(n: Int): Double {
        val entries = getMoodEntriesForLastNDays(n)
        return if (entries.isNotEmpty()) {
            entries.map { it.moodLevel.toDouble() }.average()
        } else 0.0
    }

    fun getMostCommonActivitiesForLastNDays(n: Int): Map<String, Int> {
        val entries = getMoodEntriesForLastNDays(n)
        val activityCount = mutableMapOf<String, Int>()
        
        entries.forEach { entry ->
            entry.activities.forEach { activity ->
                activityCount[activity] = activityCount.getOrDefault(activity, 0) + 1
            }
        }
        
        return activityCount
    }
}