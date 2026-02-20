package com.mentaltrack.ai.data.model

enum class ActivityType(val value: String, val displayName: String) {
    SLEEP("sleep", "Sleep"),
    WORK("work", "Work"),
    EXERCISE("exercise", "Exercise"),
    SOCIALIZE("socialize", "Socialize"),
    MEDITATE("meditate", "Meditate"),
    READ("read", "Read"),
    OTHER("other", "Other");

    companion object {
        fun fromValue(value: String): ActivityType {
            return values().find { it.value == value } ?: OTHER
        }
    }
}