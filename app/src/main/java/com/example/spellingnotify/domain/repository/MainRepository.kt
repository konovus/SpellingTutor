package com.example.spellingnotify.domain.repository

import com.example.spellingnotify.data.utils.Resource
import com.example.spellingnotify.domain.models.WordModel

interface MainRepository {

    suspend fun fetchWordData(word: String): Resource<WordModel>
}