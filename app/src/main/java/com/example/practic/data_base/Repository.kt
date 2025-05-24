package com.example.practic.data_base

import android.content.Context
import com.example.practic.data.Manga
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) {
    private val dbHelper = dataBaseHelper(context)

    suspend fun getAllMangas(sortBy: String?, sortOrder: String?): List<Manga> = withContext(Dispatchers.IO) {
        dbHelper.getAllMangas(sortBy, sortOrder)
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
}