package com.example.practic.data_base

import android.content.Context
import com.example.practic.data.Manga
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) {
    private val dbHelper = dataBaseHelper(context)

    suspend fun getAllMangas(sortBy: String? = null, sortOrder: String? = null): List<Manga> {
        return withContext(Dispatchers.IO) {
            if (sortBy != null && sortOrder != null) {
                dbHelper.getAllMangasSorted(sortBy, sortOrder)
            } else {
                dbHelper.getAllMangas()
            }
        }
    }

    suspend fun getMangaById(id: Int): Manga? = withContext(Dispatchers.IO) {
        dbHelper.getMangaById(id)
    }

    suspend fun updateManga(manga: Manga): Int = withContext(Dispatchers.IO) {
        dbHelper.updateManga(manga)
    }

    suspend fun deleteManga(id: Int): Int = withContext(Dispatchers.IO) {
        dbHelper.deleteManga(id)
    }

    suspend fun addManga(manga: Manga): Long = withContext(Dispatchers.IO) {
        dbHelper.addManga(manga)
    }
}