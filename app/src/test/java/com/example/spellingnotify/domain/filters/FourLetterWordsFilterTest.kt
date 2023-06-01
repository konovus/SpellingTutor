package com.example.spellingnotify.domain.filters

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FourLetterWordsFilterTest {

    private lateinit var fourLetterWordsFiler: FourLetterWordsFilter

    @Before
    fun setup() {
        fourLetterWordsFiler = FourLetterWordsFilter()
    }

    @Test
    fun `filter, empty word, returns false`() {
        val result = fourLetterWordsFiler.filter("")
        assertThat(result).isFalse()
    }

    @Test
    fun `filter, correct word, returns true`() {
        val result = fourLetterWordsFiler.filter("good")
        assertThat(result).isTrue()
    }

    @Test
    fun `filter, incorrect word, returns false`() {
        val result = fourLetterWordsFiler.filter("bad")
        assertThat(result).isFalse()
    }
}