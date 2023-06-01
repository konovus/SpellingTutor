package com.example.spellingnotify.data.repository

import com.example.spellingnotify.data.api.DictionaryApi
import com.example.spellingnotify.data.remote.toWordModel
import com.example.spellingnotify.data.utils.Resource
import com.example.spellingnotify.domain.models.WordModel
import com.example.spellingnotify.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi
): MainRepository {

    override suspend fun fetchWordData(word: String): Resource<WordModel> {
        if (word.isEmpty())
            return Resource.Error(DictionaryApi.EMPTY_WORD)

        val response = dictionaryApi.fetchWordData(word)
        return if (response.isSuccessful && response.body() != null)
            Resource.Success(data = response.body()!!.first().toWordModel())
        else Resource.Error(response.message().ifEmpty { DictionaryApi.WORD_NOT_FOUND })
    }
}