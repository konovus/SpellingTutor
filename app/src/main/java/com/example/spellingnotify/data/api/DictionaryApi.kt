package com.example.spellingnotify.data.api

import com.example.spellingnotify.data.remote.WordResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface DictionaryApi {

    companion object ErrorMessage{
        const val EMPTY_WORD = "Empty word received, please try again or report bug"
        const val WORD_NOT_FOUND = "No definitions found"
    }

    @GET("{word}")
    suspend fun fetchWordData(@Path("word") word: String): Response<List<WordResponseItem>>

}