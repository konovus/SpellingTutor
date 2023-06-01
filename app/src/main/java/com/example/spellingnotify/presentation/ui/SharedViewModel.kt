package com.example.spellingnotify.presentation.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(

): ViewModel() {

    var topBarTitle = mutableStateOf("")
        private set

    fun onTopBarTitleChanged(title: String) {
        topBarTitle.value = title
    }
}