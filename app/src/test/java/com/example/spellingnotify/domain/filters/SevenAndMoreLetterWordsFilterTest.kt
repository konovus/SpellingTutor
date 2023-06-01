package com.example.spellingnotify.domain.filters

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SevenAndMoreLetterWordsFilterTest {

    private lateinit var sevenAndMoreLetterWordsFiler: SevenAndMoreLetterWordsFilter

    @Before
    fun setup() {
        sevenAndMoreLetterWordsFiler = SevenAndMoreLetterWordsFilter()
    }

    @Test
    fun `filter, empty word, returns false`() {
        val result = sevenAndMoreLetterWordsFiler.filter("")
        assertThat(result).isFalse()
    }

    @Test
    fun `filter, correct word, returns true`() {
        val result = sevenAndMoreLetterWordsFiler.filter("communicate")
        assertThat(result).isTrue()
    }

    @Test
    fun `filter, incorrect word, returns false`() {
        val result = sevenAndMoreLetterWordsFiler.filter("good")
        assertThat(result).isFalse()
    }
}