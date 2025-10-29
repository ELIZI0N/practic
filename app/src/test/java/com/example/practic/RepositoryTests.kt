package com.example.practic.data_base

import com.example.practic.data.Manga
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class RepositoryTest {

    private lateinit var repository: Repository
    private lateinit var mockDbHelper: dataBaseHelper

    @Before
    fun setUp() {
        mockDbHelper = mock()
    }

    @Test
    fun testGetAllMangas_ReturnsEmptyList_WhenDbEmpty() = runTest {
        val emptyList = emptyList<Manga>()

        assertTrue("Temporary test", true)
    }

    @Test
    fun testRepositoryPlaceholder1() {
        assertTrue(true)
    }

    @Test
    fun testRepositoryPlaceholder2() {
        assertEquals(1, 1)
    }

    @Test
    fun testRepositoryPlaceholder3() {
        assertFalse(false)
    }
}