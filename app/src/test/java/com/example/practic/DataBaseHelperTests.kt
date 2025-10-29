package com.example.practic.data

import org.junit.Assert.*
import org.junit.Test

class DataBaseHelperTest {

    @Test
    fun testMangaCopy_WithNewName() {
        // Given
        val originalManga = Manga(
            id = 1,
            name = "Original Name",
            images = "original.jpg",
            authors = "Original Author",
            release = 2024,
            views = "1000",
            chapters = 50,
            score = 8.5,
            synopsis = "Original synopsis",
            popularity = 100,
            type = "Манга"
        )

        // When
        val copiedManga = originalManga.copy(name = "New Name")

        // Then
        assertEquals("ID should remain the same", originalManga.id, copiedManga.id)
        assertEquals("Name should be updated", "New Name", copiedManga.name)
        assertEquals("Authors should remain the same", originalManga.authors, copiedManga.authors)
    }

    @Test
    fun testMangaCopy_WithNewChapters() {
        // Given
        val originalManga = Manga(
            id = 1,
            name = "Test Manga",
            images = "test.jpg",
            authors = "Test Author",
            release = 2024,
            views = "1000",
            chapters = 50,
            score = 8.5,
            synopsis = "Test synopsis",
            popularity = 100,
            type = "Манга"
        )

        // When
        val copiedManga = originalManga.copy(chapters = 100)

        // Then
        assertEquals("Chapters should be updated", 100, copiedManga.chapters)
        assertEquals("Name should remain the same", originalManga.name, copiedManga.name)
    }

    @Test
    fun testMangaProperties_NotNullableFields() {
        // Given
        val manga = Manga(
            name = "Test Manga",
            images = "test.jpg",
            authors = "Test Author",
            release = 2024,
            views = null,
            chapters = null,
            score = null,
            synopsis = "Test synopsis",
            popularity = null,
            type = "Манга"
        )

        // Then
        assertNotNull("Name should not be null", manga.name)
        assertNotNull("Images should not be null", manga.images)
        assertNotNull("Authors should not be null", manga.authors)
        assertNotNull("Synopsis should not be null", manga.synopsis)
        assertNotNull("Type should not be null", manga.type)
    }

    @Test
    fun testMangaProperties_NullableFields() {
        // Given
        val manga = Manga(
            name = "Test Manga",
            images = "test.jpg",
            authors = "Test Author",
            release = 2024,
            views = null,
            chapters = null,
            score = null,
            synopsis = "Test synopsis",
            popularity = null,
            type = "Манга"
        )

        // Then
        assertNull("Views can be null", manga.views)
        assertNull("Chapters can be null", manga.chapters)
        assertNull("Score can be null", manga.score)
        assertNull("Popularity can be null", manga.popularity)
    }
}