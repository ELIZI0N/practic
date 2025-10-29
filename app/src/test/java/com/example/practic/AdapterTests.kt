package com.example.practic.adapter

import com.example.practic.data.Manga
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AdapterTest {

    private lateinit var testMangaList: List<Manga>

    @Before
    fun setUp() {
        testMangaList = listOf(
            Manga(
                id = 1,
                name = "Test Manga 1",
                images = "test1.jpg",
                authors = "Author 1",
                release = 2024,
                views = "1000",
                chapters = 50,
                score = 8.5,
                synopsis = "Description 1",
                popularity = 100,
                type = "Манга"
            ),
            Manga(
                id = 2,
                name = "Test Manga 2",
                images = "test2.jpg",
                authors = "Author 2",
                release = 2023,
                views = "2000",
                chapters = 75,
                score = 9.0,
                synopsis = "Description 2",
                popularity = 150,
                type = "Маньхуа"
            )
        )
    }

    @Test
    fun testAllAdapter_ItemCount_Correct() {
        // Given
        val adapter = AllAdapter(testMangaList) { }

        // When
        val itemCount = adapter.itemCount

        // Then
        assertEquals("Item count should match list size", testMangaList.size, itemCount)
    }

    @Test
    fun testAllAdapter_Constructor_InitializesData() {
        // Given & When
        val adapter = AllAdapter(testMangaList) { }

        // Then
        assertEquals("Adapter should initialize with correct data", testMangaList.size, adapter.itemCount)
    }

}