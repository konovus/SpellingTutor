package com.example.spellingnotify.domain.filters

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FiveLetterWordsFilterTest {

    private lateinit var fiveLetterWordsFiler: FiveLetterWordsFilter

    @Before
    fun setup() {
        fiveLetterWordsFiler = FiveLetterWordsFilter()
    }

    @Test
    fun `filter, empty word, returns false`() {
        val result = fiveLetterWordsFiler.filter("")
        assertThat(result).isFalse()
    }

    @Test
    fun `filter, correct word, returns true`() {
        val result = fiveLetterWordsFiler.filter("great")
        assertThat(result).isTrue()
    }

    @Test
    fun `filter, incorrect word, returns false`() {
        val result = fiveLetterWordsFiler.filter("bad")
        assertThat(result).isFalse()
    }
}