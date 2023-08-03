package com.example.spellingnotify.domain.utils

interface SettingsManager {

    companion object{
        const val LEARNING_INTERVALS_LIST = "learningIntervalsList"
        const val EXERCISING_INTERVALS_LIST = "exercisingIntervalsList"
        const val ARCHIVED_WORDS_LIST = "archivedWordsList"
        const val SCORE_BOARD_LIST = "scoreBoardList"
    }

    suspend fun saveIntSetting(key: String, value: Int)

    suspend fun saveBooleanSetting(key: String, value: Boolean)

    suspend fun saveStringSetting(key: String, value: String)

    suspend fun saveStringListSetting(key: String, value: List<String>)

    suspend fun readStringSetting(key: String): String

    suspend fun readIntSetting(key: String): Int

    suspend fun readBooleanSetting(key: String): Boolean

    suspend fun readStringListSetting(key: String): List<String>
}