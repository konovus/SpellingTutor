package com.example.spellingnotify.data.repository

import com.example.spellingnotify.data.api.DictionaryApi
import com.example.spellingnotify.data.local.WordsData
import com.example.spellingnotify.data.utils.Resource
import com.example.spellingnotify.domain.models.WordModel
import com.example.spellingnotify.domain.repository.MainRepository

class FakeMainRepositoryImpl: MainRepository {

    override suspend fun fetchWordData(word: String): Resource<WordModel> {
        if (word.isEmpty())
            return Resource.Error(DictionaryApi.EMPTY_WORD)
        if (!WordsData.allWords.contains(word))
            return Resource.Error(DictionaryApi.WORD_NOT_FOUND)

        return Resource.Success(WordModel(word, "Definition..."))
    }
}