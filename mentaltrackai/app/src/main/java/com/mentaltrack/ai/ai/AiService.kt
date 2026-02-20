package com.mentaltrack.ai.ai

import com.mentaltrack.ai.data.model.MoodEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AI Service for MentalTrack AI application
 * This service will eventually integrate with Google's AI Edge SDK (Gemini Nano via AICore)
 * For now, it provides simulated AI insights based on mood data analysis
 */
@Singleton
class AiService @Inject constructor() {
    
    /**
     * Generates personalized insights based on mood history
     */
    fun generateInsights(moodEntries: List<MoodEntry>): Flow<List<String>> = flow {
        val insights = mutableListOf<String>()
        
        // Simulated AI analysis
        if (moodEntries.isNotEmpty()) {
            // Insight about mood trend
            val recentEntries = moodEntries.takeLast(7)
            if (recentEntries.size >= 7) {
                val recentAvg = recentEntries.map { it.moodLevel }.average()
                val previousWeekAvg = moodEntries.dropLast(7).takeLast(7).map { it.moodLevel }.average()
                
                if (recentAvg > previousWeekAvg) {
                    insights.add("Your mood has improved compared to last week! Great progress.")
                } else if (recentAvg < previousWeekAvg) {
                    insights.add("Your mood seems lower than last week. Remember to practice self-care.")
                }
            }
            
            // Insight about activities correlation
            val activitiesWithMood = mutableMapOf<String, MutableList<Int>>()
            moodEntries.forEach { entry ->
                entry.activities.forEach { activity ->
                    if (!activitiesWithMood.containsKey(activity)) {
                        activitiesWithMood[activity] = mutableListOf()
                    }
                    activitiesWithMood[activity]?.add(entry.moodLevel)
                }
            }
            
            activitiesWithMood.forEach { (activity, moods) ->
                val avgMood = moods.average()
                if (avgMood >= 4.0) { // High average mood
                    insights.add("Your mood tends to be higher when you engage in $activity.")
                }
            }
            
            // Insight about patterns
            if (moodEntries.size > 14) {
                // Check for weekly patterns
                val mondayMoods = moodEntries.filter { it.timestamp.dayOfWeek.value == 1 }.map { it.moodLevel }
                val fridayMoods = moodEntries.filter { it.timestamp.dayOfWeek.value == 5 }.map { it.moodLevel }
                
                if (mondayMoods.isNotEmpty() && fridayMoods.isNotEmpty()) {
                    val mondayAvg = mondayMoods.average()
                    val fridayAvg = fridayMoods.average()
                    
                    if (fridayAvg > mondayAvg + 0.5) {
                        insights.add("Your mood tends to be better on Fridays compared to Mondays.")
                    } else if (mondayAvg > fridayAvg + 0.5) {
                        insights.add("Your mood tends to be better on Mondays compared to Fridays - unusual but interesting!")
                    }
                }
            }
        } else {
            insights.add("Start tracking your mood to receive personalized insights.")
        }
        
        emit(insights)
    }
    
    /**
     * Generates a motivational message based on current mood and history
     */
    fun generateMotivationalMessage(currentMood: Int, moodEntries: List<MoodEntry>): Flow<String> = flow {
        val message = when {
            currentMood >= 4 -> {
                if (moodEntries.size > 1 && moodEntries.last().moodLevel > moodEntries[moodEntries.size - 2].moodLevel) {
                    "Keep up the great work! Your positive trend is inspiring."
                } else {
                    "It's wonderful to see you're feeling good today!"
                }
            }
            currentMood == 3 -> {
                if (moodEntries.size > 5) {
                    val recentAvg = moodEntries.takeLast(5).map { it.moodLevel }.average()
                    if (recentAvg > 3.0) {
                        "You're maintaining a balanced mood. That's a sign of emotional resilience."
                    } else {
                        "Remember that it's okay to have neutral days. Tomorrow brings new possibilities."
                    }
                } else {
                    "Today might be a balanced day. Take time to reflect and recharge."
                }
            }
            else -> { // Low mood
                if (moodEntries.size > 1 && moodEntries.last().moodLevel < moodEntries[moodEntries.size - 2].moodLevel) {
                    "I notice you might be having a challenging day. Remember, tough moments are temporary."
                } else {
                    "Sending you virtual comfort. Even small steps toward wellness matter."
                }
            }
        }
        
        emit(message)
    }
    
    /**
     * Provides suggestions based on mood patterns
     */
    fun generateSuggestions(moodEntries: List<MoodEntry>): Flow<List<String>> = flow {
        val suggestions = mutableListOf<String>()
        
        if (moodEntries.isNotEmpty()) {
            // Look for activities that correlate with higher moods
            val highMoodActivities = mutableMapOf<String, Int>()
            moodEntries.filter { it.moodLevel >= 4 }.forEach { entry ->
                entry.activities.forEach { activity ->
                    highMoodActivities[activity] = highMoodActivities.getOrDefault(activity, 0) + 1
                }
            }
            
            if (highMoodActivities.isNotEmpty()) {
                val topActivity = highMoodActivities.maxByOrNull { it.value }
                if (topActivity != null) {
                    suggestions.add("Consider engaging in ${topActivity.key} more often - it seems to positively impact your mood.")
                }
            }
            
            // Suggest patterns based on time of day/week
            val eveningEntries = moodEntries.filter { 
                it.timestamp.hour in 18..23 
            }
            val morningEntries = moodEntries.filter { 
                it.timestamp.hour in 6..11 
            }
            
            if (eveningEntries.isNotEmpty() && morningEntries.isNotEmpty()) {
                val eveningAvg = eveningEntries.map { it.moodLevel }.average()
                val morningAvg = morningEntries.map { it.moodLevel }.average()
                
                if (morningAvg > eveningAvg) {
                    suggestions.add("Your mornings tend to be brighter. Consider establishing a positive morning routine.")
                } else if (eveningAvg > morningAvg) {
                    suggestions.add("Your evenings seem better. Perhaps try bringing some evening calm into your mornings.")
                }
            }
        }
        
        emit(suggestions)
    }
}