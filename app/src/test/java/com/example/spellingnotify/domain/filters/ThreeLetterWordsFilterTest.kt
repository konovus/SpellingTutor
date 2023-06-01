package com.example.spellingnotify.domain.filters

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ThreeLetterWordsFilterTest {

    private lateinit var threeLetterWordsFiler: ThreeLetterWordsFilter

    @Before
    fun setup() {
        threeLetterWordsFiler = ThreeLetterWordsFilter()
    }

    @Test
    fun `filter, empty word, returns false`() {
        val result = threeLetterWordsFiler.filter("")
        assertThat(result).isFalse()
    }

    @Test
    fun `filter, correct word, returns true`() {
        val result = threeLetterWordsFiler.filter("bad")
        assertThat(result).isTrue()
    }

    @Test
    fun `filter, incorrect word, returns false`() {
        val result = threeLetterWordsFiler.filter("good")
        assertThat(result).isFalse()
    }
}