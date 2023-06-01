package com.example.spellingnotify.domain.filters

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DoubleConsonantsWordsFilterTest {

    private lateinit var doubleConsonantsWordsFiler: DoubleConsonantsWordsFilter

    @Before
    fun setup() {
        doubleConsonantsWordsFiler = DoubleConsonantsWordsFilter()
    }

    @Test
    fun `filter, empty word, returns false`() {
        val result = doubleConsonantsWordsFiler.filter("")
        assertThat(result).isFalse()
    }

    @Test
    fun `filter, correct word, returns true`() {
        val result = doubleConsonantsWordsFiler.filter("butter")
        assertThat(result).isTrue()
    }

    @Test
    fun `filter, incorrect word, returns false`() {
        val result = doubleConsonantsWordsFiler.filter("test")
        assertThat(result).isFalse()
    }
}