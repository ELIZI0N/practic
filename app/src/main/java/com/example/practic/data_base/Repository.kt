package com.example.practic.data_base

import android.content.Context
import com.example.practic.data.Manga
import com.example.practic.data.MangaDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class Repository(context: Context) {
    private val mangaDao = MangaDatabase.getDatabase(context).mangaDao()

    fun getAllMangas(): Flow<List<Manga>> {
        return mangaDao.getAllMangas()
    }

    suspend fun getAllMangasSorted(sortBy: String? = null, sortOrder: String? = null): List<Manga> {
        return withContext(Dispatchers.IO) {
            val allManga = mangaDao.getAllMangas().first()
            when (sortBy) {
                "ID" -> if (sortOrder == "ASC") allManga.sortedBy { it.id } else allManga.sortedByDescending { it.id }
                "Release" -> if (sortOrder == "ASC") allManga.sortedBy { it.release } else allManga.sortedByDescending { it.release }
                "Chapters" -> if (sortOrder == "ASC") allManga.sortedBy { it.chapters } else allManga.sortedByDescending { it.chapters }
                "Score" -> if (sortOrder == "ASC") allManga.sortedBy { it.score } else allManga.sortedByDescending { it.score }
                "Popularity" -> if (sortOrder == "ASC") allManga.sortedBy { it.popularity } else allManga.sortedByDescending { it.popularity }
                else -> allManga
            }
        }
    }

    suspend fun getMangaById(id: Int): Manga? = withContext(Dispatchers.IO) {
        mangaDao.getMangaById(id).first()
    }

    suspend fun updateManga(manga: Manga) = withContext(Dispatchers.IO) {
        mangaDao.updateManga(manga)
    }

    suspend fun deleteManga(id: Int) = withContext(Dispatchers.IO) {
        mangaDao.deleteMangaById(id)
    }

    suspend fun addManga(manga: Manga) = withContext(Dispatchers.IO) {
        mangaDao.insertManga(manga)
    }

    suspend fun searchManga(query: String): List<Manga> = withContext(Dispatchers.IO) {
        mangaDao.searchManga(query).first()
    }

    suspend fun getTopManga(limit: Int = 8): List<Manga> = withContext(Dispatchers.IO) {
        mangaDao.getTopManga(limit).first()
    }

    suspend fun getAllMangasAsList(): List<Manga> = withContext(Dispatchers.IO) {
        mangaDao.getAllMangas().first()
    }
}