package com.example.spellingnotify.presentation.utils

object ScoreFormula {

    fun create(score: Int, streak: Int, word: String): Int {
        return score + streak + word.trim().length
    }
}

object SettingTimeInterval {

    fun create(hours: Int, minutes: Int): String {
        val formattedHour = if (hours < 10) "0${hours}" else hours.toString()
        val formattedMinutes = if (minutes < 10) "0${minutes}" else minutes.toString()
        return "At $formattedHour:$formattedMinutes"
    }

    fun getHour(interval: String): Int {
        return interval.split(" ")[1].split(":")[0].toInt()
    }

    fun getMinute(interval: String): Int {
        return interval.split(" ")[1].split(":")[1].toInt()
    }
}

object ScoreBoardRow {
    const val SCOREBOARD_DATE_FORMAT = "dd.MM.YY"

    fun create(date: String, score: Int): String {
        return "$date $score"
    }

    fun getDate(row: String): String {
        return row.split(" ")[0]
    }

    fun getScore(row: String): Int {
        return row.split(" ")[1].toInt()
    }
}